package me.xapu.trollgui.main;

import me.xapu.trollgui.other.UpdateChecker;
import me.xapu.trollgui.ui.PlayerSelectorInventory;
import me.xapu.trollgui.ui.SettingsMenuInventory;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Set;

public class TrollCommand extends CommandHandler {
   Set<String> keys = Core.instance.config.getDefaultSection().getKeys(true);
   String[] args;

    // Not 1:1 Stolen fon GeeksforGeeks i swear....
    public static String[] convert(Set<String> setOfString)
    {
        String[] arrayOfString = new String[setOfString.size()];
        int index = 0;
        for (String str : setOfString)
            arrayOfString[index++] = str;
        return arrayOfString;
    }

    public static boolean isNumeric(String str) { // Surely not stolen.... xd

        if (str == null || str.length() == 0) {
            return false;
        }

        try {
            Double.parseDouble(str);
            return true;

        } catch (NumberFormatException e) {
            return false;
        }

    }


    public TrollCommand(CommandMap commandMap, JavaPlugin plugin) {
        super(plugin, "troll");
        keys.remove("items");
        keys.remove("menu");
        keys.remove("values");
        keys.remove("menu.messages");
        keys.remove("items.messages");
        keys.remove("values.explode-item");
        keys.remove("values.burn-item");
        keys.remove("values.random-look");
        keys.remove("values.fake-clear");
        keys.remove("values.advanced-perms");
        keys.remove("values.fakeBlocks");
        addDescription("");
        addUsage("");
        addPermission("ms3.use");
        addPermissionMessage(Core.instance.getP()+ Core.tcc(Core.instance.config.getString("no-perms")));
        registerCommand(commandMap);
        addListTabbComplete(0,"ms3.use", "settings", "giveskull", "credits", "update", "contact", "config");

        addListTabbComplete(1, "ms3.use", convert(keys));
        addListTabbComplete(2, "ms3.use", "%SPACE_SEPERATOR%", "%EMPTY_CHAR%");
        setAliases(Arrays.asList("beeingabletotrolltheonlyplayerisfunbecauseitiskindafun"));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] strings) {
        String ags = strings.length > 0 ? strings[0] : "";
        if (sender instanceof Player){
            Player p = (Player) sender;
            if(Core.advCheck("ms3.use", p)){
                switch (ags){
                    case "settings":
                            SettingsMenuInventory sm = new SettingsMenuInventory();
                            sm.openInventory(p);
                        break;
                    case "giveskull":
                            p.getInventory().addItem(Core.instance.getSkull());
                            p.sendMessage(Core.instance.getP()+"§bYou got the skull item.");
                        break;
                    case "checkUpdate":
                            p.sendMessage(Core.instance.getP()+"§bChecking updates...");
                        new UpdateChecker(Core.instance, 78194).getVersion(version -> {
                            p.sendMessage(Float.parseFloat(Core.instance.getDescription().getVersion()) >= Float.parseFloat(version) ? Core.instance.getP() + "§7Latest version: §a§l"+version+"§7, Current version: §a§l"+ Core.instance.getDescription().getVersion() : Core.instance.getP() + " §7An update is §aAVAILABLE §7For the version: §a§l" + version + "§7, Your version: §c§l" + Core.instance.getDescription().getVersion() + "§7. §7Update it from here: https://www.spigotmc.org/resources/troll-plugin-gui-anything-is-configurable.78194/");
                        });
                        break;
                    case "credits":
                        new UpdateChecker(Core.instance, 78194).getCredits(credits -> {
                            credits = credits.replaceAll("%TROLL-SEPERATOR%", "\n").replaceAll("%GREEN%", "§a").replaceAll("%GREY%", "§7");
                            p.sendMessage(Core.instance.getP()+"§7§m---------§9 CREDITS §7§m---------");
                            p.sendMessage("§7"+credits);
                            p.sendMessage(Core.instance.getP()+"§7§m---------§9 CREDITS §7§m---------");
                        });
                        break;
                    case "config":
                        if(strings.length > 1){
                            if(Core.instance.config.contains(strings[1])){
                                if(strings.length > 2){
                                            /*
                                            I WANT TO SAY THANKS TO THE STACKOVERFLOW COMMUNITY XD BUT DAIM THIS WAS SURELY HARD.
                                             */
                                            String[] s2 = strings;
                                            s2 = (String[]) ArrayUtils.remove(s2, 0);
                                            s2 = (String[]) ArrayUtils.remove(s2, 0);
                                            String v = String.join(" ", s2).replace("%SPACE_SEPERATOR%", " ").replace("%EMPTY_CHAR%", "");
                                            if(v.equalsIgnoreCase("false") || v.equalsIgnoreCase("true")){
                                                Core.instance.config.set(strings[1], Boolean.parseBoolean(v));
                                            } else if(isNumeric(v)){
                                                Core.instance.config.set(strings[1], Integer.parseInt(v));
                                            } else {
                                                Core.instance.config.set(strings[1], v);
                                            }

                                            Core.instance.saveConfig();
                                            Core.instance.reloadConfig();
                                            p.sendMessage(Core.instance.getP() + "§7Successfully Changed §9" + strings[1] + "§7 to §7\"§a" + v + "§7\"");
                                            break;
                                } else {
                                p.sendMessage(Core.instance.getP() + "§cValue cant be empty.");
                                break;
                                }
                            } else {
                                p.sendMessage(Core.instance.getP()+"§7Error. Could not find: §c"+strings[1]+"§7 In the config file.");
                                break;
                            }
                        }
                        p.sendMessage(" ");
                        p.sendMessage(" ");
                        p.sendMessage(" ");
                        p.sendMessage(Core.instance.getP() + "Current config: "+strings.length);
                        Core.instance.config.getDefaultSection().getKeys(true).forEach(i -> {
                            if(!Core.instance.getConfig().get(i).toString().contains("MemorySection")) {
                                p.sendMessage("§7" + i + "§7: §b\"" + Core.instance.getConfig().get(i) + "\"");
                            }
                        });
                        break;
                    case "contact":
                        p.sendMessage(Core.instance.getP()+"§7Got errors? Report them here: ");
                        p.sendMessage(Core.instance.getP()+"§9Discord§7: Ram#6691");
                        p.sendMessage(Core.instance.getP()+"§6Spigot DM§7: https://www.spigotmc.org/members/xapu1337.955834/");
                        p.sendMessage(Core.instance.getP()+"§7Github Issue page§7: https://github.com/Xapu1337/TrollGUI/issues");
                        break;
                    default:
                            PlayerSelectorInventory ps = new PlayerSelectorInventory();
                            ps.openSel(p);
                        break;
                }
            }
//            if(ags.equals("settings")){
//                SettingsMenu sm = new SettingsMenu();
//                sm.openInventory(p);
//            } else if(ags.equals("giveskull")) {
//                p.getInventory().addItem(core.instance.getSkull());
//                p.sendMessage(core.instance.getP()+"§bYou got the skull item.");
//            } else {
//                PlayerSelector ps = new PlayerSelector();
//                ps.openSel(p);
//            }

        }else{
            sender.sendMessage("no player");
        }

        return true;
    }

}
