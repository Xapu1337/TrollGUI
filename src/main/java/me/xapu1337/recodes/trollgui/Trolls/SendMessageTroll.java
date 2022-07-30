package me.xapu1337.recodes.trollgui.Trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Cores.TrollCore;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Types.TrollItemMetaData;
import me.xapu1337.recodes.trollgui.Utilities.MessageCollector;
import me.xapu1337.recodes.trollgui.Utilities.Utilities;
import org.bukkit.Bukkit;

import java.util.concurrent.atomic.AtomicReference;

public class SendMessageTroll extends TrollHandler {


    /**
     * @return
     */
    @Override
    public TrollItemMetaData setMetaData() {
        return (
                new TrollItemMetaData()
                        .setItem(XMaterial.PAPER)
                        .setConfigData("sendMessage")
                );
    }

    @Override
    public void execute() {
        caller.sendMessage(" \n ");
        caller.sendMessage(Utilities.getSingleInstance().getConfigPath("Messages.enterMessage", true).replaceAll("%VICTIM%", victim.getName()));
        caller.sendMessage(" \n ");
        MessageCollector.getSingleInstance()
                .registerHandler(
                        caller,
                        (player, playerReply) -> {
                            Bukkit.getScheduler().runTask(TrollCore.instance, () ->
                                    victim.chat(playerReply));
                        }
                );
    }

}
