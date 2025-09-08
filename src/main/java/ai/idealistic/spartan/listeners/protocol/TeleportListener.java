package ai.idealistic.spartan.listeners.protocol;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.listeners.bukkit.TeleportEvent;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeleportListener extends PacketAdapter {

    public static final PacketType[] packetTypes = new PacketType[]{
            PacketType.Play.Server.POSITION,
            PacketType.Play.Server.RESPAWN
    };

    public TeleportListener() {
        super(
                Register.plugin,
                ListenerPriority.HIGHEST,
                packetTypes
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
        PacketType packetType = event.getPacket().getType();

        if (packetType.equals(PacketType.Play.Server.POSITION)) {
            CheckThread.run(() -> TeleportEvent.teleport(player, true, event));
        } else if (packetType.equals(PacketType.Play.Server.RESPAWN)) {
            CheckThread.run(() -> TeleportEvent.respawn(player, true, event));
        }
    }

    public static Location add(Location f, Location t) {
        return new Location(
                t.getWorld(),
                f.getX() + t.getX(),
                f.getY() + t.getY(),
                f.getZ() + t.getZ()
        );
    }

}
