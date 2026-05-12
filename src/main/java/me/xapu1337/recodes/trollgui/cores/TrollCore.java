package me.xapu1337.recodes.trollgui.cores;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIConfig;
import me.xapu1337.recodes.trollgui.commands.TrollCommand;
import me.xapu1337.recodes.trollgui.loaders.TrollLoader;
import me.xapu1337.recodes.trollgui.managers.EventManager;
import me.xapu1337.recodes.trollgui.utilities.*;
import org.bukkit.entity.Player;
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
    private EventManager eventManager = new EventManager(this);

    private Services services;

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

        MessageUtils messages   = new MessageUtils();
        DebuggingUtil debug     = new DebuggingUtil(messages);
        Utils utils             = new Utils();

        TrollToggablesStorage toggles   = new TrollToggablesStorage(debug);
        TrollLoader           trollLoader = new TrollLoader(debug);
        services = new Services(debug, messages, utils, toggles, trollLoader);
        trollLoader.refreshTrolls(services);

        CommandAPI.onEnable(this);

        new TrollCommand(services);


        eventManager.registerEvent(PlayerMoveEvent.class, (event) -> {
            services.debug().l("MoveEvent");
            Player player = event.getPlayer();
            if (services.toggles().hasToggle(player.getUniqueId(), "freezePlayer")) {
                event.setCancelled(true);
            }
        });

        eventManager.registerEvent(BlockBreakEvent.class, (event) -> {
            services.debug().l("BlockBreakEvent");
            Player player = event.getPlayer();
            if (services.toggles().hasToggle(player.getUniqueId(), "noBreak")) {
                event.setCancelled(true);
            }
        });

        eventManager.registerEvent(BlockPlaceEvent.class, (event) -> {
            services.debug().l("BlockPlaceEvent");
            Player player = event.getPlayer();
            if (services.toggles().hasToggle(player.getUniqueId(), "noBuild")) {
                event.setCancelled(true);
            }
        });

        eventManager.registerEvent(PlayerDropItemEvent.class, (event) -> {
            services.debug().l("DropEvent");
            Player player = event.getPlayer();
            if (services.toggles().hasToggle(player.getUniqueId(), "noDrop")) {
                event.setCancelled(true);
            }
        });

        eventManager.registerEvent(AsyncPlayerChatEvent.class, (event) -> {
            services.debug().l("ChatEvent");
            Player player = event.getPlayer();
            MessageCollector collector = MessageCollector.getCollector(player);
            if (collector != null) {
                collector.collect(event.getMessage());
                event.setCancelled(true);
            }

            if (services.toggles().hasToggle(player.getUniqueId(), "reverseMessage")) {
                event.setMessage(services.utils().reverseMessage(event.getMessage()));
            }
        }, EventPriority.HIGH);

    }

    @Override
    public void onDisable() {
        super.onDisable();

        TrollVariableStorage.shutdown();
        CommandAPI.onDisable();
        eventManager = null;
    }

    @Override
    public void onLoad() {
        super.onLoad();

        CommandAPI.onLoad(new CommandAPIConfig().silentLogs(true));
    }
}
