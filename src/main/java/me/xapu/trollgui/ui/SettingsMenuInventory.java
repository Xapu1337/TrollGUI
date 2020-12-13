package me.xapu.trollgui.ui;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import me.xapu.trollgui.main.Core;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
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

import java.util.Arrays;

public class SettingsMenuInventory implements Listener, InventoryHolder {
    private final Inventory inv;
    public String centerTitle(String title) {
        StringBuilder result = new StringBuilder();
        int spaces = (27 - ChatColor.stripColor(title).length());

        for (int i = 0; i < spaces; i++) {
            result.append(" ");
        }

        return result.append(title).toString();
    }
    public SettingsMenuInventory()
    {
        Bukkit.getPluginManager().registerEvents(this, Core.instance);
        inv = Bukkit.createInventory(this, 9, centerTitle(Core.instance.getP()+ Core.getPathCC("menu.settings")));
        initializeItems();
    }

    @Override
    public Inventory getInventory()
    {
        return inv;
    }

    // You can call this whenever you want to put the items in
    public void initializeItems()
    {
        ItemStack plc = new ItemStack(XMaterial.BLACK_STAINED_GLASS_PANE.parseMaterial(), 1);
        ItemMeta meta2 = plc.getItemMeta();
        meta2.setDisplayName(" ");
        plc.setItemMeta(meta2);
        for(Integer i = 0; i < inv.getSize(); i++){
            inv.setItem(i, plc);
        }
        ItemStack restart = new ItemStack(XMaterial.REDSTONE.parseMaterial(), 1);
        ItemMeta meta = restart.getItemMeta();
        meta.setDisplayName(Core.getPathCC("items.restart-name"));
        meta.setLore(Arrays.asList(Core.tcc(Core.getPathCC("items.restart-lore"))));
        meta.addEnchant(XEnchantment.DAMAGE_ALL.parseEnchantment(), 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        restart.setItemMeta(meta);

        inv.setItem(4,restart);

    }

    // You can open the inventory with this
    public void openInventory(final HumanEntity ent)
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

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType() == XMaterial.AIR.parseMaterial()) return;

        final Player p = (Player) e.getWhoClicked();
        if(e.getRawSlot() == 4){
            Core.instance.reloadConfig();
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Core.instance.getP()+ Core.getPathCC("menu.messages.restart")));
            p.playSound(p.getLocation(), XSound.ENTITY_PLAYER_LEVELUP.parseSound(), 2f, 2f);
        }
    }
}
