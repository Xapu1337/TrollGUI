package me.xapu1337.recodes.trollgui.Trolls;

import me.xapu1337.recodes.trollgui.Cores.Core;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import org.bukkit.entity.Player;

public class BurnPlayerTroll extends TrollHandler {
    public BurnPlayerTroll(Player caller, Player victim) {
        super(caller, victim);
    }

    @Override
    public void execute() {
        victim.setFireTicks(Core.instance.getConfig().getInt("MenuItems.trollMenu.burnPlayer.options.burnTime") * 20);
    }
}
