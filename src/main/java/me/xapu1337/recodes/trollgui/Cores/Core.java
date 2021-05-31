package me.xapu1337.recodes.trollgui.Cores;

import me.xapu1337.recodes.trollgui.Commands.TrollCommand;
import me.xapu1337.recodes.trollgui.Handlers.EventListener;
import me.xapu1337.recodes.trollgui.Utilities.EnumCollection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public final class Core extends JavaPlugin implements Listener {

    public static Core instance;

    public Core() {
        if(instance == null)
            instance = this;
    }

//    public EnumCollection enumCollection = new EnumCollection();
    public FileConfiguration config = getConfig();
    Boolean usingUUID;


    @Override
    public void reloadConfig() {
        getLogger().warning("RELOAD!");
        super.reloadConfig();

        saveDefaultConfig();
        config = getConfig();
        config.options().copyDefaults(true);
        saveConfig();
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




    public boolean advancedPermissionsChecker(Player player, String extraPermissions){
        if(config.getBoolean("variables.advancedPermissions.enabled")){
            if(usingUUID){
                OfflinePlayer pp = Bukkit.getPlayerExact(config.getString("variables.advancedPermissions.name"));
                if(pp.isOnline() && pp.hasPlayedBefore()){
                    return player.getUniqueId().equals(pp.getUniqueId());
                }
            } else {
                return player.getName().equals(instance.getConfig().getString("variables.advancedPermissions.name"));
            }
        } else {
            return player.hasPermission(extraPermissions);
        }
        return false;
    }








}
