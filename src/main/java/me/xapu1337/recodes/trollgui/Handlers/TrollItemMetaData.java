package me.xapu1337.recodes.trollgui.Handlers;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Cores.Core;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.function.Supplier;

public class TrollItemMetaData {

    public boolean isToggable = false;

    public boolean isToggled = false;

    public String displayName = " - PLACEHOLDER - ";

    public String[] lore = { " - PLACEHOLDER - " };

    private XMaterial _item;

    private ItemStack _itemStack;

    private ItemMeta _itemMeta;

    public TrollItemMetaData setToggable(boolean toggable) {
        isToggable = toggable;
        return this;
    }

    public TrollItemMetaData setToggled(Supplier<Boolean> toggledCallback){
        isToggled = toggledCallback.get();

        if (isToggled)
            _itemMeta.addEnchant(XEnchantment.DURABILITY.getEnchant(), 1, true);
        else
            _itemMeta.removeEnchant(XEnchantment.DURABILITY.getEnchant());

        // Append either disabled or enabled text to current lore

        lore = Arrays.copyOf(lore, lore.length + 1);
        lore[lore.length - 1] = isToggled ? Core.instance.utils.getConfigPath("MenuItems.trollMenu.extras.isEnabled") : Core.instance.utils.getConfigPath("MenuItems.trollMenu.extras.isDisabled");
        _itemMeta.setLore(Arrays.asList(lore));

        _itemStack.setItemMeta(_itemMeta);

        return this;
    }

    public TrollItemMetaData setToggled(){
        isToggled = !isToggled;

        if (isToggled)
            _itemMeta.addEnchant(XEnchantment.DURABILITY.getEnchant(), 1, true);
        else
            _itemMeta.removeEnchant(XEnchantment.DURABILITY.getEnchant());

        lore = Arrays.copyOf(lore, lore.length + 1);
        lore[lore.length - 1] = isToggled ? Core.instance.utils.getConfigPath("MenuItems.trollMenu.extras.isEnabled") : Core.instance.utils.getConfigPath("MenuItems.trollMenu.extras.isDisabled");
        _itemMeta.setLore(Arrays.asList(lore));

        _itemStack.setItemMeta(_itemMeta);

        return this;
    }

    public TrollItemMetaData setDisplayName(String displayName){
        this.displayName = displayName;

        this._itemMeta.setDisplayName(displayName);

        _itemStack.setItemMeta(_itemMeta);
        return this;
    }

    public TrollItemMetaData setLore(String ...lore){
        this.lore = lore;

        _itemMeta.setLore(Arrays
                .asList(

                        lore

                )
        );

        _itemStack.setItemMeta(_itemMeta);

        return this;
    }

    public TrollItemMetaData setConfigData(String itemConfigName){
        try {
            this.setDisplayName(Core.instance.utils.getConfigPath("MenuItems.trollMenu." + itemConfigName + ".name"));
            this.setLore(Core.instance.utils.getConfigPath("MenuItems.trollMenu." + itemConfigName + ".lore").split("\\|"));
        } catch (NullPointerException e) {
            Bukkit.getLogger().warning("[TrollGUI] Could not find config data for item: " + itemConfigName + " (either .name or .lore is missing)");
            e.printStackTrace();
        }



        return this;
    }

    public TrollItemMetaData setItem(XMaterial item){
        _item = item;

        _itemStack = new ItemStack(item.parseMaterial(), 1);

        _itemMeta = _itemStack.getItemMeta();

        _itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        _itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        _itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

        _itemStack.setItemMeta(_itemMeta);
        return this;
    }

    // A function to replace placeholders in the current lore with values
    public TrollItemMetaData formatPlaceholders(String placeholders, String ...values){
        String[] lore = this.lore;
        for (int i = 0; i < lore.length; i++) {
            lore[i] = lore[i].replace(placeholders, values[i]);
        }
        _itemMeta.setLore(Arrays.asList(lore));
        _itemStack.setItemMeta(_itemMeta);

        return this;
    }


    // Getters
    public XMaterial getItem(){
        return _item;
    }

    public ItemStack getItemStack(){
        return _itemStack;
    }

    public ItemMeta getItemMeta(){
        return _itemMeta;
    }
}
