package me.xapu.trollgui.other;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import me.xapu.trollgui.main.Core;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

// Everything inside here owns:
//https://github.com/DxsSucuk/TrollV4/tree/2638b18f5b6a89fe263d5861522e3bf3999e6c99


public class Packets {

    private final static Map<String, Class<?>> ocbClasses = new HashMap<>();
    private final static Map<Class<?>, Map<String, Method>> cachedMethods = new HashMap<>();

    public static Object getConnection(Player player) throws SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
        Class<?> ocbPlayer = Packets.getOCBClass("entity.CraftPlayer");
        Method getHandle = Packets.getMethod(ocbPlayer, "getHandle");
        Object nmsPlayer = getHandle.invoke(player);
        Field conField = nmsPlayer.getClass().getField("playerConnection");
        Object con = conField.get(nmsPlayer);
        return con;
    }

    public static boolean sendPacket(Player player, int packetNum, float number) {
        try {
            Class<?> packetClass = Packets.getNMSClass("PacketPlayOutGameStateChange");
            Constructor<?> packetConstructor = packetClass.getConstructor(int.class, float.class);
            Object packet = packetConstructor.newInstance(packetNum, number);
            Method sendPacket = Packets.getNMSClass("PlayerConnection").getMethod("sendPacket",
                    Packets.getNMSClass("Packet"));
            sendPacket.invoke(getConnection(player), packet);
        } catch (Exception e) {
            System.out.println("Your Server Version isnt Supporting this Packet! (" + packetNum + ")");
            return false;
        }
        return true;
    }

    public static Class<?> getNMSClass(String name) {
        String version = org.bukkit.Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            System.out.println("Your Server Version isnt Supporting this Packet! (" + name + ")");
        }
        return null;
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", new Class[]{getNMSClass("Packet")})
                    .invoke(playerConnection, new Object[]{packet});
        } catch (Exception e) {
            System.out.println("Your Server Version isnt Supporting this Packet! (" + packet + ")");
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

        String declaration = "org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + localPackage;
        Class<?> clazz;

        try {
            clazz = Class.forName(declaration);
        } catch (Throwable e) {
            System.out.println("Your Server Version isnt Supporting this Packet! (" + localPackage + ")");
            return ocbClasses.put(localPackage, null);
        }

        ocbClasses.put(localPackage, clazz);
        return clazz;
    }
}