package me.xapu1337.recodes.trollgui.trolls;

import com.cryptomorin.xseries.XMaterial;


import me.xapu1337.recodes.trollgui.cores.TrollCore;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollAttributes;
import me.xapu1337.recodes.trollgui.types.TrollMetaData;

public class InvShareTroll extends Troll {

    @Override
    public TrollMetaData setMetaData() {
        return (
                new TrollMetaData(XMaterial.ENDER_CHEST)
                        .setTrollName("invShare")
                        .setAttributes( TrollAttributes.POSSIBLE_DEATH_OR_ITEM_LOSS )

        );
    }


    @Override
    public void execute() {
        TrollCore.getInstance().getServer().getOnlinePlayers().forEach((player -> {
            if(player != getVictim()) player.openInventory(getVictim().getInventory());
        }));
    }
}
