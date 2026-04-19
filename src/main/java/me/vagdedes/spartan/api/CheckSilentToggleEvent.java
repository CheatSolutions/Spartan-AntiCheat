package me.vagdedes.spartan.api;

import ai.idealistic.spartan.abstraction.check.CheckEnums;
import lombok.Getter;
import lombok.Setter;
import me.vagdedes.spartan.system.Enums;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CheckSilentToggleEvent extends Event implements Cancellable {

    private final CheckEnums.HackType ht;
    private final Enums.ToggleAction ta;
    @Setter
    @Getter
    private boolean cancelled;

    public CheckSilentToggleEvent(CheckEnums.HackType hackType, Enums.ToggleAction toggleAction) {
        ht = hackType;
        ta = toggleAction;
        cancelled = false;
    }

    public CheckEnums.HackType getHackType() {
        return ht;
    }

    public Enums.ToggleAction getToggleAction() {
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
