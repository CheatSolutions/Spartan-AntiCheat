package ai.idealistic.spartan.abstraction.inventory.implementation;

import ai.idealistic.spartan.abstraction.inventory.InventoryMenu;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.api.Permission;
import ai.idealistic.spartan.functionality.connection.PluginAddons;
import ai.idealistic.spartan.functionality.moderation.clickable.ClickableMessage;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class DetectionsMenu extends InventoryMenu {

    public static final String title = "Check Detections";
    private static final Map<String, Material> items = new HashMap<>(53);

    public DetectionsMenu() {
        super(title, 54, Permission.MANAGE);
    }

    @Override
    public boolean internalOpen(PlayerProtocol protocol, boolean permissionMessage, Object object) {
        if (!PluginAddons.isSyn()) {
            protocol.bukkit().closeInventory();
            String missing = PluginAddons.synClick.replace(PluginAddons.synMissingPlaceholder, title);
            ClickableMessage.sendURL(
                    protocol.bukkit(),
                    missing,
                    missing + " (" + PluginAddons.synURL + ")",
                    PluginAddons.synURL
            );
            return false;
        }
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(Config.advanced.getFile());
        Set<String> keys = configuration.getKeys(true);

        if (!keys.isEmpty()) {
            int added = 0;

            for (String key : keys) {
                if (key.contains(".")) {
                    Object value = configuration.get(key);

                    if (value instanceof Boolean) {
                        Material material = items.get(key);

                        while (material == null) {
                            int maxMaterials = Material.values().length;
                            int randomIndex = (int) (Math.random() * maxMaterials);
                            Material find = Material.values()[randomIndex];

                            if (find.isItem()
                                    && !items.containsValue(find)) {
                                material = find;
                                items.put(key, find);
                            }
                        }
                        key = key
                                .replace(".", " >> ")
                                .replace("-", " ")
                                .replace("_", "-");
                        List<String> list = new ArrayList<>();
                        list.add("");

                        if ((Boolean) value) {
                            list.add("§aEnabled");
                            add("§2" + key, list, new ItemStack(material), -1);
                        } else {
                            list.add("§cDisabled");
                            add("§4" + key, list, new ItemStack(material), -1);
                        }
                        added++;

                        if (added == 53) {
                            break;
                        }
                    }
                }
            }
        }
        add("§4Back", null, new ItemStack(Material.ARROW), 53);
        return true;
    }

    @Override
    public boolean internalHandle(PlayerProtocol protocol) {
        String item = itemStack.getItemMeta().getDisplayName();

        if (item.equals("§4Back")) {
            PluginBase.synMenu.open(protocol, protocol.bukkit().getName());
        } else {
            item = item.startsWith("§") ? item.substring(2) : item;
            item = item.replace(" >> ", ".")
                    .replace("-", "_")
                    .replace(" ", "-");
            Config.advanced.setOption(item, !Config.advanced.getBoolean(item));
            open(protocol);
        }
        return true;
    }

}
