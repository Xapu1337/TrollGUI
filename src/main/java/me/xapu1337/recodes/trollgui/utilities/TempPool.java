package me.xapu1337.recodes.trollgui.utilities;

import me.xapu1337.recodes.trollgui.cores.TrollCore;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
public class TempPool {

    private static final SingletonBase<TempPool> instance = new SingletonBase<>(TempPool.class);

    private final ConcurrentMap<UUID, String> messages;
    private final ConcurrentMap<String, String> specialKeys;
    private final Map<String, Long> specialKeyExpireTimes;
    private final long specialKeyDefaultExpireTime;

    private boolean refreshing;
    private int taskId = -1;
    private long lastRefreshTime = 0;

    // Caching
    private static final int CACHE_SIZE = 100;
    private final LinkedHashMap<UUID, String> cache = new LinkedHashMap<UUID, String>(CACHE_SIZE, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<UUID, String> eldest) {
            return size() > CACHE_SIZE;
        }
    };

    public TempPool() {
        messages = new ConcurrentHashMap<>();
        specialKeys = new ConcurrentHashMap<>();
        specialKeyExpireTimes = new HashMap<>();
        specialKeyDefaultExpireTime = TimeUnit.MINUTES.toSeconds(30); // 30 minutes by default
    }

    public static TempPool getInstance() {
        return instance.get();
    }

    public void setMessage(UUID uuid, String message) {
        messages.put(uuid, message);
        startRefreshTaskIfNotRunning();
    }

    public void removeMessage(UUID uuid) {
        messages.remove(uuid);
        stopRefreshTaskIfNoMessages();
    }

    public String getMessageAndDelete(UUID uuid) {
        String message = getMessage(uuid);
        removeMessage(uuid);
        return message;
    }

    public String getMessage(UUID uuid) {
        String cachedMessage = cache.get(uuid);
        if (cachedMessage != null) {
            return cachedMessage;
        }
        return ConfigUtils.getInstance().$(messages.getOrDefault(uuid, "-- No message found --"));
    }

    public void setSpecialKey(String key, String value, long expireTime, TimeUnit timeUnit) {
        specialKeys.put(key, value);
        specialKeyExpireTimes.put(key, System.currentTimeMillis() + timeUnit.toMillis(expireTime));
    }

    public void setSpecialKey(String key, String value) {
        specialKeys.put(key, value);
        specialKeyExpireTimes.put(key, System.currentTimeMillis() + specialKeyDefaultExpireTime);
    }

    public String getSpecialKey(String key) {
        String value = specialKeys.get(key);
        if (value != null && isSpecialKeyExpired(key)) {
            specialKeys.remove(key);
            specialKeyExpireTimes.remove(key);
            return null;
        }
        return value;
    }

    private boolean isSpecialKeyExpired(String key) {
        Long expireTime = specialKeyExpireTimes.get(key);
        return expireTime != null && System.currentTimeMillis() >= expireTime;
    }

    private void startRefreshTaskIfNotRunning() {
        if (!refreshing) {
            refreshing = true;
            taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(TrollCore.getInstance(), this::refresh, 0L, 20L);
        }
    }

    private void stopRefreshTaskIfNoMessages() {
        if (messages.isEmpty() && taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
            refreshing = false;
            taskId = -1;
        }
    }

    public boolean isExpired(UUID uuid) {
        Long expirationTime = specialKeyExpireTimes.get(uuid.toString());
        if (expirationTime != null) {
            long currentTime = System.currentTimeMillis();
            return expirationTime < currentTime;
        } else {
            return true;
        }
    }



    private void refresh() {
        long currentTime = System.currentTimeMillis();
        long timeSinceLastRefresh = currentTime - lastRefreshTime;

        // Only refresh if a certain amount of time has elapsed since the last refresh
        if (timeSinceLastRefresh >= 1000) { // Refresh every 1 second
            for (Map.Entry<UUID, String> entry : messages.entrySet()) {
                UUID uuid = entry.getKey();
                String message = entry.getValue();
                message = ConfigUtils.getInstance().$(message);
                cache.put(uuid, message);
            }
            specialKeys.entrySet().removeIf(entry -> isSpecialKeyExpired(entry.getKey()));
            specialKeyExpireTimes.entrySet().removeIf(entry -> isSpecialKeyExpired(entry.getKey()));
            cache.entrySet().removeIf(entry -> isExpired(entry.getKey()));
            stopRefreshTaskIfNoMessages();

            lastRefreshTime = currentTime;
        }
    }
}
