package me.xapu1337.recodes.trollgui.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollMetaData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

public class DebuggingUtil {

    private static final SingletonBase<DebuggingUtil> instance = new SingletonBase<>(DebuggingUtil.class);
    private final boolean debuggingEnabled = true;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public DebuggingUtil() {}

    public static DebuggingUtil getInstance() {
        return instance.get();
    }

    public void log(Level level, String message, int depth) {
        if (debuggingEnabled) {
            String className = getCallerClassName(depth);
            String CLASS_NAME_COLOR = "&l&#0079B3";
            String logMessage = String.format("%s%s&7: %s",
                    CLASS_NAME_COLOR, className, message);
            String formattedMessage = formatLogMessage(level, logMessage);
            Bukkit.getConsoleSender().sendMessage(MessageUtils.getInstance().$(formattedMessage));
        }
    }

    public void log(String message) {
        log(Level.INFO, message, 2);
    }

    public void log(String message, Object... args) {
        log(Level.INFO, String.format(message, args), 2);
    }

    public void l(String message) {
        log(Level.INFO, message, 2);
    }

    public void l(String message, Object... args) {
        log(Level.INFO, String.format(message, args), 2);
    }


    public void logObject(Object obj) {
        l(" \n ");
        l(" \n ");
        if (obj == null) {
            l("Object is null");
            return;
        }
        l("| START OF OBJECT " + obj.getClass().getName() + " \n ");
        Class<?> objClass = obj.getClass();
        if (objClass.isArray()) {
            int length = Array.getLength(obj);
            for (int i = 0; i < length; i++) {
                logObject(Array.get(obj, i));
            }
            return;
        }
        String typeName = objClass.getName();
        switch (typeName) {
            case "java.util.HashMap", "java.util.Map" -> {
                Map<?, ?> map = (Map<?, ?>) obj;
                if (map.isEmpty()) {
                    l("|  Empty Map");
                } else {
                    l("|  Map Contents:");
                    l("|    " + gson.toJson(map));
                }
            }
            case "java.util.List" -> {
                List<?> list = (List<?>) obj;
                if (list.isEmpty()) {
                    l("|  Empty List");
                } else {
                    l("|  List Contents:");
                    for (Object element : list) {
                        l("|    "+(String) element);
                    }
                }
            }
            case "org.bukkit.inventory.ItemStack" -> {
                ItemStack itemStack = (ItemStack) obj;
                l("|  ItemStack Contents:");
                l("|    Type: " + itemStack.getType());
                l("|    Amount: " + itemStack.getAmount());
                l("|    Durability: " + itemStack.getDurability());
                l("|    Meta: " + itemStack.getItemMeta());
            }
            case "org.bukkit.inventory.meta.ItemMeta" -> {
                ItemMeta itemMeta = (ItemMeta) obj;
                l("|  ItemMeta Contents:");
                l("|    DisplayName: " + itemMeta.getDisplayName());
                l("|    Lore: " + itemMeta.getLore());
                l("|    Enchants: " + itemMeta.getEnchants());
            }
            case "me.xapu1337.recodes.trollgui.types.Troll", "me.xapu1337.recodes.trollgui.types.TrollMetaData" -> {
                TrollMetaData metaData = typeName.equals("me.xapu1337.recodes.trollgui.types.Troll") ? ((Troll) obj).getTrollMetaData() : (TrollMetaData) obj;
                l("|  Logging contents of TrollMetaData:");
                l("|    Troll name: " + metaData.getTrollName());
                l("|    Item Name: " + metaData.getItemMeta().getDisplayName());
                l("|    Item Lore: " + metaData.getItemMeta().getLore());
                l("|    Item Enchants: " + metaData.getItemMeta().getEnchants());
                l("|    Item Type: " + metaData.getItem().getType());
            }
            default -> {
                l("|  Object fields:");
                Field[] fields = objClass.getDeclaredFields();
                for (Field field : fields) {
                    try {
                        if(!field.trySetAccessible()) continue;
                        l("|    " + field.getName() + ": " + field.get(obj));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                l("|   String representation:" + obj.toString());
                break;
            }
        }
    }



    public void send(CommandSender sender, String message) {
        if (debuggingEnabled && sender != null && message != null) {
            String DEBUG_PREFIX = "&8[&bDebug&8] &r";
            String logMessage = String.format("%s%s", DEBUG_PREFIX, message);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', logMessage));
        }
    }

    public void warn(String message) {
        String formattedMessage = formatLogMessage(WARNING, message);
        Bukkit.getConsoleSender().sendMessage(formattedMessage);
    }

    public void error(String message) {
        String formattedMessage = formatLogMessage(SEVERE, message);
        Bukkit.getConsoleSender().sendMessage(formattedMessage);
    }

    private String formatLogMessage(Level level, String message) {
        String prefix;
        ChatColor levelColor;

        if (Objects.equals(level, WARNING)) {
            levelColor = ChatColor.YELLOW;
            prefix = "&8[&eWarning &8| " + levelColor + level.getName() + "&8] &r";
        } else if (Objects.equals(level, SEVERE)) {
            levelColor = ChatColor.RED;
            prefix = "&8[&cError &8| " + levelColor + level.getName() + "&8] &r";
        } else {
            levelColor = ChatColor.WHITE;
            prefix = "&8[&bDebug &8| " + levelColor + level.getName() + "&8] &r";
        }

        String formattedPrefix = String.format("%s%s%s: ",
                ChatColor.DARK_GRAY, ChatColor.BOLD, prefix);
        return String.format("%s%s", formattedPrefix, message);
    }

    private String getStackTraceAsString(Throwable throwable) {
        if (throwable == null) {
            return "";
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
    private String getCallerClassName(int stackTraceDepth) {
        String fullClassName = Thread.currentThread().getStackTrace()[3 + stackTraceDepth].getClassName();
        return fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
    }

    public void error(String message, Throwable throwable, Map<String, Object> data) {
        String formattedMessage = formatLogMessage(SEVERE, message);
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getInstance().$(formattedMessage));

        if (throwable != null) {
            String stackTrace = getStackTraceAsString(throwable);
            String stackTraceMessage = MessageUtils.getInstance().$("&c&lStackTrace: &r\n" + stackTrace);
            Bukkit.getConsoleSender().sendMessage(stackTraceMessage);
        }

        if (data != null && !data.isEmpty()) {
            Bukkit.getConsoleSender().sendMessage(MessageUtils.getInstance().$("&c&lData:"));
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                Object value = entry.getValue();
                String valueString = value != null ? value.toString() : "null";
                String entryMessage = String.format("%s: %s (%s)", entry.getKey(), valueString, value != null ? value.getClass().getSimpleName() : "null");
                Bukkit.getConsoleSender().sendMessage(MessageUtils.getInstance().$("┆ " + entryMessage));
            }
            Bukkit.getConsoleSender().sendMessage("╰");
        }
    }



}