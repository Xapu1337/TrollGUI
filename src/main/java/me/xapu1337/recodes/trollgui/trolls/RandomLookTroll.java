package me.xapu1337.recodes.trollgui.trolls;

import com.cryptomorin.xseries.XMaterial;

import me.xapu1337.recodes.trollgui.cores.TrollCore;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollAttributes;
import me.xapu1337.recodes.trollgui.types.TrollMetaData;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class RandomLookTroll extends Troll {
    Random random = new Random();


    @Override
    public TrollMetaData setMetaData() {
        return (
                new TrollMetaData(XMaterial.BARRIER)
                        .setTrollName("randomLook")
                        .setAttributes( TrollAttributes.POSSIBLE_KICK )

        );
    }


    @Override
    public void execute() {
        Location loc = getVictim().getLocation();
        float restoreYaw = loc.getYaw();
        float restorePitch = loc.getPitch();
        new BukkitRunnable(){
            private int i = 0;
            public void run() {
                if(i >= TrollCore.getInstance().getConfig().getInt("menus.troll-menu.items.trolls.randomLook.options.randomLookTime") * 20) {
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
