package me.xapu1337.recodes.trollgui.utilities;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TrollToggablesStorage {

    private final DebuggingUtil debug;
    private final ConcurrentMap<UUID, Map<String, Boolean>> playerToggles = new ConcurrentHashMap<>();

    public TrollToggablesStorage(DebuggingUtil debug) {
        this.debug = debug;
    }

    public boolean hasToggle(UUID playerUUID, String toggleName) {
        Map<String, Boolean> playerTogglesMap = playerToggles.computeIfAbsent(playerUUID,
                id -> new ConcurrentHashMap<>());
        return playerTogglesMap.getOrDefault(toggleName, false);
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
        Map<String, Boolean> playerTogglesMap = playerToggles.computeIfAbsent(playerUUID,
                id -> new ConcurrentHashMap<>());
        debug.l("Toggling " + toggleName + " for " + playerUUID);
        Boolean currentValue = playerTogglesMap.get(toggleName);
        boolean newValue = currentValue == null || !currentValue;
        playerTogglesMap.put(toggleName, newValue);
        debug.l("Toggle %s: %s → %s", toggleName, currentValue, newValue);
        return newValue;
    }

    public void removePlayer(UUID playerId) {
        playerToggles.remove(playerId);
    }

}
