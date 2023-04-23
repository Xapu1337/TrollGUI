package me.xapu1337.recodes.trollgui.trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollMetaData;


public class NoDropTroll extends Troll {


    @Override
    public TrollMetaData setMetaData() {
        return (
                new TrollMetaData(XMaterial.DROPPER)
                        .setTrollName("noDrop")

        );
    }


    @Override

    public void execute() { this.toggleTroll(getVictim()); }
}
