package ai.idealistic.spartan.functionality.connection;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.moderation.AwarenessNotifications;
import ai.idealistic.spartan.functionality.server.Permissions;
import ai.idealistic.spartan.functionality.server.PluginBase;

import java.util.List;

public class CloudBase {

    // Functionality
    private static long
            connectionRefreshCooldown = 0L,
            connectionFailedCooldown = 0L;
    private static final long refreshTime = 60_000L;

    // Parameters
    static final String separator = ">@#&!%<;=";

    static {
        PluginBase.runRepeatingTask(
                () -> PluginBase.connectionThread.execute(CloudBase::refresh),
                1L,
                refreshTime
        );
    }

    public static void announce(PlayerProtocol protocol) {
        if (Permissions.isStaff(protocol.bukkit())) {
            PluginBase.connectionThread.execute(() -> {
                String[][] announcements = CloudConnections.getStaffAnnouncements();

                if (announcements.length == 3) {
                    for (String[] announcement : announcements) {
                        if (AwarenessNotifications.canSend(
                                protocol.getUUID(),
                                "staff-announcement-" + announcement[0],
                                Integer.parseInt(announcement[2])
                        )) {
                            protocol.bukkit().sendMessage(AwarenessNotifications.getNotification(announcement[1]));
                        }
                    }
                }
            });
        }
    }

    // Separator

    public static void throwError(Exception object, String function) {
        long ms = System.currentTimeMillis();

        if (connectionFailedCooldown >= ms) {
            connectionFailedCooldown = ms + refreshTime;
            String message = "(" + function + ") Failed to connect to the Game Cloud."
                    + "\nError: " + object.getMessage()
                    + "\nIn Depth: " + object;
            AwarenessNotifications.optionallySend(message);
        }
    }

    public static String identification() {
        return "identification=" + IDs.platform() + "|" + IDs.user() + "|" + IDs.file();
    }

    public static void refresh() {
        long ms = System.currentTimeMillis();

        if (connectionRefreshCooldown <= ms) {
            connectionRefreshCooldown = ms + refreshTime;

            // Separator
            PluginBase.connectionThread.executeIfUnknownThreadElseHere(PluginAddons::refresh);

            // Separator
            PluginBase.connectionThread.executeIfUnknownThreadElseHere(() -> {
                List<PlayerProtocol> protocols = Permissions.getStaff();

                if (!protocols.isEmpty()) {
                    String[][] announcements = CloudConnections.getStaffAnnouncements();

                    if (announcements.length == 3) {
                        for (String[] announcement : announcements) {
                            for (PlayerProtocol p : protocols) {
                                if (AwarenessNotifications.canSend(
                                        p.getUUID(),
                                        "staff-announcement-" + announcement[0],
                                        Integer.parseInt(announcement[2])
                                )) {
                                    p.bukkit().sendMessage(AwarenessNotifications.getNotification(announcement[1]));
                                }
                            }
                        }
                    }
                }
            });

        }
    }

}
