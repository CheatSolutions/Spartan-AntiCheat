package com.vagdedes.spartan.functionality.performance;

import com.vagdedes.spartan.abstraction.player.SpartanPlayer;
import com.vagdedes.spartan.abstraction.protocol.SpartanProtocol;
import com.vagdedes.spartan.functionality.connection.cloud.CloudBase;
import com.vagdedes.spartan.functionality.server.Config;
import com.vagdedes.spartan.functionality.server.SpartanBukkit;

import java.util.*;

public class PlayerDetectionSlots {

    private static final List<UUID>
            list = Collections.synchronizedList(new ArrayList<>()),
            priority = Collections.synchronizedList(new ArrayList<>());

    static {
        SpartanBukkit.runRepeatingTask(() -> {
            int amount = CloudBase.getDetectionSlots();

            if (amount > 0) {
                Set<Map.Entry<UUID, SpartanProtocol>> players = SpartanBukkit.getPlayerEntries();

                if (players.size() <= amount) {
                    synchronized (priority) {
                        synchronized (list) {
                            priority.clear();
                            list.clear();

                            for (Map.Entry<UUID, SpartanProtocol> entry : players) {
                                list.add(entry.getKey());
                            }
                        }
                    }
                } else {
                    List<UUID> newList = new ArrayList<>(amount);

                    // Add prioritised players first
                    if (!priority.isEmpty()) {
                        synchronized (priority) {
                            Iterator<UUID> iterator = priority.iterator();

                            while (iterator.hasNext() && newList.size() < amount) {
                                newList.add(iterator.next());
                                iterator.remove();
                            }
                        }
                    }

                    // Add non-prioritised players that are not being checked
                    synchronized (list) {
                        for (Map.Entry<UUID, SpartanProtocol> entry : players) {
                            if (Config.isEnabled(entry.getValue().spartanPlayer.dataType)) {
                                UUID uuid = entry.getKey();

                                if (!list.contains(uuid)) {
                                    if (newList.size() < amount) {
                                        newList.add(uuid);
                                    } else {
                                        synchronized (priority) {
                                            if (!priority.contains(uuid)) {
                                                priority.add(uuid);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Add non-prioritised players that are being checked
                        for (Map.Entry<UUID, SpartanProtocol> entry : players) {
                            if (Config.isEnabled(entry.getValue().spartanPlayer.dataType)) {
                                UUID uuid = entry.getKey();

                                if (newList.size() < amount) {
                                    newList.add(uuid);
                                } else {
                                    synchronized (priority) {
                                        if (!priority.contains(uuid)) {
                                            priority.add(uuid);
                                        }
                                    }
                                }
                            }
                        }

                        // Set the new list
                        list.clear();
                        list.addAll(newList);
                    }
                }
            }
        }, 1L, 1L);
    }

    private static boolean add(SpartanPlayer player, int optionAmount) {
        synchronized (list) {
            if (list.contains(player.protocol.getUUID())) {
                return true;
            } else if (list.size() < optionAmount
                    && Config.isEnabled(player.dataType)) {
                list.add(player.protocol.getUUID());

                synchronized (priority) {
                    priority.remove(player.protocol.getUUID());
                }
                return true;
            } else {
                return false;
            }
        }
    }

    public static boolean isChecked(SpartanPlayer player) {
        int amount = CloudBase.getDetectionSlots();

        if (amount <= 0) {
            return true;
        } else {
            return add(player, amount);
        }
    }

    public static int getRemaining() {
        return Math.max(CloudBase.getDetectionSlots() - list.size(), 0);
    }

}
