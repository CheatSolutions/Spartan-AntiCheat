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
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DetailsMenu extends InventoryMenu {

    public static final String title = "Check Details";

    public DetailsMenu() {
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
        int startingLine = 1;
        List<String> list = new ArrayList<>();

        for (CheckEnums.HackCategoryType categoryType : CheckEnums.HackCategoryType.values()) {
            int slot = (startingLine * 9) + 2;

            for (HackType hackType : HackType.values()) {
                if (hackType.category == categoryType) {
                    list.clear();
                    list.add("§6" + hackType.category.toString());
                    list.add("");

                    for (Check.DataType dataType : Check.DataType.values()) {
                        if (hackType.getCheck().isDetectionDetails(dataType)) {
                            list.add("§aEnabled for the " + dataType.toString() + " edition. §7(Left click to toggle)");
                        } else {
                            list.add("§cDisabled for the " + dataType.toString() + " edition. §7(Right click to toggle)");
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

            if (clickType == ClickType.LEFT) {
                Check check = Config.getCheckByName(item);

                if (check != null) {
                    check.setDetectionDetails(Check.DataType.JAVA, !check.isDetectionDetails(Check.DataType.JAVA));
                }
                open(protocol);
            } else if (clickType == ClickType.RIGHT) {
                Check check = Config.getCheckByName(item);

                if (check != null) {
                    check.setDetectionDetails(Check.DataType.BEDROCK, !check.isDetectionDetails(Check.DataType.BEDROCK));
                }
                open(protocol);
            }
        }
        return true;
    }

}
