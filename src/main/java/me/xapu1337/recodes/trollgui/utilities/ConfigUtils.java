package me.xapu1337.recodes.trollgui.utilities;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigUtils {

    private final JavaPlugin plugin;
    private final FileConfiguration config;
    private final Map<String, String> placeholders;

    public ConfigUtils(JavaPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.placeholders = new HashMap<>();
    }


    public ConfigUtils setPlaceholder(String key, String value) {
        this.placeholders.put(key, value);
        return this;
    }

    public ConfigUtils setPlaceholders(Map<String, String> placeholders) {
        this.placeholders.putAll(placeholders);
        return this;
    }

    public String getMessage(String path) {
        String message = config.getString(path, "");

        // Replace placeholders
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            message = message.replace("{" + entry.getKey() + "}", entry.getValue());
        }

        // Translate color codes
        message = ChatColor.translateAlternateColorCodes('&', message);

        return message;
    }

    public String $(String message) {
        return translateMessage(message);
    }

    public String translateMessage(String message) {
        // Translate color codes
        message = hexColor(message);
        message = ChatColor.translateAlternateColorCodes('&', message);

        // Replace placeholders
        Pattern placeholderPattern = Pattern.compile("\\{([A-Za-z0-9_-]+)\\}");
        Matcher matcher = placeholderPattern.matcher(message);
        while (matcher.find()) {
            String placeholder = matcher.group(1);
            String value = placeholders.getOrDefault(placeholder, "");
            message = message.replace("{" + placeholder + "}", value);
        }

        // Replace config values
        Pattern configPattern = Pattern.compile("config:([A-Za-z0-9._-]+)");
        matcher = configPattern.matcher(message);
        while (matcher.find()) {
            String path = matcher.group(1);
            String value = config.getString(path, "");
            message = message.replace("config:" + path, value);
        }

        // Replace temp pool values
        Pattern tempPattern = Pattern.compile("\\$([A-Za-z0-9_-]+)");
        matcher = tempPattern.matcher(message);
        while (matcher.find()) {
            String key = matcher.group(1);
            UUID uuid = UUID.fromString(key);
            String value = TempPool.getInstance().getMessage(uuid);
            message = message.replace("$" + key, value);
        }

        return message;
    }

    static String hexColor(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder("");
            for (char c : ch) {
                builder.append("&" + c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return message;
    }

}
