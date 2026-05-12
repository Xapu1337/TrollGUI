package me.xapu1337.recodes.trollgui.types;

import com.cryptomorin.xseries.XMaterial;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Represents an item type that can be used for pagination.
 */
public enum PaginationItemType {
    NEXT_PAGE(XMaterial.ARROW, "&aNext Page"),
    PREVIOUS_PAGE(XMaterial.ARROW, "&cPrevious Page"),
    CLOSE(XMaterial.BARRIER, "&cClose");

    private final ItemStack itemStack;

    PaginationItemType(XMaterial material, String displayName) {
        ItemStack item = material.parseItem();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        item.setItemMeta(meta);
        this.itemStack = item;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
