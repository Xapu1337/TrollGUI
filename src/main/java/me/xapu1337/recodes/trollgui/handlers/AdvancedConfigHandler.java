package me.xapu1337.recodes.trollgui.handlers;

import me.xapu1337.recodes.trollgui.cores.TrollCore;
import me.xapu1337.recodes.trollgui.utilities.SingletonBase;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdvancedConfigHandler {





    public static final SingletonBase<AdvancedConfigHandler> instance = new SingletonBase<>(AdvancedConfigHandler.class);

    public static AdvancedConfigHandler getInstance() {
        return instance.get();
    }

    public static FileConfiguration con = TrollCore.getInstance().getConfig();

    /**
     * @return config
     * a simple method to get the config from the main class
     * getConfig()
     */
    public static FileConfiguration getConfig() {
        return con;
    }

    /**
     * reloadConfig()
     * <p>
     * a more advance reload method that preserves comments and ensures all values (if not present it will replace them) are there
     *
     */

    public static void reloadConfig() {
        // call the spigot's api reloadConfig() method
        TrollCore.getInstance().reloadConfig();
        // save default config (this will ensure all values are there)
        TrollCore.getInstance().saveDefaultConfig();
        // get the config
        con = TrollCore.getInstance().getConfig();
        // copy defaults (this will ensure all comments are there)
        con.options().copyDefaults(true);

    }

    /**
     * a method used to render config values with placeholders (for example ${config:prefix} will be replaced with the value of config.prefix)
     * @param template the message containing templates
     * @param context a map containing a set of keys and their corresponding values
     * @return the rendered message
     */
    public static String renderTemplate(String template, Map<String, Object> context) {
        Matcher matcher = Pattern.compile("\\$\\{(.*?)}").matcher(template);
        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            String expression = matcher.group(1);
            String[] parts = expression.split(":");
            String value = null;
            if (parts.length == 2) {
                if (parts[0].equals("config")) {
                    value = con.getString(parts[1]);
                } else {
                    value = context.get(parts[0]).toString();
                }
            } else {
                value = context.get(expression).toString();
            }
            if (value != null) {
                matcher.appendReplacement(result, value);
            }
        }
        matcher.appendTail(result);
        return result.toString();
    }



}
