package ai.idealistic.spartan.abstraction.protocol;

import ai.idealistic.spartan.functionality.moderation.DetectionNotifications;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.utils.minecraft.server.ConfigUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;

public class PlayerPunishments {

    private final PlayerProtocol parent;
    private long kickCooldown, warnCooldown;

    public PlayerPunishments(PlayerProtocol protocol) {
        this.parent = protocol;
    }

    public boolean kick(CommandSender punisher, String reason) {
        if (kickCooldown < System.currentTimeMillis()) {
            kickCooldown = System.currentTimeMillis() + 1_000L;
            Player target = this.parent.bukkit();
            String punisherName = punisher instanceof ConsoleCommandSender
                    ? Config.messages.getColorfulString("console_name")
                    : punisher.getName(),
                    kick = ConfigUtils.replaceWithSyntax(target,
                            Config.messages.getColorfulString("kick_reason")
                                    .replace("{reason}", reason)
                                    .replace("{punisher}", punisherName),
                            null),
                    announcement = ConfigUtils.replaceWithSyntax(target,
                            Config.messages.getColorfulString("kick_broadcast_message")
                                    .replace("{reason}", reason)
                                    .replace("{punisher}", punisherName),
                            null);

            Collection<PlayerProtocol> protocols = PluginBase.getProtocols();

            if (!protocols.isEmpty()) {
                for (PlayerProtocol protocol : protocols) {
                    if (DetectionNotifications.hasPermission(protocol)) {
                        protocol.bukkit().sendMessage(announcement);
                    }
                }
            }

            PluginBase.transferTask(
                    this.parent,
                    () -> {
                        if (target.isOnline()) {
                            PlayerProtocol protocol = PluginBase.getProtocol(target.getUniqueId());

                            if (protocol != null) {
                                target.kickPlayer(kick);
                            }
                        }
                    }
            );
            return true;
        } else {
            return false;
        }
    }

    public boolean warn(CommandSender punisher, String reason) {
        if (warnCooldown < System.currentTimeMillis()) {
            warnCooldown = System.currentTimeMillis() + 1_000L;
            Player target = this.parent.bukkit();
            String punisherName = punisher instanceof ConsoleCommandSender
                    ? Config.messages.getColorfulString("console_name")
                    : punisher.getName(),
                    warning = ConfigUtils.replaceWithSyntax(target,
                            Config.messages.getColorfulString("warning_message")
                                    .replace("{reason}", reason)
                                    .replace("{punisher}", punisherName),
                            null),
                    feedback = ConfigUtils.replaceWithSyntax(target,
                            Config.messages.getColorfulString("warning_feedback_message")
                                    .replace("{reason}", reason)
                                    .replace("{punisher}", punisherName),
                            null);
            target.sendMessage(warning);
            punisher.sendMessage(feedback);
            return true;
        } else {
            return false;
        }
    }

}
