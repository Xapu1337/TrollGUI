package me.xapu1337.recodes.trollgui.Cores;

import me.xapu1337.recodes.trollgui.Commands.TrollCommand;
import me.xapu1337.recodes.trollgui.Listeners.EventListener;
import me.xapu1337.recodes.trollgui.Utilities.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public class Core extends JavaPlugin implements Listener {

    public FileConfiguration config = getConfig();
    public static Core instance;
    public Boolean usingUUID;
    public Util utils = new Util();

    public Core() {
        if(instance == null)
            instance = this;
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        saveDefaultConfig();
        config = getConfig();
        config.options().copyDefaults(true);
    }

    @Override
    public void onEnable() {

        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getServer().getPluginManager().registerEvents(new EventListener(), this);
        super.onEnable();
        reloadConfig();

        if (Integer.parseInt(Bukkit.getServer().getVersion().split("MC: ")[1].replaceAll("\\)", "").trim().split("\\.")[1]) < 7 || !Bukkit.getServer().getOnlineMode())
            usingUUID = false;

        try {

            Field f = null;
            f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            CommandMap commandMap = (CommandMap) f.get(Bukkit.getServer());

            new TrollCommand(commandMap, this);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            getLogger().severe(e.getLocalizedMessage());
            e.printStackTrace();
            this.getPluginLoader().disablePlugin(this);
        }

    }

    @Override
    public void onDisable() {
        super.onDisable();
    }


}
