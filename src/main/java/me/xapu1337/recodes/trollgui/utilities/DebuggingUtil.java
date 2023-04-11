package me.xapu1337.recodes.trollgui.utilities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

public class DebuggingUtil {

    private static final SingletonBase<DebuggingUtil> instance = new SingletonBase<>(DebuggingUtil.class);
    private final boolean debuggingEnabled = true;

    public DebuggingUtil() {}

    public static DebuggingUtil getInstance() {
        return instance.get();
    }

    public void log(Level level, String message, int depth) {
        if (debuggingEnabled) {
            String className = getCallerClassName(depth);
            String CLASS_NAME_COLOR = "&l&#0079B3";
            String logMessage = String.format("%s%s&7: %s",
                    CLASS_NAME_COLOR, className, message);
            String formattedMessage = formatLogMessage(level, logMessage);
            Bukkit.getConsoleSender().sendMessage(ConfigUtils.getInstance().$(formattedMessage));
        }
    }

    public void log(String message) {
        log(Level.INFO, message, 2);
    }

    public void log(String message, Object... args) {
        log(Level.INFO, String.format(message, args), 2);
    }

    public void l(String message) {
        log(Level.INFO, message, 2);
    }

    public void l(String message, Object... args) {
        log(Level.INFO, String.format(message, args), 2);
    }

    public void send(CommandSender sender, String message) {
        if (debuggingEnabled && sender != null && message != null) {
            String DEBUG_PREFIX = "&8[&bDebug&8] &r";
            String logMessage = String.format("%s%s", DEBUG_PREFIX, message);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', logMessage));
        }
    }

    public void warn(String message) {
        String formattedMessage = formatLogMessage(WARNING, message);
        Bukkit.getConsoleSender().sendMessage(formattedMessage);
    }

    public void error(String message) {
        String formattedMessage = formatLogMessage(SEVERE, message);
        Bukkit.getConsoleSender().sendMessage(formattedMessage);
    }

    private String formatLogMessage(Level level, String message) {
        String prefix;
        ChatColor levelColor;

        if (Objects.equals(level, WARNING)) {
            levelColor = ChatColor.YELLOW;
            prefix = "&8[&eWarning &8| " + levelColor + level.getName() + "&8] &r";
        } else if (Objects.equals(level, SEVERE)) {
            levelColor = ChatColor.RED;
            prefix = "&8[&cError &8| " + levelColor + level.getName() + "&8] &r";
        } else {
            levelColor = ChatColor.WHITE;
            prefix = "&8[&bDebug &8| " + levelColor + level.getName() + "&8] &r";
        }

        String formattedPrefix = String.format("%s%s%s: ",
                ChatColor.DARK_GRAY, ChatColor.BOLD, prefix);
        return String.format("%s%s", formattedPrefix, message);
    }

    private String getStackTraceAsString(Throwable throwable) {
        if (throwable == null) {
            return "";
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
    private String getCallerClassName(int stackTraceDepth) {
        String fullClassName = Thread.currentThread().getStackTrace()[3 + stackTraceDepth].getClassName();
        return fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
    }

    public void error(String message, Throwable throwable, Map<String, Object> data) {
        String formattedMessage = formatLogMessage(SEVERE, message);
        Bukkit.getConsoleSender().sendMessage(ConfigUtils.getInstance().$(formattedMessage));

        if (throwable != null) {
            String stackTrace = getStackTraceAsString(throwable);
            String stackTraceMessage = ConfigUtils.getInstance().$("&c&lStackTrace: &r\n" + stackTrace);
            Bukkit.getConsoleSender().sendMessage(stackTraceMessage);
        }

        if (data != null && !data.isEmpty()) {
            Bukkit.getConsoleSender().sendMessage(ConfigUtils.getInstance().$("&c&lData:"));
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                Object value = entry.getValue();
                String valueString = value != null ? value.toString() : "null";
                String entryMessage = String.format("%s: %s (%s)", entry.getKey(), valueString, value != null ? value.getClass().getSimpleName() : "null");
                Bukkit.getConsoleSender().sendMessage(ConfigUtils.getInstance().$("┆ " + entryMessage));
            }
            Bukkit.getConsoleSender().sendMessage("╰");
        }
    }



}