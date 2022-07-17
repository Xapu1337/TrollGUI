package me.xapu1337.recodes.trollgui.Utilities;

import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Inventorys.TrollGUI;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Singleton {
    public Singleton instance;
    public final HashMap<String, String> clearedPlayerInventories = new HashMap<String, String>();
    public final HashMap<String, String> frozenPlayers = new HashMap<String, String>();
    public final HashMap<String, String> noBuildPlayers = new HashMap<String, String>();
    public final HashMap<String, String> noBreakPlayers = new HashMap<String, String>();

    public final HashMap<String, String> noDropPlayers = new HashMap<String, String>();
    public final HashMap<String, String> reverseMessagePlayers = new HashMap<String, String>();

    // Simple map holding owners of currently open inventories.
    public final HashMap<Player, TrollGUI> currentPlayersTrolling = new HashMap<Player, TrollGUI>();

    public final IndexableMap<String, TrollHandler> holdingTrolls = new IndexableMap<String, TrollHandler>();

    public Singleton(){
        if(instance == null)
            instance = this;
    }
}
