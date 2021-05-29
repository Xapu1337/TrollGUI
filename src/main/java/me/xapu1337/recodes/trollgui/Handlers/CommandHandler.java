package me.xapu1337.recodes.trollgui.Handlers;
import me.xapu1337.recodes.trollgui.Cores.Core;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author redsarow
 */
public abstract class CommandHandler<T extends JavaPlugin> extends Command implements CommandExecutor, PluginIdentifiableCommand {

    private final T plugin;
    private final boolean register = false;
    private final HashMap<Integer, ArrayList<TabCommand>> tabComplete;


    /**
     * @param plugin plugin responsible of the command.
     * @param name   name of the command.
     */
    public CommandHandler(T plugin, String name) {
        super(name);

        assert plugin != null;
        assert name != null;
        assert name.length() > 0;

        setLabel(name);
        this.plugin = plugin;
        tabComplete = new HashMap<>();
    }


    //<editor-fold desc="add">
    /**
     * @param description description of the command
     *
     */
    protected void addDescription(String description) {
        if (register || description != null)
            setDescription(description);
    }

    /**
     * @param use use of the command (ex: /myCmd [val1]
     *
     */
    protected void addUsage(String use) {
        if (register || use != null)
            setUsage(use);
    }

    /**
     * @param aliases aliases of the command.
     *
     * @return CommandHandler, instance of the class
     */
    protected CommandHandler addAliases(String... aliases) {
        if (aliases != null && (register || aliases.length > 0))
            setAliases(Arrays.stream(aliases).collect(Collectors.toList()));
        return this;
    }

    //<editor-fold desc="TabbComplete">
    /**
     * Adds an argument to an index with permission and the words before
     *
     * @param indice     index where the argument is in the command. /myCmd is at the index -1, so
     *                   /myCmd index0 index1 ...
     * @param permission permission to add (may be null)
     * @param arg        word to add
     * @param beforeText text preceding the argument (may be null)
     *
     * @return CommandHandler, instance of the class
     */
    protected CommandHandler addOneTabbComplete(int indice, String permission, String arg, String... beforeText) {
        if (arg != null && indice >= 0) {
            if (tabComplete.containsKey(indice)) {
                tabComplete.get(indice).add(new TabCommand(indice, arg, permission, beforeText));
            } else {
                ArrayList<TabCommand> tabCommands = new ArrayList<>();
                tabCommands.add(new TabCommand(indice, arg, permission, beforeText));
                tabComplete.put(indice, tabCommands);
            }
        }
        return this;
    }

    /**
     * Add multiple arguments to an index with permission and words before
     *
     * @param indice     index where the argument is in the command. /myCmd is at the index -1, so
     *                   /myCmd index0 index1 ...
     * @param permission permission to add (may be null)
     * @param beforeText text preceding the argument (may be null)
     *
     * @param arg        mots à ajouter
     */
    protected void addListTabbComplete(int indice, String permission, String[] beforeText, String... arg) {
        if (arg != null && arg.length > 0 && indice >= 0) {
            if (tabComplete.containsKey(indice)) {
                tabComplete.get(indice).addAll(Arrays.stream(arg).collect(
                        ArrayList::new,
                        (tabCommands, s) -> tabCommands.add(new TabCommand(indice, s, permission, beforeText)),
                        ArrayList::addAll));
            } else {
                tabComplete.put(indice, Arrays.stream(arg).collect(
                        ArrayList::new,
                        (tabCommands, s) -> tabCommands.add(new TabCommand(indice, s, permission, beforeText)),
                        ArrayList::addAll)
                );
            }
        }
    }

    /**
     * Add multiple arguments to an index
     *  @param index index where the argument is in the command. /myCmd is at the index -1, so
     *               /myCmd index0 index1 ...
     * @param arg    mots à ajouter
     *
     */
    protected void addListTabbComplete(int index, String... arg) {
        if (arg != null && arg.length > 0 && index >= 0) {
            addListTabbComplete(index, "ms3.use", null, arg);
        }
    }
    //</editor-fold>

    /**
     * add permission to command
     *
     * @param permission permission to add (may be null)
     *
     */
    protected void addPermission(String permission) {
        if (register || permission != null)
            setPermission(permission);
    }

    /**
     * @param permissionMessage message if the player does not have permission
     *
     */
    protected void addPermissionMessage(String permissionMessage) {
        if (register || permissionMessage != null)
            setPermissionMessage(permissionMessage);
    }
    //</editor-fold>

    /**
     * /!\ to do at the end /!\ to save the command.
     *
     * @param commandMap via:<br/>
     *                   <code>
     *                   Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");<br/>
     *                   f.setAccessible(true);<br/>
     *                   CommandMap commandMap = (CommandMap) f.get(Bukkit.getServer());
     *                   </code>
     *
     */
    protected void registerCommand(CommandMap commandMap) {
        if (!register) {
            commandMap.register("", this);
        }
    }

    //<editor-fold desc="get">
    /**
     * @return plugin responsible for the command
     */
    @Override
    public T getPlugin() {
        return this.plugin;
    }

    /**
     * @return tabComplete
     */
    public HashMap<Integer, ArrayList<TabCommand>> getTabComplete() {
        return tabComplete;
    }
    //</editor-fold>


    //<editor-fold desc="Override">
    /**
     * @param commandSender sender
     * @param command       command
     * @param arg           argument of the command
     *
     * @return true if ok, false otherwise
     */
    @Override
    public boolean execute(CommandSender commandSender, String command, String[] arg) {
        if(commandSender instanceof Player){
            Player p = (Player) commandSender;
            if (getPermission() != null) {
                if (!Core.instance.advancedPermissionsChecker(p, getPermission())) {
                    if (getPermissionMessage() == null) {
                        if(Core.instance.config.getBoolean("Messages.extra.missingPermissionsMessage")){
                            commandSender.sendMessage(ChatColor.RED + "no permit!");
                        }
                    } else {
                        if(Core.instance.config.getBoolean("Messages.extra.missingPermissionsMessage")){
                            commandSender.sendMessage(getPermissionMessage());
                        }
                    }
                    return false;
                }
            }
            if (onCommand(commandSender, this, command, arg))
                return true;
            commandSender.sendMessage(ChatColor.DARK_RED + getUsage());
            return false;
        }
        return false;
    }

    /**
     * @param sender sender
     * @param alias alias used
     * @param args argument of the command
     *
     * @return a list of possible values
     */
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {

        int indice = args.length - 1;
        if(sender instanceof Player){
            if ((getPermission() != null && !Core.instance.advancedPermissionsChecker(((Player) sender).getPlayer(), getPermission())) || tabComplete.size() == 0 || !tabComplete.containsKey(indice))
                return super.tabComplete(sender, alias, args);

            ArrayList<String> list = tabComplete.get(indice).stream().filter(tabCommand ->
                    (tabCommand.getTextAvant() == null || tabCommand.getTextAvant().contains(args[indice - 1])) &&
                            (tabCommand.getPermission() == null || Core.instance.advancedPermissionsChecker(((Player) sender).getPlayer(), tabCommand.getPermission())) &&
                            (tabCommand.getText().startsWith(args[indice]))
            ).map(TabCommand::getText).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

            return list.size() < 1 ? super.tabComplete(sender, alias, args) : list;

        }
        return null;
    }
    //</editor-fold>

    //<editor-fold desc="class TabCommand">
    private static class TabCommand {

        private final int indice;
        private final String text;
        private final String permission;
        private final ArrayList<String> textAvant;

        private TabCommand(int indice, String text, String permission, String... textAvant) {
            this.indice = indice;
            this.text = text;
            this.permission = permission;
            if (textAvant == null || textAvant.length < 1) {
                this.textAvant = null;
            } else {
                this.textAvant = Arrays.stream(textAvant).collect(ArrayList::new,
                        ArrayList::add,
                        ArrayList::addAll);
            }
        }

        private TabCommand(int indice, String text, String permission) {
            this(indice, text, permission, "");
        }

        private TabCommand(int indice, String text, String[] textAvant) {
            this(indice, text, null, textAvant);
        }

        private TabCommand(int indice, String text) {
            this(indice, text, null, "");
        }

        //<editor-fold desc="get&set">
        public String getText() {
            return text;
        }

        public int getIndice() {
            return indice;
        }

        public String getPermission() {
            return permission;
        }

        public ArrayList<String> getTextAvant() {
            return textAvant;
        }
        //</editor-fold>

    }
    //</editor-fold>
}
