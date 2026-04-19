package ai.idealistic.spartan.compatibility.necessary.protocollib;

import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.listeners.protocol.*;
import ai.idealistic.spartan.listeners.protocol.combat.CombatListener;
import ai.idealistic.spartan.listeners.protocol.combat.LegacyCombatListener;
import ai.idealistic.spartan.listeners.protocol.standalone.EntityActionListener;
import ai.idealistic.spartan.listeners.protocol.standalone.JoinListener;
import ai.idealistic.spartan.utils.minecraft.entity.PlayerUtils;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;

import java.util.ArrayList;
import java.util.List;

public class BackPlib {

    private static final List<String> packets = new ArrayList<>();

    static boolean isPacketSupported(String packet) {
        return packets.contains(packet);
    }

    private static void handle() {
        for (PacketType type : PacketType.Play.Client.getInstance()) {
            packets.add(type.name());
        }
    }

    static void run() {
        handle();
        ProtocolManager p = ProtocolLibrary.getProtocolManager();
        p.addPacketListener(new JoinListener());
        p.addPacketListener(new EntityActionListener());
        p.addPacketListener(new VelocityListener());

        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)) {
            p.addPacketListener(new CombatListener());
        } else {
            p.addPacketListener(new LegacyCombatListener());
        }

        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
            p.addPacketListener(new MultiBlockListener());
        } else {
            p.addPacketListener(new MultiBlockLegacyListener());
        }

        p.addPacketListener(new MovementListener());
        p.addPacketListener(new TeleportListener());
        p.addPacketListener(new VehicleHandle());
        p.addPacketListener(new DeathListener());
        //p.addPacketListener(new BlockPlaceBalancerListener());
        p.addPacketListener(new BlockPlaceListener());
        p.addPacketListener(new ClicksListener());
        p.addPacketListener(new PacketPistonHandle());
        p.addPacketListener(new ExplosionListener());
        p.addPacketListener(new PacketServerBlockHandle());
        p.addPacketListener(new PacketLatencyHandler());
        p.addPacketListener(new AbilitiesListener());
        p.addPacketListener(new UseItemStatusHandle());
        p.addPacketListener(new UseEntityListener());

        if (PlayerUtils.trident) {
            p.addPacketListener(new HandUseListener());
        }
        //p.addPacketListener(new PacketDebug());
    }

    public static double getSafeDouble(PacketContainer packet, int index) {
        try {
            return packet.getDoubles().read(index);
        } catch (Throwable t) {
            return (double) packet.getModifier().read(index);
        }
    }

    public static int getSafeInt(PacketContainer packet, int index) {
        try {
            return packet.getIntegers().read(index);
        } catch (Throwable t) {
            return (int) packet.getModifier().read(index);
        }
    }

    public static int[] getSafeIntArray(PacketContainer packet, int index) {
        try {
            return packet.getIntegerArrays().read(index);
        } catch (Throwable t) {
            return (int[]) packet.getModifier().read(index);
        }
    }

    public static float getSafeFloat(PacketContainer packet, int index) {
        try {
            return packet.getFloat().read(index);
        } catch (Throwable t) {
            return (float) packet.getModifier().read(index);
        }
    }

    public static long getSafeLong(PacketContainer packet, int index) {
        try {
            return packet.getLongs().read(index);
        } catch (Throwable t) {
            return (long) packet.getModifier().read(index);
        }
    }

    public static short getSafeShort(PacketContainer packet, int index) {
        try {
            return packet.getShorts().read(index);
        } catch (Throwable t) {
            return (short) packet.getModifier().read(index);
        }
    }

    public static short[] getSafeShortArray(PacketContainer packet, int index) {
        try {
            return packet.getShortArrays().read(index);
        } catch (Throwable t) {
            return (short[]) packet.getModifier().read(index);
        }
    }

    public static boolean getSafeBoolean(PacketContainer packet, int index) {
        try {
            return packet.getBooleans().read(index);
        } catch (Throwable t) {
            return (boolean) packet.getModifier().read(index);
        }
    }

    public static byte getSafeByte(PacketContainer packet, int index) {
        try {
            return packet.getBytes().read(index);
        } catch (Throwable t) {
            return (byte) packet.getModifier().read(index);
        }
    }

    public static byte[] getSafeByteArray(PacketContainer packet, int index) {
        try {
            return packet.getByteArrays().read(index);
        } catch (Throwable t) {
            return (byte[]) packet.getModifier().read(index);
        }
    }

    public static String getSafeString(PacketContainer packet, int index) {
        try {
            return packet.getStrings().read(index);
        } catch (Throwable t) {
            return (String) packet.getModifier().read(index);
        }
    }

    public static String[] getSafeStringArray(PacketContainer packet, int index) {
        try {
            return packet.getStringArrays().read(index);
        } catch (Throwable t) {
            return (String[]) packet.getModifier().read(index);
        }
    }

}
