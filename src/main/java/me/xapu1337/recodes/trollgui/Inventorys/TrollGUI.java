package me.xapu1337.recodes.trollgui.Inventorys;

import me.xapu1337.recodes.trollgui.Cores.Core;
import me.xapu1337.recodes.trollgui.Utilities.EnumCollection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

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
        GUI = Bukkit.createInventory(this, 54, centerTitle(EnumCollection.MenuTitles.TROLLGUI.get()));
        initializeItems();
    }

    public void initializeItems(){

    }

    @Override
    public Inventory getInventory() { return GUI; }
}
