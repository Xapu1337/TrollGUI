package me.xapu1337.recodes.trollgui.utilities;

import me.xapu1337.recodes.trollgui.cores.TrollCore;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigUtils {

    private static final SingletonBase<ConfigUtils> instance = new SingletonBase<>(ConfigUtils.class);
    private final Map<String, String> placeholders;
    private final Map<Class<?>, Map<String, String>> classPlaceholders;
    private final DynamicCache<Object> cache = new DynamicCache<>();
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{([A-Za-z0-9_-]+)}");
    private static final Pattern CONFIG_PATTERN = Pattern.compile("config:([A-Za-z0-9._-]+)");
    private static final Pattern TEMP_PATTERN = Pattern.compile("VOID=([a-fA-F0-9]{8}(-[a-fA-F0-9]{4}){4}[a-fA-F0-9]{8})");
    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("&#([a-fA-F0-9]{6})");

    private final Map<String, String> messageCache;

    public ConfigUtils() {
        this.placeholders = new ConcurrentHashMap<>();
        this.classPlaceholders = new ConcurrentHashMap<>();
        this.messageCache = new ConcurrentHashMap<>();
    }

    public static ConfigUtils getInstance() {
        return instance.get();
    }

    public ConfigUtils setPlaceholder(String key, String value) {
        this.placeholders.put(key, value);
        return this;
    }

    public ConfigUtils setPlaceholders(Map<String, String> placeholders) {
        this.placeholders.putAll(placeholders);
        return this;
    }

    public ConfigUtils setClassPlaceholders(Class<?> clazz, Map<String, String> placeholders) {
        this.classPlaceholders.put(clazz, placeholders);
        return this;
    }

    public ConfigUtils setClassPlaceholders(Class<?> clazz, String key, String value) {
        this.classPlaceholders.put(clazz, new HashMap<String, String>() {{
            put(key, value);
        }});
        return this;
    }

    public String getMessage(String path) {
        if (messageCache.containsKey(path)) {
            return messageCache.get(path);
        }

        String message = TrollCore.getInstance().getConfig().getString(path);

        if (message == null) {
            return "< - Error: Config value not found - >";
        }

        // Replace placeholders in config value
        message = translateMessage(message);

        // Replace placeholders
        Map<String, String> combinedPlaceholders = new HashMap<>(placeholders);
        Map<String, String> classPlaceholders = this.classPlaceholders.getOrDefault(TrollCore.getInstance().getClass(), new HashMap<>());
        combinedPlaceholders.putAll(classPlaceholders);

        for (Map.Entry<String, String> entry : combinedPlaceholders.entrySet()) {
            message = message.replace("{" + entry.getKey() + "}", entry.getValue());
        }

        // Translate color codes
        message = hexColor(message);
        message = ChatColor.translateAlternateColorCodes('&', message);

        messageCache.put(path, message);
        return message;
    }

    public String $(String message) {
        return translateMessage(message);
    }

    public String translateMessage(String message) {
        String hash = String.valueOf(HashingUtil.murmurhash3(message.getBytes(), 0));
        if (messageCache.containsKey(hash)) return messageCache.get(hash);

        message = hexColor(message);
        message = ChatColor.translateAlternateColorCodes('&', message);

        // Replace placeholders
        Map<String, String> combinedPlaceholders = new HashMap<>(placeholders);
        Map<String, String> classPlaceholders = this.classPlaceholders.getOrDefault(TrollCore.getInstance().getClass(), new HashMap<>());
        combinedPlaceholders.putAll(classPlaceholders);

        Matcher matcher = PLACEHOLDER_PATTERN.matcher(message);
        while (matcher.find()) {
            String placeholder = matcher.group(1);
            String value = combinedPlaceholders.getOrDefault(placeholder, "");
            message = message.replace("{" + placeholder + "}", value);
        }

        matcher = CONFIG_PATTERN.matcher(message);
        while (matcher.find()) {
            String path = matcher.group(1);
            String value = getMessage(path);
            if (value == null) {
                value = "< - Error: Config value not found - >";
            }
            message = message.replace("{" + matcher.group() + "}", value);
        }

        matcher = TEMP_PATTERN.matcher(message);
        while (matcher.find()) {
            String key = matcher.group(1);
            UUID uuid = UUID.fromString(key);
            String value = (String) cache.get(uuid);
            cache.remove(uuid);
            message = message.replace("{" + matcher.group() + "}", value);
        }

        messageCache.put(hash, message);
        return message;
    }

    static String hexColor(String message) {
        Pattern pattern = Pattern.compile("&#([a-fA-F0-9]{6})");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = matcher.group(1);

            StringBuilder builder = new StringBuilder("§x");
            for (char c : hexCode.toCharArray()) {
                builder.append("§").append(c);
            }

            message = message.replace("&#" + hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return message;
    }


}
