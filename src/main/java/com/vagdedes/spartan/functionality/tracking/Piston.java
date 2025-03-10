package com.vagdedes.spartan.functionality.tracking;

import com.vagdedes.spartan.abstraction.protocol.PlayerBukkit;
import com.vagdedes.spartan.abstraction.protocol.PlayerProtocol;
import com.vagdedes.spartan.abstraction.protocol.PlayerTrackers;
import com.vagdedes.spartan.functionality.server.PluginBase;
import com.vagdedes.spartan.functionality.server.TPS;
import com.vagdedes.spartan.utils.math.AlgebraUtils;
import com.vagdedes.spartan.utils.minecraft.entity.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Collection;
import java.util.List;

public class Piston {

    private static final double
            horizontalDistance = 3.0,
            verticalDistance = 2.0;

    public static void run(Block block, List<Block> blocks) {
        Collection<PlayerProtocol> protocols = PluginBase.getProtocols();

        if (!protocols.isEmpty()) {
            boolean runBlocks = !blocks.isEmpty();
            World world = block.getWorld();

            for (PlayerProtocol protocol : protocols) {
                if (protocol.getWorld().equals(world)) {
                    Location location = protocol.getLocationOrVehicle();
                    double preX = AlgebraUtils.getSquare(location.getX(), block.getX()),
                            diffY = location.getY() - block.getY(),
                            preZ = AlgebraUtils.getSquare(location.getZ(), block.getZ());

                    if (!run(protocol.bukkitExtra, preX, diffY, preZ) // Check if the player is nearby to the piston
                            && runBlocks
                            && Math.sqrt(preX + (diffY * diffY) + preZ) <= PlayerUtils.chunk) { // Check if the player is nearby to the piston affected blocks
                        for (Block affected : blocks) {
                            preX = AlgebraUtils.getSquare(location.getX(), affected.getX());
                            diffY = location.getY() - block.getY();
                            preZ = AlgebraUtils.getSquare(location.getZ(), affected.getZ());

                            if (run(protocol.bukkitExtra, preX, diffY, preZ)) { // Check if the player is nearby to the piston affected block
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean run(PlayerBukkit player, double preX, double diffY, double preZ) {
        if (Math.sqrt(preX + preZ) <= horizontalDistance
                && Math.abs(diffY) <= verticalDistance) {
            player.trackers.add(PlayerTrackers.TrackerType.PISTON, AlgebraUtils.integerCeil(TPS.maximum));
            return true;
        }
        return false;
    }
}
