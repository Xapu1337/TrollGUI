package me.xapu1337.recodes.trollgui.Listeners;

import me.xapu1337.recodes.trollgui.Cores.TrollCore;
import me.xapu1337.recodes.trollgui.Utilities.MessageCollector;
import me.xapu1337.recodes.trollgui.Inventorys.TrollGUI;
import me.xapu1337.recodes.trollgui.Utilities.Singleton;
import me.xapu1337.recodes.trollgui.Utilities.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

public class EventListener implements Listener {

    @EventHandler
    public void onMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if(Singleton.getSingleInstance().frozenPlayers.containsKey(Utilities.getSingleInstance().uuidOrName(player, TrollCore.instance.usingUUID))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBuildEvent(BlockPlaceEvent event) {
        if(Singleton.getSingleInstance().noBuildPlayers.containsKey(Utilities.getSingleInstance().uuidOrName(event.getPlayer(), TrollCore.instance.usingUUID)))
            event.setCancelled(true);
    }

    @EventHandler
    public void onBreakEvent(BlockBreakEvent event) {
        if(Singleton.getSingleInstance().noBreakPlayers.containsKey(Utilities.getSingleInstance().uuidOrName(event.getPlayer(), TrollCore.instance.usingUUID)))
            event.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onChat(@NotNull AsyncPlayerChatEvent event){
        Player p = event.getPlayer();
        if(MessageCollector.getSingleInstance().getHandlers().containsKey(p)) {
            MessageCollector.getSingleInstance().getHandlers().get(p).accept(p, event.getMessage());
            MessageCollector.getSingleInstance().getHandlers().remove(p);
            event.setCancelled(true);
        }
        if (Singleton.getSingleInstance().reverseMessagePlayers.containsKey(Utilities.getSingleInstance().uuidOrName(p, TrollCore.instance.usingUUID))) {
            event.setMessage(Utilities.getSingleInstance().reverseMessage(event.getMessage()));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        if (Singleton.getSingleInstance().currentPlayersTrolling.size() > 0) {
            // Find the current TrollGUI with the player that left as a victim.
            TrollGUI currentTrollGUI = Singleton.getSingleInstance().currentPlayersTrolling.values().stream()
                    .filter(trollGUI -> trollGUI.getVictim().getUniqueId().equals(event.getPlayer().getUniqueId()))
                    .findFirst()
                    .orElse(null);

            if (currentTrollGUI == null) return;

            currentTrollGUI.getCaller().closeInventory();
            currentTrollGUI.getCaller().sendMessage(Utilities.getSingleInstance().getConfigPath("Messages.playerNotAvailable", true).replaceAll("%PLAYER%", event.getPlayer().getName()));
        }

    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (Singleton.getSingleInstance().currentPlayersTrolling.values().stream().map(TrollGUI::getInventory).toList().contains(event.getInventory())) {
            // Find the current TrollGUI with the player that left as a victim.
            TrollGUI currentTrollGUI = Singleton.getSingleInstance().currentPlayersTrolling.values().stream()
                    .filter(trollGUI -> trollGUI.getInventory().equals(event.getInventory()))
                    .findFirst()
                    .orElse(null);
            Singleton.getSingleInstance().currentPlayersTrolling.remove(event);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (Singleton.getSingleInstance().noDropPlayers.containsKey(Utilities.getSingleInstance().uuidOrName(event.getPlayer(), TrollCore.instance.usingUUID))) {
            event.setCancelled(true);
        }
    }




}
