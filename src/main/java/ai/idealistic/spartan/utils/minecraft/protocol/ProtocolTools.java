package ai.idealistic.spartan.utils.minecraft.protocol;

import ai.idealistic.spartan.compatibility.necessary.protocollib.BackPlib;
import ai.idealistic.spartan.compatibility.necessary.protocollib.ProtocolLib;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.listeners.protocol.MovementListener;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class ProtocolTools {

    public static Location readLocation(PacketEvent event) {
        PacketContainer packet = event.getPacket();

        if (packet.getDoubles().size() >= 3) {
            return new Location(
                    ProtocolLib.getWorld(event.getPlayer()),
                    BackPlib.getSafeDouble(packet, 0),
                    BackPlib.getSafeDouble(packet, 1),
                    BackPlib.getSafeDouble(packet, 2)
            );
        } else {
            return null;
        }
    }

    public static boolean isFlying(PacketEvent event, Location to, Location from) {
        PacketType p = event.getPacket().getType();

        if (p.equals(PacketType.Play.Client.POSITION)
                && to.toVector().equals(from.toVector())) {
            return true;
        } else {
            return p.equals(MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)
                    ? PacketType.Play.Client.GROUND
                    : PacketType.Play.Client.FLYING);
        }
    }

    public static boolean onGroundPacketLevel(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        return packet.getBooleans().size() > 0
                && BackPlib.getSafeBoolean(packet, 0);
    }

    public static Set<MovementListener.tpFlags> getTeleportFlags(PacketEvent event) {
        String s = event.getPacket().getStructures().read(0).toString();
        Set<MovementListener.tpFlags> flags = new HashSet<>(3);
        s = s.replace("X_ROT", "").replace("Y_ROT", "");
        if (s.contains("X")) flags.add(MovementListener.tpFlags.X);
        if (s.contains("Y")) flags.add(MovementListener.tpFlags.Y);
        if (s.contains("Z")) flags.add(MovementListener.tpFlags.Z);
        return flags;
    }

    public static boolean isLoadLocation(Location location) {
        return (location.getX() == 1 && location.getY() == 1 && location.getZ() == 1);
    }

    public static Location getLoadLocation(Player player) {
        return new Location(ProtocolLib.getWorld(player), 1, 1, 1);
    }

    public static boolean hasPosition(PacketType type) {
        return (type.equals(PacketType.Play.Client.POSITION)
                || type.equals(PacketType.Play.Client.POSITION_LOOK));
    }

    public static boolean hasRotation(PacketType type) {
        return (type.equals(PacketType.Play.Client.LOOK)
                || type.equals(PacketType.Play.Client.POSITION_LOOK));
    }
}
