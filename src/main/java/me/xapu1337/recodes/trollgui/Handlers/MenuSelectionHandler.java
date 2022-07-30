package me.xapu1337.recodes.trollgui.Handlers;


import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Cores.TrollCore;
import me.xapu1337.recodes.trollgui.Utilities.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Utilities class used for menu section handling.
 * this is like a builder, but it outputs a new GUI and handles the input.
 * after it is done it will call a callback function with the selected menu item.
 * @author xapu1337
 */
public class MenuSelectionHandler implements Listener, InventoryHolder {


    private ItemStack _tmpItem;
    private ItemMeta _tmpMeta;
    private Inventory _inventory;
    private String _title;

    public final List<ItemStack> selectableItems = new ArrayList<>();
    public BiConsumer<ItemStack, String> callback;




    public MenuSelectionHandler newItem() {
        _tmpItem = null;
        _tmpMeta = null;
        return this;
    }


    public MenuSelectionHandler setMaterial(XMaterial material) {
        _tmpItem = material.parseItem();
        return this;
    }

    // Add multiple functions for setting each display name, lore and material.
    public MenuSelectionHandler setDisplayName(String displayName) {
        _tmpMeta = _tmpItem.getItemMeta();
        _tmpMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        _tmpItem.setItemMeta(_tmpMeta);
        return this;
    }

    public MenuSelectionHandler setLore(String... lore) {
        _tmpMeta = _tmpItem.getItemMeta();
        _tmpMeta.setLore(Arrays.stream(lore).map(s -> ChatColor.translateAlternateColorCodes('&', s)).toList());
        _tmpItem.setItemMeta(_tmpMeta);
        return this;
    }

    public MenuSelectionHandler setItemID(String itemID) {
        _tmpMeta = _tmpItem.getItemMeta();
        _tmpMeta.getPersistentDataContainer().set(new NamespacedKey(TrollCore.instance, "selectionItemID"), org.bukkit.persistence.PersistentDataType.STRING, itemID);
        _tmpItem.setItemMeta(_tmpMeta);
        return this;
    }

    public MenuSelectionHandler finishItem() {
        selectableItems.add(_tmpItem);
        return this;
    }

    public MenuSelectionHandler setCallback(BiConsumer<ItemStack, String> callback) {
        this.callback = callback;
        return this;
    }

    public MenuSelectionHandler setTitle(String title) {
        this._title = title;
        return this;
    }

    public MenuSelectionHandler addNewItemFixed(String itemID, String displayName, XMaterial material, String ...lore) {
        this.newItem();
        this.setMaterial(material);
        this.setDisplayName(displayName);
        this.setLore(lore);
        this.setItemID(itemID);
        this.finishItem();
        return this;
    }

    public Inventory build() {

        Bukkit.getPluginManager().registerEvents(this, TrollCore.instance);
        _inventory = TrollCore.instance.getServer().createInventory(this, 9, _title);
        ItemStack placeholder = Utilities.getSingleInstance().createItem(XMaterial.BLACK_STAINED_GLASS_PANE, "§r");
        // Seperate items with placeholders. between each item.
        for (int i = 0; i < 9; i++) {
            /**
             * Items are placed in the center, on the outside use placeholders.
             * This is to make the menu look more clean.
             * so like this: P P P P I P P P P
             */
            int placeHolderCount = (9 - selectableItems.size()) / 2;
            if (i < placeHolderCount) {
                _inventory.setItem(i, placeholder);
            } else if (i >= placeHolderCount && i < placeHolderCount + selectableItems.size()) {
                _inventory.setItem(i, selectableItems.get(i - placeHolderCount));
            } else {
                _inventory.setItem(i, placeholder);
            }


        }

        return _inventory;
    }

    public MenuSelectionHandler openForPlayer(@NotNull Player player) {
        player.openInventory(build());
        return this;
    }




    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() != this) return;
        event.setCancelled(true);

        if (event.getCurrentItem() == null) return;
        if (!event.getCurrentItem().hasItemMeta()) return;

        if (event.getCurrentItem().getItemMeta().getPersistentDataContainer() != null) {
            String itemID = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(TrollCore.instance, "selectionItemID"), org.bukkit.persistence.PersistentDataType.STRING);
            if (itemID != null) {
                callback.accept(event.getCurrentItem(), itemID);
            }
        }

    }


    /**
     * Get the object's inventory.
     *
     * @return The inventory.
     */
    @NotNull
    @Override
    public Inventory getInventory() {
        return _inventory;
    }
}
