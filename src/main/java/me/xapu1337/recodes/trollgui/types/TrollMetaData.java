package me.xapu1337.recodes.trollgui.types;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.utilities.ConfigUtils;
import me.xapu1337.recodes.trollgui.utilities.ItemStackBuilder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TrollMetaData {
    private String trollName;
    private ItemStack itemStack;
    private ItemMeta itemMeta;
    private boolean isTogglable;
    private boolean isToggled;
    private String name;
    private List<String> lore;

    public TrollMetaData(XMaterial material) {
        // set default values
        this.trollName = "Default Troll";
        this.itemStack = new ItemStackBuilder(material).build();
        this.itemMeta = this.itemStack.getItemMeta();
        this.isTogglable = false;
        this.isToggled = false;
        this.name = "";
        this.lore = new ArrayList<>();
    }

    public TrollMetaData setLore(String... lore) {
        this.lore = List.of(lore);
        return this;
    }

    public TrollMetaData setName(String name) {
        this.name = name;
        return this;
    }

    public TrollMetaData setTrollName(String trollName) {
        this.trollName = trollName;
        return this;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public String getName() {
        return this.name;
    }

    public String getTrollName() {
        return this.trollName;
    }

    public boolean isToggled() {
        return this.isToggled;
    }

    public TrollMetaData setToggled(boolean isToggled) {
        this.isToggled = isToggled;
        return this;
    }

    public boolean isTogglable() {
        return this.isTogglable;
    }

    public TrollMetaData setTogglable(boolean isTogglable) {
        this.isTogglable = isTogglable;
        return this;
    }

    public TrollMetaData loadConfigData(String trollName) {
        this.trollName = trollName;
        this.name = ConfigUtils.getInstance().$(ConfigUtils.getInstance().$("{config:menus.troll-menu.items.trolls." + trollName + ".name}"));
        this.lore = Collections.singletonList(ConfigUtils.getInstance().$(ConfigUtils.getInstance().$("{config:menus.troll-menu.items.trolls." + trollName + ".lore}")));

        return this;
    }

    public ItemStack getItem() {
        if (!this.name.isEmpty()) {
            itemMeta.setDisplayName(ConfigUtils.getInstance().$(this.name));
            if (this.lore != null && this.lore.size() > 0) {
                itemMeta.setLore(this.getLore().stream().map(ConfigUtils.getInstance()::$).toList());
            }
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    public ItemMeta getItemMeta() {
        return itemMeta;
    }

    public TrollMetaData setItemMeta(ItemMeta itemMeta) {
        this.itemMeta = itemMeta;
        return this;
    }

    public TrollMetaData setAttributes(TrollAttributes ...attributes){

        if (attributes.length == 0)
            return this;


        for (TrollAttributes attribute : attributes) {
            this.lore.add(attribute.getAttributeLore());
        }

        itemMeta.setLore(lore);

        itemStack.setItemMeta(itemMeta);

        return this;
    }



}
