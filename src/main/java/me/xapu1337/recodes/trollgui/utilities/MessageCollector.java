package me.xapu1337.recodes.trollgui.utilities;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class MessageCollector {

    private static final Map<UUID, MessageCollector> collectors = new HashMap<>();

    private final Player player;
    private final Plugin plugin;
    private final Consumer<String> consumer;
    private final BukkitRunnable timeoutTask;
    private String collectedMessage;

    public MessageCollector(Player player, Plugin plugin, Consumer<String> consumer) {
        this.player = player;
        this.plugin = plugin;
        this.consumer = consumer;

        timeoutTask = new BukkitRunnable() {
            @Override
            public void run() {
                end();
            }
        };
        timeoutTask.runTaskLater(plugin, 6000);
        collectors.put(player.getUniqueId(), this);
    }

    public void collect(String message) {
        if (message.trim().isEmpty() || message.equalsIgnoreCase("close")) {
            end();
            return;
        }
        collectedMessage = message;
    }

    private void end() {
        timeoutTask.cancel();
        collectors.remove(player.getUniqueId());
        consumer.accept(collectedMessage);
    }

    public static MessageCollector getCollector(Player player) {
        return collectors.get(player.getUniqueId());
    }

    public static void clearCollector(Player player) {
        MessageCollector collector = collectors.remove(player.getUniqueId());
        if (collector != null) {
            collector.timeoutTask.cancel();
        }
    }
}
