package me.xapu1337.recodes.trollgui.Utilities;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.function.BiConsumer;




public class MessageCollector {

    /**
     * Singleton part of the class.
     */

    private volatile static MessageCollector instance;
    private MessageCollector() {}

    public static MessageCollector getSingleInstance() {
        if (instance == null) {
            synchronized (MessageCollector.class) {
                if (instance == null) {
                    instance = new MessageCollector();
                }
            }
        }
        return instance;
    }


    private final IndexableMap< Player, BiConsumer< Player, String > > _handlers = new IndexableMap<>();

    public void registerHandler(Player player, BiConsumer< Player, String > handler) {
        this._handlers.put(player, handler);
    }

    public HashMap< Player, BiConsumer< Player, String > > getHandlers() {

        return _handlers;
    }


}
