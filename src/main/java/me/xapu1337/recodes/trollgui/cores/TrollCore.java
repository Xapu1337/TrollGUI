package me.xapu1337.recodes.trollgui.cores;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIConfig;
import me.xapu1337.recodes.trollgui.commands.TrollCommand;
import me.xapu1337.recodes.trollgui.loaders.TrollLoader;
import me.xapu1337.recodes.trollgui.utilities.DebuggingUtil;
import me.xapu1337.recodes.trollgui.utilities.SingletonBase;
import me.xapu1337.recodes.trollgui.utilities.TempPool;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class TrollCore extends JavaPlugin implements Listener {

    public static TrollCore instance;

    public TrollCore() {
        if (instance == null)
            instance = this;
    }

    public static TrollCore getInstance() {
        return instance;
    }

    public static Plugin getPlugin() {
        return getInstance();
    }
    public final DebuggingUtil debuggingUtil = new DebuggingUtil(true);

    @Override
    public void onEnable() {
        super.onEnable();

        TrollLoader.getInstance().refreshTrolls();

        CommandAPI.onEnable(this);

        new TrollCommand();
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
