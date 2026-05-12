package me.xapu1337.recodes.trollgui.trolls;

import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollName;

@TrollName("noBuild")
public class NoBuildTroll extends Troll {

    @Override
    public void execute() {
        this.toggleTroll(getVictim());
    }
}
