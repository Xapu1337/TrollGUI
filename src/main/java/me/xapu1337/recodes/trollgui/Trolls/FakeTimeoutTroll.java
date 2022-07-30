package me.xapu1337.recodes.trollgui.Trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Enums.TrollAttributes;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Types.TrollItemMetaData;

public class FakeTimeoutTroll extends TrollHandler {

    @Override
    public TrollItemMetaData setMetaData() {
        return (
                new TrollItemMetaData()
                        .setItem(XMaterial.LEAD)
                        .setConfigData("fakeTimeout")
                        .setAttributes( TrollAttributes.POSSIBLE_KICK )

        );
    }


    @Override
    public void execute() {
        victim.kickPlayer("java.net.ConnectException: Connection timed out: no further information:");
    }
}
