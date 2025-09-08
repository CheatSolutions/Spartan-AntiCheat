package ai.idealistic.spartan.abstraction.inventory.implementation;

import ai.idealistic.spartan.abstraction.inventory.InventoryMenu;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.api.Permission;
import ai.idealistic.spartan.functionality.connection.PluginAddons;
import ai.idealistic.spartan.functionality.moderation.clickable.ClickableMessage;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.utils.minecraft.inventory.EnchantmentUtils;
import ai.idealistic.spartan.utils.minecraft.inventory.MaterialUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GeneralMenu extends InventoryMenu {

    public static final String title = "General Functionality";

    public GeneralMenu() {
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
        List<String> list = new ArrayList<>();
        list.add("");
        ItemStack itemStack = new ItemStack(Material.BOOK);

        if (Config.settings.getBoolean("Logs.log_file")) {
            itemStack.addUnsafeEnchantment(EnchantmentUtils.DURABILITY, 1);
            list.add("§2Enabled");
        } else {
            list.add("§4Disabled");
        }
        add("§aLogs >> File", list, itemStack, 11);
        list.clear();
        list.add("");
        itemStack = new ItemStack(Material.PAPER);

        if (Config.settings.getBoolean("Logs.log_console")) {
            itemStack.addUnsafeEnchantment(EnchantmentUtils.DURABILITY, 1);
            list.add("§2Enabled");
        } else {
            list.add("§4Disabled");
        }
        add("§eLogs >> Console", list, itemStack, 12);
        list.clear();
        list.add("");
        itemStack = new ItemStack(Material.LEVER);

        if (Config.settings.getBoolean("Notifications.individual_only_notifications")) {
            itemStack.addUnsafeEnchantment(EnchantmentUtils.DURABILITY, 1);
            list.add("§2Enabled");
        } else {
            list.add("§4Disabled");
        }
        add("§eNotifications >> Individual Only", list, itemStack, 13);
        list.clear();
        list.add("");
        itemStack = new ItemStack(Material.COMPASS);

        if (Config.settings.getBoolean("Notifications.enable_notifications_on_login")) {
            itemStack.addUnsafeEnchantment(EnchantmentUtils.DURABILITY, 1);
            list.add("§2Enabled");
        } else {
            list.add("§4Disabled");
        }
        add("§eNotifications >> Enable On Login", list, itemStack, 14);
        list.clear();
        list.add("");
        itemStack = new ItemStack(Material.ENDER_PEARL);

        if (Config.settings.getBoolean("Notifications.awareness_notifications")) {
            itemStack.addUnsafeEnchantment(EnchantmentUtils.DURABILITY, 1);
            list.add("§2Enabled");
        } else {
            list.add("§4Disabled");
        }
        add("§cNotifications >> Awareness", list, itemStack, 15);

        //

        list.clear();
        list.add("");
        itemStack = new ItemStack(Material.BEDROCK);

        if (Config.settings.getBoolean("Important.op_bypass")) {
            itemStack.addUnsafeEnchantment(EnchantmentUtils.DURABILITY, 1);
            list.add("§2Enabled");
        } else {
            list.add("§4Disabled");
        }
        add("§cImportant >> Op Bypass", list, itemStack, 20);
        list.clear();
        list.add("");
        itemStack = new ItemStack(MaterialUtils.get("enchanting_table"));

        if (Config.settings.getBoolean("Important.bedrock_on_protocollib")) {
            itemStack.addUnsafeEnchantment(EnchantmentUtils.DURABILITY, 1);
            list.add("§2Enabled");
        } else {
            list.add("§4Disabled");
        }
        add("§cImportant >> Bedrock On ProtocolLib", list, itemStack, 21);
        list.clear();
        list.add("");
        itemStack = new ItemStack(Material.CRAFTING_TABLE);

        if (Config.settings.getBoolean("Important.bedrock_client_permission")) {
            itemStack.addUnsafeEnchantment(EnchantmentUtils.DURABILITY, 1);
            list.add("§2Enabled");
        } else {
            list.add("§4Disabled");
        }
        add("§eImportant >> Bedrock Client Permission", list, itemStack, 22);
        list.clear();
        list.add("");
        itemStack = new ItemStack(MaterialUtils.get("piston"));

        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            itemStack.addUnsafeEnchantment(EnchantmentUtils.DURABILITY, 1);
            list.add("§2Enabled");
        } else {
            list.add("§4Disabled");
        }
        add("§cImportant >> Enable Developer API", list, itemStack, 23);
        list.clear();
        list.add("");
        itemStack = new ItemStack(Material.NAME_TAG);

        if (Config.settings.getBoolean("Important.enable_watermark")) {
            itemStack.addUnsafeEnchantment(EnchantmentUtils.DURABILITY, 1);
            list.add("§2Enabled");
        } else {
            list.add("§4Disabled");
        }
        add("§aImportant >> Enable Watermark", list, itemStack, 24);

        //

        list.clear();
        list.add("");
        itemStack = new ItemStack(Material.IRON_HELMET);

        if (Config.settings.getBoolean("Important.enable_npc")) {
            itemStack.addUnsafeEnchantment(EnchantmentUtils.DURABILITY, 1);
            list.add("§2Enabled");
        } else {
            list.add("§4Disabled");
        }
        add("§aImportant >> Enable NPC", list, itemStack, 30);
        list.clear();
        list.add("");
        itemStack = new ItemStack(Material.STONE);

        if (Config.settings.getBoolean("Detections.ground_teleport_on_detection")) {
            itemStack.addUnsafeEnchantment(EnchantmentUtils.DURABILITY, 1);
            list.add("§2Enabled");
        } else {
            list.add("§4Disabled");
        }
        add("§cDetections >> Ground Teleport", list, itemStack, 31);
        list.clear();
        list.add("");
        itemStack = new ItemStack(Material.REDSTONE);

        if (Config.settings.getBoolean("Detections.damage_on_detection")) {
            itemStack.addUnsafeEnchantment(EnchantmentUtils.DURABILITY, 1);
            list.add("§2Enabled");
        } else {
            list.add("§4Disabled");
        }
        add("§cDetections >> Damage", list, itemStack, 32);

        add("§4Back", null, new ItemStack(Material.ARROW), 49);
        return true;
    }

    @Override
    public boolean internalHandle(PlayerProtocol protocol) {
        String item = itemStack.getItemMeta().getDisplayName();
        item = item.startsWith("§") ? item.substring(2) : item;

        if (item.equals("Back")) {
            PluginBase.synMenu.open(protocol, protocol.bukkit().getName());
        } else if (item.equals("Logs >> File")) {
            Config.settings.setOption("Logs.log_file", !Config.settings.getBoolean("Logs.log_file"));
            open(protocol);
        } else if (item.equals("Logs >> Console")) {
            Config.settings.setOption("Logs.log_console", !Config.settings.getBoolean("Logs.log_console"));
            open(protocol);
        } else if (item.equals("Notifications >> Individual Only")) {
            Config.settings.setOption("Notifications.individual_only_notifications",
                    !Config.settings.getBoolean("Notifications.individual_only_notifications"));
            open(protocol);
        } else if (item.equals("Notifications >> Enable On Login")) {
            Config.settings.setOption("Notifications.enable_notifications_on_login",
                    !Config.settings.getBoolean("Notifications.enable_notifications_on_login"));
            open(protocol);
        } else if (item.equals("Notifications >> Awareness")) {
            Config.settings.setOption("Notifications.awareness_notifications",
                    !Config.settings.getBoolean("Notifications.awareness_notifications"));
            open(protocol);
        } else if (item.equals("Important >> Op Bypass")) {
            Config.settings.setOption("Important.op_bypass", !Config.settings.getBoolean("Important.op_bypass"));
            open(protocol);
        } else if (item.equals("Important >> Bedrock On ProtocolLib")) {
            Config.settings.setOption("Important.bedrock_on_protocollib",
                    !Config.settings.getBoolean("Important.bedrock_on_protocollib"));
            open(protocol);
        } else if (item.equals("Important >> Bedrock Client Permission")) {
            Config.settings.setOption("Important.bedrock_client_permission",
                    !Config.settings.getBoolean("Important.bedrock_client_permission"));
            open(protocol);
        } else if (item.equals("Important >> Enable Developer API")) {
            Config.settings.setOption("Important.enable_developer_api",
                    !Config.settings.getBoolean("Important.enable_developer_api"));
            open(protocol);
        } else if (item.equals("Important >> Enable Watermark")) {
            Config.settings.setOption("Important.enable_watermark",
                    !Config.settings.getBoolean("Important.enable_watermark"));
            open(protocol);
        } else if (item.equals("Important >> Enable NPC")) {
            Config.settings.setOption("Important.enable_npc", !Config.settings.getBoolean("Important.enable_npc"));
            open(protocol);
        } else if (item.equals("Detections >> Ground Teleport")) {
            Config.settings.setOption(
                    "Detections.ground_teleport_on_detection",
                    !Config.settings.getBoolean("Detections.ground_teleport_on_detection")
            );
            open(protocol);
        } else if (item.equals("Detections >> Damage")) {
            Config.settings.setOption(
                    "Detections.damage_on_detection",
                    !Config.settings.getBoolean("Detections.damage_on_detection")
            );
            open(protocol);
        }
        return true;
    }

}
