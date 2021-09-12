package me.xapu1337.recodes.trollgui.Trolls;

import me.xapu1337.recodes.trollgui.Cores.Core;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import org.bukkit.entity.Player;

public class ReverseMessageTroll extends TrollHandler {

    public ReverseMessageTroll(Player caller, Player victim) {
        super(caller, victim);
    }

    @Override
    public void execute() {
        Core.instance.utils.addOrRemove(Core.instance.singletons.reverseMessagePlayers, victim);
    }
}
