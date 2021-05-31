package me.xapu1337.recodes.trollgui.Utilities;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class Util {
    public static Util instance;

    Util() {
        if (instance == null)
            instance = this;
    }



    public static ItemStack createItem(final XMaterial xMat, final Boolean isEnchanted , final String name, final String... lore) {
        final ItemStack item = new ItemStack(xMat.parseMaterial(), 1);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        if(isEnchanted){
            meta.addEnchant(XEnchantment.DURABILITY.parseEnchantment(), 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        // Set the lore of the ite
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }




}
