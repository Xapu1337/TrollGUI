package me.xapu1337.recodes.trollgui.Inventorys;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Cores.TrollCore;
import me.xapu1337.recodes.trollgui.Utilities.TriConsumer;
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
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;
import java.util.function.BiConsumer;

public class PlayerSelector implements Listener, InventoryHolder{
    public Inventory GUI;

    public Player player;
    int maxItemsPerPage = 43;
    int page = 0;
    int index = 0;
    private Inventory _previousInventory;
    private boolean _hasPreviousInventory;
    ArrayList<Player> players = new ArrayList<>(TrollCore.instance.getServer().getOnlinePlayers());
    /**
     * @param caller 1st argument
     * @param victim 2nd argument
     */
    public TriConsumer<PlayerSelector, Player, Player> onPlayerSelect;

    public PlayerSelector setPreviousInventory(Inventory inventory) {
        _previousInventory = inventory;
        _hasPreviousInventory = true;
        return this;
    }

    public PlayerSelector(Player player, String action) {
        this._hasPreviousInventory = false;
        this.player = player;
        Bukkit.getPluginManager().registerEvents(this, TrollCore.instance);
        GUI = Bukkit.createInventory(this, 54,
                Utilities.getSingleInstance().getConfigPath("MenuItems.playerSelector.title").replace("%ACTION%", action)
        );
        initializeItems();
    }

    public PlayerSelector(Player player, String action, Player ...excludedPlayers) {
        this._hasPreviousInventory = false;
        this.player = player;
        for (Player excludedPlayer : excludedPlayers) {
            if (excludedPlayer.isOnline() && players.contains(excludedPlayer)) {
                players.remove(excludedPlayer);
            }
        }
        Bukkit.getPluginManager().registerEvents(this, TrollCore.instance);
        String title = Utilities.getSingleInstance().getConfigPath("MenuItems.playerSelector.title").replace("%ACTION%", action);
        GUI = Bukkit.createInventory(this, 54,
                title
        );
        initializeItems();

    }

    public PlayerSelector(Player player, String action, Inventory previousInventory) {
        this._hasPreviousInventory = true;
        this._previousInventory = previousInventory;
        this.player = player;
        Bukkit.getPluginManager().registerEvents(this, TrollCore.instance);
        GUI = Bukkit.createInventory(this, 54,
                Utilities.getSingleInstance().getConfigPath("MenuItems.playerSelector.title").replace("%ACTION%", action)
        );
        initializeItems();
    }

    public PlayerSelector(Player player, String action, Inventory previousInventory, Player ...excludedPlayers) {
        this._hasPreviousInventory = true;
        this._previousInventory = previousInventory;
        this.player = player;
        for (Player excludedPlayer : excludedPlayers) {
            if (excludedPlayer.isOnline() && players.contains(excludedPlayer)) {
                players.remove(excludedPlayer);
            }
        }
        Bukkit.getPluginManager().registerEvents(this, TrollCore.instance);
        String title = Utilities.getSingleInstance().getConfigPath("MenuItems.playerSelector.title").replace("%ACTION%", action);
        GUI = Bukkit.createInventory(this, 54,
                title
        );
        initializeItems();

    }


    public void initializeItems(){
        for(int i = 45; i < 54; i++)
            GUI.setItem(i, Utilities.getSingleInstance().createItem(XMaterial.GRAY_STAINED_GLASS_PANE,  " "));
        GUI.setItem(48, Utilities.getSingleInstance().createItem(XMaterial.OAK_BUTTON,  Utilities.getSingleInstance().getConfigPath("MenuItems.playerSelector.left.name"), Utilities.getSingleInstance().getConfigPath("MenuItems.playerSelector.left.lore")));
        GUI.setItem(49, Utilities.getSingleInstance().createItem(XMaterial.BARRIER,
                _hasPreviousInventory ?
                        Utilities.getSingleInstance().getConfigPath("MenuItems.playerSelector.close.withHistory.name") : Utilities.getSingleInstance().getConfigPath("MenuItems.playerSelector.close.noHistory.name"),
                _hasPreviousInventory ?
                        Utilities.getSingleInstance().getConfigPath("MenuItems.playerSelector.close.withHistory.lore") : Utilities.getSingleInstance().getConfigPath("MenuItems.playerSelector.close.noHistory.lore")));
        GUI.setItem(50, Utilities.getSingleInstance().createItem(XMaterial.OAK_BUTTON,  Utilities.getSingleInstance().getConfigPath("MenuItems.playerSelector.right.name"), Utilities.getSingleInstance().getConfigPath("MenuItems.playerSelector.right.lore")));
        GUI.setItem(53, Utilities.getSingleInstance().createItem(XMaterial.DROPPER,  Utilities.getSingleInstance().getConfigPath("MenuItems.playerSelector.random.name"), Utilities.getSingleInstance().getConfigPath("MenuItems.playerSelector.random.lore")));
        if(players != null && !players.isEmpty()) {
            for(int i = 0; i < maxItemsPerPage; i++) {
                index = maxItemsPerPage * page + i;
                if(index >= players.size()) break;
                if (players.get(index) != null){
                    ItemStack playerItem = new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial(), 1);
                    SkullMeta playerMeta = (SkullMeta) playerItem.getItemMeta();
                    playerMeta.setDisplayName(ChatColor.RED + players.get(index).getDisplayName());
                    playerMeta.setOwningPlayer(Bukkit.getPlayer(players.get(index).getUniqueId()));
                    playerMeta.getPersistentDataContainer().set(new NamespacedKey(TrollCore.instance, "uuid"), PersistentDataType.STRING, Utilities.getSingleInstance().uuidOrName(players.get(index), TrollCore.instance.usingUUID));
                    if(players.get(index).isOp())
                        playerMeta.setLore(Collections.singletonList(Utilities.getSingleInstance().getConfigPath("MenuItems.playerSelector.extras.hasOP")));

                    playerItem.setItemMeta(playerMeta);
                    GUI.addItem(playerItem);

                }
            }
        }

    }

    @Override
    public Inventory getInventory() { return GUI; }

    public PlayerSelector setOnPlayerSelect(TriConsumer<PlayerSelector, Player, Player> onPlayerSelect) {
        this.onPlayerSelect = onPlayerSelect;
        return this;
    }

    public PlayerSelector disposePreviousInventory() {
        if(_hasPreviousInventory) {
            _previousInventory.clear();
            _previousInventory = null;
        }
        return this;
    }

    public PlayerSelector openForPlayer(Player player) {
        player.openInventory(GUI);
        return this;
    }


    @EventHandler
    public void onInventoryClicked(InventoryClickEvent event) {
        if (event.getInventory().getHolder() != this) return; // IF the inventory belongs not to this class dismiss.
            event.setCancelled(true); // Disable the item to be draggable.

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == XMaterial.AIR.parseMaterial()) return;

        String itemStore = clickedItem
                .getItemMeta()
                .getPersistentDataContainer()
                .get(new NamespacedKey(TrollCore.instance, "uuid"), PersistentDataType.STRING);
        if (clickedItem.getType() == XMaterial.PLAYER_HEAD.parseMaterial()) {
            // The id might be an uuid or a name depending on the online mode.
            Player target = null;
            if (itemStore != null && !itemStore.isEmpty() && Utilities.getSingleInstance().isValidUUID(itemStore)) {
                target = Bukkit.getPlayer(UUID.fromString(itemStore));
            } else {
                target = Bukkit.getPlayer(itemStore);
            }

//            Player selectedPlayer = Bukkit
//                    .getPlayer(UUID
//                            .fromString(itemStore));

            if(target == null) {
                event.getWhoClicked().sendMessage(Utilities.getSingleInstance().getConfigPath("Messages.playerNotAvailable", true).replaceAll("%PLAYER%", itemStore));
                return;
            }

            onPlayerSelect.accept(this, (Player) event.getWhoClicked(), target);


        } else if (clickedItem.getType() == XMaterial.BARRIER.parseMaterial()) {
            // Check if we have a previous inventory, if so go back to it else close it
            if (_hasPreviousInventory) {
                event.getWhoClicked().closeInventory();
                event.getWhoClicked().openInventory(_previousInventory);
            } else {
                event.getWhoClicked().closeInventory();
            }
        } else if (clickedItem.getType().equals(XMaterial.OAK_BUTTON.parseMaterial())) {
            if (ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(Utilities.getSingleInstance().getConfigPath("MenuItems.playerSelector.left.name")))) {
                if (page == 0) player.sendMessage(Utilities.getSingleInstance().getConfigPath("Messages.alreadyOnFirstPage"));
                else {
                    page--;
                    GUI.clear();
                    initializeItems();
                }
            } else if (ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(Utilities.getSingleInstance().getConfigPath("MenuItems.playerSelector.right.name")))) {
                if (!((index + 1) >= players.size())) {
                    page++;
                    GUI.clear();
                    initializeItems();
                } else player.sendMessage(Utilities.getSingleInstance().getConfigPath("Messages.alreadyOnLastPage"));
            }
        } else if (clickedItem.getType() == XMaterial.DROPPER.parseMaterial()) {
            if(players != null && !players.isEmpty()) {
                int random = new Random().nextInt(players.size());
                Player selectedPlayer = players.get(random);
                onPlayerSelect.accept(this, (Player) event.getWhoClicked(), selectedPlayer);
            }
        }
    }
}
