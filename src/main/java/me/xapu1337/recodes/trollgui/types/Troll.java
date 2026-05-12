package me.xapu1337.recodes.trollgui.types;

import com.cryptomorin.xseries.XEnchantment;
import me.xapu1337.recodes.trollgui.cores.TrollCore;
import me.xapu1337.recodes.trollgui.inventories.TrollSelectionInventory;
import me.xapu1337.recodes.trollgui.utilities.Services;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.lang.ref.WeakReference;

@SuppressWarnings("unchecked")
public abstract class Troll {
    private Player caller;
    private Player victim;
    private TrollMetaData trollMetaData;
    protected Services services;
    public static final NamespacedKey trollClassKey = new NamespacedKey(TrollCore.getInstance(),
            "assigned-troll-class");

    private WeakReference<TrollSelectionInventory> callingGUI;

    public Troll() {
    }

    public void injectServices(Services services) {
        this.services = services;
    }

    public <T extends Troll> T Init() {
        services.debug().logObject(this.trollMetaData);
        ItemMeta itemMeta = this.trollMetaData.getItemMeta();
        services.debug().logObject(itemMeta);
        itemMeta.getPersistentDataContainer().set(trollClassKey, PersistentDataType.STRING, getClass().getName());
        this.trollMetaData.setItemMeta(itemMeta);

        return (T) this;
    }

    public TrollMetaData setMetaData() {
        return null;
    }

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

    public void toggleTroll(Player v) {
        services.toggles().toggle(v.getUniqueId(), trollMetaData.getTrollName());
        checkToggled();
    }

    public <T extends Troll> T setCallingGUI(TrollSelectionInventory callingGUI) {
        this.callingGUI = new WeakReference<>(callingGUI);
        return (T) this;
    }

    public <T extends Troll> T checkToggled() {
        services.debug().log("Checking if troll is toggled");
        TrollSelectionInventory gui = getCallingGUI();
        if (services.toggles().hasToggle(victim.getUniqueId(), trollMetaData.getTrollName())) {
            services.debug().log("Troll is toggled");
            ItemMeta itemMeta = trollMetaData.getItemMeta();
            services.debug().logObject(itemMeta);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.addEnchant(XEnchantment.DURABILITY.getEnchant(), 1, true);
            getTrollMetaData().setItemMeta(itemMeta);
            if (gui != null)
                gui.builder.build();
        } else {
            services.debug().log("Troll is not toggled");
            ItemMeta itemMeta = trollMetaData.getItemMeta();
            services.debug().logObject(itemMeta);
            itemMeta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.removeEnchant(XEnchantment.DURABILITY.getEnchant());
            getTrollMetaData().setItemMeta(itemMeta);
            if (gui != null)
                gui.builder.build();
        }
        return (T) this;
    }

    public TrollSelectionInventory getCallingGUI() {
        return callingGUI != null ? callingGUI.get() : null;
    }

    public abstract void execute();
}
