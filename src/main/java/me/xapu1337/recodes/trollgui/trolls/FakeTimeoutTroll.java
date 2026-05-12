package me.xapu1337.recodes.trollgui.trolls;

import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollName;

@TrollName("fakeTimeout")
public class FakeTimeoutTroll extends Troll {

    @Override
    public void execute() {
        getVictim().kickPlayer("java.net.ConnectException: Connection timed out: no further information:");
    }
}
