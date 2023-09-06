package me.xapu1337.recodes.trollgui.utilities;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.xapu1337.recodes.trollgui.cores.TrollCore;
import net.md_5.bungee.api.ChatColor;

public class MessageUtils {
    private static final SingletonBase<MessageUtils> INSTANCE = new SingletonBase<>(MessageUtils.class);
    private final Map<String, String> placeholders = new ConcurrentHashMap<>();
    private final Map<Class<?>, Map<String, String>> classPlaceholders = new ConcurrentHashMap<>();
    private final DynamicCache<Object> cache = new DynamicCache<>();
    private final Map<String, String> messageCache = new ConcurrentHashMap<>();
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{([A-Za-z0-9_-]+)}");
    private static final Pattern CONFIG_PATTERN = Pattern.compile("config:([A-Za-z0-9._-]+)");
    private static final Pattern TEMP_PATTERN = Pattern.compile("VOID=([a-fA-F0-9]{8}(-[a-fA-F0-9]{4}){4}[a-fA-F0-9]{8})");
    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("&#([a-fA-F0-9]{6})");

    private MessageUtils() {
    }

    public static MessageUtils getInstance() {
        return INSTANCE.get();
    }

    public MessageUtils setPlaceholder(String key, String value) {
        placeholders.put(key, value);
        return this;
    }

    public MessageUtils setPlaceholders(Map<String, String> placeholders) {
        this.placeholders.putAll(placeholders);
        return this;
    }

    public MessageUtils setClassPlaceholders(Class<?> clazz, Map<String, String> placeholders) {
        classPlaceholders.put(clazz, new HashMap<>(placeholders));
        return this;
    }

    public MessageUtils setClassPlaceholders(Class<?> clazz, String key, String value) {
        classPlaceholders.put(clazz, Map.of(key, value));
        return this;
    }

    public String getMessage(String path) {
        return messageCache.computeIfAbsent(path, p -> {
            String message = Optional.ofNullable(TrollCore.getInstance().getConfig().getString(p))
                    .orElse("< - Error: Config value not found - >");
            message = translateMessage(message);
            Map<String, String> combinedPlaceholders = new HashMap<>(placeholders);
            Map<String, String> classPlaceholders = this.classPlaceholders.getOrDefault(TrollCore.getInstance().getClass(), Map.of());
            combinedPlaceholders.putAll(classPlaceholders);
            Matcher matcher = PLACEHOLDER_PATTERN.matcher(message);
            while (matcher.find()) {
                String placeholderKey = matcher.group(1);
                String placeholderValue = combinedPlaceholders.getOrDefault(placeholderKey, "");
                message = message.replace(matcher.group(), placeholderValue);
            }
            message = hexColor(message);
            message = ChatColor.translateAlternateColorCodes('&', message);
            return message.replace("{", "").replace("}", "");
        });
    }


    public String $(String message) {
        return translateMessage(message);
    }

    public String translateMessage(String message) {
        Matcher matcher = CONFIG_PATTERN.matcher(message);
        while (matcher.find()) {
            String configPath = matcher.group(1);
            message = message.replace(matcher.group(), Optional.ofNullable(TrollCore.getInstance().getConfig().getString(configPath))
                    .orElse("< - Error: Config value not found - >"));
        }
        matcher = TEMP_PATTERN.matcher(message);
        while (matcher.find()) {
            String uuid = matcher.group(1);
            message = message.replace(matcher.group(), cache.getOrElse(UUID.fromString(uuid), () ->
                    "< - Error: Placeholder value not found - >").toString());
        }
        message  = getMessage(message);
        return message;
    }

    public String hexColor(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder();
            for (char c : ch) {
                builder.append("&").append(c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return message;
    }
    public void cache(Object object) {
        cache.set(UUID.randomUUID(), object);
    }

    public void clearCache() {
        cache.clear();
    }

    public void clearCache(UUID uuid) {
        cache.remove(uuid);
    }

    public DynamicCache<Object> getCache() {
        return cache;
    }
}

