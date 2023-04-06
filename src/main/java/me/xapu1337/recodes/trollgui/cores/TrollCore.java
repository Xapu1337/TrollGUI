package me.xapu1337.recodes.trollgui.cores;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIConfig;
import me.xapu1337.recodes.trollgui.utilities.SingletonBase;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class TrollCore extends JavaPlugin implements Listener {

    public static final SingletonBase<TrollCore> instance = new SingletonBase<>(TrollCore.class);

    public static TrollCore getInstance() {
        return instance.get();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        CommandAPI.onEnable(this);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        CommandAPI.onDisable();
    }

    @Override
    public void onLoad() {
        super.onLoad();

        CommandAPI.onLoad(new CommandAPIConfig().silentLogs(true));
    }
}
