package me.xapu1337.recodes.trollgui.Listeners;

import me.xapu1337.recodes.trollgui.Cores.Core;
import me.xapu1337.recodes.trollgui.Inventorys.TrollGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

public class EventListener implements Listener {

    @EventHandler
    public void OnMoveEvent(PlayerMoveEvent playerMoveEvent) {
        Player player = playerMoveEvent.getPlayer();
        if(Core.instance.singletons.frozenPlayers.containsKey(Core.instance.utils.uuidOrName(player, Core.instance.usingUUID)))
            playerMoveEvent.setCancelled(true);
    }

    @EventHandler
    public void OnBuildEvent(BlockPlaceEvent blockPlaceEvent) {
        if(Core.instance.singletons.noBuildPlayers.containsKey(Core.instance.utils.uuidOrName(blockPlaceEvent.getPlayer(), Core.instance.usingUUID)))
            blockPlaceEvent.setCancelled(true);
    }

    @EventHandler
    public void OnBreakEvent(BlockBreakEvent blockBreakEvent) {
        if(Core.instance.singletons.noBreakPlayers.containsKey(Core.instance.utils.uuidOrName(blockBreakEvent.getPlayer(), Core.instance.usingUUID)))
            blockBreakEvent.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onChat(@NotNull AsyncPlayerChatEvent e){
        Player p = e.getPlayer();
        if (Core.instance.singletons.reverseMessagePlayers.containsKey(Core.instance.utils.uuidOrName(p, Core.instance.usingUUID))) {
            e.setMessage(Core.instance.utils.reverseMessage(e.getMessage()));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {

        if (Core.instance.singletons.currentPlayersTrolling.size() > 0) {
            // Find the current TrollGUI with the player that left as a victim.
            TrollGUI currentTrollGUI = Core.instance.singletons.currentPlayersTrolling.values().stream()
                    .filter(trollGUI -> trollGUI.getVictim().getUniqueId().equals(e.getPlayer().getUniqueId()))
                    .findFirst()
                    .orElse(null);

            if (currentTrollGUI == null) return;

            currentTrollGUI.getCaller().closeInventory();
            currentTrollGUI.getCaller().sendMessage(Core.instance.utils.getConfigPath("Messages.playerNotAvailable", true).replaceAll("%PLAYER%", e.getPlayer().getName()));
        }

    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (Core.instance.singletons.currentPlayersTrolling.values().stream().map(TrollGUI::getInventory).collect(Collectors.toList()).contains(e.getInventory())) {
            Core.instance.singletons.currentPlayersTrolling.remove(Core.instance.singletons.currentPlayersTrolling.values().stream().filter(trollGUI -> trollGUI.getInventory() == e.getInventory()).collect(Collectors.toList()).stream().findFirst().get());
        }
    }


}
