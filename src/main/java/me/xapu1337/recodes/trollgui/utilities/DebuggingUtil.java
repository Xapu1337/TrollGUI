package me.xapu1337.recodes.trollgui.utilities;

import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollMetaData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.logging.Level;

public class DebuggingUtil {

    // Log format:  §8[§bms3§8] §7[§3ClassName§7] §fmessage
    // Levels:       INFO=aqua/white  WARN=yellow  ERROR=red
    private static final String TAG      = "&8[&bms3&8] ";
    private static final String INFO_C   = "&b"; // class name colour
    private static final String WARN_C   = "&e";
    private static final String ERROR_C  = "&c";
    private static final String RESET    = "&r";

    private final MessageUtils messages;
    private final Level minLogLevel;

    public DebuggingUtil(MessageUtils messages, FileConfiguration config) {
        this.messages = messages;
        this.minLogLevel = parseLevel(config.getString("log-level", "WARNING"));
    }


    /** Emit a raw (already formatted) line. Skips level check — callers guard. */
    private void emit(String line) {
        Bukkit.getConsoleSender().sendMessage(messages.$(line));
    }

    private boolean allows(Level level) {
        return level.intValue() >= minLogLevel.intValue();
    }


    public void log(Level level, String message, int callerOffset) {
        if (!allows(level)) return;
        String cls = callerClass();
        String cc  = levelColour(level);
        emit(TAG + cc + "&l[" + level.getName() + "] " + INFO_C + cls + "&7: " + RESET + message);
    }

    public void log(String message)                { log(Level.INFO, message, 1); }
    public void log(String fmt, Object... args)    { log(Level.INFO, String.format(fmt, args), 1); }

    /** Shorthand alias for {@link #log(String)}. */
    public void l(String message)                  { log(Level.INFO, message, 1); }
    public void l(String fmt, Object... args)      { log(Level.INFO, String.format(fmt, args), 1); }

    public void warn(String message)               { log(Level.WARNING, message, 1); }
    public void warn(String fmt, Object... args)   { log(Level.WARNING, String.format(fmt, args), 1); }

    public void error(String message)              { log(Level.SEVERE, message, 1); }
    public void error(String fmt, Object... args)  { log(Level.SEVERE, String.format(fmt, args), 1); }

    /** Send a prefixed debug message directly to a CommandSender. */
    public void send(CommandSender sender, String message) {
        if (sender == null || message == null) return;
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                TAG + INFO_C + message));
    }

    // ── rich error ────────────────────────────────────────────────────────────

    public void error(String message, Throwable throwable, Map<String, Object> data) {
        if (!allows(Level.SEVERE)) return;
        log(Level.SEVERE, message, 1);

        if (throwable != null) {
            StringWriter sw = new StringWriter();
            throwable.printStackTrace(new PrintWriter(sw));
            // each stack-trace line gets its own emit for readable console output
            for (String line : sw.toString().split("\n")) {
                emit(ERROR_C + line);
            }
        }

        if (data != null && !data.isEmpty()) {
            emit(ERROR_C + "&lContext:");
            for (Map.Entry<String, Object> e : data.entrySet()) {
                Object v = e.getValue();
                emit(ERROR_C + "  " + e.getKey() + " &7= &f" + v
                        + " &8(" + (v != null ? v.getClass().getSimpleName() : "null") + ")");
            }
        }
    }

    /**
     * Compact single-block object dump.
     * Arrays, maps, lists, ItemStacks, TrollMetaData, and arbitrary objects
     * are all printed in a condensed bracket style rather than many loose lines.
     */
    public void logObject(Object obj) {
        if (!allows(Level.INFO)) return;
        String cls = callerClass();
        emit(TAG + INFO_C + cls + " &7inspect: " + RESET + describe(obj, 0));
    }


    private String describe(Object obj, int depth) {
        if (obj == null) return "&cnull";
        if (depth > 3)   return "&8<...>"; // prevent runaway recursion

        Class<?> c = obj.getClass();

        if (c.isArray()) {
            int len = Array.getLength(obj);
            StringJoiner sj = new StringJoiner("&8, &f", "&8[&f", "&8]");
            for (int i = 0; i < len; i++) sj.add(describe(Array.get(obj, i), depth + 1));
            return sj.toString();
        }

        if (obj instanceof Map<?,?> map) {
            if (map.isEmpty()) return "&8{&7empty&8}";
            StringJoiner sj = new StringJoiner("&8, ", "&8{", "&8}");
            for (Map.Entry<?,?> e : map.entrySet())
                sj.add("&b" + e.getKey() + "&8=&f" + describe(e.getValue(), depth + 1));
            return sj.toString();
        }

        if (obj instanceof List<?> list) {
            if (list.isEmpty()) return "&8[&7empty&8]";
            StringJoiner sj = new StringJoiner("&8, &f", "&8[&f", "&8]");
            for (Object el : list) sj.add(describe(el, depth + 1));
            return sj.toString();
        }

        if (obj instanceof ItemStack is) {
            ItemMeta meta = is.getItemMeta();
            int dmg = (meta instanceof Damageable d) ? d.getDamage() : 0;
            String name = (meta != null && meta.hasDisplayName()) ? meta.getDisplayName() : "&7-";
            return "&8{&btype&8=&f" + is.getType()
                    + " &bamt&8=&f" + is.getAmount()
                    + " &bname&8=&f" + name
                    + (dmg != 0 ? " &bdmg&8=&f" + dmg : "")
                    + "&8}";
        }

        if (obj instanceof ItemMeta meta) {
            return "&8{&bname&8=&f" + (meta.hasDisplayName() ? meta.getDisplayName() : "&7-")
                    + " &blore&8=&f" + meta.getLore()
                    + " &benchants&8=&f" + meta.getEnchants()
                    + "&8}";
        }

        if (obj instanceof TrollMetaData meta) {
            ItemMeta im = meta.getItemMeta();
            return "&8{&btroll&8=&f" + meta.getTrollName()
                    + " &bitem&8=&f" + meta.getItem().getType()
                    + " &bname&8=&f" + (im != null ? im.getDisplayName() : "&7-")
                    + " &blore&8=&f" + (im != null ? im.getLore() : "&7-")
                    + "&8}";
        }

        if (obj instanceof Troll troll) {
            return describe(troll.getTrollMetaData(), depth);
        }

        // generic reflection fallback
        StringJoiner sj = new StringJoiner("&8, ", c.getSimpleName() + "&8{", "&8}");
        for (Field f : c.getDeclaredFields()) {
            if (!f.trySetAccessible()) continue;
            try {
                sj.add("&b" + f.getName() + "&8=&f" + f.get(obj));
            } catch (IllegalAccessException ignored) {}
        }
        return sj.toString();
    }

    private String levelColour(Level level) {
        if (level == Level.SEVERE)  return ERROR_C;
        if (level == Level.WARNING) return WARN_C;
        return INFO_C;
    }

    private Level parseLevel(String name) {
        try { return Level.parse(name.toUpperCase()); }
        catch (Exception e) { return Level.WARNING; }
    }

    /**
     * Walks the stack and returns the simple class name of the first frame
     * that is not Thread, DebuggingUtil, or a JDK internal.
     */
    private String callerClass() {
        String myClass = DebuggingUtil.class.getName();
        for (StackTraceElement frame : Thread.currentThread().getStackTrace()) {
            String cn = frame.getClassName();
            if (cn.equals("java.lang.Thread")) continue;
            if (cn.startsWith(myClass)) continue;
            // strip lambda suffix: e.g. "com.example.Foo$$Lambda$12" → "Foo"
            int dollar = cn.indexOf("$$");
            if (dollar >= 0) cn = cn.substring(0, dollar);
            return cn.substring(cn.lastIndexOf('.') + 1);
        }
        return "?";
    }
}
