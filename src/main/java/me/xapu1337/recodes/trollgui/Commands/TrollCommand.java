package me.xapu1337.recodes.trollgui.Commands;

import me.xapu1337.recodes.trollgui.Cores.TrollCore;
import me.xapu1337.recodes.trollgui.Handlers.CommandHandler;
import me.xapu1337.recodes.trollgui.Inventorys.PlayerSelector;
import me.xapu1337.recodes.trollgui.Inventorys.Settings;
import me.xapu1337.recodes.trollgui.Inventorys.TrollGUI;
import me.xapu1337.recodes.trollgui.Utilities.UpdateChecker;
import me.xapu1337.recodes.trollgui.Utilities.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public class TrollCommand extends CommandHandler<TrollCore> {


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

    public TrollCommand(TrollCore plugin) {
        super(plugin, Utilities.getSingleInstance().getString("Variables.commands.troll.name", "ms3").replaceAll("\\s", ""));

        if(TrollCore.instance.getConfig().getBoolean("Variables.commands.troll.alias.enabled"))
            setAliases(Arrays.stream(TrollCore.instance.getConfig().getStringList("Variables.commands.troll.alias.aliases").toArray(new String[0])).map(a -> a.replaceAll("\\s", "")).toArray(String[]::new));
        setPermission("ms3.use");
        setPermissionMessage(Objects.requireNonNull(Utilities.getSingleInstance().getConfigPath("Messages.missingPermissions", true)));

        addTabbComplete(0, "settings", "update", "contact");
        registerCommand();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player)sender;
            switch (args.length > 0 ? args[0].toLowerCase(Locale.ROOT) : "") {
                case "settings" -> p.openInventory(new Settings().getInventory());
                case "update" -> {
                    p.sendMessage(Utilities.getSingleInstance().getConfigPath("Variables.prefix") + "§bChecking updates...");
                    new UpdateChecker(TrollCore.instance, 78194).getVersion(version -> {
                        p.sendMessage(Float.parseFloat(TrollCore.instance.getDescription().getVersion()) >= Float.parseFloat(version) ? Utilities.getSingleInstance().getConfigPath("Variables.prefix")
                                + "§7Latest version: §a§l" + version + "§7, Current version: §a§l" + TrollCore.instance.getDescription().getVersion() : Utilities.getSingleInstance().getConfigPath("Variables.prefix")
                                + "§7An update is §aAVAILABLE §7For the version: §a§l" + version + "§7, Your version: §c§l" + TrollCore.instance.getDescription().getVersion()
                                + "§7. §7Update it from here: https://www.spigotmc.org/resources/troll-plugin-gui-anything-is-configurable.78194/");
                    });
                }
                case "contact" -> {
                    p.sendMessage(Utilities.getSingleInstance().getConfigPath("§7§m-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-", true));
                    p.sendMessage(Utilities.getSingleInstance().getConfigPath("§7Got errors? Report them here: ", true));
                    p.sendMessage(Utilities.getSingleInstance().getConfigPath("§9Discord§7: Ram#1337", true));
                    p.sendMessage(Utilities.getSingleInstance().getConfigPath("§6Spigot DM§7: https://www.spigotmc.org/members/xapu1337.955834/", true));
                    p.sendMessage(Utilities.getSingleInstance().getConfigPath("§7Github Issue page§7: https://github.com/Xapu1337/TrollGUI/issues", true));
                    p.sendMessage(Utilities.getSingleInstance().getConfigPath("§7§m-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-", true));
                    p.sendMessage(Utilities.getSingleInstance().getConfigPath("§7Love this plugin?", true));
                    p.sendMessage(Utilities.getSingleInstance().getConfigPath("§7It would be §c§b§ibreathtaking!§7 if you were to support me ^^", true));
                    p.sendMessage(Utilities.getSingleInstance().getConfigPath("§6Positive review on Spigot§7: https://www.spigotmc.org/resources/troll-plugin-gui-anything-is-configurable.78194/", true));
                    p.sendMessage(Utilities.getSingleInstance().getConfigPath("§9Donate me some Coffee §c<3§7: https://paypal.me/xapu1338", true));
                    p.sendMessage(Utilities.getSingleInstance().getConfigPath("§7§m-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-", true));
                }
                default ->
                        new PlayerSelector(p, Utilities.getSingleInstance().getConfigPath("MenuItems.playerSelector.extras.actions.openTrollGUI")).setOnPlayerSelect(
                                (callingSelector, caller, victim) -> {
                                    p.openInventory(new TrollGUI(p, victim).setCallingSelector(callingSelector).getInventory());
                                }
                        ).openForPlayer(p);
            }
        }
        return true;
    }
}
