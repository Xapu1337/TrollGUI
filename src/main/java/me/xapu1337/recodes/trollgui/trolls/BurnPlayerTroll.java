package me.xapu1337.recodes.trollgui.trolls;

import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollName;

@TrollName("burnPlayer")
public class BurnPlayerTroll extends Troll {

    @Override
    public void execute() {

        getCaller().setFireTicks(
                services.config().getInt("menus.troll-menu.trolls.items.burnPlayer.options.burnTime")
                        * 20);
    }
}
