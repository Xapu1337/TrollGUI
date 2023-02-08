package me.xapu1337.recodes.trollgui.cores;

import me.xapu1337.recodes.trollgui.utilities.SingletonBase;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class TrollCore extends JavaPlugin implements Listener {

    public static final SingletonBase<TrollCore> instance = new SingletonBase<>(TrollCore.class);

    public static TrollCore getInstance() {
        return instance.get();
    }


}
