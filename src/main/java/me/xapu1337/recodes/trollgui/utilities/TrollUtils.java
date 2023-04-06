package me.xapu1337.recodes.trollgui.utilities;

import me.xapu1337.recodes.trollgui.cores.TrollCore;
import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrollUtils {


    public static final SingletonBase<TrollUtils> instance = new SingletonBase<>(TrollUtils.class);

    public static TrollUtils getInstance() {
        return instance.get();
    }


    public String translateHexColorCodes(String startTag, String endTag, String message) {
        final Pattern hexPattern = Pattern.compile(startTag + "([A-Fa-f0-9]{6})" + endTag);
        Matcher matcher = hexPattern.matcher(message);
        StringBuilder buffer = new StringBuilder(message.length() + 4 * 8);

        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, '§' + "x"
                    + '§' + group.charAt(0) + '§' + group.charAt(1)
                    + '§' + group.charAt(2) + '§' + group.charAt(3)
                    + '§' + group.charAt(4) + '§' + group.charAt(5)
            );
        }

        // Translate any leftover color codes
        return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
    }

    public String translateHexColorCodes(String endTag, String message) {
        return translateHexColorCodes("&#", endTag, message);
    }

    public String translateHexColorCodes(String message) {
        return translateHexColorCodes("&#", "", message);
    }



}
