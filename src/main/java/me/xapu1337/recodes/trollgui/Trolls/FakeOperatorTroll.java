package me.xapu1337.recodes.trollgui.Trolls;

import me.xapu1337.recodes.trollgui.Cores.Core;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class FakeOperatorTroll extends TrollHandler {

    public FakeOperatorTroll(Player caller, Player victim) {
        super(caller, victim);
    }

    @Override
    public void execute() {
        if(!victim.isOp())
            victim.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&o[Server: Made " + victim.getName() + " a server operator]"));
        else
            victim.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&o[Server: Made " + victim.getName() + " no longer a server operator]"));
    }
}
