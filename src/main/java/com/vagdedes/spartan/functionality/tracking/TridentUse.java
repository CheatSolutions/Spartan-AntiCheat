package com.vagdedes.spartan.functionality.tracking;

import com.vagdedes.spartan.abstraction.player.PlayerTrackers;
import com.vagdedes.spartan.abstraction.player.SpartanPlayer;
import com.vagdedes.spartan.functionality.server.MultiVersion;
import com.vagdedes.spartan.functionality.server.TPS;
import com.vagdedes.spartan.utils.math.AlgebraUtils;
import me.vagdedes.spartan.system.Enums;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class TridentUse {

    private static final int riptideMaxSafeLevel = 3;

    public static void run(SpartanPlayer p) {
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
            PlayerInventory inventory = p.getInstance().getInventory();

            for (ItemStack item : new ItemStack[]{inventory.getItemInHand(), inventory.getItemInOffHand()}) {
                if (item.getType() == Material.TRIDENT) {
                    int level = item.getEnchantmentLevel(Enchantment.RIPTIDE);
                    if (level > 0) {
                        int ticks = AlgebraUtils.integerRound(Math.log(level) * TPS.maximum);

                        if (level > riptideMaxSafeLevel) {
                            p.trackers.add(PlayerTrackers.TrackerType.ABSTRACT_VELOCITY, ticks);
                        } else {
                            p.trackers.add(PlayerTrackers.TrackerType.TRIDENT, ticks);
                        }

                        p.getExecutor(Enums.HackType.Speed).handle(false, level);
                    }
                    break;
                }
            }
        }
    }

}
