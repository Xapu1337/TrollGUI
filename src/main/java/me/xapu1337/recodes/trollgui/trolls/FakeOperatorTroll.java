package me.xapu1337.recodes.trollgui.trolls;


import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollName;
import org.bukkit.ChatColor;

@TrollName("fakeOperator")
public class FakeOperatorTroll extends Troll {

    @Override
    public void execute() {
        if (!getVictim().isOp())
            getVictim().sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&7&o[Server: Made " + getVictim().getName() + " a server operator]"));
        else
            getVictim().sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&7&o[Server: Made " + getVictim().getName() + " no longer a server operator]"));
    }
}
