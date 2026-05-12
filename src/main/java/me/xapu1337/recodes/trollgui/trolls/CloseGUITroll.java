package me.xapu1337.recodes.trollgui.trolls;

import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollName;

@TrollName("closeGUI")
public class CloseGUITroll extends Troll {

    @Override
    public void execute() {
        getVictim().closeInventory();
    }
}
