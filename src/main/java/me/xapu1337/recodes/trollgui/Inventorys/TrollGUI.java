package me.xapu1337.recodes.trollgui.Inventorys;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Cores.Core;
import me.xapu1337.recodes.trollgui.Trolls.*;
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

public class TrollGUI implements Listener, InventoryHolder {
    private Player player;
    public Inventory GUI;
    public String centerTitle(String title) {
        StringBuilder result = new StringBuilder();
        int spaces = (27 - ChatColor.stripColor(title).length());

        for (int i = 0; i < spaces; i++) {
            result.append(" ");
        }

        return result.append(title).toString();
    }
    public TrollGUI(Player player) {
        this.player = player;
        Bukkit.getPluginManager().registerEvents(this, Core.instance);
        GUI = Bukkit.createInventory(this, 54, centerTitle(Core.instance.utils.getConfigPath("MenuTitles.trollGUI").replace("%PLAYER%", player.getName())));
        initializeItems();
    }

    public void initializeItems(){
        for(int i = 0; i < GUI.getSize(); i++)
            GUI.setItem(i, Core.instance.utils.createItem(XMaterial.GRAY_STAINED_GLASS_PANE, false, " "));

        GUI.setItem(10, Core.instance.utils.createItem(XMaterial.BLAZE_POWDER, false,
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.burnPlayer.name"),
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.burnPlayer.lore")));

        GUI.setItem(11, Core.instance.utils.createItem(XMaterial.BARRIER, false,
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.closeGUI.name"),
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.closeGUI.lore")));

        GUI.setItem(12, Core.instance.utils.createItem(XMaterial.CAULDRON, false,
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.dropAll.name"),
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.dropAll.lore")));

        GUI.setItem(13, Core.instance.utils.createItem(XMaterial.WATER_BUCKET, false,
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.dropItem.name"),
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.dropItem.lore")));

        GUI.setItem(14, Core.instance.utils.createItem(XMaterial.TNT, false,
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.explodePlayer.name"),
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.explodePlayer.lore")));

        XMaterial customMatForFakeBlock;

        if(XMaterial.matchXMaterial(Core.instance.config.getString("MenuItems.trollMenu.fakeBlock.options.block")).isPresent())
            customMatForFakeBlock = XMaterial.matchXMaterial(Core.instance.config.getString("MenuItems.trollMenu.fakeBlock.options.block")).get();
        else
            customMatForFakeBlock = XMaterial.TNT;

        GUI.setItem(15, Core.instance.utils.createItem(customMatForFakeBlock, false,
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.fakeBlock.name"),
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.fakeBlock.lore")));
    }

    @Override
    @NotNull
    public Inventory getInventory() { return GUI; }


    @EventHandler
    public void onInventoryClicked(InventoryClickEvent event) {
        if (event.getInventory().getHolder() != this) return; // IF the inventory belongs not to this class dismiss.
        event.setCancelled(true); // Disable the item to be draggable.

        final ItemStack clickedItem = event.getCurrentItem();
        Player caller = (Player) event.getWhoClicked();

        if (clickedItem == null || clickedItem.getType() == XMaterial.AIR.parseMaterial()) return;

        switch (event.getRawSlot()){
            case 10:
                new BurnPlayerTroll(caller, player).execute();
                break;
            case 11:
                new CloseGUITroll(caller, player).execute();
                break;
            case 12:
                new DropAllTroll(caller, player).execute();
                break;
            case 13:
                new DropItemTroll(caller, player).execute();
                break;
            case 14:
                new ExplodePlayerTroll(caller, player).execute();
                break;
            case 15:
                new FakeBlockTroll(caller, player).execute();
                break;
        }


    }
}
