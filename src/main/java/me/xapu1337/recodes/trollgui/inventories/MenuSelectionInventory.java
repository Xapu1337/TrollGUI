package me.xapu1337.recodes.trollgui.inventories;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.cores.TrollCore;
import me.xapu1337.recodes.trollgui.handlers.PaginationHandler;
import me.xapu1337.recodes.trollgui.types.PaginationItemType;
import me.xapu1337.recodes.trollgui.utilities.*;
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
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class MenuSelectionInventory implements Listener, InventoryHolder {
    private ItemStack _tmpItem;
    private ItemMeta _tmpMeta;
    private Inventory _inventory;
    private String _title;
    private InventoryHolder _callbackClass;
    public final List<ItemStack> selectableItems = new ArrayList<>();
    public BiConsumer<ItemStack, String>callback;
    private WeakReference<Inventory> previousInventory;
    public MenuSelectionInventory() {

    }


    public MenuSelectionInventory newItem() {
        _tmpItem = null;
        _tmpMeta = null;
        return this;
    }

    public MenuSelectionInventory setMaterial(XMaterial material) {
        _tmpItem = material.parseItem();
        return this;
    }

    public MenuSelectionInventory setDisplayName(String displayName) {
        _tmpMeta = _tmpItem.getItemMeta();
        _tmpMeta.setDisplayName(ConfigUtils.getInstance().$(displayName));
        _tmpItem.setItemMeta(_tmpMeta);
        return this;
    }

    public MenuSelectionInventory setLore(String... lore) {
        _tmpMeta = _tmpItem.getItemMeta();
        _tmpMeta.setLore(Arrays.stream(lore).map(s -> ConfigUtils.getInstance().$(s)).toList());
        _tmpItem.setItemMeta(_tmpMeta);
        return this;
    }

    public MenuSelectionInventory setItemID(String itemID) {
        _tmpMeta = _tmpItem.getItemMeta();
        _tmpMeta.getPersistentDataContainer().set(Utils.getInstance().UUID_KEY, org.bukkit.persistence.PersistentDataType.STRING, itemID);
        _tmpItem.setItemMeta(_tmpMeta);
        return this;
    }

    public MenuSelectionInventory finishItem() {
        selectableItems.add(_tmpItem);
        return this;
    }

    public MenuSelectionInventory setCallback(BiConsumer<ItemStack, String> callback) {
        this.callback = callback;
        return this;
    }

    public MenuSelectionInventory setTitle(String title) {
        this._title = title;
        return this;
    }

    public MenuSelectionInventory addNewItemFixed(String itemID, String displayName, XMaterial material, String... lore) {
        this.newItem();
        this.setMaterial(material);
        this.setDisplayName(displayName);
        this.setLore(lore);
        this.setItemID(itemID);
        this.finishItem();
        return this;
    }

    public void setInventoryHolderClass(InventoryHolder _callbackClass) {
        this._callbackClass = _callbackClass;
    }

    public Inventory build() {
        Bukkit.getPluginManager().registerEvents(this, TrollCore.getInstance());
        _inventory = TrollCore.getInstance().getServer().createInventory(this, 9, _title);
        ItemStack placeholder = new ItemBuilder(XMaterial.BLACK_STAINED_GLASS_PANE).withName("§r").build();
        for (int i = 0; i < 9; i++) {
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

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() != this) return;
        event.setCancelled(true);
        int slot = event.getSlot();
        if (slot >= 0 && slot < selectableItems.size()) {
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null) {
                String itemID = clickedItem.getItemMeta().getPersistentDataContainer().get(Utils.getInstance().UUID_KEY, org.bukkit.persistence.PersistentDataType.STRING);
                if (callback != null && itemID != null) {
                    callback.accept(clickedItem, itemID);
                }
            }
        }
    }


    public MenuSelectionInventory openForPlayer(Player player) {
        player.openInventory(build());
        return this;
    }

    /**
     * @return
     */
    @NotNull
    @Override
    public Inventory getInventory() {
        return _inventory;
    }
}


