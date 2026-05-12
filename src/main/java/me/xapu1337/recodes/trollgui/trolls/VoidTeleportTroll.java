package me.xapu1337.recodes.trollgui.trolls;


import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollName;
import org.bukkit.Location;

@TrollName("voidTeleport")
public class VoidTeleportTroll extends Troll {

    /**
     * @return
     */

    /**
     * The method that gets executed on item click
     */
    @Override
    public void execute() {
        // get minimal height and teleport 3 blocks down (height of the player + 1 block
        // buffer)
        getVictim().teleport(new Location(getVictim().getWorld(), getVictim().getLocation().getX(),
                getVictim().getWorld().getMinHeight() - 3, getVictim().getLocation().getZ()));
    }
}
