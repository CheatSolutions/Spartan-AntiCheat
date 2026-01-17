package ai.idealistic.spartan.listeners.bukkit;

import ai.idealistic.spartan.abstraction.event.CPlayerRiptideEvent;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.PluginBase;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRiptideEvent;

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
        }
    }

}
