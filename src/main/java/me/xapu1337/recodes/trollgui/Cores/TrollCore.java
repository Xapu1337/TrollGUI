package me.xapu1337.recodes.trollgui.Cores;


import me.xapu1337.recodes.trollgui.Commands.TrollCommand;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Listeners.EventListener;
import me.xapu1337.recodes.trollgui.Utilities.Singleton;
import me.xapu1337.recodes.trollgui.Utilities.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;


public class TrollCore extends JavaPlugin implements Listener {

    public FileConfiguration config = getConfig();
    public static TrollCore instance;
    public boolean usingUUID;
    public TrollCore() {
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
        getLogger().warning("§bChecking updates...");

        new UpdateChecker(TrollCore.instance, 78194).getVersion(version -> {
            getLogger().warning(Float.parseFloat(TrollCore.instance.getDescription().getVersion()) >= Float.parseFloat(version) ?
                      "§7Latest version: §a§l" + version + "§7, Current version: §a§l"+ TrollCore.instance.getDescription().getVersion() :
                      "§7An update is §aAVAILABLE §7For the version: §a§l" + version + "§7, Your version: §c§l" + TrollCore.instance.getDescription().getVersion()
                    + "§7. §7Update it from here: https://www.spigotmc.org/resources/troll-plugin-gui-anything-is-configurable.78194/");
        });


        // Register Listeners
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getServer().getPluginManager().registerEvents(new EventListener(), this);

        super.onEnable();
        reloadConfig();


        usingUUID = !(Integer.parseInt(Bukkit.getServer().getVersion().split("MC: ")[1].replaceAll("\\)", "").trim().split("\\.")[1]) < 7 || !Bukkit.getServer().getOnlineMode());



        new TrollCommand(this);

        // Run reflection to get all the loaded troll handlers
        for (Class<?> clazz : Singleton.getSingleInstance().trollHandlerClasses.loadClasses().stream().sorted(Comparator.comparing(Class::getSimpleName)).toList()) {
            try {
                if(!Singleton.getSingleInstance().loadedTrollHandlers.containsKey(clazz.getName())) {
                    Singleton.getSingleInstance().loadedTrollHandlers.put(clazz.getName(), (TrollHandler) clazz.getConstructor().newInstance());
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void onDisable() {
        super.onDisable();
        Singleton.getSingleInstance().loadedTrollHandlers.values().forEach(TrollHandler::onServerDisable);
        Singleton.getSingleInstance().currentPlayersTrolling.clear();

    }




}
