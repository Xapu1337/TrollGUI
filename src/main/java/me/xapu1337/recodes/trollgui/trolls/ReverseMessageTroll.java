package me.xapu1337.recodes.trollgui.trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollMetaData;


public class ReverseMessageTroll extends Troll {


    @Override
    public TrollMetaData setMetaData() {
        return (
                new TrollMetaData(XMaterial.PAPER)
                        .setTrollName("reverseMessage")

        );
    }


    @Override
    public void execute() { this.toggleTroll(getVictim()); }
}
