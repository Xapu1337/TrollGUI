package me.xapu1337.recodes.trollgui.Trolls;

import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import org.bukkit.entity.Player;

public class TestTroll extends TrollHandler {

    public TestTroll(Player caller, Player victim) {
        super(caller, victim);
    }

    @Override
    public void execute() {
        victim.sendMessage("FRICK YOU!");
        caller.sendMessage("Wholesome uwu");
    }
}
