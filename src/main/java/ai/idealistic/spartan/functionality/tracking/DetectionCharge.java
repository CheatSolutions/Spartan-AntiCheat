package ai.idealistic.spartan.functionality.tracking;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.data.Buffer;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.connection.PluginAddons;
import ai.idealistic.spartan.functionality.moderation.AwarenessNotifications;
import ai.idealistic.spartan.functionality.server.Permissions;
import ai.idealistic.spartan.utils.java.OverflowMap;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DetectionCharge {

    private static final long
            notify = 1_000L * 60L * 60L, // 1 hour
            add = 1_000L * 5L; // 5 seconds
    private static long charge = System.currentTimeMillis() + (notify * 2L); // 2 hours head start
    private static final Map<UUID, Buffer.IndividualBuffer> map = new OverflowMap<>(
            new ConcurrentHashMap<>(),
            128
    );

    public static boolean charge(PlayerProtocol protocol) {
        if (PluginAddons.isFreeEdition()) {
            Buffer.IndividualBuffer buffer = map.computeIfAbsent(
                    protocol.getUUID(),
                    uuid -> new Buffer.IndividualBuffer()
            );

            if (buffer.count(1, 20) <= 10) { // 10 cps
                if (charge > System.currentTimeMillis()) {
                    charge += add;
                } else {
                    charge = System.currentTimeMillis() + add;
                }
                return true;
            }
        }
        return false;
    }

    public static boolean has() {
        return !PluginAddons.isFreeEdition() || charge > System.currentTimeMillis();
    }

    public static long remaining() {
        return Math.max(charge - System.currentTimeMillis(), 0L);
    }

    public static boolean isLow() {
        return PluginAddons.isFreeEdition() && remaining() <= notify;
    }

    public static void notify(PlayerProtocol protocol) {
        if (isLow()
                && Permissions.isStaff(protocol.bukkit())) {
            AwarenessNotifications.forcefullySend(
                    protocol,
                    "Detections are running low on charge"
                            + ", run \"/" + Register.pluginName.toLowerCase() + " charge\" in-game to add more.",
                    true
            );
        }
    }

}
