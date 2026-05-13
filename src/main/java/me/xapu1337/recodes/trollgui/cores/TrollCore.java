package me.xapu1337.recodes.trollgui.cores;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIPaperConfig;
import me.xapu1337.recodes.trollgui.commands.TrollCommand;
import me.xapu1337.recodes.trollgui.loaders.TrollLoader;
import me.xapu1337.recodes.trollgui.utilities.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class TrollCore extends JavaPlugin implements Listener {

    private static TrollCore instance;

    private Services services;

    public TrollCore() {
        if (instance == null)
            instance = this;
    }

    public static TrollCore getInstance() {
        return instance;
    }

    public static Services getServices() {
        return instance.services;
    }

    @Override
    public void onEnable() {
        super.onEnable();

        saveDefaultConfig();

        MessageUtils messages = new MessageUtils();
        DebuggingUtil debug = new DebuggingUtil(messages, getConfig());
        Utils utils = new Utils();

        TrollToggablesStorage toggles = new TrollToggablesStorage(debug);
        TrollLoader trollLoader = new TrollLoader(debug);
        services = new Services(debug, messages, utils, toggles, trollLoader, getConfig());
        trollLoader.refreshTrolls(services);

        CommandAPI.onEnable();

        new TrollCommand(services);

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        TrollVariableStorage.shutdown();
        CommandAPI.onDisable();
    }

    @Override
    public void onLoad() {
        super.onLoad();

        CommandAPI.onLoad(new CommandAPIPaperConfig(this).silentLogs(true));
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (services.toggles().hasToggle(player.getUniqueId(), "freezePlayer")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (services.toggles().hasToggle(player.getUniqueId(), "noBreak")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (services.toggles().hasToggle(player.getUniqueId(), "noBuild")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (services.toggles().hasToggle(player.getUniqueId(), "noDrop")) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        MessageCollector collector = MessageCollector.getCollector(player);
        if (collector != null) {
            collector.collect(event.getMessage());
            event.setCancelled(true);
        }
        if (services.toggles().hasToggle(player.getUniqueId(), "reverseMessage")) {
            event.setMessage(services.utils().reverseMessage(event.getMessage()));
        }
    }
}
