package ai.idealistic.spartan.listeners.protocol;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.PluginBase;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.entity.Player;

public class AbilitiesListener extends PacketAdapter {

    public AbilitiesListener() {
        super(Register.plugin, ListenerPriority.NORMAL,
                PacketType.Play.Client.ABILITIES);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();
        if (player.getAllowFlight()) {
            CheckThread.run(() -> PluginBase.getProtocol(player).flyingTicks = 2);
        }
    }

}