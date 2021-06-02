package me.xapu1337.recodes.trollgui.Utilities;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Cores.Core;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Objects;

public class Util {

    public ItemStack createItem(final XMaterial xMat, final Boolean isEnchanted , final String name, final String... lore) {
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

    public String getConfigPath(String path){
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Core.instance.getConfig().getString(path)));
    }

    public String getConfigPath(String path, boolean withPrefix){
        return ChatColor.translateAlternateColorCodes('&', (withPrefix ? ( getConfigPath("Variables.prefix") + getConfigPath(path)) : (getConfigPath(path))));
    }


    public boolean advancedPermissionsChecker(Player player, String extraPermissions){
        if(Core.instance.getConfig().getBoolean("variables.advancedPermissions.enabled")){
            if(Core.instance.usingUUID){
                OfflinePlayer pp = Bukkit.getPlayerExact(Core.instance.getConfig().getString("variables.advancedPermissions.name"));
                if(pp.isOnline() && pp.hasPlayedBefore()){
                    return player.getUniqueId().equals(pp.getUniqueId());
                }
            } else {
                return player.getName().equals(Core.instance.getConfig().getString("variables.advancedPermissions.name"));
            }
        } else {
            return player.hasPermission(extraPermissions);
        }
        return false;
    }


}
