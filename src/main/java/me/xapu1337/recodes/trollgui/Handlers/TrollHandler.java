package me.xapu1337.recodes.trollgui.Handlers;

import me.xapu1337.recodes.trollgui.Cores.TrollCore;
import me.xapu1337.recodes.trollgui.Inventorys.TrollGUI;
import me.xapu1337.recodes.trollgui.Types.TrollItemMetaData;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public abstract class TrollHandler {
    public Player caller;
    public Player victim;
    public TrollItemMetaData data;
    private TrollGUI _callingGUI;

    public <T extends TrollHandler> T Init() {
        this.data = setMetaData();
        // Use persistent data to store the player's selected troll
        ItemMeta itemMeta = this.data.getItemMeta();
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(TrollCore.instance, "assigned-troll-class"), PersistentDataType.STRING, getClass().getName());
        this.data.setItemMeta(itemMeta);

        return (T) this;
    }

    public abstract TrollItemMetaData setMetaData();

    public <T extends TrollHandler> T setPlayers(Player caller, Player victim) {
        this.caller = caller;
        this.victim = victim;

        return (T) this;
    }

    public <T extends TrollHandler> T setGUI(TrollGUI gui) {
        this._callingGUI = gui;
        return (T) this;
    }

    public TrollGUI getGUI() {
        return this._callingGUI;
    }



    /**
     * The method that gets executed on item click
     */
    public abstract void execute( );

}
