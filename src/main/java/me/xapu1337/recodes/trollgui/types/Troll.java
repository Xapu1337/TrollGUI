package me.xapu1337.recodes.trollgui.types;

import me.xapu1337.recodes.trollgui.cores.TrollCore;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public abstract class Troll {
    private Player caller;
    private Player victim;
    private TrollMetaData trollMetaData;
    public static final NamespacedKey trollClassKey = new NamespacedKey(TrollCore.getInstance(), "assigned-troll-class");

    public Troll() {
    }

    public <T extends Troll> T Init() {
        this.trollMetaData = setMetaData();
        ItemMeta itemMeta = this.trollMetaData.getItemMeta();
        itemMeta.getPersistentDataContainer().set(trollClassKey, PersistentDataType.STRING, getClass().getName());
        this.trollMetaData.setItemMeta(itemMeta);

        return (T) this;
    }

    public abstract TrollMetaData setMetaData();

    public Player getCaller() {
        return caller;
    }

    public <T extends Troll> T setCaller(Player caller) {
        this.caller = caller;
        return (T) this;
    }

    public Player getVictim() {
        return victim;
    }

    public <T extends Troll> T setVictim(Player victim) {
        this.victim = victim;
        return (T) this;
    }

    public TrollMetaData getTrollMetaData() {
        return trollMetaData;
    }

    public <T extends Troll> T setTrollMetaData(TrollMetaData trollMetaData) {
        this.trollMetaData = trollMetaData;
        return (T) this;
    }

    public abstract void execute();
}
