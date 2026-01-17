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
import com.comphenix.protocol.wrappers.MultiBlockChangeInfo;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public class MultiBlockLegacyListener extends PacketAdapter {
    public MultiBlockLegacyListener() {
        super(Register.plugin, ListenerPriority.HIGHEST,
                PacketType.Play.Server.MULTI_BLOCK_CHANGE);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        Player player = event.getPlayer();
        PlayerProtocol protocol = PluginBase.getProtocol(player);
        World world = player.getWorld();
        PacketContainer packet = event.getPacket();

        if (packet.getMultiBlockChangeInfoArrays().getValues().isEmpty()) {
            return;
        }
        List<MultiBlockChangeInfo[]> mList = packet.getMultiBlockChangeInfoArrays().getValues();

        CheckThread.run(() -> {
            for (MultiBlockChangeInfo[] m : mList) {
                for (MultiBlockChangeInfo info : m) {
                    Material material = info.getData().getType();
                    if (material.toString().equals("SLIME_BLOCK")) {
                        if (info.getLocation(world).distance(protocol.getLocation()) < 6) {
                            protocol.predictedSlimeTicks = 6;
                        }
                    }
                }
            }
        });
    }
}