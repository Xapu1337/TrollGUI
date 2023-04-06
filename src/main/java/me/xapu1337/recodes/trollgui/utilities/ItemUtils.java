package me.xapu1337.recodes.trollgui.utilities;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XItemStack;
import com.cryptomorin.xseries.XMaterial;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ItemUtils {

    public static ItemStack createItem(String name, XMaterial material, String... lore) {
        ItemStack item = material.parseItem();
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack createItem(String name, XMaterial material, boolean invisibleEnchant, String... lore) {
        ItemStack item = createItem(name, material, lore);

        if (invisibleEnchant) {
            Enchantment invisEnchant = XEnchantment.FIRE_ASPECT.getEnchant();
            item.addUnsafeEnchantment(invisEnchant, 1);
        }

        return item;
    }
}
