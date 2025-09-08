package ai.idealistic.spartan.abstraction.inventory.implementation;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.inventory.InventoryMenu;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.api.Permission;
import ai.idealistic.spartan.functionality.connection.PluginAddons;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.utils.minecraft.inventory.MaterialUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SynMenu extends InventoryMenu {

    public static final Permission[] permissions = new Permission[]{Permission.MANAGE, Permission.INFO};

    public SynMenu() {
        super(PluginAddons.synName, 45, permissions);
    }

    @Override
    public boolean internalOpen(PlayerProtocol protocol, boolean permissionMessage, Object object) {
        List<String> list = new ArrayList<>();
        list.add("");
        list.add("§7A menu that allows you to");
        list.add("§7toggle the functionality");
        list.add("§7of checks and their detections.");
        add("§2" + EditionMenu.title, list, new ItemStack(Material.LEVER), 11);
        list.clear();
        list.add("");
        list.add("§7A menu that allows you to");
        list.add("§7toggle the details of a");
        list.add("§7check's detections.");
        add("§e" + DetailsMenu.title, list, new ItemStack(Material.BOOK), 12);
        list.clear();
        list.add("");
        list.add("§7A menu that allows you to");
        list.add("§7toggle the preventions of a");
        list.add("§7check's detections.");
        add("§c" + PreventionsMenu.title, list, new ItemStack(MaterialUtils.get("lead")), 13);
        list.clear();
        list.add("");
        list.add("§7A menu that allows you");
        list.add("§7to manage the punishment");
        list.add("§7commands of checks.");
        add("§4" + CommandsMenu.title, list, new ItemStack(Material.REDSTONE_BLOCK), 14);
        list.clear();
        list.add("");
        list.add("§7A menu that allows you to");
        list.add("§7toggle the punishments of a");
        list.add("§7check and view their commands.");
        add("§4" + PunishmentsMenu.title, list, new ItemStack(Material.IRON_AXE), 15);

        list.clear();
        list.add("§7A menu that allows you to");
        list.add("§7manage the detections of");
        list.add("§7all checks.");
        add("§b" + DetectionsMenu.title, list, new ItemStack(Material.REDSTONE), 30);
        list.clear();
        list.add("");
        list.add("§7A menu that allows you to");
        list.add("§7manage the general settings");
        list.add("§7of " + Register.plugin + ".");
        add("§a" + GeneralMenu.title, list, new ItemStack(Material.ENDER_CHEST), 31);
        list.clear();
        list.add("");
        list.add("§7A menu that allows you to");
        list.add("§7view the statistics of " + Register.plugin + ".");
        add("§6" + StatisticsMenu.title, list, new ItemStack(Material.PAPER), 32);
        return true;
    }

    @Override
    public boolean internalHandle(PlayerProtocol protocol) {
        String item = itemStack.getItemMeta().getDisplayName();
        item = item.startsWith("§") ? item.substring(2) : item;

        if (item.equals(EditionMenu.title)) {
            PluginBase.editionMenu.open(protocol, protocol.bukkit().getName());
        } else if (item.equals(DetailsMenu.title)) {
            PluginBase.detailsMenu.open(protocol, protocol.bukkit().getName());
        } else if (item.equals(PreventionsMenu.title)) {
            PluginBase.preventionsMenu.open(protocol, protocol.bukkit().getName());
        } else if (item.equals(PunishmentsMenu.title)) {
            PluginBase.punishmentsMenu.open(protocol, protocol.bukkit().getName());
        } else if (item.equals(DetectionsMenu.title)) {
            PluginBase.detectionsMenu.open(protocol, protocol.bukkit().getName());
        } else if (item.equals(GeneralMenu.title)) {
            PluginBase.generalMenu.open(protocol, protocol.bukkit().getName());
        } else if (item.equals(StatisticsMenu.title)) {
            PluginBase.statisticsMenu.open(protocol, protocol.bukkit().getName());
        } else if (item.equals(CommandsMenu.title)) {
            PluginBase.commandsMenu.open(protocol, protocol.bukkit().getName());
        }
        return true;
    }

}
