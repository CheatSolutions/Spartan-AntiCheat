package ai.idealistic.spartan.listeners.bukkit;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.PluginBase;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    private void Event(PlayerDeathEvent e) {
        CheckThread.run(() -> event(e.getEntity(), false, e));
    }

    public static void event(Player player, boolean packets, Object object) {
        PlayerProtocol protocol = PluginBase.getProtocol(player, true);
        protocol.timerBalancer.addBalance(50);

        if (protocol.packetsEnabled() == packets) {
            protocol.executeRunners(null, object);
        }
    }

}
