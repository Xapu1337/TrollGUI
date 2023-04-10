package me.xapu1337.recodes.trollgui.types;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.utilities.ItemStackBuilder;
import org.bukkit.inventory.ItemStack;

/**
 * Represents an item type that can be used for pagination.
 */
public enum PaginationItemType {
    NEXT_PAGE(new ItemStackBuilder(XMaterial.ARROW).withDisplayName("&aNext Page").build()),
    PREVIOUS_PAGE(new ItemStackBuilder(XMaterial.ARROW).withDisplayName("&cPrevious Page").build()),
    CLOSE(new ItemStackBuilder(XMaterial.BARRIER).withDisplayName("&cClose").build());

    private final ItemStack itemStack;

    PaginationItemType(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
