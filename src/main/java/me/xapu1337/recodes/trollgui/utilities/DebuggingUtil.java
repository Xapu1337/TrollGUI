package me.xapu1337.recodes.trollgui.utilities;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class DebuggingUtil {

    private final boolean debuggingEnabled;

    public DebuggingUtil(boolean debuggingEnabled) {
        this.debuggingEnabled = debuggingEnabled;
    }

    public void log(Level level, String message) {
        if (debuggingEnabled) {
            Bukkit.getLogger().log(level, message);
        }
    }

    public void log(String message) {
        log(Level.INFO, message);
    }

    public void send(CommandSender sender, String message) {
        if (debuggingEnabled && sender != null && message != null) {
            sender.sendMessage(ChatColor.GRAY + "[Debug] " + ChatColor.WHITE + message);
        }
    }

    public void send(Player player, String message) {
        send((CommandSender) player, message);
    }

    public void error(String message, Throwable throwable) {
        if (debuggingEnabled) {
            String stackTrace = getStackTraceAsString(throwable);
            log(Level.SEVERE, message + "\n" + stackTrace);
            displayError(message, throwable);
        }
    }

    public void error(String message) {
        if (debuggingEnabled) {
            log(Level.SEVERE, message);
            displayError(message, null);
        }
    }

    private void displayError(String message, Throwable throwable) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("plugin.debug")) {
                player.sendMessage(ChatColor.RED + "[Error] " + ChatColor.WHITE + message);
                if (throwable != null) {
                    send(player, getStackTraceAsString(throwable));
                }
            }
        }
    }

    private String getStackTraceAsString(Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(throwable.toString()).append("\n");
        for (StackTraceElement stackTraceElement : throwable.getStackTrace()) {
            stringBuilder.append("\t").append(stackTraceElement.toString()).append("\n");
        }
        return stringBuilder.toString();
    }

}
