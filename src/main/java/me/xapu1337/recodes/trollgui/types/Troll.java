package me.xapu1337.recodes.trollgui.types;

import com.cryptomorin.xseries.XEnchantment;
import me.xapu1337.recodes.trollgui.cores.TrollCore;
import me.xapu1337.recodes.trollgui.inventories.TrollSelectionInventory;
import me.xapu1337.recodes.trollgui.utilities.DebuggingUtil;
import me.xapu1337.recodes.trollgui.utilities.TrollToggablesStorage;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.lang.ref.WeakReference;

public abstract class Troll {
    private Player caller;
    private Player victim;
    private TrollMetaData trollMetaData;
    public static final NamespacedKey trollClassKey = new NamespacedKey(TrollCore.getInstance(), "assigned-troll-class");

    private WeakReference<TrollSelectionInventory> callingGUI;

    public Troll() {
    }

    public <T extends Troll> T Init() {
        this.trollMetaData = setMetaData();
        DebuggingUtil.getInstance().logObject(this.trollMetaData);
        ItemMeta itemMeta = this.trollMetaData.getItemMeta();
        DebuggingUtil.getInstance().logObject(itemMeta);
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

    public void toggleTroll(Player v) { TrollToggablesStorage.getInstance().toggle(v.getUniqueId(), trollMetaData.getTrollName()); checkToggled(); }

    public <T extends Troll> T setCallingGUI(TrollSelectionInventory callingGUI) {
        this.callingGUI = new WeakReference<>(callingGUI);
        return (T) this;
    }


    public <T extends Troll> T checkToggled() {
        DebuggingUtil.getInstance().log("Checking if troll is toggled");
        if (TrollToggablesStorage.getInstance().hasToggle(victim.getUniqueId(), trollMetaData.getTrollName())) {
            DebuggingUtil.getInstance().log("Troll is toggled");
            ItemMeta itemMeta = trollMetaData.getItemMeta();
            DebuggingUtil.getInstance().logObject(itemMeta);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.addEnchant(XEnchantment.DURABILITY.getEnchant(), 1, true);
            getTrollMetaData().setItemMeta(itemMeta);
            getCallingGUI().builder.build();
        } else {
            DebuggingUtil.getInstance().log("Troll is not toggled");
            ItemMeta itemMeta = trollMetaData.getItemMeta();
            DebuggingUtil.getInstance().logObject(itemMeta);
            itemMeta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.removeEnchant(XEnchantment.DURABILITY.getEnchant());
            getTrollMetaData().setItemMeta(itemMeta);
            getCallingGUI().builder.build();
        }
        return (T) this;
    }

    public TrollSelectionInventory getCallingGUI() {
        return callingGUI != null ? callingGUI.get() : null;
    }
    public abstract void execute();
}
