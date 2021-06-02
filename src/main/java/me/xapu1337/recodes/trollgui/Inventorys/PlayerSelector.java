package me.xapu1337.recodes.trollgui.Inventorys;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Cores.Core;
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
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerSelector implements Listener, InventoryHolder{
    public Inventory GUI;
    public Player player;
    int maxItemsPerPage = 1;
    int page = 0;
    int index = 0;
    public String centerTitle(String title) {
        StringBuilder result = new StringBuilder();
        int spaces = (27 - ChatColor.stripColor(title).length());

        for (int i = 0; i < spaces; i++) {
            result.append(" ");
        }

        return result.append(title).toString();
    }
    public PlayerSelector(Player player) {
        this.player = player;
        Bukkit.getPluginManager().registerEvents(this, Core.instance);
        GUI = Bukkit.createInventory(this, 54,
                centerTitle(
                        Core.instance.utils.getConfigPath("MenuTitles.selectPlayer")
                )
        );
        initializeItems();
    }

    public void initializeItems(){
        ArrayList<Player> players = new ArrayList<>(Core.instance.getServer().getOnlinePlayers());
        for(int i = 45; i < 54; i++)
            GUI.setItem(i, Core.instance.utils.createItem(XMaterial.GRAY_STAINED_GLASS_PANE, false, " "));
        GUI.setItem(48, Core.instance.utils.createItem(XMaterial.OAK_BUTTON, false, Core.instance.utils.getConfigPath("MenuItems.playerSelector.left.name"), Core.instance.utils.getConfigPath("MenuItems.playerSelector.left.lore")));
        GUI.setItem(49, Core.instance.utils.createItem(XMaterial.BARRIER, false, Core.instance.utils.getConfigPath("MenuItems.playerSelector.close.name"), Core.instance.utils.getConfigPath("MenuItems.playerSelector.close.lore")));
        GUI.setItem(50, Core.instance.utils.createItem(XMaterial.OAK_BUTTON, false, Core.instance.utils.getConfigPath("MenuItems.playerSelector.right.name"), Core.instance.utils.getConfigPath("MenuItems.playerSelector.right.lore")));
//        InventoryTitleHelper.sendInventoryTitle(player, GUI, centerTitle(EnumCollection.MenuTitles.PLAYER_SELECTOR.get().replace("%CURRENT_PAGE", String.valueOf(index + 1)).replace("%MAX_PAGES%", String.valueOf(Math.round(Bukkit.getOnlinePlayers().size() / maxItemsPerPage)))));

        if(players != null && !players.isEmpty()) {
            for(int i = 0; i < maxItemsPerPage; i++) {
                index = maxItemsPerPage * page + i;
                if(index >= players.size()) break;
                if (players.get(index) != null){
                    ItemStack playerItem = new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial(), 1);
                    SkullMeta playerMeta = (SkullMeta) playerItem.getItemMeta();
                    playerMeta.setDisplayName(ChatColor.RED + players.get(index).getDisplayName());
                    playerMeta.setOwningPlayer(Bukkit.getPlayer(players.get(index).getUniqueId()));
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
        if (event.getInventory().getHolder() != this) return; // IF the inventory belongs not to this class dismiss.
            event.setCancelled(true); // Disable the item to be draggable.

        ArrayList<Player> players = new ArrayList<>(Core.instance.getServer().getOnlinePlayers());
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == XMaterial.AIR.parseMaterial()) return;


        if (clickedItem.getType() == XMaterial.PLAYER_HEAD.parseMaterial()) {
            Player selectedPlayer = Bukkit.getPlayer(UUID.fromString(clickedItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Core.instance, "uuid"), PersistentDataType.STRING)));
            player.openInventory(new TrollGUI(selectedPlayer).getInventory());
        } else if (clickedItem.getType() == XMaterial.BARRIER.parseMaterial()) {
            player.closeInventory();
        } else if (clickedItem.getType().equals(XMaterial.OAK_BUTTON.parseMaterial())) {
            if (ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(Core.instance.utils.getConfigPath("MenuItems.playerSelector.left.name")))) {
                if (page == 0) player.sendMessage(Core.instance.utils.getConfigPath("Messages.alreadyOnFirstPage"));
                else {
                    page--;
                    GUI.clear();
                    initializeItems();
                }
            } else if (ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(Core.instance.utils.getConfigPath("MenuItems.playerSelector.right.name")))) {
                if (!((index + 1) >= players.size())) {
                    page++;
                    GUI.clear();
                    initializeItems();
                } else player.sendMessage(Core.instance.utils.getConfigPath("Messages.alreadyOnLastPage"));
            }
        }
    }
}
