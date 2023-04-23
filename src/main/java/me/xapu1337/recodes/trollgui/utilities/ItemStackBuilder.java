package me.xapu1337.recodes.trollgui.utilities;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.cores.TrollCore;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class ItemStackBuilder {

    
    private final XMaterial material;
    private String displayName;
    private List<String> lore = new ArrayList<>();
    private boolean isEnchantGlint;
    private UUID owner;

    public ItemStackBuilder(XMaterial material) {
        this.material = material;
    }

    public ItemStackBuilder withDisplayName(String displayName) {
        this.displayName = ConfigUtils.getInstance().$(displayName);
        return this;
    }

    public ItemStackBuilder withLore(List<String> lore) {
        this.lore = lore.stream().map(ConfigUtils.getInstance()::$).toList();
        return this;
    }

    public ItemStackBuilder withInvisibleEnchantmentGlint() {
        this.isEnchantGlint = true;
        return this;
    }

    public ItemStackBuilder withPlayerHead(UUID owner) {
        this.owner = owner;
        return this;
    }

    public ItemStack build() {
        ItemStack itemStack = null;

        if (material == XMaterial.PLAYER_HEAD && owner != null) {
            DebuggingUtil.getInstance().log("Creating player head with owner: " + owner);
            itemStack = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
            meta.setOwningPlayer(Bukkit.getOfflinePlayer(owner));
            meta.getPersistentDataContainer().set(Utils.getInstance().UUID_KEY, PersistentDataType.STRING, owner.toString());
            itemStack.setItemMeta(meta);
        } else {
            itemStack = material.parseItem();
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return null;
        }

        if (displayName != null) {
            meta.setDisplayName(ConfigUtils.getInstance().$(displayName));
        }

        if (!lore.isEmpty()) {
            meta.setLore(lore.stream().map(ConfigUtils.getInstance()::$).toList());
        }

        if (isEnchantGlint) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

            // Add enchantment effect to the item
            meta.addEnchant(XEnchantment.KNOCKBACK.getEnchant(), 1, true);
        }

        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
