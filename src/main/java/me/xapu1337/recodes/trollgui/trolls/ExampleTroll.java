package me.xapu1337.recodes.trollgui.trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollMetaData;

public class ExampleTroll extends Troll {

    @Override
    public TrollMetaData setMetaData() {
        return new TrollMetaData(XMaterial.COMMAND_BLOCK)
                .loadConfigData("debug");
    }

    @Override
    public void execute() {
        // do something
        getCaller().sendMessage("Hello World!");
        getVictim().sendMessage("Hello Fuck You!");
    };
}
