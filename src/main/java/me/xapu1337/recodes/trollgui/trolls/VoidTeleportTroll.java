package me.xapu1337.recodes.trollgui.trolls;


import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollName;
import org.bukkit.Location;

@TrollName("voidTeleport")
public class VoidTeleportTroll extends Troll {

    @Override
    public void execute() {
        // teleport below min height so the void kills them
        getVictim().teleport(new Location(getVictim().getWorld(), getVictim().getLocation().getX(),
                getVictim().getWorld().getMinHeight() - 3, getVictim().getLocation().getZ()));
    }
}
