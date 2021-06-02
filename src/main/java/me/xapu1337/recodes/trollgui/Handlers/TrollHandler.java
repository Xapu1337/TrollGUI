package me.xapu1337.recodes.trollgui.Handlers;

import org.bukkit.entity.Player;

public abstract class TrollHandler {
    public Player caller;
    public Player victim;

    public TrollHandler(Player caller, Player victim) {
        this.caller = caller;
        this.victim = victim;
    }

    /**
     * Executed from the TrollGUI Class everything inside this function gets executed.
     */
    public abstract void execute();

}
