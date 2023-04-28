package me.xapu1337.recodes.trollgui.inventories;

import me.xapu1337.recodes.trollgui.cores.TrollCore;
import me.xapu1337.recodes.trollgui.utilities.DebuggingUtil;
import me.xapu1337.recodes.trollgui.utilities.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class MenuSelectionInventory implements InventoryHolder, Listener {
    private final int size = 9;
    private final String title;
    private final List<ItemStack> items = new ArrayList<>();
    private final BiConsumer<Player, String> clickHandler;
    private MenuSelectionInventory(String title, List<ItemStack> items, BiConsumer<Player, String> clickHandler) {
        this.title = title;
        this.items.addAll(items);
        this.clickHandler = clickHandler;
    }

    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, size, title);

        Bukkit.getPluginManager().registerEvents(this, TrollCore.getInstance());
        for (int i = 0; i < size; i++) {
            if (i < items.size()) {
                inventory.setItem(i, items.get(i));
            } else {
                inventory.setItem(i, new ItemStack(Material.GLASS_PANE));
            }
        }
        return inventory;
    }

    @EventHandler
    public void handleClick(InventoryClickEvent event) {
        if (!Utils.getInstance().checkUniqueInventory(event, this)) return;
        event.setCancelled(true);
        int slot = event.getSlot();
        if (slot < items.size()) {
            DebuggingUtil.getInstance().l("Clicked item");
            ItemStack item = items.get(slot);
            PersistentDataContainer itemData = item.getItemMeta().getPersistentDataContainer();
            if (itemData.has(Utils.getInstance().ITEM_ID_KEY, PersistentDataType.STRING)) {
                DebuggingUtil.getInstance().l("Clicked item has valid key.");
                String itemId = itemData.get(Utils.getInstance().ITEM_ID_KEY, PersistentDataType.STRING);
                Player player = (Player) event.getWhoClicked();
                DebuggingUtil.getInstance().l("%s clicked by %s", itemId, player.getDisplayName());
                clickHandler.accept(player, itemId);
            }
        }
    }

    public static class Builder {
        private final List<ItemStack> items = new ArrayList<>();
        private BiConsumer<Player, String> clickHandler;

        public Builder() {
            this.clickHandler = (player, itemId) -> {};
        }

        public Builder item(ItemStack itemStack, String itemId) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.getPersistentDataContainer().set(Utils.getInstance().ITEM_ID_KEY, PersistentDataType.STRING, itemId);
            itemStack.setItemMeta(itemMeta);
            items.add(itemStack);
            return this;
        }

        public Builder onClick(BiConsumer<Player, String> clickHandler) {
            this.clickHandler = clickHandler;
            return this;
        }

        public MenuSelectionInventory build(String title) {
            return new MenuSelectionInventory(title, items, clickHandler);
        }
    }
}
