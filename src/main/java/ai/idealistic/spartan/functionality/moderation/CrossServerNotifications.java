package ai.idealistic.spartan.functionality.moderation;

import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.functionality.tracking.ResearchEngine;
import ai.idealistic.spartan.utils.java.OverflowMap;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CrossServerNotifications {

    private static final int rowLimit = CheckEnums.HackType.values().length;
    private static final Map<Long, Boolean> processed = new OverflowMap<>(
            new ConcurrentHashMap<>(),
            rowLimit
    );

    static {
        PluginBase.runRepeatingTask(() -> PluginBase.connectionThread.executeIfFree(() -> {
            if (Config.sql.isEnabled()) {
                List<PlayerProtocol> protocols = DetectionNotifications.getPlayers();

                if (!protocols.isEmpty()) {
                    run(protocols);
                }
            }
        }), 1L, 1L);
    }

    @SneakyThrows
    private static void run(List<PlayerProtocol> protocols) {
        @Cleanup
        ResultSet rs = Config.sql.query("SELECT "
                + "id, player_name, server_name, notification, information, functionality"
                + " FROM " + Config.sql.getTable()
                + " WHERE notification IS NOT NULL"
                + " ORDER BY id DESC LIMIT " + rowLimit + ";");

        if (rs != null) {
            while (rs.next()) {
                long id = rs.getLong("id");

                if (!processed.containsKey(id)) {
                    String functionality = rs.getString("functionality"),
                            playerName = rs.getString("player_name");

                    if (playerName != null) {
                        for (CheckEnums.HackType hackType : CheckEnums.HackType.values()) {
                            if (hackType.toString().equals(functionality)) {
                                String notification = rs.getString("notification"),
                                        serverName = rs.getString("server_name"),
                                        detection = ResearchEngine.findInformation(
                                                rs.getString("information"),
                                                CheckDetection.detectionIdentifier
                                        ),
                                        levelString = ResearchEngine.findInformation(
                                                rs.getString("information"),
                                                CheckDetection.levelIdentifier
                                        ),
                                        notificationString = ResearchEngine.findInformation(
                                                rs.getString("information"),
                                                CheckDetection.notificationLevelIdentifier
                                        );

                                if (levelString != null
                                        && notificationString != null) {
                                    notification = "§l[" + serverName + "]§r " + notification;

                                    for (PlayerProtocol protocol : protocols) {
                                        CheckDetection
                                                staffDetection = protocol.getRunner(hackType).getDetection(detection);

                                        if (staffDetection != null
                                                && staffDetection.canSendNotification(
                                                System.currentTimeMillis(),
                                                Integer.parseInt(levelString),
                                                Integer.parseInt(notificationString)
                                        )) {
                                            protocol.bukkit().sendMessage(notification);
                                            processed.put(id, true);
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static String getServerName() {
        return Config.settings.getString("Important.server_name");
    }

}
