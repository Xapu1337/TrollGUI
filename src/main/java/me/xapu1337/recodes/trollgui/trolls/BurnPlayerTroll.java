package me.xapu1337.recodes.trollgui.trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.cores.TrollCore;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollAttributes;
import me.xapu1337.recodes.trollgui.types.TrollMetaData;

public class BurnPlayerTroll extends Troll {


    @Override
    public TrollMetaData setMetaData() {
        return
                new TrollMetaData(XMaterial.BLAZE_POWDER)
                        .setTrollName("burnPlayer")
                        .setAttributes( TrollAttributes.POSSIBLE_DEATH_OR_ITEM_LOSS );

    }

    @Override
    public void execute() {
        getCaller().setFireTicks(TrollCore.getInstance().getConfig().getInt("menus.troll-menu.trolls.items.burnPlayer.options.burnTime") * 20);
    }
}
