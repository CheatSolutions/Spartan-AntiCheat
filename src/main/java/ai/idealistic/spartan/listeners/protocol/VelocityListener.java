package ai.idealistic.spartan.listeners.protocol;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.event.CPlayerVelocityEvent;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.compatibility.necessary.protocollib.BackPlib;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.listeners.bukkit.VelocityEvent;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class VelocityListener extends PacketAdapter {


    public VelocityListener() {
        super(
                Register.plugin,
                ListenerPriority.MONITOR,
                PacketType.Play.Server.ENTITY_VELOCITY
        );
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        Player player = event.getPlayer();
        PlayerProtocol protocol = PluginBase.getProtocol(player);

        if (!Config.settings.getBoolean("Important.bedrock_on_protocollib")
                && protocol.isBedrockPlayer()) {
            return;
        }
        PacketContainer packet = event.getPacket();

        if (!packet.getType().equals(PacketType.Play.Server.ENTITY_VELOCITY)) {
            return;
        }
        if (packet.getIntegers().size() >= 4) {
            int id = BackPlib.getSafeInt(packet, 0);

            if (protocol.getEntityId() == id) {
                final double x = ((double) BackPlib.getSafeInt(packet, 1)) / 8000.0D;
                final double y = ((double) BackPlib.getSafeInt(packet, 2)) / 8000.0D;
                final double z = ((double) BackPlib.getSafeInt(packet, 3)) / 8000.0D;
                final boolean isCancelled = event.isCancelled();

                CheckThread.run(protocol, () -> {
                    // Now it's safe to use these variables asynchronously
                    CPlayerVelocityEvent velocityEvent = new CPlayerVelocityEvent(player, new Vector(x, y, z));
                    velocityEvent.setCancelled(isCancelled);
                    VelocityEvent.event(velocityEvent, true);
                });
            }
        }
    }

}
