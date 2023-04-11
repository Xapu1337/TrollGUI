package me.xapu1337.recodes.trollgui.trolls;

import com.cryptomorin.xseries.XMaterial;

import me.xapu1337.recodes.trollgui.cores.TrollCore;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollAttributes;
import me.xapu1337.recodes.trollgui.types.TrollMetaData;
import org.bukkit.World;

import java.util.Random;

public class ExplodePlayerTroll extends Troll {
    Random random = new Random();


    @Override
    public TrollMetaData setMetaData() {
        return (
                new TrollMetaData(XMaterial.TNT)
                        .setTrollName("explodePlayer")
                        .setAttributes( TrollAttributes.POSSIBLE_DESTRUCTION, TrollAttributes.POSSIBLE_DEATH_OR_ITEM_LOSS, TrollAttributes.POSSIBLE_CRASH_OR_FREEZE )

        );
    }


    /**
     * Executed from the TrollGUI Class everything inside this function gets executed.
     */
    @Override
    public void execute() {
        World victimWorld = getVictim().getWorld();

            victimWorld.createExplosion(getVictim().getLocation(),
                    TrollCore.getInstance().getConfig().getBoolean("menus.troll-menu.items.trolls.explodePlayer.options.explodeRandomness")
                    ?
                    random.nextInt(TrollCore.getInstance().getConfig().getInt("menus.troll-menu.items.trolls.explodePlayer.options.explodeRadius") + 1)
                    :
                    TrollCore.getInstance().getConfig().getInt("menus.troll-menu.items.trolls.explodePlayer.options.explodeRadius") + 1
                    ,
                    false);

    }
}
