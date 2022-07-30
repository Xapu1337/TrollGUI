package me.xapu1337.recodes.trollgui.Trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Cores.TrollCore;
import me.xapu1337.recodes.trollgui.Enums.TrollAttributes;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Types.TrollItemMetaData;

public class BurnPlayerTroll extends TrollHandler {


    @Override
    public TrollItemMetaData setMetaData() {
        return
                new TrollItemMetaData()
                        .setItem(XMaterial.BLAZE_POWDER)
                        .setConfigData("burnPlayer")
                        .setAttributes( TrollAttributes.POSSIBLE_DEATH_OR_ITEM_LOSS);

    }

    @Override
    public void execute() {
        victim.setFireTicks(TrollCore.instance.getConfig().getInt("MenuItems.trollMenu.burnPlayer.options.burnTime") * 20);
    }
}
