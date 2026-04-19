package ai.idealistic.spartan.listeners.protocol;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.compatibility.necessary.protocollib.BackPlib;
import ai.idealistic.spartan.compatibility.necessary.protocollib.EntityUseActionPlib;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.vehicle.VehicleEnterEvent;

public class UseEntityListener extends PacketAdapter {

    public UseEntityListener() {
        super(Register.plugin, ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();
        PlayerProtocol protocol = PluginBase.getProtocol(player);

        if (!Config.settings.getBoolean("Important.bedrock_on_protocollib")
                && protocol.isBedrockPlayer()) {
            return;
        }
        PacketContainer packet = event.getPacket();
        int entityId = BackPlib.getSafeInt(packet, 0);

        if (packet.getEntityUseActions().size() > 0
                ? !EntityUseActionPlib.getSafeEntityUseActions(packet, 0).equals(EnumWrappers.EntityUseAction.ATTACK)
                : !EntityUseActionPlib.getSafeEnumEntityUseActions(packet, 0).getAction().equals(
                EnumWrappers.EntityUseAction.ATTACK)) {
            CheckThread.run(protocol, () -> {
                Entity t = null;
                for (Entity entity : protocol.getNearbyEntities(5)) {
                    if (entity.getEntityId() == entityId) {
                        t = entity;
                        break;
                    }
                }
                if (t instanceof Vehicle) {
                    protocol.executeRunners(
                            false,
                            new VehicleEnterEvent(null, player)
                    );
                }
            });
        }
    }

}
