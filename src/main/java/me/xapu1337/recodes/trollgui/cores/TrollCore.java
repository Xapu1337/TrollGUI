package me.xapu1337.recodes.trollgui.cores;

import com.google.gson.Gson;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIConfig;
import me.xapu1337.recodes.trollgui.commands.TrollCommand;
import me.xapu1337.recodes.trollgui.loaders.TrollLoader;
import me.xapu1337.recodes.trollgui.managers.EventManager;
import me.xapu1337.recodes.trollgui.utilities.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class TrollCore extends JavaPlugin implements Listener {

    private static TrollCore instance;
    private EventManager eventManager = new EventManager(this);

    public TrollCore() {
        if (instance == null)
            instance = this;
    }

    public static TrollCore getInstance() {
        return instance;
    }


    @Override
    public void onEnable() {
        super.onEnable();

        TrollLoader.getInstance().refreshTrolls();

        CommandAPI.onEnable(this);

        new TrollCommand();


        eventManager.registerEvent(PlayerMoveEvent.class, (event) -> {
            DebuggingUtil.getInstance().l("MoveEvent");
            Player player = event.getPlayer();
            if (TrollToggablesStorage.getInstance().hasToggle(player.getUniqueId(), "freezePlayer")) {
                event.setCancelled(true);
            }
        });

        eventManager.registerEvent(BlockBreakEvent.class, (event) -> {
            DebuggingUtil.getInstance().l("BlockBreakEvent");
            Player player = event.getPlayer();
            if (TrollToggablesStorage.getInstance().hasToggle(player.getUniqueId(), "noBreak")) {
                event.setCancelled(true);
            }
        });

        eventManager.registerEvent(BlockPlaceEvent.class, (event) -> {
            DebuggingUtil.getInstance().l("BlockPlaceEvent");
            Player player = event.getPlayer();
            if (TrollToggablesStorage.getInstance().hasToggle(player.getUniqueId(), "noPlace")) {
                event.setCancelled(true);
            }
        });

        eventManager.registerEvent(PlayerDropItemEvent.class, (event) -> {
            DebuggingUtil.getInstance().l("DropEvent");
            Player player = event.getPlayer();
            if (TrollToggablesStorage.getInstance().hasToggle(player.getUniqueId(), "noDrop")) {
                event.setCancelled(true);
            }
        });

        eventManager.registerEvent(AsyncPlayerChatEvent.class, (event) -> {
            DebuggingUtil.getInstance().l("ChatEvent");
            Player player = event.getPlayer();
            MessageCollector collector = MessageCollector.getCollector(player);
            if (collector != null) {
                collector.collect(event.getMessage());
                event.setCancelled(true);
            }

            if (TrollToggablesStorage.getInstance().hasToggle(player.getUniqueId(), "reverseMessage")) {
                event.setMessage(Utils.getInstance().reverseMessage(event.getMessage()));
            }
        }, EventPriority.HIGH);






    }

    @Override
    public void onDisable() {
        super.onDisable();

        CommandAPI.onDisable();
        eventManager = null;
    }

    @Override
    public void onLoad() {
        super.onLoad();

        CommandAPI.onLoad(new CommandAPIConfig().silentLogs(true));
    }
}
