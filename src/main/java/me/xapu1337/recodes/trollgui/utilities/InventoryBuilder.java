package me.xapu1337.recodes.trollgui.utilities;

import java.util.HashMap;
import java.util.Map;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryBuilder {
    private final Map<Character, XMaterial> materials = new HashMap<>();
    private String[] pattern = new String[0];
    private int size;
    private Inventory _inventory;

    public InventoryBuilder setMaterial(char key, XMaterial material) {
        this.materials.put(key, material);
        return this;
    }

    public InventoryBuilder setPattern(String... pattern) {
        if (pattern.length < 1 || pattern.length > 6) throw new IllegalArgumentException("Pattern must have between 1 and 6 lines");
        int numCols = pattern[0].length();
        if (numCols < 1 || numCols > 9) throw new IllegalArgumentException("Pattern lines must have between 1 and 9 characters");
        for (String line : pattern) if (line.length() != numCols) throw new IllegalArgumentException("Pattern lines must have the same length");
        this.pattern = pattern;
        this.size = pattern.length * 9;
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
        for (int i = 0; i < pattern.length; i++) for (int j = 0; j < pattern[i].length(); j++) inventory[(i * 9) + j] = materials.containsKey(pattern[i].charAt(j)) ? new ItemStack(materials.get(pattern[i].charAt(j)).parseMaterial()) : new ItemStack(Material.AIR);
        _inventory.setContents(inventory);
        return _inventory;
    }
}
