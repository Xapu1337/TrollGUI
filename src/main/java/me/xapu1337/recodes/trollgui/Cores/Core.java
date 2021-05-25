package me.xapu1337.recodes.trollgui.Cores;

import me.xapu1337.recodes.trollgui.Commands.TrollCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Random;

public final class Core extends JavaPlugin {

    public static Core instance;
    public FileConfiguration config = getConfig();
    Boolean usingUUID;

    public String getStringedServerVersion() {
        return Bukkit.getServer().getVersion().split("MC: ")[1].replaceAll("\\)", "").trim();
    }


    @Override
    public void onEnable() {

        reloadConfig();
        if (Integer.parseInt(stringCollection.VERSION.get()) < 7) {
            usingUUID = false;
            this.getServer().getLogger().info("using UUID disable because server is version " + getStringedServerVersion());
        }

        try {

            Field field = null;
            field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap commandMap = (CommandMap) field.get(Bukkit.getServer());

            new TrollCommand(commandMap, this);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            getLogger().severe(e.getLocalizedMessage());
            e.printStackTrace();
            this.getPluginLoader().disablePlugin(this);
        }

    }

    @Override
    public void onDisable() {

    }


    public String translateColorCodes(String input){
        return ChatColor.translateAlternateColorCodes('&', input);
    }


    public enum stringCollection{
        PREFIX(instance.translateColorCodes("global.prefix")),
        VERSION(Bukkit.getServer().getVersion().split("MC: ")[1].replaceAll("\\)", "").trim());

        String string;

        public String get() {
            return this.string;
        }

        public void set(String string) {
            this.string = string;
        }

        stringCollection(String inp){
            this.string = inp;
        }
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


    @Override
    public void reloadConfig() { // Found somewhere on the internet, its fucking useful because well idk its good.
        super.reloadConfig();
        saveDefaultConfig();
        config = getConfig();
        config.options().copyDefaults(true);
    }

    public Core() {
        if(instance == null)
            instance = this;
    }
}
