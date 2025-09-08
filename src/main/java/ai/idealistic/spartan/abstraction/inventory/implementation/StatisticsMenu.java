package ai.idealistic.spartan.abstraction.inventory.implementation;

import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.inventory.InventoryMenu;
import ai.idealistic.spartan.abstraction.profiling.PlayerProfile;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.api.Permission;
import ai.idealistic.spartan.functionality.connection.PluginAddons;
import ai.idealistic.spartan.functionality.moderation.clickable.ClickableMessage;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.functionality.tracking.ResearchEngine;
import ai.idealistic.spartan.utils.java.TimeUtils;
import ai.idealistic.spartan.utils.math.AlgebraUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class StatisticsMenu extends InventoryMenu {

    public static final String title = "Plugin Statistics";

    public StatisticsMenu() {
        super(title, 54, new Permission[]{Permission.MANAGE, Permission.INFO});
    }

    @Override
    public boolean internalOpen(PlayerProtocol protocol, boolean permissionMessage, Object object) {
        if (!PluginAddons.isSyn()) {
            protocol.bukkit().closeInventory();
            String missing = PluginAddons.synClick.replace(PluginAddons.synMissingPlaceholder, title);
            ClickableMessage.sendURL(
                    protocol.bukkit(),
                    missing,
                    missing + " (" + PluginAddons.synURL + ")",
                    PluginAddons.synURL
            );
            return false;
        }
        List<String> list = new ArrayList<>();
        int startingLine = 1;
        Collection<PlayerProfile> profiles = ResearchEngine.getPlayerProfiles();
        int java = 0;
        long totalOnlineTime = 0L,
                averageOnlineTime = 0L,
                violationsFired = 0L;
        Map<String, Long> detectionViolationTimes = new HashMap<>();

        if (!profiles.isEmpty()) {
            for (PlayerProfile profile : profiles) {
                if (profile.getLastDataType() == Check.DataType.JAVA) {
                    java++;
                }
                totalOnlineTime += profile.getContinuity().getOnlineTime();
                averageOnlineTime += profile.getContinuity().getAverageOnlineTime();

                for (CheckRunner runner : protocol.getRunners()) {
                    for (CheckDetection detection : runner.getDetections()) {
                        int amount = profile.getTimeDifferences(
                                runner.hackType,
                                profile.getLastDataType(),
                                detection.name
                        ).size();
                        violationsFired += amount;
                        detectionViolationTimes.put(
                                detection.name,
                                detectionViolationTimes.getOrDefault(detection.name, 0L)
                                        + amount
                                        + profile.getTimeDifferences(
                                        runner.hackType,
                                        profile.getOppositeDataType(),
                                        detection.name
                                ).size()
                        );
                    }
                }
            }
        }
        averageOnlineTime /= profiles.size();

        //

        for (CheckEnums.HackCategoryType categoryType : CheckEnums.HackCategoryType.values()) {
            int slot = (startingLine * 9) + 2;

            for (CheckEnums.HackType hackType : CheckEnums.HackType.values()) {
                if (hackType.category == categoryType) {
                    int count = 0;
                    list.clear();
                    list.add("§6" + hackType.category.toString());

                    for (CheckDetection detection : protocol.getRunner(hackType).getDetections()) {
                        long fired = detectionViolationTimes.getOrDefault(detection.name, 0L);
                        list.add("");
                        String name = detection.name;

                        if (AlgebraUtils.validInteger(name)) {
                            name = "default";
                        } else {
                            name = name.toLowerCase().replace("_", "-");
                        }
                        list.add("§7" + name + "§8:");
                        list.add("§aViolations Fired§8: §f" + fired);
                        list.add("§eViolations Fired From Total§8: §f" + AlgebraUtils.cut(
                                (fired / (double) violationsFired) * 100.0, 2
                        ) + "%");

                        for (Check.DataType dataType : Check.DataType.values()) {
                            double average = ResearchEngine.getAverageViolationTime(
                                    detection,
                                    dataType
                            );
                            list.add("§cAverage " + dataType + " Violation Ms§8: §f"
                                    + ((long) AlgebraUtils.cut(average, 2)));
                        }
                        count++;

                        if (count == 4) {
                            break;
                        }
                    }
                    add("§3" + hackType.getCheck().getName(), list, new ItemStack(hackType.material), slot);
                    slot++;
                }
            }
            startingLine++;
        }

        //

        list.clear();
        list.add("");
        list.add("§7Total Player Profiles§8: §f" + profiles.size());
        list.add("§7Java Player Profiles§8: §f" + java);
        list.add("§7Bedrock Player Profiles§8: §f" + (profiles.size() - java));
        list.add("§7Total Player Online Time§8: §f" + TimeUtils.convertMilliseconds(totalOnlineTime));
        list.add("§7Average Player Online Time§8: §f" + TimeUtils.convertMilliseconds(averageOnlineTime));
        list.add("§7Total Violations Fired§8: §f" + violationsFired);
        add("§4General Statistics", list, new ItemStack(Material.ENDER_CHEST), 4);
        add("§4Back", null, new ItemStack(Material.ARROW), 49);
        return true;
    }

    @Override
    public boolean internalHandle(PlayerProtocol protocol) {
        String item = itemStack.getItemMeta().getDisplayName();

        if (item.equals("§4Back")) {
            PluginBase.synMenu.open(protocol, protocol.bukkit().getName());
        }
        return true;
    }

}
