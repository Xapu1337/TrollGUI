package me.xapu1337.recodes.trollgui.Trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Cores.TrollCore;
import me.xapu1337.recodes.trollgui.Enums.TrollAttributes;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Types.TrollItemMetaData;
import org.bukkit.World;

import java.util.Random;

public class ExplodePlayerTroll extends TrollHandler {
    Random random = new Random();


    @Override
    public TrollItemMetaData setMetaData() {
        return (
                new TrollItemMetaData()
                        .setItem(XMaterial.TNT)
                        .setConfigData("explodePlayer")
                        .setAttributes( TrollAttributes.POSSIBLE_DESTRUCTION, TrollAttributes.POSSIBLE_DEATH_OR_ITEM_LOSS, TrollAttributes.POSSIBLE_CRASH_OR_FREEZE )

        );
    }


    /**
     * Executed from the TrollGUI Class everything inside this function gets executed.
     */
    @Override
    public void execute() {
        World victimWorld = victim.getWorld();

            victimWorld.createExplosion(victim.getLocation(),
                    TrollCore.instance.getConfig().getBoolean("MenuItems.trollMenu.explodePlayer.options.explodeRandomness")
                    ?
                    random.nextInt(TrollCore.instance.getConfig().getInt("MenuItems.trollMenu.explodePlayer.options.explodeRadius") + 1)
                    :
                    TrollCore.instance.getConfig().getInt("MenuItems.trollMenu.explodePlayer.options.explodeRadius") + 1
                    ,
                    false);

    }
}
