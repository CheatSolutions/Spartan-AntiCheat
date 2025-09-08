package ai.idealistic.spartan.compatibility.manual.abilities;

import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.compatibility.Compatibility;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.functionality.server.PluginBase;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomEntity;
import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.Events.CustomBlockBreakEvent;
import dev.lone.itemsadder.api.Events.CustomBlockPlaceEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ItemsAdder implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void Place(CustomBlockPlaceEvent e) {
        Config.compatibility.evadeFalsePositives(
                PluginBase.getProtocol(e.getPlayer()),
                Compatibility.CompatibilityType.GRAPPLING_HOOK,
                CheckEnums.HackCategoryType.WORLD,
                20
        );
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void Break(CustomBlockBreakEvent e) {
        Config.compatibility.evadeFalsePositives(
                PluginBase.getProtocol(e.getPlayer()),
                Compatibility.CompatibilityType.GRAPPLING_HOOK,
                CheckEnums.HackCategoryType.WORLD,
                20
        );
    }

    public static boolean is(PlayerProtocol protocol) {
        if (Compatibility.CompatibilityType.ITEMS_ADDER.isFunctional()) {
            PlayerInventory inventory = protocol.getInventory();

            for (ItemStack armor : inventory.getArmorContents()) {
                if (armor != null
                        && is(armor)) {
                    return true;
                }
            }
            return is(inventory.getItemInHand())
                    || MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9)
                    && is(inventory.getItemInOffHand());
        }
        return false;
    }

    public static boolean is(Block block) {
        return Compatibility.CompatibilityType.ITEMS_ADDER.isFunctional()
                && CustomBlock.byAlreadyPlaced(block) != null;
    }

    private static boolean is(ItemStack itemStack) {
        if (Compatibility.CompatibilityType.ITEMS_ADDER.isFunctional()) {
            CustomStack customStack = CustomStack.byItemStack(itemStack);

            if (customStack != null) {
                return CustomStack.isInRegistry(customStack.getNamespacedID());
            }
        }
        return false;
    }

    public static boolean is(Entity entity) {
        return Compatibility.CompatibilityType.ITEMS_ADDER.isFunctional()
                && CustomEntity.isCustomEntity(entity);
    }

}

