package me.xapu.trollgui.other;

import me.xapu.trollgui.main.Core;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PacketManager {

    // Thanks TrollV4
    // https://github.com/DxsSucuk/TrollV4/blob/24b8704989c043861d4d8207bc9a47fc0c98ad05/trollv4/src/de/presti/trollv4/utils/Packets.java
    // Used. daim. this is helpful !

    private final static Map<String, Class<?>> ocbClasses = new HashMap<>();
    private final static Map<Class<?>, Map<String, Method>> cachedMethods = new HashMap<>();

    public static Object getConnection(Player player) throws SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
        Class<?> ocbPlayer = PacketManager.getOCBClass("entity.CraftPlayer");
        Method getHandle = PacketManager.getMethod(ocbPlayer, "getHandle");
        Object nmsPlayer = getHandle.invoke(player);
        Field conField = nmsPlayer.getClass().getField("playerConnection");
        Object con = conField.get(nmsPlayer);
        return con;
    }

    public static boolean sendPacket(Player player, int packetNum, float number) {
        try {
            Class<?> packetClass = PacketManager.getNMSClass("PacketPlayOutGameStateChange");
            Constructor<?> packetConstructor = packetClass.getConstructor(int.class, float.class);
            Object packet = packetConstructor.newInstance(packetNum, number);
            Method sendPacket = PacketManager.getNMSClass("PlayerConnection").getMethod("sendPacket",
                    PacketManager.getNMSClass("Packet"));
            sendPacket.invoke(getConnection(player), packet);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static Class<?> getNMSClass(String name) {
        String version = org.bukkit.Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        Bukkit.getServer().getLogger().warning(Bukkit.getServer().getClass().getName());

        try {
            Bukkit.getServer().getLogger().warning(Arrays.toString(Class.forName("net.minecraft.server").getClasses()));
            return Class.forName("net.minecraft.server." + version + "." + name);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", new Class[] { getNMSClass("Packet") })
                    .invoke(playerConnection, new Object[] { packet });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... params) {
        if (!cachedMethods.containsKey(clazz))
            cachedMethods.put(clazz, new HashMap<String, Method>());

        Map<String, Method> methods = cachedMethods.get(clazz);

        if (methods.containsKey(methodName))
            return methods.get(methodName);

        try {
            Method method = clazz.getMethod(methodName, params);
            methods.put(methodName, method);
            cachedMethods.put(clazz, methods);
            return method;
        } catch (Throwable e) {
            e.printStackTrace();
            methods.put(methodName, null);
            cachedMethods.put(clazz, methods);
            return null;
        }
    }

    public static Class<?> getOCBClass(String localPackage) {

        if (ocbClasses.containsKey(localPackage))
            return ocbClasses.get(localPackage);

        String declaration = "org.bukkit.craftbukkit." + Core.instance.getServerVersion() + "." + localPackage;
        Class<?> clazz;

        try {
            clazz = Class.forName(declaration);
        } catch (Throwable e) {
            e.printStackTrace();
            return ocbClasses.put(localPackage, null);
        }

        ocbClasses.put(localPackage, clazz);
        return clazz;
    }
}