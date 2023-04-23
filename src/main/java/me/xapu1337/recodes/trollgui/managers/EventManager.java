package me.xapu1337.recodes.trollgui.managers;

import org.bukkit.Bukkit;
import org.bukkit.event.*;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class EventManager implements Listener {
    private final Plugin plugin;
    private final Map<Class<? extends Event>, Consumer<? extends Event>> eventHandlers;

    public EventManager(Plugin plugin) {
        this.plugin = plugin;
        eventHandlers = new HashMap<>();
    }

    public <T extends Event> void registerEvent(Class<T> eventType, Consumer<T> eventHandler) {
        eventHandlers.put(eventType, eventHandler);
        Bukkit.getPluginManager().registerEvent(eventType, this, org.bukkit.event.EventPriority.NORMAL, (listener, event) -> {
            if (eventType.isAssignableFrom(event.getClass())) {
                Consumer<T> handler = (Consumer<T>) eventHandlers.get(event.getClass());
                if (handler != null) {
                    handler.accept((T) event);
                }
            }
        }, plugin, false);
    }

    public <T extends Event> void registerEvent(Class<T> eventType, Consumer<T> eventHandler, EventPriority priority) {

        eventHandlers.put(eventType, eventHandler);
        Bukkit.getPluginManager().registerEvent(eventType, this, priority, (listener, event) -> {
            if (eventType.isAssignableFrom(event.getClass())) {
                Consumer<T> handler = (Consumer<T>) eventHandlers.get(event.getClass());
                if (handler != null) {
                    handler.accept((T) event);
                }
            }
        }, plugin, false);
    }

    @EventHandler
    public void onPluginDisable(org.bukkit.event.server.PluginDisableEvent event) {
        if (event.getPlugin().equals(plugin)) {
            eventHandlers.clear();
        }
    }

}
