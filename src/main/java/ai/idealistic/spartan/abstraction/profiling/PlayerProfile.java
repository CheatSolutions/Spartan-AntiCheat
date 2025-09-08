package ai.idealistic.spartan.abstraction.profiling;

import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.utils.java.ConcurrentList;
import ai.idealistic.spartan.utils.minecraft.inventory.InventoryUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerProfile {

    public static final String
            activeFor = " was active for: ";

    public final String name;
    private final MiningHistory[] miningHistory;
    private ItemStack skull;
    private OfflinePlayer offlinePlayer;
    @Getter
    @Setter
    private Check.DataType lastDataType;
    @Getter
    private final ProfileContinuity continuity;
    private final Map<String, List<Long>>[][] data;

    // Separator

    public PlayerProfile(String name) {
        this.name = name;
        this.skull = null;
        this.miningHistory = new MiningHistory[MiningHistory.MiningOre.values().length];
        this.lastDataType = Check.DataType.JAVA;
        this.continuity = new ProfileContinuity(this);
        this.data = new Map[CheckEnums.HackType.values().length][Check.DataType.values().length];

        for (CheckEnums.HackType hackType : CheckEnums.HackType.values()) {
            for (Check.DataType dataType : Check.DataType.values()) {
                this.data[hackType.ordinal()][dataType.ordinal()] = new ConcurrentHashMap<>(1);
            }
        }
        for (MiningHistory.MiningOre ore : MiningHistory.MiningOre.values()) {
            this.miningHistory[ore.ordinal()] = new MiningHistory(this, ore);
        }

        // Separator

        PlayerProtocol protocol = PluginBase.getProtocol(name);

        if (protocol != null) {
            this.offlinePlayer = protocol.bukkit();
        } else {
            this.offlinePlayer = null;
        }
    }

    public PlayerProfile(PlayerProtocol protocol) {
        this.name = protocol.bukkit().getName();
        this.offlinePlayer = protocol.bukkit(); // Attention
        this.skull = null;
        this.miningHistory = new MiningHistory[MiningHistory.MiningOre.values().length];
        this.lastDataType = protocol.getDataType();
        this.continuity = new ProfileContinuity(this);
        this.data = new Map[CheckEnums.HackType.values().length][Check.DataType.values().length];

        for (CheckEnums.HackType hackType : CheckEnums.HackType.values()) {
            for (Check.DataType dataType : Check.DataType.values()) {
                this.data[hackType.ordinal()][dataType.ordinal()] = new ConcurrentHashMap<>(1);
            }
        }
        for (MiningHistory.MiningOre ore : MiningHistory.MiningOre.values()) {
            this.miningHistory[ore.ordinal()] = new MiningHistory(this, ore);
        }
    }

    // Separator

    public void update(PlayerProtocol protocol) {
        this.offlinePlayer = protocol.bukkit();
        this.lastDataType = protocol.getDataType();
    }

    PlayerProtocol protocol() {
        return PluginBase.getAnyCaseProtocol(this.name);
    }

    public CheckRunner getRunner(CheckEnums.HackType hackType) {
        PlayerProtocol protocol = this.protocol();
        return protocol == null
                ? null
                : protocol.getRunner(hackType);
    }

    public Check.DataType getOppositeDataType() {
        return this.lastDataType == Check.DataType.JAVA
                ? Check.DataType.BEDROCK
                : Check.DataType.JAVA;
    }

    // Separator

    public ItemStack getSkull() {
        if (this.skull == null) {
            if (this.offlinePlayer == null) {
                return InventoryUtils.getSkull(null, name, false);
            } else {
                return this.skull = InventoryUtils.getSkull(offlinePlayer, name, false);
            }
        } else {
            return this.skull;
        }
    }

    // Separator

    public MiningHistory getMiningHistory(MiningHistory.MiningOre ore) {
        return miningHistory[ore.ordinal()];
    }

    // Separator

    public List<Long> getTimeDifferences(
            CheckEnums.HackType hackType,
            Check.DataType dataType,
            String detection
    ) {
        List<Long> data = this.data[hackType.ordinal()][dataType.ordinal()].get(detection);

        if (data != null) {
            if (!data.isEmpty()) {
                for (Long time : data) {
                    if (time < 0L) {
                        data.remove(time); // Remove negative
                        data.add(Math.abs(time)); // Recover negative as positive
                    }
                }
            }
            return data;
        } else {
            return new ArrayList<>(0);
        }
    }

    public double getAverageTimeDifference(
            CheckEnums.HackType hackType,
            Check.DataType dataType,
            String detection
    ) {
        List<Long> data = this.data[hackType.ordinal()][dataType.ordinal()].get(detection);

        if (data != null
                && !data.isEmpty()) {
            double total = 0.0;

            for (Long time : data) {
                if (time < 0L) {
                    long absTime = Math.abs(time);
                    data.remove(time); // Remove negative
                    data.add(absTime); // Recover negative as positive
                    total += absTime;
                } else {
                    total += time;
                }
            }
            if (total > 0.0) {
                return total / data.size();
            }
        }
        return 0.0;
    }

    public void setTimeDifferences(
            CheckEnums.HackType hackType,
            Check.DataType dataType,
            String detection,
            List<Long> data
    ) {
        List<Long> list = this.data[hackType.ordinal()][dataType.ordinal()].computeIfAbsent(
                detection,
                k -> new ConcurrentList<>()
        );
        list.clear();
        list.addAll(data);
    }

    public void addTimeDifference(
            CheckEnums.HackType hackType,
            Check.DataType dataType,
            String detection,
            long time
    ) {
        List<Long> list = this.data[hackType.ordinal()][dataType.ordinal()].computeIfAbsent(
                detection,
                k -> new ConcurrentList<>()
        );
        int size = list.size() - 1_024;

        if (size > 0) {
            Iterator<Long> iterator = list.iterator();

            while (iterator.hasNext() && size > 0) {
                if (list.remove(iterator.next())) {
                    size--;
                }
            }
        }
        list.add(time);
    }

    public void clearTimeDifferences(
            CheckEnums.HackType hackType,
            Check.DataType dataType,
            String detection
    ) {
        List<Long> list = this.data[hackType.ordinal()][dataType.ordinal()].get(detection);

        if (list != null) {
            list.clear();
        }
    }

    public final void sortTimeDifferences(
            CheckEnums.HackType hackType,
            String detection
    ) {
        for (Check.DataType dataType : Check.DataType.values()) {
            List<Long> data = this.data[hackType.ordinal()][dataType.ordinal()].get(detection);

            if (data != null
                    && !data.isEmpty()) {
                Collections.sort(data);
                Iterator<Long> iterator = data.iterator();

                if (iterator.hasNext()) {
                    List<Long> differences = new ArrayList<>(data.size() - 1);
                    long previous = iterator.next();

                    while (iterator.hasNext()) {
                        long current = iterator.next();

                        if (current >= 0L) { // Check for negative
                            if (previous >= 0L) { // Check for negative
                                if (this.getContinuity().wasOnline(current, previous)) {
                                    differences.add(current - previous);
                                }
                                previous = current;
                            }
                        } else {
                            differences.add(Math.abs(current));
                        }
                    }
                    this.setTimeDifferences(hackType, dataType, detection, differences);
                }
            }
        }
    }

}
