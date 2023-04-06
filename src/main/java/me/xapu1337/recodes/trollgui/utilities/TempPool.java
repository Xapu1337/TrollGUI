package me.xapu1337.recodes.trollgui.utilities;

import org.bukkit.plugin.java.JavaPlugin;
import me.xapu1337.recodes.trollgui.utilities.ConfigUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TempPool {
    private final Map<UUID, String> messages;
    private final JavaPlugin plugin;
    private boolean refreshing;
    private int taskId = -1;

    private static final SingletonBase<TempPool> instance = new SingletonBase<>(TempPool.class);

    private TempPool(JavaPlugin plugin) {
        this.plugin = plugin;
        this.messages = new HashMap<>();
    }

    public static TempPool getInstance() {
        return instance.get();
    }

    public void setMessage(UUID uuid, String message) {
        messages.put(uuid, message);
        if (!refreshing) {
            refreshing = true;
            taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this::refresh, 0L, 20L);
        }
    }

    public void removeMessage(UUID uuid) {
        messages.remove(uuid);
        if (messages.isEmpty() && taskId != -1) {
            plugin.getServer().getScheduler().cancelTask(taskId);
            refreshing = false;
            taskId = -1;
        }
    }

    public String getMessage(UUID uuid) {
        return messages.getOrDefault(uuid, "");
    }

    public void refresh() {
        for (Map.Entry<UUID, String> entry : messages.entrySet()) {
            UUID uuid = entry.getKey();
            String message = entry.getValue();
            message = ConfigUtils.hexColor(message);
            messages.put(uuid, message);
        }

        if (messages.isEmpty() && taskId != -1) {
            plugin.getServer().getScheduler().cancelTask(taskId);
            refreshing = false;
            taskId = -1;
        }
    }
}
