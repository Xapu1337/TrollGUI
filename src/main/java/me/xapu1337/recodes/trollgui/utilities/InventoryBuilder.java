package me.xapu1337.recodes.trollgui.utilities;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import com.cryptomorin.xseries.XItemStack;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryBuilder {
    private final Map<Character, ItemStack> materials = new HashMap<>();
    private String[] pattern = new String[0];
    private int size = 54;
    private Inventory _inventory;
    private Function<Inventory, Inventory> _inventoryContents;

    public InventoryBuilder(InventoryHolder holder) {
        this._inventory = Bukkit.createInventory(holder, size);
    }

    public InventoryBuilder setMaterial(char key, XMaterial material) {
        this.materials.put(key, material.parseItem());
        return this;
    }

    public InventoryBuilder setItem(char key, ItemStack item) {
        this.materials.put(key, item);
        return this;
    }

    public InventoryBuilder withDefaults() {
        this.materials.put('B', new ItemStackBuilder(XMaterial.BLACK_STAINED_GLASS_PANE).withDisplayName(" ").build());
        this.materials.put('X', new ItemStackBuilder(XMaterial.AIR).build());
        return this;
    }

    public InventoryBuilder setInventoryContents(Function<Inventory, Inventory> contents) {
        this._inventoryContents = contents;
        return this;
    }
    public int countSimilar(char key) {
        int count = 0;
        for (String line : pattern) {
            for (char c : line.toCharArray()) {
                if (c == key) {
                    count++;
                }
            }
        }
        return count;
    }

    public int countSimilar(ItemStack itemStack) {
        int count = 0;
        for (ItemStack item : materials.values()) {
            if (item.isSimilar(itemStack)) {
                count++;
            }
        }
        return count * countSimilar(itemStack.getType().toString().charAt(0));
    }

    public int[] getItemSlots(ItemStack item) {
        List<Integer> slots = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ItemStack slotItem = _inventory.getItem(i);

            if (slotItem == null) {
                slots.add(i);
            }

            if (slotItem != null && slotItem.isSimilar(item)) {
                slots.add(i);
            }
        }
        int[] slotArray = new int[slots.size()];
        for (int i = 0; i < slots.size(); i++) {
            slotArray[i] = slots.get(i);
        }
        return slotArray;
    }

    public int[] getItemSlots(char key) {
        ItemStack item = this.materials.get(key);
        if (item == null) {
            return getItemSlots(new ItemStackBuilder(XMaterial.AIR).build());
        }
        return getItemSlots(item);
    }



    public InventoryBuilder setPattern(String... pattern) {
        if (pattern.length < 1 || pattern.length > 6) throw new IllegalArgumentException("Pattern must have between 1 and 6 lines");
        int numCols = pattern[0].length();
        if (numCols < 1 || numCols > 9) throw new IllegalArgumentException("Pattern lines must have between 1 and 9 characters");
        for (String line : pattern) if (line.length() != numCols) throw new IllegalArgumentException("Pattern lines must have the same length");
        this.pattern = pattern;
        this.size = pattern.length * 9;
        _inventory = Bukkit.createInventory(_inventory.getHolder(), size);

        return this;
    }

    public InventoryBuilder setSize(int size) {
        if (size < 1 || size > 9 * 6) throw new IllegalArgumentException("Size must be between 1 and 54");
        this.pattern = new String[size / 9];
        for (int i = 0; i < size / 9; i++) this.pattern[i] = "123456789".substring(0, Math.min(size - (i * 9), 9)).replaceAll(".", "A");
        this.size = size;
        return this;
    }

    public InventoryBuilder setSlot(int row, int col, Material material, int quantity) {
        if (row < 0 || row >= pattern.length) throw new IllegalArgumentException("Row must be between 0 and " + (pattern.length - 1));
        if (col < 0 || col >= pattern[0].length()) throw new IllegalArgumentException("Column must be between 0 and " + (pattern[0].length() - 1));
        ItemStack item = new ItemStack(material, quantity);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(material.name());
        item.setItemMeta(meta);
        int index = (row * 9) + col;
        if (index >= 0 && index < size) _inventory.setItem(index, item);
        return this;
    }

    public Inventory build() {
        ItemStack[] inventory = new ItemStack[size];
        for (int i = 0; i < pattern.length; i++) for (int j = 0; j < pattern[i].length(); j++) inventory[(i * 9) + j] = materials.containsKey(pattern[i].charAt(j)) ? materials.get(pattern[i].charAt(j)) : new ItemStack(Material.AIR);
        _inventory.setContents(inventory);
        if (_inventoryContents != null) {
            _inventory = _inventoryContents.apply(_inventory);
        }
        return _inventory;
    }

    // update the inventory
    //
    public Inventory getInventory() {
        return _inventory;
    }


}
