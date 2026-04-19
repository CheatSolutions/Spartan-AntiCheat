package ai.idealistic.spartan.abstraction.protocol;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public class CPlayerInventory implements PlayerInventory {

    private final PlayerInventory inventory;

    public CPlayerInventory(PlayerInventory inventory) {
        this.inventory = inventory;
    }

    public @Nullable ItemStack[] getArmorContents() {
        return this.inventory == null
                ? new ItemStack[]{}
                : this.inventory.getArmorContents();
    }

    public @Nullable ItemStack[] getExtraContents() {
        return this.inventory == null
                ? new ItemStack[]{}
                : this.inventory.getExtraContents();
    }

    public @Nullable ItemStack getHelmet() {
        return this.inventory == null
                ? null
                : this.inventory.getHelmet();
    }

    public @Nullable ItemStack getChestplate() {
        return this.inventory == null
                ? null
                : this.inventory.getChestplate();
    }

    public @Nullable ItemStack getLeggings() {
        return this.inventory == null
                ? null
                : this.inventory.getLeggings();
    }

    public @Nullable ItemStack getBoots() {
        return this.inventory == null
                ? null
                : this.inventory.getBoots();
    }

    public int getSize() {
        return this.inventory == null
                ? 0
                : this.inventory.getSize();
    }

    public int getMaxStackSize() {
        return this.inventory == null
                ? 0
                : this.inventory.getMaxStackSize();
    }

    public void setMaxStackSize(int i) {
        if (this.inventory != null) {
            this.inventory.setMaxStackSize(i);
        }
    }

    public @Nullable ItemStack getItem(int i) {
        return this.inventory == null
                ? null
                : this.inventory.getItem(i);
    }

    public void setItem(int i, @Nullable ItemStack itemStack) {
        if (this.inventory != null) {
            this.inventory.setItem(i, itemStack);
        }
    }

    public @NotNull HashMap<Integer, ItemStack> addItem(@NotNull ItemStack... itemStacks) throws IllegalArgumentException {
        if (this.inventory != null) {
            return this.inventory.addItem(itemStacks);
        } else {
            return new HashMap<>(0);
        }
    }

    public @NotNull HashMap<Integer, ItemStack> removeItem(@NotNull ItemStack... itemStacks) throws IllegalArgumentException {
        if (this.inventory != null) {
            return this.inventory.removeItem(itemStacks);
        } else {
            return new HashMap<>(0);
        }
    }

    public @NotNull HashMap<Integer, ItemStack> removeItemAnySlot(@NotNull ItemStack... itemStacks) throws IllegalArgumentException {
        if (this.inventory != null) {
            return this.inventory.removeItemAnySlot(itemStacks);
        } else {
            return new HashMap<>(0);
        }
    }

    public @Nullable ItemStack[] getContents() {
        return this.inventory == null
                ? new ItemStack[]{}
                : this.inventory.getContents();
    }

    public void setContents(@Nullable ItemStack[] itemStacks) throws IllegalArgumentException {
        if (this.inventory != null) {
            this.inventory.setContents(itemStacks);
        }
    }

    public @Nullable ItemStack[] getStorageContents() {
        return this.inventory == null
                ? new ItemStack[]{}
                : this.inventory.getStorageContents();
    }

    public void setStorageContents(@Nullable ItemStack[] itemStacks) throws IllegalArgumentException {
        if (this.inventory != null) {
            this.inventory.setStorageContents(itemStacks);
        }
    }

    public boolean contains(@NotNull Material material) throws IllegalArgumentException {
        return this.inventory != null
                && this.inventory.contains(material);
    }

    public boolean contains(@Nullable ItemStack itemStack) {
        return this.inventory != null
                && this.inventory.contains(itemStack);
    }

    public boolean contains(@NotNull Material material, int i) throws IllegalArgumentException {
        return this.inventory != null
                && this.inventory.contains(material, i);
    }

    public boolean contains(@Nullable ItemStack itemStack, int i) {
        return this.inventory != null
                && this.inventory.contains(itemStack, i);
    }

    public boolean containsAtLeast(@Nullable ItemStack itemStack, int i) {
        return this.inventory != null
                && this.inventory.containsAtLeast(itemStack, i);
    }

    public @NotNull HashMap<Integer, ? extends ItemStack> all(@NotNull Material material) throws IllegalArgumentException {
        return this.inventory == null
                ? new HashMap<>(0)
                : this.inventory.all(material);
    }

    public @NotNull HashMap<Integer, ? extends ItemStack> all(@Nullable ItemStack itemStack) {
        return this.inventory == null
                ? new HashMap<>(0)
                : this.inventory.all(itemStack);
    }

    public int first(@NotNull Material material) throws IllegalArgumentException {
        return this.inventory == null
                ? 0
                : this.inventory.first(material);
    }

    public int first(@NotNull ItemStack itemStack) {
        return this.inventory == null
                ? 0
                : this.inventory.first(itemStack);
    }

    public int firstEmpty() {
        return this.inventory == null
                ? 0
                : this.inventory.firstEmpty();
    }

    public boolean isEmpty() {
        return this.inventory != null
                && this.inventory.isEmpty();
    }

    public void remove(@NotNull Material material) throws IllegalArgumentException {
        if (this.inventory != null) {
            this.inventory.remove(material);
        }
    }

    public void remove(@NotNull ItemStack itemStack) {
        if (this.inventory != null) {
            this.inventory.remove(itemStack);
        }
    }

    public void clear(int i) {
        if (this.inventory != null) {
            this.inventory.clear(i);
        }
    }

    public void clear() {
        if (this.inventory != null) {
            this.inventory.clear();
        }
    }

    public int close() {
        return this.inventory == null
                ? 0
                : this.inventory.close();
    }

    public @NotNull List<HumanEntity> getViewers() {
        return this.inventory == null
                ? new ArrayList<>(0)
                : this.inventory.getViewers();
    }

    public @NotNull InventoryType getType() {
        return inventory == null
                ? InventoryType.PLAYER
                : inventory.getType();
    }

    public void setItem(@NotNull EquipmentSlot equipmentSlot, @Nullable ItemStack itemStack) {
        if (this.inventory != null) {
            this.inventory.setItem(equipmentSlot, itemStack);
        }
    }

    public @NotNull ItemStack getItem(@NotNull EquipmentSlot equipmentSlot) {
        return this.inventory == null
                ? new ItemStack(Material.AIR)
                : this.inventory.getItem(equipmentSlot);
    }

    public void setArmorContents(@Nullable ItemStack[] itemStacks) {
        if (this.inventory != null) {
            this.inventory.setArmorContents(itemStacks);
        }
    }

    public void setExtraContents(@Nullable ItemStack[] itemStacks) {
        if (this.inventory != null) {
            this.inventory.setExtraContents(itemStacks);
        }
    }

    public void setHelmet(@Nullable ItemStack itemStack) {
        if (this.inventory != null) {
            this.inventory.setHelmet(itemStack);
        }
    }

    public void setChestplate(@Nullable ItemStack itemStack) {
        if (this.inventory != null) {
            this.inventory.setChestplate(itemStack);
        }
    }

    public void setLeggings(@Nullable ItemStack itemStack) {
        if (this.inventory != null) {
            this.inventory.setLeggings(itemStack);
        }
    }

    public void setBoots(@Nullable ItemStack itemStack) {
        if (this.inventory != null) {
            this.inventory.setBoots(itemStack);
        }
    }

    public @NotNull ItemStack getItemInMainHand() {
        return this.inventory == null
                ? new ItemStack(Material.AIR)
                : this.inventory.getItemInMainHand();
    }

    public void setItemInMainHand(@Nullable ItemStack itemStack) {
        if (this.inventory != null) {
            this.inventory.setItemInMainHand(itemStack);
        }
    }

    public @NotNull ItemStack getItemInOffHand() {
        return this.inventory == null
                ? new ItemStack(Material.AIR)
                : this.inventory.getItemInOffHand();
    }

    public void setItemInOffHand(@Nullable ItemStack itemStack) {
        if (this.inventory != null) {
            this.inventory.setItemInOffHand(itemStack);
        }
    }

    @Deprecated
    public @NotNull ItemStack getItemInHand() {
        return this.inventory == null
                ? new ItemStack(Material.AIR)
                : this.inventory.getItemInHand();
    }

    @Deprecated
    public void setItemInHand(@Nullable ItemStack itemStack) {
        if (this.inventory != null) {
            this.inventory.setItemInHand(itemStack);
        }
    }

    public int getHeldItemSlot() {
        return this.inventory == null
                ? 0
                : this.inventory.getHeldItemSlot();
    }

    public void setHeldItemSlot(int i) {
        if (this.inventory != null) {
            this.inventory.setHeldItemSlot(i);
        }
    }

    public @Nullable HumanEntity getHolder() {
        return this.inventory == null
                ? null
                : this.inventory.getHolder();
    }

    public @Nullable InventoryHolder getHolder(boolean b) {
        return this.inventory == null
                ? null
                : this.inventory.getHolder(b);
    }

    public @NotNull ListIterator<ItemStack> iterator() {
        if (this.inventory == null) {
            List<ItemStack> list = new ArrayList<>(0);
            return list.listIterator();
        } else {
            return this.inventory.iterator();
        }
    }

    public @NotNull ListIterator<ItemStack> iterator(int i) {
        if (this.inventory == null) {
            List<ItemStack> list = new ArrayList<>(0);
            return list.listIterator(i);
        } else {
            return this.inventory.iterator(i);
        }
    }

    public @Nullable Location getLocation() {
        return this.inventory == null
                ? null
                : this.inventory.getLocation();
    }

}
