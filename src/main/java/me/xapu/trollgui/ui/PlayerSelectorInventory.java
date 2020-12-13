package me.xapu.trollgui.ui;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import me.xapu.trollgui.main.Core;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;

public class PlayerSelectorInventory implements InventoryHolder, Listener {
    private final Inventory inv;
    private final Inventory inv2;
    public String centerTitle(String title) {
        StringBuilder result = new StringBuilder();
        int spaces = (27 - ChatColor.stripColor(title).length());

        for (int i = 0; i < spaces; i++) {
            result.append(" ");
        }

        return result.append(title).toString();
    }
    static PlayerSelectorInventory main;

    public static PlayerSelectorInventory getPS(){
        return main;
    }
    public PlayerSelectorInventory()
    {
        Bukkit.getPluginManager().registerEvents(this, Core.instance);
        // Create a new inventory, with "this" owner for comparison with other inventories, a size of nine, called example
        inv = Bukkit.createInventory(this, 54, centerTitle(Core.instance.getP()+ Core.tcc(Core.instance.getConfig().getString("menu.select-player"))));
        inv2 = Bukkit.createInventory(this, 54, centerTitle(Core.instance.getP()+ Core.tcc(Core.instance.getConfig().getString("menu.select-player"))));
        // Put the items into the inventory
        main = this;
        initializeItems();
    }

    @Override
    public @NotNull Inventory getInventory()
    {
        return inv;
    }
    // Nice little method to create a gui item with a custom name, and description
    protected ItemStack createGuiItem(final XMaterial XMaterial, final Boolean isEnchanted , final String name, final String... lore) {
        final ItemStack item = new ItemStack(XMaterial.parseMaterial(), 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);
        if(isEnchanted){
            meta.addEnchant(XEnchantment.DURABILITY.parseEnchantment(), 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }
    ItemStack nextPage = createGuiItem(XMaterial.REDSTONE_BLOCK, true, Core.getPathCC("items.nextpage-name"), Core.getPathCC("items.nextpage-lore"));
    ItemStack mainPage = createGuiItem(XMaterial.REDSTONE_BLOCK, true, Core.getPathCC("items.mainpage-name"), Core.getPathCC("items.mainpage-lore"));
    // You can call this whenever you want to put the items in
    int playersdone = 0;
    public void initializeItems()
    {

        Bukkit.getOnlinePlayers().forEach(o -> {

            final ItemStack item = new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial(), 1);

            // Set the lore of the item
            SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(o.getUniqueId()));
            skullMeta.setDisplayName(o.getName());
            if(o.isOp()){
                skullMeta.setLore(Collections.singletonList(Core.getPathCC("items.messages.isOP")));
            }

            item.setItemMeta(skullMeta);
            if(playersdone > inv.getSize()-3){
                inv2.addItem(item);
            } else {
                inv.addItem(item);
                playersdone++;
            }


        });

        if(Bukkit.getOnlinePlayers().size() > inv.getSize()-3){
            inv.setItem(53, nextPage);
        }
        if(Bukkit.getOnlinePlayers().size() > inv2.getSize()-3){
            inv2.setItem(53, mainPage);
        }
    }

    // You can open the inventory with this
    public void openUniInv(final HumanEntity ent, Inventory inv)
    {
        ent.openInventory(inv);
    }

    public void openSel(final HumanEntity ent)
    {
        ent.openInventory(inv);
    }
    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e)
    {
        if (e.getInventory().getHolder() != this) return;
            e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == XMaterial.AIR.parseMaterial()) return;

        final Player p = (Player) e.getWhoClicked();
        if(clickedItem.equals(nextPage)){
            p.closeInventory();
            openUniInv(p, inv2);
        } else if(clickedItem.equals(mainPage)){
            p.closeInventory();
            openUniInv(p, inv);
        }
        final Player Vic = Bukkit.getPlayerExact(clickedItem.getItemMeta().getDisplayName());
        if(Vic != null){
            if(Vic instanceof Player){
                TrollInventory gt = new TrollInventory(Vic);
                gt.openInventory(p);
            }
        }
    }
}
