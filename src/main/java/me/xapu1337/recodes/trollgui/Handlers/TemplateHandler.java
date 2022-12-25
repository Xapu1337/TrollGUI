package me.xapu1337.recodes.trollgui.Handlers;
import me.xapu1337.recodes.trollgui.Utilities.Singleton;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateHandler {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{(.+?)}");

    private static TemplateHandler instance;
    private final Map<String, Object> values;

    private TemplateHandler() {
        this.values = new HashMap<>();
    }

    public static TemplateHandler getInstance() {
        if (instance == null) {
            instance = new TemplateHandler();
        }
        return instance;
    }

    public void addCustomValue(String key, Object value) {
        this.values.put(key, value);
    }

    public void changeCustomValue(String key, Object newValue) {
        this.values.replace(key, newValue);
    }

    public String parseString(String input) {
        StringBuilder output = new StringBuilder();
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(input);
        while (matcher.find()) {
            String key = matcher.group(1);
            Object value = this.values.getOrDefault(key, "");
            matcher.appendReplacement(output, value.toString());
        }
        matcher.appendTail(output);
        return output.toString();
    }

    /**
     * Dumps all fields and their values from the given object into the values map.
     *
     * @param object the object to dump
     */
    public void dumpObject(Object object) {
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                this.values.put(field.getName(), field.get(object));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Dumps the value of a given method from the given object into the values map.
     *
     * @param object the object to dump the method from
     * @param methodName the name of the method to dump
     */
    public void dumpMethod(Object object, String methodName) {
        try {
            Method method = object.getClass().getDeclaredMethod(methodName);
            method.setAccessible(true);
            this.values.put(methodName, method.invoke(object));
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }


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
