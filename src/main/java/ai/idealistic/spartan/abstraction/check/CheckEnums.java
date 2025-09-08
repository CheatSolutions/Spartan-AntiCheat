package ai.idealistic.spartan.abstraction.check;

import ai.idealistic.spartan.abstraction.check.implementation.misc.ImpossibleInventory;
import ai.idealistic.spartan.abstraction.check.implementation.misc.InventoryClicks;
import ai.idealistic.spartan.abstraction.check.implementation.movement.GravitySimulation;
import ai.idealistic.spartan.abstraction.check.implementation.movement.SpeedSimulation;
import ai.idealistic.spartan.abstraction.check.implementation.movement.exploits.Exploits;
import ai.idealistic.spartan.utils.minecraft.inventory.MaterialUtils;
import lombok.Getter;
import org.bukkit.Material;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CheckEnums {

    public enum HackType {
        HIT_REACH(
                HackCategoryType.COMBAT,
                ai.idealistic.spartan.abstraction.check.implementation.combat.HitReach.class,
                Material.ARROW
        ),
        CRITICALS(
                HackCategoryType.COMBAT,
                ai.idealistic.spartan.abstraction.check.implementation.combat.Criticals.class,
                Material.REDSTONE
        ),
        FAST_CLICKS(
                HackCategoryType.COMBAT,
                ai.idealistic.spartan.abstraction.check.implementation.combat.FastClicks.class,
                Material.LEVER
        ),
        VELOCITY(
                HackCategoryType.COMBAT,
                ai.idealistic.spartan.abstraction.check.implementation.combat.Velocity.class,
                Material.CHAINMAIL_BOOTS
        ),
        KILL_AURA(
                HackCategoryType.COMBAT,
                ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.KillAura.class,
                Material.IRON_SWORD
        ),
        IRREGULAR_MOVEMENTS(
                HackCategoryType.MOVEMENT,
                ai.idealistic.spartan.abstraction.check.implementation.movement.irregularmovements.IrregularMovements.class,
                Material.HOPPER
        ),
        GRAVITY_SIMULATION(
                HackCategoryType.MOVEMENT,
                GravitySimulation.class,
                Material.LADDER
        ),
        SPEED_SIMULATION(
                HackCategoryType.MOVEMENT,
                SpeedSimulation.class,
                Material.FEATHER
        ),
        EXPLOITS(
                HackCategoryType.MOVEMENT,
                Exploits.class,
                Material.TNT
        ),
        MORE_PACKETS(
                HackCategoryType.MOVEMENT,
                ai.idealistic.spartan.abstraction.check.implementation.movement.MorePackets.class,
                Material.FLINT_AND_STEEL
        ),
        X_RAY(
                HackCategoryType.WORLD,
                ai.idealistic.spartan.abstraction.check.implementation.world.XRay.class,
                Material.GLASS
        ),
        IMPOSSIBLE_ACTIONS(
                HackCategoryType.WORLD,
                ai.idealistic.spartan.abstraction.check.implementation.world.impossibleactions.ImpossibleActions.class,
                Material.BEDROCK
        ),
        FAST_BREAK(
                HackCategoryType.WORLD,
                ai.idealistic.spartan.abstraction.check.implementation.world.FastBreak.class,
                Material.ENCHANTED_BOOK
        ),
        FAST_PLACE(
                HackCategoryType.WORLD,
                ai.idealistic.spartan.abstraction.check.implementation.world.FastPlace.class,
                Material.STONE
        ),
        BLOCK_REACH(
                HackCategoryType.WORLD,
                ai.idealistic.spartan.abstraction.check.implementation.world.BlockReach.class,
                Material.FISHING_ROD
        ),
        NO_SWING(
                HackCategoryType.MISCELLANEOUS,
                ai.idealistic.spartan.abstraction.check.implementation.misc.NoSwing.class,
                Material.SHEARS
        ),
        INVENTORY_CLICKS(
                HackCategoryType.MISCELLANEOUS,
                InventoryClicks.class,
                Material.STONE_BUTTON
        ),
        FAST_HEAL(
                HackCategoryType.MISCELLANEOUS,
                ai.idealistic.spartan.abstraction.check.implementation.misc.FastHeal.class,
                Material.APPLE
        ),
        IMPOSSIBLE_INVENTORY(
                HackCategoryType.MISCELLANEOUS,
                ImpossibleInventory.class,
                Material.MILK_BUCKET
        ),
        FAST_EAT(
                HackCategoryType.MISCELLANEOUS,
                ai.idealistic.spartan.abstraction.check.implementation.misc.FastEat.class,
                Material.CAKE
        );

        @Getter
        private Check check;
        public final HackCategoryType category;
        public final Class<?> executor;
        private final Map<String, Long> detections;
        public final Material material;

        HackType(HackCategoryType category, Class<?> executor, Material material) {
            this.category = category;
            this.executor = executor;
            this.detections = new ConcurrentHashMap<>();
            this.check = new Check(this);
            this.material = material;
        }

        public void resetCheck() {
            if (this.check != null) {
                this.check = new Check(this);
            }
        }

        public void addDetection(String detection, long averageTime) {
            this.detections.put(detection, averageTime);
        }

        public void removeDetection(String detection) {
            this.detections.remove(detection);
        }

        public long getDefaultAverageTime(String detection) {
            return this.detections.get(detection);
        }

        public Collection<String> getDetections() {
            return this.detections.keySet();
        }

        @Override
        public String toString() {
            return super.toString().toLowerCase().replace("_", "-");
        }
    }

    public enum HackCategoryType {
        COMBAT(Material.IRON_SWORD),
        MOVEMENT(Material.FEATHER),
        WORLD(Material.DIAMOND_PICKAXE),
        MISCELLANEOUS(MaterialUtils.get("crafting_table"));

        public final Material material;

        HackCategoryType(Material material) {
            this.material = material;
        }

        @Override
        public String toString() {
            return super.toString().toLowerCase().replace("_", "-");
        }

    }

}
