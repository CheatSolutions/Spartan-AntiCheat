package ai.idealistic.spartan.api;

import ai.idealistic.spartan.abstraction.check.CheckEnums;
import lombok.Getter;
import lombok.Setter;
import me.vagdedes.spartan.system.Enums;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CheckToggleEvent extends Event implements Cancellable {

    private final CheckEnums.HackType ht;
    private final ToggleAction ta;
    @Setter
    @Getter
    private boolean cancelled;

    public CheckToggleEvent(CheckEnums.HackType hackType, ToggleAction toggleAction) {
        ht = hackType;
        ta = toggleAction;
        cancelled = false;
        me.vagdedes.spartan.api.CheckToggleEvent event = new me.vagdedes.spartan.api.CheckToggleEvent(
                hackType,
                Enums.ToggleAction.valueOf(toggleAction.toString())
        );
        event.setCancelled(this.isCancelled());
        Bukkit.getPluginManager().callEvent(event);
    }

    public CheckEnums.HackType getHackType() {
        return ht;
    }

    public ToggleAction getToggleAction() {
        return ta;
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
