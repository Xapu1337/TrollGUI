package me.xapu1337.recodes.trollgui.trolls;


import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollName;
import org.bukkit.World;

import java.util.Random;

@TrollName("explodePlayer")
public class ExplodePlayerTroll extends Troll {
    Random random = new Random();

    /**
     * Executed from the TrollGUI Class everything inside this function gets
     * executed.
     */
    @Override
    public void execute() {
        World victimWorld = getVictim().getWorld();

        victimWorld.createExplosion(getVictim().getLocation(),
                services.config()
                        .getBoolean("menus.troll-menu.items.trolls.explodePlayer.options.explodeRandomness")
                                ? random.nextInt(services.config().getInt(
                                        "menus.troll-menu.items.trolls.explodePlayer.options.explodeRadius") + 1)
                                : services.config().getInt(
                                        "menus.troll-menu.items.trolls.explodePlayer.options.explodeRadius") + 1,
                false);

    }
}
