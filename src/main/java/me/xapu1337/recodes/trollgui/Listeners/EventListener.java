package me.xapu1337.recodes.trollgui.Listeners;

import me.xapu1337.recodes.trollgui.Cores.Core;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

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

}
