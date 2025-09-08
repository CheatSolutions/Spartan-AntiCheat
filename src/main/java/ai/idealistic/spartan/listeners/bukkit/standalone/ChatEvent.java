package ai.idealistic.spartan.listeners.bukkit.standalone;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.PluginBase;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    private void Event(AsyncPlayerChatEvent e) {
        PlayerProtocol p = PluginBase.getProtocol(e.getPlayer());
        CheckThread.run(() -> p.executeRunners(e.isCancelled(), e));
    }

}
