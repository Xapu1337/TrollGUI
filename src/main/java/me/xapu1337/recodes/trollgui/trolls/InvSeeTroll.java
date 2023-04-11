package me.xapu1337.recodes.trollgui.trolls;

import com.cryptomorin.xseries.XMaterial;

import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollAttributes;
import me.xapu1337.recodes.trollgui.types.TrollMetaData;


public class InvSeeTroll extends Troll {

    @Override
    public TrollMetaData setMetaData() {
        return (
                new TrollMetaData(XMaterial.TRAPPED_CHEST)
                        .setTrollName("invSee")
                        .setAttributes( TrollAttributes.POSSIBLE_DEATH_OR_ITEM_LOSS )

        );
    }


    @Override
    public void execute() {
        getCaller().openInventory(getVictim().getInventory());
    }
}
