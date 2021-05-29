package me.xapu1337.recodes.trollgui.Inventorys;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Cores.Core;
import me.xapu1337.recodes.trollgui.Utilities.EnumCollection;
import me.xapu1337.recodes.trollgui.Utilities.Util;
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
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerSelector implements Listener, InventoryHolder {
    public Inventory GUI;
    public String centerTitle(String title) {
        StringBuilder result = new StringBuilder();
        int spaces = (27 - ChatColor.stripColor(title).length());

        for (int i = 0; i < spaces; i++) {
            result.append(" ");
        }

        return result.append(title).toString();
    }
    public PlayerSelector() {
        Bukkit.getPluginManager().registerEvents(this, Core.instance);
        GUI = Bukkit.createInventory(this, 53, centerTitle(EnumCollection.MenuTitles.TROLLGUI.get()));
        GUI.setItem(48, Util.instance.createItem(XMaterial.OAK_BUTTON, false, "<- LEFT PAGE"));
        GUI.setItem(49, Util.instance.createItem(XMaterial.BARRIER, false, "CLOSE"));
        GUI.setItem(50, Util.instance.createItem(XMaterial.OAK_BUTTON, false, "RIGHT PAGE ->"));
        initializeItems();
    }

    int maxItemsPerPage = 28;
    int page = 0;
    int index = 0;
    public void initializeItems(){
        ArrayList<Player> players = new ArrayList<>(Core.instance.getServer().getOnlinePlayers());

        if(players != null && !players.isEmpty()) {
            for(int i = 0; i < maxItemsPerPage; i++) {
                index = maxItemsPerPage * page + i;
                if(index >= players.size()) break;
                if (players.get(index) != null){
                    ItemStack playerItem = new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial(), 1);
                    ItemMeta playerMeta = playerItem.getItemMeta();
                    playerMeta.setDisplayName(ChatColor.RED + players.get(index).getDisplayName());

                    playerMeta.getPersistentDataContainer().set(new NamespacedKey(Core.instance, "uuid"), PersistentDataType.STRING, players.get(index).getUniqueId().toString());
                    playerItem.setItemMeta(playerMeta);

                    GUI.addItem(playerItem);

                }
            }
        }

    }

    @Override
    public Inventory getInventory() { return GUI; }

    @EventHandler
    public void onInventoryClicked(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ArrayList<Player> players = new ArrayList<>(Core.instance.getServer().getOnlinePlayers());

        if (event.getCurrentItem().getType() == XMaterial.PLAYER_HEAD.parseMaterial()) {
            Bukkit.broadcastMessage("Fuck you " + Bukkit.getPlayer(UUID.fromString(event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Core.instance, "uuid"), PersistentDataType.STRING))).getDisplayName());
        } else if (event.getCurrentItem().getType() == XMaterial.BARRIER.parseMaterial()) {
            player.closeInventory();
        } else if (event.getCurrentItem().getType().equals(XMaterial.OAK_BUTTON.parseMaterial())) {
            if (ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("<- LEFT PAGE")) {
                if (page == 0) {
                    player.sendMessage(ChatColor.getByChar("#6900FF") + "You are already on the first page.");
                } else {
                    page--;

                }
            } else if (ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("RIGHT PAGE ->")) {
                if (!((index + 1) >= players.size())) {
                    page++;
                    initializeItems();
                } else {
                    player.sendMessage(ChatColor.GRAY + "You are on the last page.");
                }
            }


        }
    }
}
