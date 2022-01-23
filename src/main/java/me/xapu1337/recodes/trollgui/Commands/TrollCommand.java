package me.xapu1337.recodes.trollgui.Commands;

import me.xapu1337.recodes.trollgui.Cores.Core;
import me.xapu1337.recodes.trollgui.Handlers.CommandHandler;
import me.xapu1337.recodes.trollgui.Inventorys.PlayerSelector;
import me.xapu1337.recodes.trollgui.Inventorys.Settings;
import me.xapu1337.recodes.trollgui.Utilities.UpdateChecker;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TrollCommand extends CommandHandler {
    Set<String> keys = Objects.requireNonNull(Core.instance.config.getDefaultSection()).getKeys(true);


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
        super(plugin, Core.instance.utils.getString("Variables.commands.troll.name", "ms3").replaceAll("\\s", ""));
        Arrays.asList("Messages", "Variables", "MenuTitles", "MenuItems").forEach(keys::remove);

        addDescription("");
//        addUsage("");
        if(Core.instance.getConfig().getBoolean("Variables.commands.troll.alias.enabled"))
            addAliases(Arrays.stream(Core.instance.getConfig().getStringList("Variables.commands.troll.alias.aliases").toArray(new String[0])).map(a -> a.replaceAll("\\s", "")).toArray(String[]::new));
        addPermission("ms3.use");
        addPermissionMessage(Objects.requireNonNull(Core.instance.utils.getConfigPath("Messages.missingPermissions", true)));
        registerCommand(commandMap);
        addListTabbComplete(0, "settings", "update", "contact");
//        addListTabbComplete(1, convert((keys)));
//        addListTabbComplete(2, "%SPACE_SEPARATOR%", "%EMPTY_CHAR%");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player)sender;
            switch(args.length > 0 ? args[0].toLowerCase(Locale.ROOT) : ""){
                case "settings":
                        p.openInventory(new Settings().getInventory());
                    break;
                case "update":
                    p.sendMessage(Core.instance.utils.getConfigPath("Variables.prefix") + "§bChecking updates...");
                    new UpdateChecker(Core.instance, 78194).getVersion(version -> {
                        p.sendMessage(Float.parseFloat(Core.instance.getDescription().getVersion()) >= Float.parseFloat(version) ? Core.instance.utils.getConfigPath("Variables.prefix")
                                + "§7Latest version: §a§l"+version+"§7, Current version: §a§l"+ Core.instance.getDescription().getVersion() : Core.instance.utils.getConfigPath("Variables.prefix")
                                + "§7An update is §aAVAILABLE §7For the version: §a§l" + version + "§7, Your version: §c§l" + Core.instance.getDescription().getVersion()
                                + "§7. §7Update it from here: https://www.spigotmc.org/resources/troll-plugin-gui-anything-is-configurable.78194/");
                    });
                    break;
                case "contact":
                    p.sendMessage(Core.instance.utils.getConfigPath("Variables.prefix")+"§7§m-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
                    p.sendMessage(Core.instance.utils.getConfigPath("Variables.prefix")+"§7Got errors? Report them here: ");
                    p.sendMessage(Core.instance.utils.getConfigPath("Variables.prefix")+"§9Discord§7: Ram#1337");
                    p.sendMessage(Core.instance.utils.getConfigPath("Variables.prefix")+"§6Spigot DM§7: https://www.spigotmc.org/members/xapu1337.955834/");
                    p.sendMessage(Core.instance.utils.getConfigPath("Variables.prefix")+"§7Github Issue page§7: https://github.com/Xapu1337/TrollGUI/issues");
                    p.sendMessage(Core.instance.utils.getConfigPath("Variables.prefix")+"§7§m-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
                    p.sendMessage(Core.instance.utils.getConfigPath("Variables.prefix")+"§7Love this plugin?");
                    p.sendMessage(Core.instance.utils.getConfigPath("Variables.prefix")+"§7It would be §c§b§ibreathtaking!§7 if you were to support me ^^");
                    p.sendMessage(Core.instance.utils.getConfigPath("Variables.prefix")+"§6Positive review on Spigot§7: https://www.spigotmc.org/resources/troll-plugin-gui-anything-is-configurable.78194/");
                    p.sendMessage(Core.instance.utils.getConfigPath("Variables.prefix")+"§9Donate me some Coffee §c<3§7: https://paypal.me/xapu1338");
                    p.sendMessage(Core.instance.utils.getConfigPath("Variables.prefix")+"§7§m-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
                    break;
                default:
                        p.openInventory(new PlayerSelector(p).getInventory());
                    break;
            }
        }
        return true;
    }
}
