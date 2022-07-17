package me.xapu1337.recodes.trollgui.Trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Cores.Core;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Handlers.TrollItemMetaData;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Random;

public class ExplodePlayerTroll extends TrollHandler {
    Random random = new Random();


    @Override
    public TrollItemMetaData setMetaData() {
        return (
                new TrollItemMetaData()
                        .setItem(XMaterial.TNT)
                        .setConfigData("explodePlayer")

        );
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
