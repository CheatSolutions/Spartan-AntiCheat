package ai.idealistic.spartan.listeners.protocol;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Location;

public class PacketPistonHandle extends PacketAdapter {

    public PacketPistonHandle() {
        super(Register.plugin, ListenerPriority.NORMAL, PacketType.Play.Server.BLOCK_ACTION);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();

        if (packet.getStructures().getValues().toString().contains("piston")) {
            PlayerProtocol protocol = PluginBase.getProtocol(event.getPlayer());

            if (!Config.settings.getBoolean("Important.bedrock_on_protocollib")
                    && protocol.isBedrockPlayer()) {
                return;
            }
            Location blockLocation = packet.getBlockPositionModifier()
                    .read(0)
                    .toLocation(protocol.getWorld());

            CheckThread.run(() -> {
                protocol.getComponentY().pistonHandle = true;

                if (isPlayerInBox(protocol.getLocation(), blockLocation, 5)) {
                    protocol.getComponentY().pistonTick = true;
                    protocol.getComponentXZ().pistonTick = true;
                    protocol.pistonTick = true;
                }
            });
        }
    }

    private boolean isPlayerInBox(Location playerLocation, Location centerLocation, int boxSize) {
        return Math.abs(playerLocation.getX() - centerLocation.getX()) <= boxSize
                && Math.abs(playerLocation.getY() - centerLocation.getY()) <= boxSize
                && Math.abs(playerLocation.getZ() - centerLocation.getZ()) <= boxSize;
    }
}