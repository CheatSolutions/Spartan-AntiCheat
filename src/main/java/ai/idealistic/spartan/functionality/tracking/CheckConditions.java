package ai.idealistic.spartan.functionality.tracking;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.compatibility.Compatibility;
import ai.idealistic.spartan.compatibility.manual.abilities.ItemsAdder;
import ai.idealistic.spartan.compatibility.manual.building.MythicMobs;
import ai.idealistic.spartan.compatibility.manual.vanilla.Attributes;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.List;

public class CheckConditions {

    public static boolean canCheckMovement(PlayerProtocol protocol,
                                           boolean vehicle,
                                           boolean elytra,
                                           boolean flight,
                                           boolean playerAttributes,
                                           boolean environmentalAttributes) {
        if ((elytra || !protocol.isGliding())
                && (flight || !protocol.wasFlying())
                && (vehicle || protocol.getVehicle() == null)

                && (playerAttributes
                || Attributes.getAmount(protocol, Attributes.GENERIC_MOVEMENT_SPEED) == Double.MIN_VALUE
                && Attributes.getAmount(protocol, Attributes.GENERIC_JUMP_STRENGTH) == Double.MIN_VALUE)

                && (environmentalAttributes
                || Attributes.getAmount(protocol, Attributes.GENERIC_STEP_HEIGHT) == Double.MIN_VALUE
                && Attributes.getAmount(protocol, Attributes.GENERIC_GRAVITY) == Double.MIN_VALUE)) {
            if (Compatibility.CompatibilityType.MYTHIC_MOBS.isFunctional()
                    || Compatibility.CompatibilityType.ITEMS_ADDER.isFunctional()) {
                List<Entity> entities = protocol.getNearbyEntities(
                        6.0,
                        6.0,
                        6.0
                );

                if (!entities.isEmpty()) {
                    for (Entity entity : entities) {
                        if (MythicMobs.is(entity) || ItemsAdder.is(entity)) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    public static boolean canCheckCombat(PlayerProtocol protocol) {
        if (!protocol.isLowEyeHeight() // Covers swimming & gliding
                && !protocol.wasGliding()
                && !protocol.wasFlying()
                && protocol.getVehicle() == null
                && Attributes.getAmount(protocol, Attributes.GENERIC_ARMOR) == Double.MIN_VALUE
                && Attributes.getAmount(protocol, Attributes.GENERIC_ATTACK_SPEED) == Double.MIN_VALUE
                && Attributes.getAmount(protocol, Attributes.GENERIC_KNOCKBACK_RESISTANCE) == Double.MIN_VALUE
                && Attributes.getAmount(protocol, Attributes.PLAYER_ENTITY_INTERACTION_RANGE) == Double.MIN_VALUE) {
            GameMode gameMode = protocol.getGameMode();
            return gameMode == GameMode.SURVIVAL
                    || gameMode == GameMode.ADVENTURE
                    || gameMode == GameMode.CREATIVE;
        } else {
            return false;
        }
    }

    public static boolean canCheckCombat(PlayerProtocol protocol, LivingEntity entity) {
        return !protocol.bukkit().getName().equals(entity.getName())
                && !MythicMobs.is(entity)
                && !ItemsAdder.is(entity);
    }

}
