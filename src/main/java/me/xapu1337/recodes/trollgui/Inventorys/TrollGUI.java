package me.xapu1337.recodes.trollgui.Inventorys;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Cores.TrollCore;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Utilities.Singleton;
import me.xapu1337.recodes.trollgui.Utilities.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Random;

import static me.xapu1337.recodes.trollgui.Utilities.Singleton.getSingleInstance;

public class TrollGUI implements Listener, InventoryHolder {

    static {
        Bukkit.getLogger().info("TrollGUI is loading...");
    }

    private final Player _caller;
    private final Player _victim;
    private PlayerSelector _callingSelector;
    private final ItemStack _blackPanePlaceholder = Utilities.getSingleInstance().createItem(XMaterial.BLACK_STAINED_GLASS_PANE, "§r");
    private final ItemStack _redPanePlaceholder = Utilities.getSingleInstance().createItem(XMaterial.RED_STAINED_GLASS_PANE, Utilities.getSingleInstance().getConfigPath("Messages.targetSelfWarning"));
    private final int INVENTORY_SIZE_X = 9;
    private final int INVENTORY_SIZE_Y = 6;

    private final int INVENTORY_SIZE = (INVENTORY_SIZE_X * INVENTORY_SIZE_Y);

    private int CURRENT_PAGE = 0;
    private int CURRENT_INDEX = 0;


    /***
     * [][][][][][][][][] <-- Padding
     * []              <-- Trolls
     * []              []
     * []              []
     * []              []
     * []              []
     * []              []
     * []              []
     * [][][][][][][][][PS] <-- Player Selector (PS)
     */

    public Inventory GUI;



    private final void setBackground() {
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            if (getSingleInstance().doubleChestCenterSlots.contains(i)) {
                continue;
            }

            ItemStack placeholder = i >= INVENTORY_SIZE - INVENTORY_SIZE_X && _caller.equals(_victim) ? _redPanePlaceholder : _blackPanePlaceholder;
            GUI.setItem(i, placeholder);
        }
    }


    public TrollGUI(Player _caller, Player _victim) {
        this._victim = _victim;
        this._caller = _caller;

        Bukkit.getPluginManager().registerEvents(this, TrollCore.instance);
        GUI = Bukkit.createInventory(this, INVENTORY_SIZE,
                Utilities.getSingleInstance().getConfigPath("MenuItems.trollMenu.title")
                        .replace("%VICTIM%", _victim.getName())
                        .replace("%PLAYER%", _caller.getName())

        );

        initializeItems();
        getSingleInstance().currentPlayersTrolling.put(_caller, this);
    }

    public TrollGUI setCallingSelector(PlayerSelector _callingSelector) {
        this._callingSelector = _callingSelector;
        return this;
    }

    public void initializeItems(){

        getSingleInstance( ).loadedTrollHandlers.forEach( ( className, trollHandler ) -> trollHandler.setPlayers(_caller, _victim).setGUI( this ).Init( ) );
        Bukkit.getLogger().info("TrollGUI initialized for C " + _caller.getName() + " and V " + _victim.getName());
        GUI.clear();

        setBackground();

        int MAX_ITEMS_PER_PAGE = 7 * 4;
        for(int i = 0; i < MAX_ITEMS_PER_PAGE; i++) {
            CURRENT_INDEX = MAX_ITEMS_PER_PAGE * CURRENT_PAGE + i;
            if (!(CURRENT_INDEX >= getSingleInstance().loadedTrollHandlers.size()) && (getSingleInstance().loadedTrollHandlers.getValueAt(CURRENT_INDEX) != null)) {
                GUI.addItem(getSingleInstance().loadedTrollHandlers.getValueAt(CURRENT_INDEX).data.getItemStack());
            }
        }

        GUI.setItem(49, Utilities.getSingleInstance().createItem(XMaterial.BARRIER,
                Utilities.getSingleInstance().getConfigPath("MenuItems.trollMenu.items.returnToPlayerSelector.name"),
                Utilities.getSingleInstance().getConfigPath("MenuItems.trollMenu.items.returnToPlayerSelector.lore")));


        GUI.setItem(50, Utilities.getSingleInstance().createItem(XMaterial.OAK_BUTTON,  Utilities.getSingleInstance().getConfigPath("MenuItems.playerSelector.right.name"), Utilities.getSingleInstance().getConfigPath("MenuItems.playerSelector.right.lore")));
        GUI.setItem(48, Utilities.getSingleInstance().createItem(XMaterial.OAK_BUTTON,  Utilities.getSingleInstance().getConfigPath("MenuItems.playerSelector.left.name"), Utilities.getSingleInstance().getConfigPath("MenuItems.playerSelector.left.lore")));
        GUI.setItem(53, Utilities.getSingleInstance().createItem(XMaterial.DROPPER,  Utilities.getSingleInstance().getConfigPath("MenuItems.trollMenu.items.randomTroll.name"), Utilities.getSingleInstance().getConfigPath("MenuItems.trollMenu.items.randomTroll.lore")));
        _caller.openInventory(GUI);
    }

    @Override
    @NotNull
    public Inventory getInventory() { return GUI; }

    // Getters
    @NotNull
    public Player getVictim() { return _victim; }

    @NotNull
    public Player getCaller() { return _caller; }


    @EventHandler
    public void onInventoryClicked(InventoryClickEvent event) {
        if (event.getInventory().getHolder() != this) return;
        event.setCancelled(true);

        final ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == XMaterial.AIR.parseMaterial() || clickedItem.isSimilar(Utilities.getSingleInstance().createItem(XMaterial.BLACK_STAINED_GLASS_PANE, "§r"))) return;
        if(
                !getSingleInstance().loadedTrollHandlers.containsKey(clickedItem.getItemMeta().getPersistentDataContainer().get(getSingleInstance().trollItemNamespaceKey, PersistentDataType.STRING))
        ) {
            Bukkit.getLogger().warning("TrollGUI: Unknown item clicked: " + clickedItem.getItemMeta().getPersistentDataContainer().get(getSingleInstance().trollItemNamespaceKey, PersistentDataType.STRING));
            if (event.getRawSlot() == 49) {
                _caller.closeInventory();
                getSingleInstance().currentPlayersTrolling.remove(_caller);
                _callingSelector.openForPlayer(_caller);

            }
            if (event.getRawSlot() == 53) {
                // Get a random troll
                int randomTroll = new Random().nextInt(getSingleInstance().loadedTrollHandlers.size());
                while (getSingleInstance().loadedTrollHandlers.getValueAt(randomTroll) == null) {
                    randomTroll = new Random().nextInt(getSingleInstance().loadedTrollHandlers.size());
                }
                TrollHandler trollHandler = getSingleInstance().loadedTrollHandlers.getValueAt(randomTroll);
                _caller.sendMessage(Utilities.getSingleInstance().getConfigPath("Messages.selectedRandomTroll", true).replaceAll("%TROLL%", ChatColor.translateAlternateColorCodes('&', trollHandler.data.displayName)));
                trollHandler.execute( );
                if (trollHandler.data.isToggable) {
                    initializeItems();
                }
            }

            if (clickedItem.getType().equals(XMaterial.OAK_BUTTON.parseMaterial())) {
                if (ChatColor.stripColor(Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(Utilities.getSingleInstance().getConfigPath("MenuItems.playerSelector.left.name")))) {
                    if (CURRENT_PAGE == 0) _caller.sendMessage(Utilities.getSingleInstance().getConfigPath("Messages.alreadyOnFirstPage"));
                    else {
                        CURRENT_PAGE--;
                        GUI.clear();
                        initializeItems();
                    }
                } else if (ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(Utilities.getSingleInstance().getConfigPath("MenuItems.playerSelector.right.name")))) {
                    if (!((CURRENT_INDEX + 1) >= getSingleInstance().loadedTrollHandlers.size())) {
                        CURRENT_PAGE++;
                        GUI.clear();
                        initializeItems();
                    } else _caller.sendMessage(Utilities.getSingleInstance().getConfigPath("Messages.alreadyOnLastPage"));
                }
            }
        }


//        getSingleInstance().loadedTrollHandlers.forEach((className, trollHandler) -> {
//            if (Objects.equals(className, Objects.requireNonNull(clickedItem.getItemMeta()).getPersistentDataContainer().get(new NamespacedKey(TrollCore.instance, "assigned-troll-class"), PersistentDataType.STRING))) {
//
//            }
//        });
        // Get the troll handler for the clicked item (persistent data: "assigned-troll-class")
        String className = Objects.requireNonNull(clickedItem.getItemMeta()).getPersistentDataContainer().get(getSingleInstance().trollItemNamespaceKey, PersistentDataType.STRING);
        if (className == null) return;
        TrollHandler trollHandler = getSingleInstance().loadedTrollHandlers.get(className);
        if (trollHandler == null) return;
        trollHandler.setPlayers(_caller, _victim);
        trollHandler.execute();
        if (trollHandler.data.isToggable) {
            // If multiple people are trolling, we need to update the items for each player
            getSingleInstance().currentPlayersTrolling.forEach((player, trollGUI) -> {
                trollGUI.initializeItems();
            });
        }



    }

}
