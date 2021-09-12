package me.xapu1337.recodes.trollgui.Utilities;

import java.util.HashMap;

public class Singleton {
    public Singleton instance;
    public final HashMap<String, String> clearedPlayerInventories = new HashMap<String, String>();
    public final HashMap<String, String> frozenPlayers = new HashMap<String, String>();
    public final HashMap<String, String> noBuildPlayers = new HashMap<String, String>();
    public final HashMap<String, String> noBreakPlayers = new HashMap<String, String>();
    public final HashMap<String, String> reverseMessagePlayers = new HashMap<String, String>();

    public Singleton(){
        if(instance == null)
            instance = this;
    }
}
