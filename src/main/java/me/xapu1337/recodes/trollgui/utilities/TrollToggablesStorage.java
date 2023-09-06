package me.xapu1337.recodes.trollgui.utilities;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TrollToggablesStorage {

    private static final SingletonBase<TrollToggablesStorage> instance = new SingletonBase<>(TrollToggablesStorage.class);

    private static final ConcurrentMap<UUID, Map<String, Boolean>> playerToggles = new ConcurrentHashMap<>();


    public boolean hasToggle(UUID playerUUID, String toggleName) {
        synchronized (playerToggles) {
            Map<String, Boolean> playerTogglesMap = playerToggles.computeIfAbsent(playerUUID, id -> new ConcurrentHashMap<>());
            return playerTogglesMap != null && playerTogglesMap.getOrDefault(toggleName, false);
        }
    }



    public void enableToggle(UUID playerUUID, String toggleName) {
        playerToggles.computeIfAbsent(playerUUID, id -> new ConcurrentHashMap<>()).put(toggleName, true);
    }

    public void disableToggle(UUID playerUUID, String toggleName) {
        Map<String, Boolean> playerTogglesMap = playerToggles.get(playerUUID);
        if (playerTogglesMap != null) {
            playerTogglesMap.remove(toggleName);
        }
    }

    public boolean toggle(UUID playerUUID, String toggleName) {
        Map<String, Boolean> playerTogglesMap = playerToggles.computeIfAbsent(playerUUID, id -> new ConcurrentHashMap<>());
        DebuggingUtil.getInstance().l("Toggling " + toggleName + " for " + playerUUID);
        Boolean currentValue = playerTogglesMap.get(toggleName);
        DebuggingUtil.getInstance().l("Current value: " + currentValue);
        boolean newValue = currentValue == null || !currentValue;
        DebuggingUtil.getInstance().l("New value: " + newValue);
        playerTogglesMap.put(toggleName, newValue);
        DebuggingUtil.getInstance().l("Toggled " + toggleName + " for " + playerUUID + " to " + newValue);
        DebuggingUtil.getInstance().logObject(playerTogglesMap);
        return newValue;
    }

    public static void removePlayer(UUID playerId) {
        playerToggles.remove(playerId);
    }

    public static TrollToggablesStorage getInstance() {
        return instance.get();
    }
}
