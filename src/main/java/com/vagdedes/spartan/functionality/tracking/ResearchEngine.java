package com.vagdedes.spartan.functionality.tracking;

import com.vagdedes.spartan.Register;
import com.vagdedes.spartan.abstraction.check.Check;
import com.vagdedes.spartan.abstraction.check.CheckExecutor;
import com.vagdedes.spartan.abstraction.check.DetectionExecutor;
import com.vagdedes.spartan.abstraction.check.PlayerViolation;
import com.vagdedes.spartan.abstraction.inventory.implementation.MainMenu;
import com.vagdedes.spartan.abstraction.pattern.Pattern;
import com.vagdedes.spartan.abstraction.player.SpartanPlayer;
import com.vagdedes.spartan.abstraction.profiling.MiningHistory;
import com.vagdedes.spartan.abstraction.profiling.PlayerProfile;
import com.vagdedes.spartan.abstraction.protocol.SpartanProtocol;
import com.vagdedes.spartan.functionality.connection.cloud.CloudBase;
import com.vagdedes.spartan.functionality.inventory.InteractiveInventory;
import com.vagdedes.spartan.functionality.notifications.AwarenessNotifications;
import com.vagdedes.spartan.functionality.server.Config;
import com.vagdedes.spartan.functionality.server.Permissions;
import com.vagdedes.spartan.functionality.server.SpartanBukkit;
import com.vagdedes.spartan.functionality.server.TPS;
import com.vagdedes.spartan.utils.java.TimeUtils;
import com.vagdedes.spartan.utils.math.AlgebraUtils;
import com.vagdedes.spartan.utils.math.statistics.StatisticsMath;
import me.vagdedes.spartan.system.Enums;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ResearchEngine {

    private static boolean firstLoad = false;
    public static Map<Enums.HackType, Boolean> violationFired = new ConcurrentHashMap<>();
    private static long schedulerTicks = 0L;
    private static final Map<String, PlayerProfile> playerProfiles = new ConcurrentHashMap<>();

    static {
        SpartanBukkit.runRepeatingTask(() -> {
            if (firstLoad) {
                if (schedulerTicks == 0) {
                    schedulerTicks = 1200L;

                    if (Config.sql.isEnabled()) {
                        refresh(Register.isPluginEnabled());
                    } else {
                        SpartanBukkit.analysisThread.executeIfFree(() -> {
                            updateCache(false);
                            MainMenu.refresh();
                        });
                    }
                } else {
                    schedulerTicks -= 1;

                    SpartanBukkit.analysisThread.executeIfFree(() -> {
                        updateCache(false);

                        if (schedulerTicks % TPS.maximum == 0) {
                            MainMenu.refresh();
                        }
                    });
                }
            }
        }, 1L, 1L);
    }

    // Separator

    public static List<PlayerProfile> getPlayerProfiles() {
        return new ArrayList<>(playerProfiles.values());
    }

    public static PlayerProfile getPlayerProfile(String name, boolean create) {
        name = name.toLowerCase();
        PlayerProfile playerProfile = playerProfiles.get(name);

        if (playerProfile != null) {
            return playerProfile;
        }
        if (create) {
            playerProfile = new PlayerProfile(name);
            playerProfiles.put(name, playerProfile);
            return playerProfile;
        } else {
            return null;
        }
    }

    public static PlayerProfile getPlayerProfile(SpartanProtocol protocol, boolean force) {
        PlayerProfile playerProfile;

        if (!force) {
            playerProfile = playerProfiles.get(protocol.player.getName().toLowerCase());

            if (playerProfile != null) {
                playerProfile.updateOfflinePlayer(protocol);
                return playerProfile;
            }
        }
        playerProfile = new PlayerProfile(protocol);
        playerProfiles.put(protocol.player.getName().toLowerCase(), playerProfile);
        return playerProfile;
    }

    // Separator

    public static void resetData(Enums.HackType hackType) {
        if (firstLoad) {
            SpartanBukkit.analysisThread.execute(() -> {
                String hackTypeString = hackType.toString();

                // Separator

                if (!playerProfiles.isEmpty()) {
                    for (PlayerProfile playerProfile : playerProfiles.values()) {
                        for (DetectionExecutor detectionExecutor : playerProfile.getExecutor(hackType).getDetections()) {
                            detectionExecutor.clearProbability();
                        }
                    }
                    updateCache(true);
                }

                // Separator

                if (Config.sql.isEnabled()) {
                    Config.sql.update("DELETE FROM " + Config.sql.getTable() + " WHERE functionality = '" + hackTypeString + "';");
                }

                // Separator

                Collection<File> files = getFiles();

                if (!files.isEmpty()) {
                    for (File file : files) {
                        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

                        for (String key : configuration.getKeys(false)) {
                            String value = configuration.getString(key);

                            if (value != null && value.contains(hackTypeString)) {
                                configuration.set(key, null);
                            }
                        }
                        try {
                            configuration.save(file);
                        } catch (Exception ignored) {
                        }
                    }
                }

                // Separator
                MainMenu.refresh();
            });
        }
    }

    public static void resetData(String playerName) {
        if (firstLoad) {
            // Clear Violations
            SpartanProtocol p = SpartanBukkit.getProtocol(playerName);
            boolean foundPlayer = p != null;
            PlayerProfile profile;

            if (foundPlayer) {
                profile = p.getProfile();
            } else {
                profile = getPlayerProfile(playerName, true);
            }
            Pattern.deleteFromFile(profile);

            if (isStorageMode()) {
                // Clear Files/Database
                SpartanBukkit.analysisThread.execute(() -> {
                    playerProfiles.remove(playerName);

                    if (foundPlayer) {
                        p.setProfile(getPlayerProfile(p, true));
                    }
                    if (Config.sql.isEnabled()) {
                        Config.sql.update("DELETE FROM " + Config.sql.getTable() + " WHERE information LIKE '%" + playerName + "%';");
                    }
                    Collection<File> files = getFiles();

                    if (!files.isEmpty()) {
                        for (File file : files) {
                            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

                            for (String key : configuration.getKeys(false)) {
                                String value = configuration.getString(key);

                                if (value != null && value.contains(playerName)) {
                                    configuration.set(key, null);
                                }
                            }
                            try {
                                configuration.save(file);
                            } catch (Exception ignored) {
                            }
                        }
                    }
                });
            } else {
                playerProfiles.remove(playerName);

                if (foundPlayer) {
                    p.setProfile(getPlayerProfile(p, true));
                }
            }
            MainMenu.refresh();
            InteractiveInventory.playerInfo.refresh(playerName);
        }
    }

    // Separator

    private static String getDetectionInformation(String s) {
        String find = "(" + CheckExecutor.detectionIdentifier + " ";
        int index = s.indexOf(find);

        if (index > -1) {
            s = s.substring(index + find.length());
            index = s.indexOf(")");
            return s.substring(0, index);
        }
        return null;
    }

    private static Double getDetectionViolationLevel(String s) {
        String search = "(" + CheckExecutor.violationLevelIdentifier + " ";
        int index1 = s.indexOf(search);

        if (index1 > -1) {
            s = s.substring(index1 + search.length());
            int index2 = s.indexOf(")");

            if (index2 != -1) {
                String number = s.substring(0, index2);
                String[] split = number.split("\\+", 3);

                if (split.length == 2) {
                    if (AlgebraUtils.validInteger(split[1])) { // Old
                        return Double.parseDouble(split[1]);
                    }
                } else if (AlgebraUtils.validDecimal(number) // New
                        || AlgebraUtils.validInteger(number)) { // Oldest
                    return Double.parseDouble(number);
                }
            }
        }
        return null;
    }

    private static Check.DataType getDataType(String s) {
        String find = "(" + CheckExecutor.javaPlayerIdentifier + " ";
        int index = s.indexOf(find);

        if (index > -1) {
            s = s.substring(index + find.length());
            index = s.indexOf(")");
            s = s.substring(0, index);

            for (Check.DataType dataType : Check.DataType.values()) {
                if (s.equals(dataType.toString())) {
                    return dataType;
                }
            }
        }
        return Check.DataType.JAVA;
    }

    private static boolean isStorageMode() {
        return Config.settings.getBoolean("Logs.log_file") || Config.sql.isEnabled();
    }

    private static Collection<File> getFiles() {
        File[] files = new File(AntiCheatLogs.folderPath).listFiles();

        if (files != null && files.length > 0) {
            TreeMap<Integer, File> map = new TreeMap<>();
            String start = "log", end = ".yml";
            int startLength = start.length(), endLength = end.length();

            for (File file : files) {
                if (file.isFile()) {
                    String name = file.getName();

                    if (name.startsWith(start) && name.endsWith(end)) {
                        Integer integer = AlgebraUtils.returnValidInteger(name.substring(startLength, name.length() - endLength));

                        if (integer != null) {
                            map.put(integer, file);
                        }
                    }
                }
            }
            List<File> list = new LinkedList<>(map.values());
            Collections.reverse(list);
            return list;
        }
        return new ArrayList<>(0);
    }

    private static Map<String, String> getLogs() {
        Map<String, String> cache = new LinkedHashMap<>();
        int byteSize = 0;
        boolean isFull = false,
                continueWithYAML = false;

        // Separator

        if (Config.sql.isEnabled()) {
            try {
                ResultSet rs = Config.sql.query("SELECT creation_date, information FROM " + Config.sql.getTable() + " ORDER BY id DESC LIMIT " + SpartanBukkit.maxSQLRows + ";");

                if (rs != null) {
                    while (rs.next()) {
                        String data = rs.getString("information");
                        String[] all = TimeUtils.getAll(rs.getTimestamp("creation_date"));
                        String date = all[0] + " " + all[1];
                        cache.put(date, data);
                        byteSize += date.length() + data.length();

                        if (byteSize >= SpartanBukkit.maxBytes) {
                            isFull = true;
                            break;
                        }
                    }
                    rs.close();

                    if (cache.isEmpty()) {
                        continueWithYAML = true;
                    }
                } else {
                    continueWithYAML = true;
                }
            } catch (Exception ex) {
                continueWithYAML = true;
            }
        } else {
            continueWithYAML = true;
        }

        // YAML Process

        if (!isFull && continueWithYAML) {
            Collection<File> files = getFiles();

            if (!files.isEmpty()) {
                for (File file : files) {
                    YamlConfiguration c = YamlConfiguration.loadConfiguration(file);

                    for (String key : c.getKeys(false)) {
                        String data = c.getString(key);

                        if (data != null) {
                            cache.put(key, data);
                            byteSize += key.length() + data.length();

                            if (byteSize >= SpartanBukkit.maxBytes) {
                                isFull = true;
                                break;
                            }
                        }
                    }
                    if (isFull) {
                        break;
                    }
                }
            }
        }
        return cache;
    }

    // Separator

    public static void refresh(boolean enabledPlugin) {
        Runnable runnable = () -> {
            // Complete Storage
            Config.sql.refreshDatabase();

            if (enabledPlugin) {
                buildCache();
                Pattern.reload();
                CloudBase.refresh();
            } else {
                CloudBase.clear(true);
                Pattern.clear();
            }
        };

        if (firstLoad) {
            SpartanBukkit.analysisThread.executeIfFree(runnable);
        } else {
            SpartanBukkit.analysisThread.execute(runnable);
            firstLoad = true;
        }
    }

    private static void buildCache() {
        if (isStorageMode()) {
            Map<String, String> logs = getLogs();

            if (!logs.isEmpty()) {
                for (Map.Entry<String, String> entry : logs.entrySet()) {
                    try {
                        String fullDate = entry.getKey(),
                                partialDate = fullDate.substring(0, 10),
                                data = entry.getValue();

                        if (data.contains(" failed ")) {
                            String[] split = data.split(" ");

                            if (split.length >= 3) {
                                String hackTypeString = split[2];

                                for (Enums.HackType hackType : Enums.HackType.values()) {
                                    if (hackTypeString.equals(hackType.toString())) {
                                        String detection = getDetectionInformation(data);

                                        if (detection != null) {
                                            Double violationIncrease = getDetectionViolationLevel(data);

                                            if (violationIncrease != null) {
                                                PlayerProfile profile = getPlayerProfile(split[0], true);
                                                DetectionExecutor detectionExecutor = profile.getExecutor(hackType).getDetection(detection);

                                                if (detectionExecutor != null) {
                                                    SimpleDateFormat sdf = new SimpleDateFormat(AntiCheatLogs.dateFormat);
                                                    detectionExecutor.store(
                                                            new PlayerViolation(
                                                                    sdf.parse(fullDate).getTime(),
                                                                    violationIncrease
                                                            )
                                                    );
                                                }
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                        } else if (data.contains(" found ")) {
                            String[] split = data.split(" ");

                            if (split.length >= 9) {
                                String name = split[0].toLowerCase();

                                try {
                                    MiningHistory.MiningOre ore = MiningHistory.getMiningOre(
                                            Material.getMaterial(
                                                    split[3].toUpperCase().replace("-", "_")
                                            )
                                    );

                                    if (ore != null) {
                                        MiningHistory miningHistory = getPlayerProfile(name, true).getMiningHistory(ore);

                                        if (miningHistory != null) {
                                            World.Environment environment = World.Environment.valueOf(
                                                    split[8].toUpperCase().replace("-", "_")
                                            );
                                            miningHistory.increaseMines(environment, 1, partialDate);
                                        }
                                    }
                                } catch (Exception ignored) {
                                }
                            }
                        }
                    } catch (Exception ignored) {

                    }
                }
                updateCache(true);
                MainMenu.refresh();
            }
        }
    }

    private static void updateCache(boolean force) {
        if ((force || !violationFired.isEmpty()) && !playerProfiles.isEmpty()) {
            Collection<PlayerProfile> profiles = playerProfiles.values();

            if (force) {
                for (PlayerProfile profile : profiles) {
                    for (CheckExecutor executor : profile.getExecutors()) {
                        for (DetectionExecutor detectionExecutor : executor.getDetections()) {
                            detectionExecutor.clearProbability();
                        }
                    }
                }
            }

            for (Enums.HackType hackType : (force
                    ? Arrays.asList(Enums.HackType.values())
                    : violationFired.keySet())) {
                if (!hackType.getCheck().isEnabled(null, null)) {
                    continue;
                }
                Map<String, Map<DetectionExecutor, Double>> wave = new LinkedHashMap<>();

                for (PlayerProfile profile : profiles) {
                    for (DetectionExecutor detectionExecutor : profile.getExecutor(hackType).getDetections()) {
                        Double timeDifference = detectionExecutor.getTimeDifference();

                        if (timeDifference != null) {
                            wave.computeIfAbsent(
                                    detectionExecutor.name,
                                    k -> new LinkedHashMap<>()
                            ).put(
                                    detectionExecutor,
                                    timeDifference
                            );
                        }
                    }
                }

                if (!wave.isEmpty()) {
                    for (Map<DetectionExecutor, Double> map : wave.values()) {
                        double sum = 0,
                                squareSum = 0,
                                min = Double.MAX_VALUE,
                                max = Double.MIN_VALUE;

                        for (double value : map.values()) {
                            sum += value;
                            squareSum += value * value;
                        }
                        double divisor = wave.size(),
                                mean = sum / divisor,
                                deviation = Math.sqrt(squareSum / divisor);

                        for (Map.Entry<DetectionExecutor, Double> entry : map.entrySet()) {
                            double probability = StatisticsMath.getCumulativeProbability(
                                    (entry.getValue() - mean) / deviation
                            );
                            entry.setValue(probability);

                            if (probability > max) {
                                max = probability;
                            }
                            if (probability < min) {
                                min = probability;
                            }
                        }
                        max = 1.0 - max;
                        double distributionDifference = Math.abs(max - min);

                        for (Map.Entry<DetectionExecutor, Double> entry : map.entrySet()) {
                            entry.getKey().setProbability(
                                    PlayerEvidence.modifyProbability(
                                            entry.getValue(),
                                            Math.max(min - distributionDifference, 0.0),
                                            Math.max(max - distributionDifference, 0.0)
                                    )
                            );
                        }
                    }
                }
            }
            violationFired.clear();
        }
        if (SpartanBukkit.packetsEnabled()) {
            for (Enums.HackType hackType : Enums.HackType.values()) {
                Check check = hackType.getCheck();

                if (check.isEnabled(null, null)
                        && !check.isSilent(null, null)) {
                    String message = AwarenessNotifications.getOptionalNotification(
                            "Spartan does not support Java full player preventions when running on the packet level."
                                    + " This is due to problems risen from trying to implement this functionality with packets."
                    );

                    if (message != null) {
                        List<SpartanPlayer> players = Permissions.getStaff();

                        if (!players.isEmpty()) {
                            for (SpartanPlayer p : players) {
                                if (AwarenessNotifications.canSend(p.protocol.getUUID(), "packet-preventions", 60 * 60)) {
                                    p.getInstance().sendMessage(message);
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    public static void queueToCache(Enums.HackType hackType) {
        violationFired.put(hackType, true);
    }

    public static int getRequiredPlayers(Enums.HackType hackType, double probability) {
        int requirement = PlayerEvidence.probabilityToFactors(probability),
                count = 0;

        if (playerProfiles.size() >= requirement) {
            for (PlayerProfile profile : playerProfiles.values()) {
                if (profile.hasData(hackType)) {
                    count++;

                    if (count == requirement) {
                        return 0;
                    }
                }
            }
            count = requirement - count;
        } else {
            count = requirement - playerProfiles.size();
        }
        return count;
    }

    public static int getRequiredPlayers(Enums.HackType hackType, String detection, double probability) {
        int requirement = PlayerEvidence.probabilityToFactors(probability),
                count = 0;

        if (playerProfiles.size() >= requirement) {
            for (PlayerProfile profile : playerProfiles.values()) {
                if (profile.getExecutor(hackType).getDetection(detection).hasDataToCompare()) {
                    count++;

                    if (count == requirement) {
                        return 0;
                    }
                }
            }
            count = requirement - count;
        } else {
            count = requirement - playerProfiles.size();
        }
        return count;
    }

}
