package me.xapu1337.recodes.trollgui.trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollAttributes;
import me.xapu1337.recodes.trollgui.types.TrollMetaData;


public class FakeTimeoutTroll extends Troll {

    @Override
    public TrollMetaData setMetaData() {
        return (
                new TrollMetaData(XMaterial.LEAD)
                        .setTrollName("fakeTimeout")
                        .setAttributes( TrollAttributes.POSSIBLE_KICK )

        );
    }


    @Override
    public void execute() {
        getVictim().kickPlayer("java.net.ConnectException: Connection timed out: no further information:");
    }
}
