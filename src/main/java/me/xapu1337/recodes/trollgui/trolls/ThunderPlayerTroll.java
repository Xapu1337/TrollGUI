package me.xapu1337.recodes.trollgui.trolls;


import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollName;
import org.bukkit.entity.EntityType;

@TrollName("thunder")
public class ThunderPlayerTroll extends Troll {

    @Override
    public void execute() {
        getVictim().getWorld().spawnEntity(getVictim().getLocation(), EntityType.LIGHTNING);
    }
}
