package ai.idealistic.spartan.compatibility.manual.vanilla;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.compatibility.Compatibility;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.utils.java.ReflectionUtils;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;
import java.util.Locale;

public class Attributes {

    public static final boolean classExists =
            ReflectionUtils.classExists(
                    "org.bukkit.attribute.Attribute"
            ) && ReflectionUtils.classExists(
                    "org.bukkit.attribute.AttributeInstance"
            ) && ReflectionUtils.classExists(
                    "org.bukkit.attribute.AttributeModifier"
            ) && ReflectionUtils.classExists(
                    "org.bukkit.Registry"
            ) && ReflectionUtils.classExists(
                    "org.bukkit.NamespacedKey"
            );

    public static final String
            GENERIC_MAX_HEALTH,
            GENERIC_FOLLOW_RANGE,
            GENERIC_LUCK,
            ZOMBIE_SPAWN_REINFORCEMENTS,
            GENERIC_GRAVITY,
            GENERIC_MAX_ABSORPTION,

    GENERIC_MOVEMENT_SPEED,
            GENERIC_FLYING_SPEED,

    GENERIC_ATTACK_DAMAGE,
            GENERIC_ATTACK_SPEED,

    GENERIC_ATTACK_KNOCKBACK,
            GENERIC_KNOCKBACK_RESISTANCE,

    GENERIC_ARMOR,
            GENERIC_ARMOR_TOUGHNESS,

    GENERIC_FALL_DAMAGE_MULTIPLIER,
            GENERIC_SAFE_FALL_DISTANCE,

    GENERIC_SCALE,
            GENERIC_STEP_HEIGHT,

    HORSE_JUMP_STRENGTH,
            GENERIC_JUMP_STRENGTH,

    PLAYER_BLOCK_INTERACTION_RANGE,
            PLAYER_ENTITY_INTERACTION_RANGE,

    PLAYER_BLOCK_BREAK_SPEED;

    static {
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_21)) {
            GENERIC_MAX_HEALTH = "max_health";
            GENERIC_FOLLOW_RANGE = "follow_range";
            GENERIC_LUCK = "luck";
            ZOMBIE_SPAWN_REINFORCEMENTS = "spawn_reinforcements";
            GENERIC_GRAVITY = "gravity";
            GENERIC_MAX_ABSORPTION = "max_absorption";

            GENERIC_MOVEMENT_SPEED = "movement_speed";
            GENERIC_FLYING_SPEED = "flying_speed";

            GENERIC_ATTACK_DAMAGE = "attack_damage";
            GENERIC_ATTACK_SPEED = "attack_speed";

            GENERIC_ATTACK_KNOCKBACK = "attack_knockback";
            GENERIC_KNOCKBACK_RESISTANCE = "knockback_resistance";

            GENERIC_ARMOR = "armor";
            GENERIC_ARMOR_TOUGHNESS = "armor_toughness";

            GENERIC_FALL_DAMAGE_MULTIPLIER = "fall_damage_multiplier";
            GENERIC_SAFE_FALL_DISTANCE = "safe_fall_distance";

            GENERIC_SCALE = "scale";
            GENERIC_STEP_HEIGHT = "step_height";

            HORSE_JUMP_STRENGTH = "jump_strength";
            GENERIC_JUMP_STRENGTH = "jump_strength";

            PLAYER_BLOCK_INTERACTION_RANGE = "block_interaction_range";
            PLAYER_ENTITY_INTERACTION_RANGE = "entity_interaction_range";

            PLAYER_BLOCK_BREAK_SPEED = "block_break_speed";
        } else {
            GENERIC_MAX_HEALTH = "GENERIC_MAX_HEALTH";
            GENERIC_FOLLOW_RANGE = "GENERIC_FOLLOW_RANGE";
            GENERIC_LUCK = "GENERIC_LUCK";
            ZOMBIE_SPAWN_REINFORCEMENTS = "ZOMBIE_SPAWN_REINFORCEMENTS";
            GENERIC_GRAVITY = "GENERIC_GRAVITY";
            GENERIC_MAX_ABSORPTION = "GENERIC_MAX_ABSORPTION";

            GENERIC_MOVEMENT_SPEED = "GENERIC_MOVEMENT_SPEED";
            GENERIC_FLYING_SPEED = "GENERIC_FLYING_SPEED";

            GENERIC_ATTACK_DAMAGE = "GENERIC_ATTACK_DAMAGE";
            GENERIC_ATTACK_SPEED = "GENERIC_ATTACK_SPEED";

            GENERIC_ATTACK_KNOCKBACK = "GENERIC_ATTACK_KNOCKBACK";
            GENERIC_KNOCKBACK_RESISTANCE = "GENERIC_KNOCKBACK_RESISTANCE";

            GENERIC_ARMOR = "GENERIC_ARMOR";
            GENERIC_ARMOR_TOUGHNESS = "GENERIC_ARMOR_TOUGHNESS";

            GENERIC_FALL_DAMAGE_MULTIPLIER = "GENERIC_FALL_DAMAGE_MULTIPLIER";
            GENERIC_SAFE_FALL_DISTANCE = "GENERIC_SAFE_FALL_DISTANCE";

            GENERIC_SCALE = "GENERIC_SCALE";
            GENERIC_STEP_HEIGHT = "GENERIC_STEP_HEIGHT";

            HORSE_JUMP_STRENGTH = "HORSE_JUMP_STRENGTH";
            GENERIC_JUMP_STRENGTH = "GENERIC_JUMP_STRENGTH";

            PLAYER_BLOCK_INTERACTION_RANGE = "PLAYER_BLOCK_INTERACTION_RANGE";
            PLAYER_ENTITY_INTERACTION_RANGE = "PLAYER_ENTITY_INTERACTION_RANGE";

            PLAYER_BLOCK_BREAK_SPEED = "PLAYER_BLOCK_BREAK_SPEED";
        }
    }

    public static double getAmount(PlayerProtocol p, String attributeString) {
        if (classExists && Compatibility.CompatibilityType.ITEM_ATTRIBUTES.isFunctional()) {
            try {
                Class<?> registryClass = Class.forName("org.bukkit.Registry");
                Class<?> keyClass = Class.forName("org.bukkit.NamespacedKey");
                Object attributeRegistry = registryClass.getField("ATTRIBUTE").get(null);
                Object key = keyClass.getMethod("minecraft", String.class)
                        .invoke(null, attributeString.toLowerCase(Locale.ROOT));
                Object attributeObj = attributeRegistry.getClass()
                        .getMethod("get", keyClass)
                        .invoke(attributeRegistry, key);

                if (attributeObj != null) {
                    Attribute attribute = (Attribute) attributeObj;
                    PlayerInventory inventory = p.getInventory();
                    double amount = Double.MIN_VALUE;

                    for (ItemStack itemStack : new ItemStack[]{
                            inventory.getHelmet(),
                            inventory.getChestplate(),
                            inventory.getLeggings(),
                            inventory.getBoots(),
                            inventory.getItemInHand(),
                            MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9) ? inventory.getItemInOffHand() : null
                    }) {
                        if (itemStack != null && itemStack.hasItemMeta()) {
                            ItemMeta meta = itemStack.getItemMeta();

                            if (meta != null && meta.hasAttributeModifiers()) {
                                Collection<AttributeModifier> modifiers = meta.getAttributeModifiers(attribute);

                                if (modifiers != null && !modifiers.isEmpty()) {
                                    for (AttributeModifier modifier : modifiers) {
                                        amount = Math.max(amount, modifier.getAmount());
                                    }
                                }
                            }
                        }
                    }
                    return amount;
                }
            } catch (Exception ignored) {
            }
        }
        return Double.MIN_VALUE;
    }

}
