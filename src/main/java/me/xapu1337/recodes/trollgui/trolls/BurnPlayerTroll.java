package me.xapu1337.recodes.trollgui.trolls;

import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollName;
import org.bukkit.ChatColor;

@TrollName("burnPlayer")
public class BurnPlayerTroll extends Troll {

    @Override
    public void execute() {
        int ticks = services.config().getInt("menus.troll-menu.items.trolls.burnPlayer.options.burnTime") * 20;
        getVictim().setFireTicks(ticks);
    }
}
