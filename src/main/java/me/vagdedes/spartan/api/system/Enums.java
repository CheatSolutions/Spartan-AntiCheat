package me.vagdedes.spartan.api.system;

import ai.idealistic.spartan.abstraction.check.CheckEnums;
import lombok.Getter;

public class Enums {

    // API Use
    // Should have used capital letters but won't change them now so to not break the dozen APIs who use these enums
    public enum HackType {
        XRay(CheckEnums.HackType.X_RAY),
        NoSwing(CheckEnums.HackType.NO_SWING),
        IrregularMovements(CheckEnums.HackType.IRREGULAR_MOVEMENTS),
        ImpossibleActions(CheckEnums.HackType.IMPOSSIBLE_ACTIONS),
        InventoryClicks(CheckEnums.HackType.INVENTORY_CLICKS),
        Criticals(CheckEnums.HackType.CRITICALS),
        SpeedSimulation(CheckEnums.HackType.SPEED_SIMULATION),
        Exploits(CheckEnums.HackType.EXPLOITS),
        GravitySimulation(CheckEnums.HackType.GRAVITY_SIMULATION),
        BlockReach(CheckEnums.HackType.BLOCK_REACH),
        FastClicks(CheckEnums.HackType.FAST_CLICKS),
        FastHeal(CheckEnums.HackType.FAST_HEAL),
        ImpossibleInventory(CheckEnums.HackType.IMPOSSIBLE_INVENTORY),
        HitReach(CheckEnums.HackType.HIT_REACH),
        FastBreak(CheckEnums.HackType.FAST_BREAK),
        FastPlace(CheckEnums.HackType.FAST_PLACE),
        MorePackets(CheckEnums.HackType.MORE_PACKETS),
        FastEat(CheckEnums.HackType.FAST_EAT),
        Velocity(CheckEnums.HackType.VELOCITY),
        KillAura(CheckEnums.HackType.KILL_AURA);

        @Getter
        private final CheckEnums.HackType hackType;

        HackType(CheckEnums.HackType hackType) {
            this.hackType = hackType;
        }
    }

    // API Use
    public enum Permission {
        CONDITION(ai.idealistic.spartan.api.Permission.CONDITION),
        WAVE(ai.idealistic.spartan.api.Permission.WAVE),
        ADMIN(ai.idealistic.spartan.api.Permission.ADMIN),
        RELOAD(ai.idealistic.spartan.api.Permission.RELOAD),
        KICK(ai.idealistic.spartan.api.Permission.KICK),
        BYPASS(ai.idealistic.spartan.api.Permission.BYPASS),
        MANAGE(ai.idealistic.spartan.api.Permission.MANAGE),
        INFO(ai.idealistic.spartan.api.Permission.INFO),
        WARN(ai.idealistic.spartan.api.Permission.WARN),
        USE_BYPASS(ai.idealistic.spartan.api.Permission.USE_BYPASS),
        NOTIFICATIONS(ai.idealistic.spartan.api.Permission.NOTIFICATIONS),
        BEDROCK(ai.idealistic.spartan.api.Permission.BEDROCK);

        @Getter
        private final ai.idealistic.spartan.api.Permission permission;

        Permission(ai.idealistic.spartan.api.Permission permission) {
            this.permission = permission;
        }
    }

    // API Use
    public enum ToggleAction {
        ENABLE(ai.idealistic.spartan.api.ToggleAction.ENABLE),
        DISABLE(ai.idealistic.spartan.api.ToggleAction.DISABLE);

        @Getter
        private final ai.idealistic.spartan.api.ToggleAction toggleAction;

        ToggleAction(ai.idealistic.spartan.api.ToggleAction toggleAction) {
            this.toggleAction = toggleAction;
        }
    }

}
