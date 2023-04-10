package me.xapu1337.recodes.trollgui.handlers;

import me.xapu1337.recodes.trollgui.types.PaginationItemType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A handler for pagination within an inventory.
 */
public class PaginationHandler {

    private int currentPage;
    private int maxPage;
    private BiConsumer<Integer, Integer> onPageChange;

    /**
     * Creates a new PaginationHandler with the given maximum page, fixed items, onClick action, and onPageChange event.
     * @param maxPage The maximum page of the inventory.
     */
    public PaginationHandler(int maxPage) {
        this.currentPage = 1;
        this.maxPage = maxPage;
    }

    /**
     * Sets the maximum page of the inventory.
     * @param maxPage The maximum page of the inventory.
     */
    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    /**
     * Sets the current page of the inventory.
     * @param currentPage The current page of the inventory.
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * Sets the current page of the inventory.
     * @param onPageChange The current page of the inventory.
     */
    public void setOnPageChange(BiConsumer<Integer, Integer> onPageChange) {
        this.onPageChange = onPageChange;
    }

    /**
     * Gets the current page of the inventory.
     * @return The current page of the inventory.
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * Gets the maximum page of the inventory.
     * @return The maximum page of the inventory.
     */
    public int getMaxPage() {
        return maxPage;
    }

    /**
     * Handles a click event within the inventory.
     * @param event The click event to handle.
     */
    public void handleOnInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);

        if (event.getClickedInventory() == null || event.getCurrentItem() == null) {
            return;
        }

        PaginationItemType itemType = getItemType(event.getCurrentItem());

        if (itemType == null) {
            return;
        }

        switch (itemType) {
            case NEXT_PAGE -> {
                if (currentPage < maxPage) {
                    currentPage++;
                }
            }
            case PREVIOUS_PAGE -> {
                if (currentPage > 1) {
                    currentPage--;
                }
            }
            case CLOSE -> event.getWhoClicked().closeInventory();
        }

        onPageChange.accept(currentPage, maxPage);
    }

    /**
     * Gets the PaginationItemType of an item, if it exists in the fixed items.
     * @param item The item to get the type of.
     * @return The PaginationItemType of the item, or null if it is not a fixed item.
     */
    private PaginationItemType getItemType(ItemStack item) {
        for (PaginationItemType type : PaginationItemType.values()) {
            if (item.isSimilar(type.getItemStack())) {
                return type;
            }
        }
        return null;
    }

}
