package me.xapu1337.recodes.trollgui.Handlers;

import me.xapu1337.recodes.trollgui.Cores.Core;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public abstract class TrollHandler {
    public Player caller;
    public Player victim;
    public TrollItemMetaData metaData;

    public <T extends TrollHandler> T Init() {
        Core.instance.singletons.holdingTrolls.put(this.getClass().getName(), this);
        this.metaData = setMetaData();

        return (T) this;
    }

    public abstract TrollItemMetaData setMetaData();

    public <T extends TrollHandler> T setPlayers(Player caller, Player victim) {
        this.caller = caller;
        this.victim = victim;

        return (T) this;
    }


    /**
     * The method that gets executed on item click
     */
    public abstract void execute();

}
