package me.xapu1337.recodes.trollgui.Commands;

import me.xapu1337.recodes.trollgui.Cores.TrollCore;
import me.xapu1337.recodes.trollgui.Handlers.CommandHandler;
import me.xapu1337.recodes.trollgui.Handlers.TemplateHandler;
import me.xapu1337.recodes.trollgui.Inventorys.PlayerSelector;
import me.xapu1337.recodes.trollgui.Inventorys.Settings;
import me.xapu1337.recodes.trollgui.Inventorys.TrollGUI;
import me.xapu1337.recodes.trollgui.Utilities.Singleton;
import me.xapu1337.recodes.trollgui.Utilities.UpdateChecker;
import me.xapu1337.recodes.trollgui.Utilities.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public class TrollCommand extends CommandHandler<TrollCore> {
    private final TemplateHandler templateHandler;

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
        super(plugin, TrollCore.instance.config.getString("Variables.commands.troll.name", "ms3").replaceAll("\\s", ""));
        this.templateHandler = new TemplateHandler();
        if(TrollCore.instance.getConfig().getBoolean("Variables.commands.troll.alias.enabled"))
            setAliases(Arrays.stream(TrollCore.instance.getConfig().getStringList("Variables.commands.troll.alias.aliases").toArray(new String[0])).map(a -> a.replaceAll("\\s", "")).toArray(String[]::new));
        setPermission("ms3.use");
        setPermissionMessage(Objects.requireNonNull(Utilities.getSingleInstance().getConfigPath("Messages.missingPermissions", true)));

        addTabbComplete(0, "settings", "update", "contact", "vanish");
        registerCommand();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, String[] args) {
        if(commandSender instanceof Player sender){
            switch (args.length > 0 ? args[0].toLowerCase(Locale.ROOT) : "") {
                case "settings" -> sender.openInventory(new Settings(sender).getInventory());
                case "vanish" -> {
                    Utilities.getSingleInstance().addOrRemove(Singleton.getSingleInstance().vanishedPlayers, sender);

                    templateHandler.addCustomValue("vanished", "config:Messages.vanished");
                    templateHandler.addCustomValue("unvanished", "config:Messages.unvanished");

                    boolean vanished = Singleton.getSingleInstance().vanishedPlayers.containsKey(Utilities.getSingleInstance().uuidOrName(sender, TrollCore.instance.usingUUID));

                    templateHandler.printValues();
                    sender.sendMessage(templateHandler.$(vanished ? "${vanished}" : "${unvanished}"));

                    if(vanished){
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.hidePlayer(TrollCore.instance, sender);
                        }
                    }else{
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.showPlayer(TrollCore.instance, sender);
                        }
                    }

                }
                case "update" -> {
                    sender.sendMessage(Utilities.getSingleInstance().getConfigPath("Variables.prefix") + "§bChecking updates...");
                    new UpdateChecker(TrollCore.instance, 78194).getVersion(version -> {
                        sender.sendMessage(Float.parseFloat(TrollCore.instance.getDescription().getVersion()) >= Float.parseFloat(version) ? Utilities.getSingleInstance().getConfigPath("Variables.prefix")
                                + "§7Latest version: §a§l" + version + "§7, Current version: §a§l" + TrollCore.instance.getDescription().getVersion() : Utilities.getSingleInstance().getConfigPath("Variables.prefix")
                                + "§7An update is §aAVAILABLE §7For the version: §a§l" + version + "§7, Your version: §c§l" + TrollCore.instance.getDescription().getVersion()
                                + "§7. §7Update it from here: https://www.spigotmc.org/resources/troll-plugin-gui-anything-is-configurable.78194/");
                    });
                }
                case "contact" -> {
                    sender.sendMessage(Utilities.getSingleInstance().getConfigPath("§7§m-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-", true));
                    sender.sendMessage(Utilities.getSingleInstance().getConfigPath("§7Got errors? Report them here: ", true));
                    sender.sendMessage(Utilities.getSingleInstance().getConfigPath("§9Discord§7: Ram#1337", true));
                    sender.sendMessage(Utilities.getSingleInstance().getConfigPath("§6Spigot DM§7: https://www.spigotmc.org/members/xapu1337.955834/", true));
                    sender.sendMessage(Utilities.getSingleInstance().getConfigPath("§7Github Issue page§7: https://github.com/Xapu1337/TrollGUI/issues", true));
                    sender.sendMessage(Utilities.getSingleInstance().getConfigPath("§7§m-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-", true));
                    sender.sendMessage(Utilities.getSingleInstance().getConfigPath("§7Love this plugin?", true));
                    sender.sendMessage(Utilities.getSingleInstance().getConfigPath("§7It would be §c§b§ibreathtaking!§7 if you were to support me ^^", true));
                    sender.sendMessage(Utilities.getSingleInstance().getConfigPath("§6Positive review on Spigot§7: https://www.spigotmc.org/resources/troll-plugin-gui-anything-is-configurable.78194/", true));
                    sender.sendMessage(Utilities.getSingleInstance().getConfigPath("§9Donate me some Coffee §c<3§7: https://paypal.me/xapu1338", true));
                    sender.sendMessage(Utilities.getSingleInstance().getConfigPath("§7§m-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-", true));
                }
                default ->
                        new PlayerSelector(sender, Utilities.getSingleInstance().getConfigPath("MenuItems.playerSelector.extras.actions.openTrollGUI")).setOnPlayerSelect(
                                (callingSelector, caller, victim) -> {
                                    sender.openInventory(new TrollGUI(sender, victim).setCallingSelector(callingSelector).getInventory());
                                }
                        ).openForPlayer(sender);
            }
        }
        return true;
    }
}
