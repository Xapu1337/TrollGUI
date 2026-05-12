package me.xapu1337.recodes.trollgui.trolls;


import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollName;

@TrollName("invSee")
public class InvSeeTroll extends Troll {

    @Override
    public void execute() {
        getCaller().openInventory(getVictim().getInventory());
    }
}
