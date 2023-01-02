//package me.xapu1337.recodes.trollgui.Handlers;
//import me.xapu1337.recodes.trollgui.Utilities.Singleton;
//import me.xapu1337.recodes.trollgui.Utilities.Utilities;
//import org.bukkit.Bukkit;
//import org.bukkit.entity.Player;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class TemplateHandler {
//
//    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{(.+?)}");
//
//    private static TemplateHandler instance;
//    private final Map<String, Object> values;
//
//    private TemplateHandler() {
//        this.values = new HashMap<>();
//    }
//
//    public static TemplateHandler getInstance() {
//        if (instance == null) {
//            instance = new TemplateHandler();
//        }
//        return instance;
//    }
//
//    public void addCustomValue(String key, Object value) {
//        if (key.startsWith("config:")) {
//            // If the key starts with "config:", treat it as a config path and get the value from the config
//            String configPath = key.substring("config:".length());
//            value = Utilities.getSingleInstance().getConfigPath(configPath);
//        }
//        this.values.put(key, value);
//    }
//
//    public void changeCustomValue(String key, Object newValue) {
//
//        if (key.startsWith("config:")) {
//            // If the key starts with "config:", treat it as a config path and get the value from the config
//            String configPath = key.substring("config:".length());
//            newValue = Utilities.getSingleInstance().getConfigPath(configPath);
//        }
//        this.values.replace(key, newValue);
//    }
//
//    public String parseString(String input) {
//        StringBuilder output = new StringBuilder();
//        Matcher matcher = PLACEHOLDER_PATTERN.matcher(input);
//        while (matcher.find()) {
//            String key = matcher.group(1);
//            Object value = this.values.getOrDefault(key, "");
//            matcher.appendReplacement(output, value.toString());
//        }
//        matcher.appendTail(output);
//        return output.toString();
//    }
//
//
//    public String $(String input) {
//        return parseString(input);
//    }
//    /**
//     * Dumps all fields and their values from the given object into the values map.
//     *
//     * @param object the object to dump
//     */
//    public void dumpObject(Object object) {
//        for (Field field : object.getClass().getDeclaredFields()) {
//            field.setAccessible(true);
//            try {
//                this.values.put(field.getName(), field.get(object));
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * Dumps the value of a given method from the given object into the values map.
//     *
//     * @param object the object to dump the method from
//     * @param methodName the name of the method to dump
//     */
//    public void dumpMethod(Object object, String methodName) {
//        try {
//            Method method = object.getClass().getDeclaredMethod(methodName);
//            method.setAccessible(true);
//            this.values.put(methodName, method.invoke(object));
//        } catch (ReflectiveOperationException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void dumpMethod(Player player, String methodName) {
//        try {
//            // Cast the player to the CraftPlayer type
//            Object craftPlayer = player.getClass().getMethod("getHandle").invoke(player);
//
//            // Get the method from the CraftPlayer class and invoke it
//            Method method = craftPlayer.getClass().getDeclaredMethod(methodName);
//            method.setAccessible(true);
//            this.values.put(methodName, method.invoke(craftPlayer));
//        } catch (ReflectiveOperationException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void printValues() {
//        // Find the longest key in the values map
//        int maxKeyLength = this.values.keySet().stream().mapToInt(String::length).max().orElse(0);
//
//        // Print out the key-value pairs in a table-like format
//        this.values.forEach((key, value) -> {
//            String paddedKey = String.format("%1$-" + maxKeyLength + "s", key);
//            Bukkit.getLogger().info(paddedKey + ": " + value);
//        });
//    }
//
//}


package me.xapu1337.recodes.trollgui.Handlers;

import me.xapu1337.recodes.trollgui.Utilities.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class for handling template strings with placeholders. Placeholders are defined using the syntax `${key}`,
 * where `key` is the name of the value to replace the placeholder with.
 */
public class TemplateHandler {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{(.+?)}");

    private static TemplateHandler instance;
    private final Map<String, Object> values;

    public TemplateHandler() {
        this.values = new HashMap<>();
    }


    public static TemplateHandler getInstance() {
        if (instance == null) {
            instance = new TemplateHandler();
        }
        return instance;
    }


    public void addCustomValue(String key, Object value) {
        if (value instanceof String && ((String) value).startsWith("config:")) {
            // If the key starts with "config:", treat it as a config path and get the value from the config
            String configPath = ((String) value).substring("config:".length());
            value = Utilities.getSingleInstance().getConfigPath(configPath);
        }
        this.values.put(key, value);
    }

    public void changeCustomValue(String key, Object newValue) {
        if (newValue instanceof String && ((String) newValue).startsWith("config:")) {
            // If the key starts with "config:", treat it as a config path and get the value from the config
            String configPath = ((String) newValue).substring("config:".length());
            newValue = Utilities.getSingleInstance().getConfigPath(configPath);
        }
        this.values.replace(key, newValue);
    }

    /**
     * Parses the given input string and replaces all placeholders with their corresponding values from the values map.
     *
     * @param input the input string to parse
     * @return the input string with all placeholders replaced with their corresponding values
     */
    public String parseString(String input) {
        StringBuilder output = new StringBuilder();
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(input);
        while (matcher.find()) {
            String key = matcher.group(1);
            Object value = null;
            if (key.startsWith("config:")) {
                // If the key starts with "config:", treat it as a config path and get the value from the config
                String configPath = key.substring("config:".length());
                value = Utilities.getSingleInstance().getConfigPath(configPath);
            } else {
                // Otherwise, get the value from the values map
                value = this.values.getOrDefault(key, "");
            }
            matcher.appendReplacement(output, value.toString());
        }
        matcher.appendTail(output);
        return ChatColor.translateAlternateColorCodes('&',  output.toString());
    }

    /**
     * Shortcut for calling {@link #parseString(String)}.
     *
     * @param input the input string to parse
     * @return the input string with all placeholders replaced with their corresponding values
     */
    public String $(String input) {
        return parseString(input);
    }

    /**
     * Dumps the value of a given method from the given object into the values map.
     *
     * @param object the object to dump the method from
     * @param methodName the name of the method to dump
     * @throws ReflectiveOperationException if an error occurs while trying to access the method or invoke it
     */
    public void dumpMethod(Object object, String methodName) throws ReflectiveOperationException {
        Method method = object.getClass().getDeclaredMethod(methodName);
        method.setAccessible(true);
        this.values.put(methodName, method.invoke(object));
    }

    /**
     * Prints all the values in the values map to the console in a table-like format.
     */
    public void printValues() {
        // Find the longest key in the values map
        int maxKeyLength = this.values.keySet().stream().mapToInt(String::length).max().orElse(0);

        // Print out the key-value pairs in a table-like format
        this.values.forEach((key, value) -> {
            String paddedKey = String.format("%1$-" + maxKeyLength + "s", key);
            Bukkit.getLogger().info(paddedKey + ": " + value);
        });
    }

}