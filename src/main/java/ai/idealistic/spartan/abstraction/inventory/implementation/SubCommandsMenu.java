package ai.idealistic.spartan.abstraction.inventory.implementation;

import ai.idealistic.spartan.abstraction.check.Check;
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

public class SubCommandsMenu extends InventoryMenu {

    private static final String title = "Check Commands: ";

    public SubCommandsMenu() {
        super(title, 36, Permission.MANAGE);
    }

    @Override
    public boolean internalOpen(PlayerProtocol protocol, boolean permissionMessage, Object object) {
        if (!PluginAddons.isSyn()) {
            protocol.bukkit().closeInventory();
            String missing = PluginAddons.synClick.replace(PluginAddons.synMissingPlaceholder, title.substring(0, title.length() - 2));
            ClickableMessage.sendURL(
                    protocol.bukkit(),
                    missing,
                    missing + " (" + PluginAddons.synURL + ")",
                    PluginAddons.synURL
            );
            return false;
        }
        Check check = (Check) object;
        setTitle(protocol, title + check.getName());
        List<String> list = new ArrayList<>();
        int startingLine = 1;
        List<String> commands = check.getPunishmentCommands();

        for (int i = 0; i < Check.maxCommands; i++) {
            int slot = (startingLine * 9) + 2 + i - (i >= 5 ? 5 : 0);
            list.clear();
            list.add("");

            if (i < commands.size()) {
                list.add("§7" + commands.get(i));
                list.add("");
                list.add("§cClick to delete this punishment command.");
                add("§a" + check.getName() + " #" + (i + 1) + " command",
                        list, new ItemStack(check.hackType.material, i + 1), slot);
            } else {
                list.add("§cNo punishment command set.");
                add("§a" + check.getName() + " #" + (i + 1) + " command",
                        list, new ItemStack(check.hackType.material, i + 1), slot);
            }
            if (i == 4) {
                startingLine++;
            }
        }
        add("§4Back", null, new ItemStack(Material.ARROW), 31);
        return true;
    }

    @Override
    public boolean internalHandle(PlayerProtocol protocol) {
        String item = itemStack.getItemMeta().getDisplayName();

        if (item.equals("§4Back")) {
            PluginBase.commandsMenu.open(protocol, protocol.bukkit().getName());
        } else {
            item = item.startsWith("§") ? item.substring(2) : item;
            item = item.split(" ", 2)[0];
            Check check = Config.getCheckByName(item);

            if (check != null) {
                check.setPunishmentCommand(
                        itemStack.getAmount(),
                        ""
                );
                open(protocol, check);
            } else {
                protocol.bukkit().closeInventory();
            }
        }
        return true;
    }

}
