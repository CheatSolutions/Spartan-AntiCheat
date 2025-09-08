package ai.idealistic.spartan.abstraction.inventory.implementation;

import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.check.CheckEnums.HackType;
import ai.idealistic.spartan.abstraction.inventory.InventoryMenu;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.api.Permission;
import ai.idealistic.spartan.functionality.connection.PluginAddons;
import ai.idealistic.spartan.functionality.moderation.clickable.ClickableMessage;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CommandsMenu extends InventoryMenu {

    public static final String title = "Check Commands";

    public CommandsMenu() {
        super(title, 54, new Permission[]{Permission.MANAGE, Permission.INFO});
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
        List<String> list = new ArrayList<>();
        int startingLine = 1;

        for (CheckEnums.HackCategoryType categoryType : CheckEnums.HackCategoryType.values()) {
            int slot = (startingLine * 9) + 2;

            for (HackType hackType : HackType.values()) {
                if (hackType.category == categoryType) {
                    list.clear();
                    list.add("§6" + hackType.category.toString());
                    list.add("");
                    int counter = 0;

                    for (String s : hackType.getCheck().getPunishmentCommands()) {
                        if (s != null) {
                            counter++;
                            String base = "§7" + counter + "§8:§f ";

                            if (s.length() > 40) {
                                list.add(base + s.substring(0, 40));
                            } else {
                                list.add(base + s);
                            }
                        }
                    }
                    add("§3" + hackType.getCheck().getName(), list, new ItemStack(hackType.material), slot);
                    slot++;
                }
            }
            startingLine++;
        }
        add("§4Back", null, new ItemStack(Material.ARROW), 49);
        return true;
    }

    @Override
    public boolean internalHandle(PlayerProtocol protocol) {
        String item = itemStack.getItemMeta().getDisplayName();

        if (item.equals("§4Back")) {
            PluginBase.synMenu.open(protocol, protocol.bukkit().getName());
        } else {
            item = item.startsWith("§") ? item.substring(2) : item;
            Check check = Config.getCheckByName(item);

            if (check != null) {
                PluginBase.subCommandsMenu.open(protocol, check);
            }
        }
        return true;
    }

}
