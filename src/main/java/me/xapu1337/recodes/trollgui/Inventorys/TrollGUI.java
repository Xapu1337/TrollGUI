package me.xapu1337.recodes.trollgui.Inventorys;

import com.cryptomorin.xseries.XMaterial;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import me.xapu1337.recodes.trollgui.Cores.Core;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

public class TrollGUI implements Listener, InventoryHolder {
    private final Player _caller;
    private final Player _victim;

    private final ItemStack _trollItemFiller = Core.instance.utils.createItem(XMaterial.STONE, "TROLL_ITEM_PLACEHOLDER_PLACE_ITEMS_HERE");
    private final int INVENTORY_SIZE_X = 9;
    private final int INVENTORY_SIZE_Y = 6;

    private final int INVENTORY_SIZE = (INVENTORY_SIZE_X * INVENTORY_SIZE_Y);

    private final int MAX_ITEMS_PER_PAGE = 7 * 4;
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
    public String centerTitle(String title) {
        StringBuilder result = new StringBuilder();
        int spaces = (27 - ChatColor.stripColor(title).length());

        for (int i = 0; i < spaces; i++) {
            result.append(" ");
        }

        return result.append(title).toString();
    }

    private final void setItemXY(int x, int y, ItemStack item) {
        int index = (y * INVENTORY_SIZE_X) + x;
        if (index < INVENTORY_SIZE && index >= 0)
            this.getInventory().setItem(index, item);
    }

    private final void setBackground() {
        int PADDING_SIZE = 1;

        for (int x = 0; x < INVENTORY_SIZE_X - PADDING_SIZE * 2; x++)
            for (int y = 0; y < INVENTORY_SIZE_Y - PADDING_SIZE * 2; y++)
                setItemXY(x + PADDING_SIZE, y + PADDING_SIZE, _trollItemFiller);

        for (int i = 0; i < INVENTORY_SIZE; i++) {
            if (this.getInventory().getItem(i) == null || this.getInventory().getItem(i).getType() == XMaterial.AIR.parseMaterial()) {
                this.getInventory().setItem(i, Core.instance.utils.createItem(XMaterial.BLACK_STAINED_GLASS_PANE, "§r"));
                // Check if we hit the last row, if we do so change nothing except if the caller is the victim, then we need to add a warning message
                if (i >= INVENTORY_SIZE_X * (INVENTORY_SIZE_Y - 1)) {
                    if (_caller.equals(_victim)) {
                        this.getInventory().setItem(i, Core.instance.utils.createItem(XMaterial.RED_STAINED_GLASS_PANE, Core.instance.utils.getConfigPath("Messages.targetSelfWarning")));
                    }
                }
            } else {
                if (this.getInventory().getItem(i).equals(_trollItemFiller))
                    this.getInventory().setItem(i, XMaterial.AIR.parseItem());
            }
        }
    }

    public TrollGUI(Player _caller, Player _victim) {
        this._victim = _victim;
        this._caller = _caller;

        Core.instance.singletons.currentPlayersTrolling.put(_caller, this);

        Bukkit.getPluginManager().registerEvents(this, Core.instance);
        GUI = Bukkit.createInventory(this, INVENTORY_SIZE, centerTitle(
                Core.instance.utils.getConfigPath("MenuTitles.trollGUI")
                        .replace("%VICTIM%", _victim.getName())
                        .replace("%PLAYER%", _caller.getName())
                )
        );

        initializeItems();
    }

    public void initializeItems(){

        try (ScanResult scanResult = new ClassGraph().enableAllInfo().acceptPackages("me.xapu1337.recodes.trollgui").scan()) {
            ClassInfoList controlClasses = scanResult.getSubclasses("me.xapu1337.recodes.trollgui.Handlers.TrollHandler");
            for (ClassInfo classInfo : controlClasses) {
                try {
                    Class<?> clazz = classInfo.loadClass();
                    ( (TrollHandler) clazz.getConstructor().newInstance()).setPlayers(_caller, _victim).Init();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }

        }

        GUI.clear();

        setBackground();

        for(int i = 0; i < MAX_ITEMS_PER_PAGE; i++) {
            CURRENT_INDEX = MAX_ITEMS_PER_PAGE * CURRENT_PAGE + i;
            if(CURRENT_INDEX >= Core.instance.singletons.holdingTrolls.size()) break;
            if (Core.instance.singletons.holdingTrolls.getValueAt(CURRENT_INDEX) != null) {

                GUI.addItem(Core.instance.singletons.holdingTrolls.getValueAt(CURRENT_INDEX).metaData.getItemStack());

            }
        }

        GUI.setItem(49, Core.instance.utils.createItem(XMaterial.BARRIER,
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.returnToPlayerSelector.name"),
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.returnToPlayerSelector.lore")));


        GUI.setItem(50, Core.instance.utils.createItem(XMaterial.OAK_BUTTON,  Core.instance.utils.getConfigPath("MenuItems.playerSelector.right.name"), Core.instance.utils.getConfigPath("MenuItems.playerSelector.right.lore")));
        GUI.setItem(48, Core.instance.utils.createItem(XMaterial.OAK_BUTTON,  Core.instance.utils.getConfigPath("MenuItems.playerSelector.left.name"), Core.instance.utils.getConfigPath("MenuItems.playerSelector.left.lore")));
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

        if (clickedItem == null || clickedItem.getType() == XMaterial.AIR.parseMaterial() || clickedItem.isSimilar(Core.instance.utils.createItem(XMaterial.BLACK_STAINED_GLASS_PANE, "§r"))) return;



        if(
                !Core.instance.singletons.holdingTrolls.values().stream().anyMatch(
                        trollHandler -> trollHandler.metaData.getItemStack().isSimilar(clickedItem)
                )
        ) {
            if (clickedItem.isSimilar(Core.instance.utils.createItem(XMaterial.BARRIER,
                    Core.instance.utils.getConfigPath("MenuItems.trollMenu.returnToPlayerSelector.name"),
                    Core.instance.utils.getConfigPath("MenuItems.trollMenu.returnToPlayerSelector.lore")))) {
                _caller.openInventory(new PlayerSelector(_caller).getInventory());
            }

            if (clickedItem.getType().equals(XMaterial.OAK_BUTTON.parseMaterial())) {
                if (ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(Core.instance.utils.getConfigPath("MenuItems.playerSelector.left.name")))) {
                    if (CURRENT_PAGE == 0) _caller.sendMessage(Core.instance.utils.getConfigPath("Messages.alreadyOnFirstPage"));
                    else {
                        CURRENT_PAGE--;
                        GUI.clear();
                        initializeItems();
                    }
                } else if (ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(Core.instance.utils.getConfigPath("MenuItems.playerSelector.right.name")))) {
                    if (!((CURRENT_INDEX + 1) >= Core.instance.singletons.holdingTrolls.size())) {
                        CURRENT_PAGE++;
                        GUI.clear();
                        initializeItems();
                    } else _caller.sendMessage(Core.instance.utils.getConfigPath("Messages.alreadyOnLastPage"));
                }
            }
        };


        Core.instance.singletons.holdingTrolls.forEach((className, trollHandler) -> {
            if (trollHandler.metaData.getItemStack().equals(clickedItem)) {
                trollHandler.execute();
                if (trollHandler.metaData.isToggable) {
                    initializeItems();

                }
            }
        });





    }
}
