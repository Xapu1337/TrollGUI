package me.xapu1337.recodes.trollgui.trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollMetaData;


public class NoBuildTroll extends Troll {


    @Override
    public TrollMetaData setMetaData() {
        return (
                new TrollMetaData(XMaterial.GRASS_BLOCK)
                        .setTrollName("noBuild")

        );
    }


    @Override
    public void execute() { this.toggleTroll(getVictim()); }
}
