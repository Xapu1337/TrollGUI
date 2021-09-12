package me.xapu1337.recodes.trollgui.Trolls;

import me.xapu1337.recodes.trollgui.Cores.Core;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class RandomLookTroll extends TrollHandler {
    Random random = new Random();

    public RandomLookTroll(Player caller, Player victim) {
        super(caller, victim);
    }

    @Override
    public void execute() {
        Location loc = victim.getLocation();
        float restoreYaw = loc.getYaw();
        float restorePitch = loc.getPitch();
        new BukkitRunnable(){
            private int i = 0;
            public void run() {
                if(i >= Core.instance.getConfig().getInt("MenuItems.trollMenu.randomLook.options.randomLookTime") * 20) {
                    loc.setPitch(restorePitch);
                    loc.setYaw(restoreYaw);
                    victim.teleport(loc);
                    cancel();
                }
                ++i;
                loc.setYaw(random.nextInt(360));
                loc.setPitch(random.nextInt(180));
                victim.teleport(loc);
            }
        }.runTaskTimer(Core.instance, 5L, 1L);
    }
}
