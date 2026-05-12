package me.xapu1337.recodes.trollgui.types;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.utilities.ItemStackBuilder;
import me.xapu1337.recodes.trollgui.utilities.Services;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrollMetaData {
    private String trollName;
    private final ItemStack itemStack;
    private ItemMeta itemMeta;
    private String name;
    private List<String> lore;
    private final Services services;

    public TrollMetaData(XMaterial material, Services services) {
        this.services = services;
        this.itemStack = new ItemStackBuilder(material, services).build();
        this.itemMeta = this.itemStack.getItemMeta();
        setDefaults();
    }

    private void setDefaults() {
        this.trollName = "Default Troll";
        this.name = "";
        this.lore = new ArrayList<>();
    }

    public TrollMetaData setName(String name) {
        this.name = name;
        return this;
    }

    public TrollMetaData setTrollName(String trollName) {
        this.trollName = trollName;
        return loadConfigData();
    }

    public List<String> getLore() {
        return this.lore;
    }
    public TrollMetaData setLore(String... lore) {
        this.lore = new ArrayList<>(Arrays.asList(lore));
        return this;
    }
    public String getName() {
        return this.name;
    }

    public String getTrollName() {
        return this.trollName;
    }


    public TrollMetaData loadConfigData() {
        String trollPath = "{config:menus.troll-menu.items.trolls." + this.trollName + ".";
        setName(services.messages().$(trollPath + "name}"));
        setLore(services.messages().$(trollPath + "lore}"));
        return this;
    }

    public ItemStack getItem() {
        if (!this.name.isEmpty()) {
            itemMeta.setDisplayName(services.messages().$(this.name));
            if (!this.lore.isEmpty()) {
                itemMeta.setLore(this.lore.stream().map(services.messages()::$).toList());
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

    public TrollMetaData setAttributes(TrollAttributes... attributes) {
        for (TrollAttributes attribute : attributes) {
            lore.add(attribute.getAttributeLore(services.messages()));
        }
        itemMeta.setLore(lore);
        services.debug().logObject(itemMeta);
        itemStack.setItemMeta(itemMeta);
        services.debug().logObject(itemStack);
        return this;
    }
}
