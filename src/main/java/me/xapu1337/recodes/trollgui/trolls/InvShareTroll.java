package me.xapu1337.recodes.trollgui.trolls;


import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollName;
import org.bukkit.Bukkit;

@TrollName("invShare")
public class InvShareTroll extends Troll {

    @Override
    public void execute() {
        Bukkit.getOnlinePlayers().forEach((player -> {
            if (player != getVictim())
                player.openInventory(getVictim().getInventory());
        }));
    }
}
