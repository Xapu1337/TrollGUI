package me.xapu1337.recodes.trollgui.Trolls;

import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class ThunderPlayerTroll extends TrollHandler {

    public ThunderPlayerTroll(Player caller, Player victim) {
        super(caller, victim);
    }

    @Override
    public void execute() {
        victim.getWorld().spawnEntity(victim.getLocation(), EntityType.LIGHTNING);
    }
}
