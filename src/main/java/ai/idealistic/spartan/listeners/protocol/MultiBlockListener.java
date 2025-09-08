package ai.idealistic.spartan.listeners.protocol;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.PluginBase;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class MultiBlockListener extends PacketAdapter {
    public MultiBlockListener() {
        super(Register.plugin, ListenerPriority.HIGHEST, PacketType.Play.Server.MULTI_BLOCK_CHANGE);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        Player player = event.getPlayer();
        PlayerProtocol protocol = PluginBase.getProtocol(player);
        World world = player.getWorld();
        PacketContainer packet = event.getPacket();

        BlockPosition b = packet.getSectionPositions().read(0);
        int chunkX = b.getX();
        int chunkZ = b.getZ();
        int chunkY = b.getY();
        short[] records = packet.getShortArrays().read(0);
        Object[] blockData = packet.getSpecificModifier(Object[].class).read(0);

        CheckThread.run(() -> {
            for (int i = 0; i < records.length; i++) {
                short record = records[i];
                int relX = (record >> 8) & 0xF;
                int relY = (record) & 0xF;
                int relZ = (record >> 4) & 0xF;

                int worldX = (chunkX << 4) | relX;
                int worldY = (chunkY << 4) | relY;
                int worldZ = (chunkZ << 4) | relZ;

                final Location loc = new Location(
                        world,
                        ((chunkX * 16) + worldX) / 2.0,
                        worldY,
                        ((chunkZ * 16) + worldZ) / 2.0
                );

                final Object nmsData = blockData[i];

                if (nmsData.toString().toLowerCase().contains("slime_block")
                        && loc.distance(protocol.getLocation()) < 14) {
                    CheckThread.run(() -> protocol.predictedSlimeTicks = 6);
                }
            }
        });
    }
}