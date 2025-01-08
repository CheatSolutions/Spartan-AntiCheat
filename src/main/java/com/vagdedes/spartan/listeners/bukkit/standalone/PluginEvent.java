package com.vagdedes.spartan.listeners.bukkit.standalone;

import com.vagdedes.spartan.functionality.server.Config;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

public class PluginEvent implements Listener {

    @EventHandler
    private void PluginEnable(PluginEnableEvent e) {
        // System
        Config.compatibility.fastRefresh();
    }

    @EventHandler
    private void PluginDisable(PluginDisableEvent e) {
        // System
        Config.compatibility.fastRefresh();
    }

}
