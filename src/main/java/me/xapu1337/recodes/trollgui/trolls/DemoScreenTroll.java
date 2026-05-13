package me.xapu1337.recodes.trollgui.trolls;

import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollName;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Shows the Minecraft demo screen overlay to the victim.
 *
 * Protocol: Game Event packet, Event=5 (Demo event), Value=0.0f ("Show welcome to demo screen").
 *
 * Version handling:
 *   1.17+ — ClientboundGameEventPacket (Mojang-mapped NMS, unversioned package)
 *   1.8–1.16 — PacketPlayOutGameStateChange (Spigot versioned NMS package)
 */
@TrollName("demoScreen")
public class DemoScreenTroll extends Troll {

    @Override
    public void execute() {
        Player victim = getVictim();
        try {
            Object handle = victim.getClass().getMethod("getHandle").invoke(victim);

            // Try modern (1.17+ Mojang-mapped) path first, fall back to versioned legacy
            try {
                sendModernPacket(handle);
            } catch (ClassNotFoundException | NoSuchFieldException e) {
                // Pre-1.17 versioned package fallback
                String pkgName = Bukkit.getServer().getClass().getPackage().getName();
                String[] parts = pkgName.split("\\.");
                // versioned package: org.bukkit.craftbukkit.v1_XX_RX → parts[3]
                String nmsVersionPkg = parts.length > 3 ? parts[3] : "v1_16_R3";
                sendLegacyPacket(handle, nmsVersionPkg);
            }
        } catch (Exception e) {
            services.debug().log("DemoScreenTroll error for " + victim.getName() + ": " + e.getMessage());
        }
    }

    /**
     * 1.17+ (Mojang-mapped NMS):
     * ClientboundGameEventPacket(ClientboundGameEventPacket.Type event, float value)
     * ServerPlayer.connection → ServerGamePacketListenerImpl.send(Packet)
     */
    private void sendModernPacket(Object handle) throws Exception {
        Class<?> packetClass = Class.forName("net.minecraft.network.protocol.game.ClientboundGameEventPacket");
        Class<?> typeClass   = Class.forName("net.minecraft.network.protocol.game.ClientboundGameEventPacket$Type");

        Field demoField = packetClass.getDeclaredField("DEMO_EVENT");
        demoField.setAccessible(true);
        Object demoEvent = demoField.get(null);

        Constructor<?> ctor = packetClass.getConstructor(typeClass, float.class);
        Object packet = ctor.newInstance(demoEvent, 0.0f);

        Field connField = findField(handle.getClass(), "connection");
        Object connection = connField.get(handle);

        Class<?> packetInterface = Class.forName("net.minecraft.network.protocol.Packet");
        connection.getClass().getMethod("send", packetInterface).invoke(connection, packet);
    }

    /**
     * 1.8–1.16 (versioned NMS package):
     * PacketPlayOutGameStateChange(int reason, float value)  reason=5 → Demo event, value=0.0f
     * EntityPlayer.playerConnection → PlayerConnection.sendPacket(Packet)
     */
    private void sendLegacyPacket(Object handle, String nmsVersion) throws Exception {
        String pkg = "net.minecraft.server." + nmsVersion + ".";

        Class<?> packetClass = Class.forName(pkg + "PacketPlayOutGameStateChange");
        Object packet = packetClass.getConstructor(int.class, float.class).newInstance(5, 0.0f);

        Field connField = findField(handle.getClass(), "playerConnection");
        Object connection = connField.get(handle);

        Class<?> packetInterface = Class.forName(pkg + "Packet");
        connection.getClass().getMethod("sendPacket", packetInterface).invoke(connection, packet);
    }

    /** Walk the class hierarchy to find a field by name, setting it accessible. */
    private Field findField(Class<?> clazz, String name) throws NoSuchFieldException {
        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            try {
                Field f = c.getDeclaredField(name);
                f.setAccessible(true);
                return f;
            } catch (NoSuchFieldException ignored) {
            }
        }
        throw new NoSuchFieldException(name + " not found in class hierarchy of " + clazz.getName());
    }
}
