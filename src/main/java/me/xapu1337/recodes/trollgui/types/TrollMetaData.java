package me.xapu1337.recodes.trollgui.types;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.utilities.MessageUtils;
import me.xapu1337.recodes.trollgui.utilities.DebuggingUtil;
import me.xapu1337.recodes.trollgui.utilities.ItemStackBuilder;
import me.xapu1337.recodes.trollgui.utilities.TrollToggablesStorage;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TrollMetaData {
    private String trollName;
    private final ItemStack itemStack;
    private ItemMeta itemMeta;
    private String name;
    private List<String> lore;

    public TrollMetaData(XMaterial material) {
        this.itemStack = new ItemStackBuilder(material).build();
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
        setName(MessageUtils.getInstance().$(trollPath + "name}"));
        setLore(MessageUtils.getInstance().$(trollPath + "lore}"));
        return this;
    }

    public ItemStack getItem() {
        if (!this.name.isEmpty()) {
            itemMeta.setDisplayName(MessageUtils.getInstance().$(this.name));
            if (!this.lore.isEmpty()) {
                itemMeta.setLore(this.lore.stream().map(MessageUtils.getInstance()::$).toList());
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
            lore.add(attribute.getAttributeLore());
        }
        itemMeta.setLore(lore);
        DebuggingUtil.getInstance().logObject(itemMeta);
        itemStack.setItemMeta(itemMeta);
        DebuggingUtil.getInstance().logObject(itemStack);
        return this;
    }



}
