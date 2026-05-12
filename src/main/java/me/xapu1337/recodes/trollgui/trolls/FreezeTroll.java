package me.xapu1337.recodes.trollgui.trolls;

import com.cryptomorin.xseries.XMaterial;

import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollAttributes;
import me.xapu1337.recodes.trollgui.types.TrollMetaData;

public class FreezeTroll extends Troll {

    @Override
    public TrollMetaData setMetaData() {
        return (new TrollMetaData(XMaterial.SNOWBALL, services)
                .setTrollName("freezePlayer")
                .setAttributes(TrollAttributes.POSSIBLE_CRASH_OR_FREEZE)

        );
    }

    @Override
    public void execute() {
        this.toggleTroll(getVictim());
    }
}
