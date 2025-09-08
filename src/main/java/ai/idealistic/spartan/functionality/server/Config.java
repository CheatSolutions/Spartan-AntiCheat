package ai.idealistic.spartan.functionality.server;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.check.CheckEnums.HackType;
import ai.idealistic.spartan.abstraction.configuration.ConfigurationBuilder;
import ai.idealistic.spartan.abstraction.configuration.implementation.Advanced;
import ai.idealistic.spartan.abstraction.configuration.implementation.Messages;
import ai.idealistic.spartan.abstraction.configuration.implementation.SQLFeature;
import ai.idealistic.spartan.abstraction.configuration.implementation.Settings;
import ai.idealistic.spartan.api.SpartanReloadEvent;
import ai.idealistic.spartan.compatibility.Compatibility;
import ai.idealistic.spartan.functionality.moderation.AwarenessNotifications;
import ai.idealistic.spartan.functionality.moderation.Wave;
import ai.idealistic.spartan.functionality.tracking.ResearchEngine;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class Config {

    public static final Settings settings = new Settings();
    public static final SQLFeature sql = new SQLFeature();
    public static final Messages messages = new Messages();
    public static final Advanced advanced = new Advanced();
    public static final Compatibility compatibility = new Compatibility();

    public static final ConfigurationBuilder[] configurations = {
            settings,
            sql,
            messages,
            advanced
    };

    // Separator

    public static Check getCheckByName(String s) {
        for (HackType hackType : CheckEnums.HackType.values()) {
            Check check = hackType.getCheck();
            String checkName = check.getName();

            if (checkName != null && checkName.equals(s)) {
                return check;
            }
        }
        return null;
    }

    // Separator

    public static void create() {
        boolean enabledPlugin = Register.isPluginEnabled();

        for (HackType hackType : CheckEnums.HackType.values()) {
            hackType.resetCheck();
        }
        if (enabledPlugin) {
            for (ConfigurationBuilder configuration : configurations) {
                configuration.create();
            }
            Compatibility.create();
            Wave.create();
            AwarenessNotifications.refresh();
        } else {
            for (ConfigurationBuilder configuration : configurations) {
                configuration.clear();
            }
            compatibility.clearCache();
            Wave.clearCache();
            AwarenessNotifications.clear();
        }

        // System
        ResearchEngine.refresh(enabledPlugin);
    }

    public static void reload(CommandSender sender) {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            SpartanReloadEvent event = new SpartanReloadEvent();
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return;
            }
        }
        if (sender != null) {
            sender.sendMessage(
                    Config.messages.getColorfulString("config_reload")
            );
        }
        create();
    }

    // Separator

    public static void enableChecks() {
        for (HackType hackType : CheckEnums.HackType.values()) {
            hackType.getCheck().setEnabled(null, true);
        }
    }

    public static void disableChecks() {
        for (HackType hackType : CheckEnums.HackType.values()) {
            hackType.getCheck().setEnabled(null, false);
        }
    }

    // Separator

    public static void enableSilentChecking() {
        for (HackType hackType : CheckEnums.HackType.values()) {
            hackType.getCheck().setSilent(null, true);
        }
    }

    public static void disableSilentChecking() {
        for (HackType hackType : CheckEnums.HackType.values()) {
            hackType.getCheck().setSilent(null, false);
        }
    }

    // Separator

    public static boolean isEnabled(Check.DataType dataType) {
        for (HackType hackType : CheckEnums.HackType.values()) {
            if (hackType.getCheck().isEnabled(dataType, null)) {
                return true;
            }
        }
        return false;
    }
}
