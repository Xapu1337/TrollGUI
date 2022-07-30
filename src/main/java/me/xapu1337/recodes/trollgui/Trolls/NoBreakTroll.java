package me.xapu1337.recodes.trollgui.Trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Cores.TrollCore;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Types.TrollItemMetaData;
import me.xapu1337.recodes.trollgui.Utilities.Singleton;
import me.xapu1337.recodes.trollgui.Utilities.Utilities;

public class NoBreakTroll extends TrollHandler {

    @Override
    public TrollItemMetaData setMetaData() {
        return (
                new TrollItemMetaData()
                        .setItem(XMaterial.STONE)
                        .setConfigData("noBreak")
                        .setToggable(true)
                        .setToggled(
                                () -> Singleton.getSingleInstance().noBreakPlayers.containsKey(Utilities.getSingleInstance().uuidOrName(victim, TrollCore.instance.usingUUID))
                        )

        );
    }


    @Override
    public void execute() {
        Utilities.getSingleInstance().addOrRemove(Singleton.getSingleInstance().noBreakPlayers, victim);
    }
}
