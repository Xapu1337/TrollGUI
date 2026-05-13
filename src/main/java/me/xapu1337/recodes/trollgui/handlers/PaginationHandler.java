package me.xapu1337.recodes.trollgui.handlers;

import me.xapu1337.recodes.trollgui.types.PaginationItemType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

public class PaginationHandler {

    private int currentPage;
    private int maxPage;
    private BiConsumer<Integer, Integer> onPageChange;

    public PaginationHandler(int maxPage) {
        this.currentPage = 1;
        this.maxPage = maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setOnPageChange(BiConsumer<Integer, Integer> onPageChange) {
        this.onPageChange = onPageChange;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void handleOnInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);

        if (onPageChange == null || event.getClickedInventory() == null || event.getCurrentItem() == null) {
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

    public void handleOnInventoryClick(InventoryClickEvent event, Inventory previousInventory) {
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
            case CLOSE -> {
                event.getWhoClicked().closeInventory();
                event.getWhoClicked().openInventory(previousInventory);
            }
        }

        onPageChange.accept(currentPage, maxPage);
    }

    private PaginationItemType getItemType(ItemStack item) {
        for (PaginationItemType type : PaginationItemType.values()) {
            if (item.isSimilar(type.getItemStack())) {
                return type;
            }
        }
        return null;
    }

}
