package ai.idealistic.spartan.listeners.protocol;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.event.ServerBlockChange;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import org.bukkit.entity.Player;

public class PacketServerBlockHandle extends PacketAdapter {

    public PacketServerBlockHandle() {
        super(Register.plugin, ListenerPriority.NORMAL, PacketType.Play.Server.BLOCK_CHANGE);
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
        BlockPosition blockPosition = packet.getBlockPositionModifier().read(0);
        WrappedBlockData blockData = packet.getBlockData().read(0);
        ServerBlockChange serverBlockChange = new ServerBlockChange(blockPosition, blockData.getType());
        CheckThread.run(() -> protocol.packetWorld.worldChange(serverBlockChange));
    }

}