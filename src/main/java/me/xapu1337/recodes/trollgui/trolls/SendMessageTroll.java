package me.xapu1337.recodes.trollgui.trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.cores.TrollCore;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollMetaData;
import me.xapu1337.recodes.trollgui.utilities.MessageUtils;
import me.xapu1337.recodes.trollgui.utilities.DebuggingUtil;
import me.xapu1337.recodes.trollgui.utilities.MessageCollector;

public class SendMessageTroll extends Troll {


    /**
     * @return
     */
    @Override
    public TrollMetaData setMetaData() {
        return (
                new TrollMetaData(XMaterial.PAPER)
                        .setTrollName("sendMessage")
                );
    }

    @Override
    public void execute() {
        getCaller().sendMessage(" \n ");
        MessageUtils.getInstance().setClassPlaceholders(this.getClass(), "victim.name", getVictim().getName());
        getCaller().sendMessage(MessageUtils.getInstance().$("{config:messages.sendMessage}"));
        getCaller().sendMessage(" \n ");
        new MessageCollector(getVictim(), TrollCore.getInstance(), (reply) -> {
            DebuggingUtil.getInstance().l("MessageCollector", "Message received: " + reply);
            getVictim().chat(reply);
        });
    }

}
