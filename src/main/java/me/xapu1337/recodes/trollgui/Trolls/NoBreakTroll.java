package me.xapu1337.recodes.trollgui.Trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Cores.Core;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Handlers.TrollItemMetaData;
import org.bukkit.entity.Player;

public class NoBreakTroll extends TrollHandler {

    @Override
    public TrollItemMetaData setMetaData() {
        return (
                new TrollItemMetaData()
                        .setItem(XMaterial.STONE)
                        .setConfigData("noBreak")
                        .setToggable(true)
                        .setToggled(
                                () -> Core.instance.singletons.noBreakPlayers.containsKey(Core.instance.utils.uuidOrName(victim, Core.instance.usingUUID))
                        )

        );
    }


    @Override
    public void execute() {
        Core.instance.utils.addOrRemove(Core.instance.singletons.noBreakPlayers, victim);
    }
}
