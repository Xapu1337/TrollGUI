package me.xapu1337.recodes.trollgui.utilities;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.xapu1337.recodes.trollgui.cores.TrollCore;
import net.md_5.bungee.api.ChatColor;

public class MessageUtils {
    private final Map<String, String> placeholders = new ConcurrentHashMap<>();
    private final Map<Class<?>, Map<String, String>> classPlaceholders = new ConcurrentHashMap<>();
    private final Map<String, String> messageCache = new ConcurrentHashMap<>();
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{([A-Za-z0-9_-]+)}");
    private static final Pattern CONFIG_PATTERN = Pattern.compile("\\$?\\{config:([A-Za-z0-9._-]+)\\}");
    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("#[a-fA-F0-9]{6}");

    public MessageUtils() {
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
        classPlaceholders.computeIfAbsent(clazz, k -> new HashMap<>()).put(key, value);
        return this;
    }

    public String getMessage(String path) {
        // Cache only the static config lookup; dynamic placeholders applied fresh each call
        String template = messageCache.computeIfAbsent(path, p ->
                translateMessage(Optional.ofNullable(TrollCore.getInstance().getConfig().getString(p))
                        .orElse("< - Error: Config value not found - >")));

        Map<String, String> combined = new HashMap<>(placeholders);
        classPlaceholders.forEach((k, v) -> combined.putAll(v));
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(template);
        String message = template;
        while (matcher.find()) {
            message = message.replace(matcher.group(), combined.getOrDefault(matcher.group(1), ""));
        }
        message = hexColor(message);
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public void clearMessageCache() {
        messageCache.clear();
    }

    public String $(String message) {
        message = translateMessage(message);
        message = hexColor(message);
        message = ChatColor.translateAlternateColorCodes('&', message);
        return message;
    }

    public String translateMessage(String message) {
        Matcher matcher = CONFIG_PATTERN.matcher(message);
        while (matcher.find()) {
            String configPath = matcher.group(1);
            message = message.replace(matcher.group(),
                    Optional.ofNullable(TrollCore.getInstance().getConfig().getString(configPath))
                            .orElse("< - Error: Config value not found - >"));
        }
        return message;
    }

    public String hexColor(String message) {
        Matcher matcher = HEX_COLOR_PATTERN.matcher(message);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            StringBuilder replacement = new StringBuilder("&x");
            for (char c : matcher.group().substring(1).toCharArray()) {
                replacement.append('&').append(c);
            }
            matcher.appendReplacement(sb, replacement.toString());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
