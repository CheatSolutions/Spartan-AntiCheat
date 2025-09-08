package ai.idealistic.spartan.listeners.bukkit;

import ai.idealistic.spartan.abstraction.event.CPlayerRiptideEvent;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.PluginBase;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRiptideEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class TridentEvent implements Listener {

    @EventHandler
    private void Event(PlayerRiptideEvent e) {
        PlayerProtocol protocol = PluginBase.getProtocol(e.getPlayer());
        CheckThread.run(() -> event(
                new CPlayerRiptideEvent(
                        protocol,
                        e.getItem(),
                        e.getPlayer().getVelocity()
                ), false));
    }

    public static void event(CPlayerRiptideEvent e, boolean packets) {
        PlayerProtocol p = e.protocol;

        if (p.packetsEnabled() == packets) {
            p.executeRunners(false, e);
            PlayerInventory inventory = e.protocol.getInventory();

            for (ItemStack item : new ItemStack[]{inventory.getItemInHand(), inventory.getItemInOffHand()}) {
                if (item.getType() == Material.TRIDENT) {
                    int level = item.getEnchantmentLevel(Enchantment.RIPTIDE);

                    if (level > 0) {
                        p.lastVelocity = System.currentTimeMillis();
                        break;
                    }
                }
            }
        }
    }

}
