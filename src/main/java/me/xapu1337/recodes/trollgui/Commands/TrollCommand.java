package me.xapu1337.recodes.trollgui.Commands;

import me.xapu1337.recodes.trollgui.Cores.Core;
import me.xapu1337.recodes.trollgui.Handlers.CommandHandler;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class TrollCommand extends CommandHandler {
    Set<String> keys = Core.instance.config.getDefaultSection().getKeys(true);

    @org.jetbrains.annotations.NotNull
    @org.jetbrains.annotations.Contract(pure = true)
    public static String[] convert(Set<String> setOfString)
    {
        String[] arrayOfString = new String[setOfString.size()];
        int index = 0;
        for (String str : setOfString)
            arrayOfString[index++] = str;
        return arrayOfString;
    }

    public static boolean isNumeric(String str) { // Surely not stolen.... xd
        if (str == null || str.length() == 0)
            return false;

        try { Double.parseDouble(str); return true; } catch (NumberFormatException e) { return false; }
    }

    public TrollCommand(CommandMap commandMap, JavaPlugin plugin) {
        super(plugin, "troll");

        addDescription("");
        addUsage("");
        addPermission("ms3.use");
        addPermissionMessage(Core.instance.config.getString("global.prefix")+ Core.instance.translateColorCodes(Core.instance.config.getString("no-perms")));
        registerCommand(commandMap);
        addListTabbComplete(0, "settings", "giveskull", "credits", "update", "contact", "config");
        addListTabbComplete(1, convert(keys));
        addListTabbComplete(2, "%SPACE_SEPARATOR%", "%EMPTY_CHAR%");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return false;
    }
}
