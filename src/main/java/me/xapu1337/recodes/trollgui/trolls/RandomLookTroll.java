package me.xapu1337.recodes.trollgui.trolls;


import me.xapu1337.recodes.trollgui.cores.TrollCore;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollName;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

@TrollName("randomLook")
public class RandomLookTroll extends Troll {
    Random random = new Random();

    @Override
    public void execute() {
        Location loc = getVictim().getLocation();
        float restoreYaw = loc.getYaw();
        float restorePitch = loc.getPitch();
        new BukkitRunnable() {
            private int i = 0;

            public void run() {
                if (i >= services.config()
                        .getInt("menus.troll-menu.items.trolls.randomLook.options.randomLookTime") * 20) {
                    loc.setPitch(restorePitch);
                    loc.setYaw(restoreYaw);
                    getVictim().teleport(loc);
                    cancel();
                }
                ++i;
                loc.setYaw(random.nextInt(360));
                loc.setPitch(random.nextInt(180));
                getVictim().teleport(loc);
            }
        }.runTaskTimer(TrollCore.getInstance(), 5L, 1L);
    }
}
