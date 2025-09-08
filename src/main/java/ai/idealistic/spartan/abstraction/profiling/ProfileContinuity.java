package ai.idealistic.spartan.abstraction.profiling;

import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.tracking.AntiCheatLogs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProfileContinuity {

    private final PlayerProfile profile;
    private final Map<Long, Long>[] continuity;

    public ProfileContinuity(PlayerProfile profile) {
        this.profile = profile;
        this.continuity = new Map[Check.DataType.values().length];

        for (Check.DataType dataType : Check.DataType.values()) {
            this.continuity[dataType.ordinal()] = new ConcurrentHashMap<>();
        }
    }

    public void clear() {
        for (Map<Long, Long> map : this.continuity) {
            map.clear();
        }
    }

    public void setActiveTime(long moment, long length, boolean log) {
        if (log) {
            AntiCheatLogs.rawLogInfo(
                    moment,
                    this.profile.name + PlayerProfile.activeFor + length,
                    false,
                    true,
                    true
            );
        }
        this.continuity[profile.getLastDataType().ordinal()].put(moment, length);
    }

    public boolean wasOnline(long current, long previous) {
        PlayerProtocol protocol = this.profile.protocol();

        if (protocol != null
                && current >= protocol.getActiveCreationTime()
                && previous >= protocol.getActiveCreationTime()) {
            return !protocol.isAFK();
        } else {
            Map<Long, Long> data = this.continuity[
                    (protocol == null
                            ? profile.getLastDataType()
                            : protocol.getDataType()).ordinal()
                    ];

            if (!data.isEmpty()) {
                for (Map.Entry<Long, Long> entry : data.entrySet()) {
                    long length = entry.getValue(),
                            moment = entry.getKey();

                    if (previous >= (moment - length)
                            && current <= moment) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public boolean hasOnlineTime() {
        PlayerProtocol protocol = this.profile.protocol();
        return !this.continuity[
                (protocol == null
                        ? profile.getLastDataType()
                        : protocol.getDataType()).ordinal()
                ].isEmpty();
    }

    public long getOnlineTime() {
        PlayerProtocol protocol = this.profile.protocol();
        long sum = 0L;

        Map<Long, Long> data = this.continuity[
                (protocol == null
                        ? profile.getLastDataType()
                        : protocol.getDataType()).ordinal()
                ];

        if (!data.isEmpty()) {
            for (long value : data.values()) {
                sum += value;
            }
        }
        return protocol == null || protocol.isAFK()
                ? sum
                : protocol.getActiveTimePlayed() + sum;
    }

    public long getAverageOnlineTime() {
        PlayerProtocol protocol = this.profile.protocol();
        long sum = 0L;

        Map<Long, Long> data = this.continuity[
                (protocol == null
                        ? profile.getLastDataType()
                        : protocol.getDataType()).ordinal()
                ];

        if (!data.isEmpty()) {
            for (long value : data.values()) {
                sum += value;
            }
            sum /= data.size();
        }
        return protocol == null || protocol.isAFK()
                ? sum
                : (protocol.getActiveTimePlayed() + sum) / 2;
    }

}
