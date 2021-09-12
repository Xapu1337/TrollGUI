package me.xapu1337.recodes.trollgui.Trolls;

import me.xapu1337.recodes.trollgui.Cores.Core;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import org.bukkit.entity.Player;

public class InvShareTroll extends TrollHandler {

    public InvShareTroll(Player caller, Player victim) {
        super(caller, victim);
    }

    @Override
    public void execute() {
        Core.instance.getServer().getOnlinePlayers().forEach((player -> {if(player != victim) player.openInventory(victim.getInventory()); }));
    }
}
