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

public class DynamicCache<T> {

    private final ConcurrentMap<UUID, T> cacheMap;
    private final Map<String, T> tempCacheMap;
    private final Map<String, Long> tempCacheExpireTimes;
    private final long tempCacheDefaultExpireTime;
    private final int maxCacheSize;

    private boolean refreshing;
    private int taskId = -1;
    private long lastRefreshTime = 0;

    // Caching
    private static final int DEFAULT_MAX_CACHE_SIZE = 100;
    private final LinkedHashMap<UUID, T> cache = new LinkedHashMap<UUID, T>(DEFAULT_MAX_CACHE_SIZE, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<UUID, T> eldest) {
            return size() > maxCacheSize;
        }
    };

    public DynamicCache(int maxCacheSize) {
        cacheMap = new ConcurrentHashMap<>();
        tempCacheMap = new HashMap<>();
        tempCacheExpireTimes = new HashMap<>();
        tempCacheDefaultExpireTime = TimeUnit.MINUTES.toSeconds(30); // 30 minutes by default
        this.maxCacheSize = maxCacheSize;
    }

    public DynamicCache() {
        this(DEFAULT_MAX_CACHE_SIZE);
    }

    public void set(UUID uuid, T value) {
        cacheMap.put(uuid, value);
        startRefreshTaskIfNotRunning();
    }

    public void set(String key, T value) {
        set(key, value, tempCacheDefaultExpireTime, TimeUnit.SECONDS);
    }

    public void set(String key, T value, long expireTime, TimeUnit timeUnit) {
        tempCacheMap.put(key, value);
        tempCacheExpireTimes.put(key, System.currentTimeMillis() + timeUnit.toMillis(expireTime));
        startRefreshTaskIfNotRunning();
    }

    public T get(UUID uuid) {
        T cachedValue = cache.get(uuid);
        if (cachedValue != null) {
            return cachedValue;
        }
        return cacheMap.get(uuid);
    }

    public T get(String key) {
        if (isTempCacheExpired(key)) {
            removeTempCache(key);
            return null;
        }
        return tempCacheMap.get(key);
    }

    public void remove(UUID uuid) {
        cacheMap.remove(uuid);
        stopRefreshTaskIfNoValues();
    }

    public void remove(String key) {
        tempCacheMap.remove(key);
        tempCacheExpireTimes.remove(key);
        stopRefreshTaskIfNoValues();
    }

    private void startRefreshTaskIfNotRunning() {
        if (!refreshing) {
            refreshing = true;
            taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(TrollCore.getInstance(), this::refresh, 0L, 20L);
        }
    }

    private void stopRefreshTaskIfNoValues() {
        if (cacheMap.isEmpty() && tempCacheMap.isEmpty() && taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
            refreshing = false;
            taskId = -1;
        }
    }

    private boolean isTempCacheExpired(String key) {
        Long expireTime = tempCacheExpireTimes.get(key);
        return expireTime != null && System.currentTimeMillis() >= expireTime;
    }

    private void removeTempCache(String key) {
        tempCacheMap.remove(key);
        tempCacheExpireTimes.remove(key);
        stopRefreshTaskIfNoValues();
    }

    private void refresh() {
        long currentTime = System.currentTimeMillis();

        // Clean up expired temp cache
        tempCacheExpireTimes.entrySet().removeIf(entry -> entry.getValue() < currentTime);
        tempCacheMap.entrySet().removeIf(entry -> !tempCacheExpireTimes.containsKey(entry.getKey()));

        // Merge temp cache into main cache
        for (Map.Entry<String, T> entry : tempCacheMap.entrySet()) {
            UUID uuid = UUID.nameUUIDFromBytes(entry.getKey().getBytes());
            cache.put(uuid, entry.getValue());
        }

        // Clear temp cache
        tempCacheMap.clear();
        tempCacheExpireTimes.clear();

        // Update last refresh time
        lastRefreshTime = currentTime;

        // Stop refreshing if no more values in cache
        stopRefreshTaskIfNoValues();
    }


    public int size() {
        return cache.size() + cacheMap.size() + tempCacheMap.size();
    }

    public void clear() {
        cache.clear();
        cacheMap.clear();
        tempCacheMap.clear();
        tempCacheExpireTimes.clear();
        stopRefreshTaskIfNoValues();
    }
}
