package me.xapu1337.recodes.trollgui.trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollMetaData;


public class NoBreakTroll extends Troll {

    @Override
    public TrollMetaData setMetaData() {
        return (
                new TrollMetaData(XMaterial.STONE)
                        .setTrollName("noBreak")

        );
    }


    @Override

    public void execute() { this.toggleTroll(getVictim()); }
}
