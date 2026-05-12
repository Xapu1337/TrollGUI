package me.xapu1337.recodes.trollgui.trolls;


import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollName;
import org.bukkit.Location;
import org.bukkit.World;

@TrollName("thunder")
public class ThunderPlayerTroll extends Troll {

    @Override
    public void execute() {
        World world = getVictim().getWorld();
        Location center = getVictim().getLocation();
        world.strikeLightning(center);
        
        for (int angle = 0; angle < 360; angle += 90) {
            double rad = Math.toRadians(angle);
            world.strikeLightningEffect(center.clone().add(Math.cos(rad) * 3, 0, Math.sin(rad) * 3));
        }
    }
}
