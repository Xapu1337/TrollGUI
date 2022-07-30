package me.xapu1337.recodes.trollgui.Inventorys;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import me.xapu1337.recodes.trollgui.Cores.TrollCore;
import me.xapu1337.recodes.trollgui.Utilities.Utilities;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Settings implements Listener, InventoryHolder {
    public Inventory GUI;

    public Settings() {
        Bukkit.getPluginManager().registerEvents(this, TrollCore.instance);
        GUI = Bukkit.createInventory(this, 9, Utilities.getSingleInstance().getConfigPath("MenuItems.settingsMenu.title"));
        for(int i = 0; i < GUI.getSize(); i++)
            GUI.setItem(i, Utilities.getSingleInstance().createItem(XMaterial.GRAY_STAINED_GLASS_PANE, "§r"));
        initializeItems();
    }

    public void initializeItems(){
        GUI.setItem(4, Utilities.getSingleInstance().createItem(XMaterial.REDSTONE_BLOCK, Utilities.getSingleInstance().getConfigPath("MenuItems.settingsMenu.reload.name"), Utilities.getSingleInstance().getConfigPath("MenuItems.settingsMenu.reload.lore")));
    }

    @Override
    public @NotNull Inventory getInventory() { return GUI; }

    @EventHandler
    public void onInventoryClicked(InventoryClickEvent event) {
        if (event.getInventory().getHolder() != this) return; // IF the inventory belongs not to this class dismiss.
        event.setCancelled(true); // Disable the item to be draggable.

        final ItemStack clickedItem = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        if (clickedItem == null || clickedItem.getType() == XMaterial.AIR.parseMaterial()) return;

        if (event.getRawSlot() == 4) {
            TrollCore.instance.reloadConfig();
            player.playSound(player.getLocation(), Objects.requireNonNull(XSound.ENTITY_VILLAGER_YES.parseSound()), 25, 1);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§a✔ §8| §7RELOADED!"));
        }

    }
}
