package me.xapu1337.recodes.trollgui.utilities;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TrollVariableStorage {

    private static final Map<UUID, Map<String, Object>> storage = new ConcurrentHashMap<>();
    private static final Map<String, Object> permanentStorage = new ConcurrentHashMap<>();

    private static final ScheduledExecutorService cleanupScheduler = Executors.newSingleThreadScheduledExecutor();

    static {
        cleanupScheduler.scheduleWithFixedDelay(TrollVariableStorage::cleanupExpiredVariables, 30, 30, TimeUnit.MINUTES);
    }

    public static void setVariable(UUID playerId, String key, Object value) {
        Map<String, Object> playerVariables = storage.computeIfAbsent(playerId, k -> new ConcurrentHashMap<>());
        playerVariables.put(key, value instanceof PermanentValue ? ((PermanentValue) value).getValue() : new CachedValue(value, System.currentTimeMillis()));
    }

    public static void setPermanentVariable(String key, Object value) {
        permanentStorage.put(key, value);
    }

    public static Object getVariable(UUID playerId, String key) {
        Map<String, Object> playerVariables = storage.get(playerId);
        if (playerVariables == null) {
            return null;
        }

        Object value = playerVariables.get(key);
        if (value instanceof CachedValue && ((CachedValue) value).isExpired()) {
            playerVariables.remove(key);
            return null;
        }

        return value instanceof CachedValue ? ((CachedValue) value).getValue() : value;
    }

    public static Object getPermanentVariable(String key) {
        return permanentStorage.get(key);
    }

    public static void removeVariable(UUID playerId, String key) {
        Map<String, Object> playerVariables = storage.get(playerId);
        if (playerVariables != null) {
            playerVariables.remove(key);
        }
    }

    public static void removePermanentVariable(String key) {
        permanentStorage.remove(key);
    }

    private static void cleanupExpiredVariables() {
        long expirationTime = System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(30);
        storage.values().forEach(playerVariables -> playerVariables.entrySet().removeIf(entry -> entry.getValue() instanceof CachedValue && ((CachedValue) entry.getValue()).isExpired()));
    }

    private static class CachedValue {

        private final Object value;
        private final long creationTime;

        public CachedValue(Object value, long creationTime) {
            this.value = value;
            this.creationTime = creationTime;
        }

        public Object getValue() {
            return value;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() - creationTime >= TimeUnit.MINUTES.toMillis(30);
        }
    }

    private static class PermanentValue {

        private final Object value;

        public PermanentValue(Object value) {
            this.value = value;
        }

        public Object getValue() {
            return value;
        }
    }
}
