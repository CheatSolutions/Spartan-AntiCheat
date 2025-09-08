package ai.idealistic.spartan.abstraction.inventory.implementation;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.inventory.InventoryMenu;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.connection.PluginAddons;
import ai.idealistic.spartan.functionality.server.Permissions;
import ai.idealistic.spartan.functionality.tracking.DetectionCharge;
import ai.idealistic.spartan.utils.java.TimeUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PluginChargeMenu extends InventoryMenu {

    private static final String
            menu = Register.pluginName + " Charge",
            item = "§7Charge 5 seconds";

    public PluginChargeMenu() {
        super(menu, 27, Permissions.staffPermissions);
    }

    @Override
    public boolean internalOpen(PlayerProtocol protocol, boolean permissionMessage, Object object) {
        if (!PluginAddons.isFreeEdition()) {
            return false;
        }
        List<String> lore = new ArrayList<>(5);
        lore.add("");
        lore.add("§a" + PluginAddons.pluginURL + " §8- §2Spartan AntiCheat");
        lore.add("§3The longest living Minecraft paid anti cheat!");
        lore.add("");
        lore.add("§7Remaining Time§8: §c" + TimeUtils.convertMilliseconds(DetectionCharge.remaining()));
        add(item, lore, new ItemStack(Material.REDSTONE_BLOCK), 13);
        return true;
    }

    @Override
    public boolean internalHandle(PlayerProtocol protocol) {
        String item = itemStack.getItemMeta().getDisplayName();

        if (PluginAddons.isFreeEdition()
                && item.equalsIgnoreCase(PluginChargeMenu.item)) {
            if (DetectionCharge.charge(protocol)) {
                open(protocol);
            }
        } else {
            protocol.bukkit().closeInventory();
        }
        return true;
    }

}
