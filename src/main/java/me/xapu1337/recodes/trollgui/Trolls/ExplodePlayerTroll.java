package me.xapu1337.recodes.trollgui.Trolls;

import me.xapu1337.recodes.trollgui.Cores.Core;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Random;

public class ExplodePlayerTroll extends TrollHandler {
    Random random = new Random();
    public ExplodePlayerTroll(Player caller, Player victim) {
        super(caller, victim);
    }

    /**
     * Executed from the TrollGUI Class everything inside this function gets executed.
     */
    @Override
    public void execute() {
        World victimWorld = victim.getWorld();
            victimWorld.createExplosion(victim.getLocation(),
                    Core.instance.getConfig().getBoolean("MenuItems.trollMenu.explodePlayer.options.explodeRandomness")
                    ?
                    random.nextInt(Core.instance.getConfig().getInt("MenuItems.trollMenu.explodePlayer.options.explodeRadius") + 1)
                    :
                    Core.instance.getConfig().getInt("MenuItems.trollMenu.explodePlayer.options.explodeRadius") + 1
                    ,
                    false);

    }
}
