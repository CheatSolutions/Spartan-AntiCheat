package com.vagdedes.spartan.compatibility.manual.abilities;

import com.snowgears.grapplinghook.api.HookAPI;
import com.vagdedes.spartan.abstraction.protocol.PlayerProtocol;
import com.vagdedes.spartan.compatibility.Compatibility;
import com.vagdedes.spartan.functionality.server.Config;
import com.vagdedes.spartan.functionality.server.PluginBase;
import com.vagdedes.spartan.utils.minecraft.server.PluginUtils;
import me.vagdedes.spartan.system.Enums;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

public class GrapplingHook implements Listener {

    private final static String name = "grapplinghook";

    private static boolean isItem(ItemStack i) {
        if (Compatibility.CompatibilityType.GRAPPLING_HOOK.isFunctional()) {
            try {
                return PluginUtils.exists(name) ? HookAPI.isGrapplingHook(i) : i.getType() == Material.FISHING_ROD;
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void Event(PlayerFishEvent e) {
        if (Compatibility.CompatibilityType.GRAPPLING_HOOK.isFunctional() && e.getState() == PlayerFishEvent.State.CAUGHT_ENTITY) {
            Entity caught = e.getCaught();

            if (caught instanceof Player) {
                PlayerProtocol p = PluginBase.getProtocol((Player) caught),
                        t = PluginBase.getProtocol(e.getPlayer());

                if (!p.equals(t) && isItem(t.bukkitExtra.getItemInHand())) {
                    if (PluginUtils.exists(name)) {
                        Config.compatibility.evadeFalsePositives(
                                p,
                                Compatibility.CompatibilityType.GRAPPLING_HOOK,
                                new Enums.HackCategoryType[]{
                                        Enums.HackCategoryType.MOVEMENT,
                                        Enums.HackCategoryType.COMBAT
                                },
                                40
                        );
                    } else {
                        Config.compatibility.evadeFalsePositives(
                                p,
                                Compatibility.CompatibilityType.GRAPPLING_HOOK,
                                new Enums.HackCategoryType[]{
                                        Enums.HackCategoryType.MOVEMENT,
                                        Enums.HackCategoryType.COMBAT
                                },
                                10
                        );
                    }
                }
            }
        }
    }
}
