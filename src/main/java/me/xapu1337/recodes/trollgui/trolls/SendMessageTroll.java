package me.xapu1337.recodes.trollgui.trolls;

import me.xapu1337.recodes.trollgui.cores.TrollCore;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollName;
import me.xapu1337.recodes.trollgui.utilities.MessageCollector;

@TrollName("sendMessage")
public class SendMessageTroll extends Troll {

    /**
     * @return
     */

    @Override
    public void execute() {
        getCaller().sendMessage(" \n ");
        services.messages().setClassPlaceholders(this.getClass(), "victim.name", getVictim().getName());
        getCaller().sendMessage(services.messages().$("{config:messages.sendMessage}"));
        getCaller().sendMessage(" \n ");
        new MessageCollector(getVictim(), TrollCore.getInstance(), (reply) -> {
            services.debug().l("MessageCollector", "Message received: " + reply);
            getVictim().chat(reply);
        });
    }

}
