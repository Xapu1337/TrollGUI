package me.xapu1337.recodes.trollgui.Trolls;

import me.xapu1337.recodes.trollgui.Cores.Core;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import org.bukkit.entity.Player;

public class NoBuildTroll extends TrollHandler {

    public NoBuildTroll(Player caller, Player victim) {
        super(caller, victim);
    }

    @Override
    public void execute() {
        Core.instance.utils.addOrRemove(Core.instance.singletons.noBuildPlayers, victim);
    }
}
