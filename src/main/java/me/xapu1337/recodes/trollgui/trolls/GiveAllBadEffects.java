package me.xapu1337.recodes.trollgui.trolls;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XPotion;
import me.xapu1337.recodes.trollgui.cores.TrollCore;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollAttributes;
import me.xapu1337.recodes.trollgui.types.TrollMetaData;


import java.util.Objects;

public class GiveAllBadEffects extends Troll {


    @Override
    public TrollMetaData setMetaData() {
        return (
                new TrollMetaData(XMaterial.SPLASH_POTION)
                        .setTrollName("giveAllBadEffects")
                        .setAttributes( TrollAttributes.POSSIBLE_DEATH_OR_ITEM_LOSS )


        );
    }


    @Override
    public void execute() {
        XPotion.DEBUFFS.stream().toList().forEach(effect -> {

            if (effect == null || !effect.isSupported()) return;

            getVictim().addPotionEffect(Objects.requireNonNull(effect.buildPotionEffect(TrollCore.getInstance().getConfig().getInt("menus.troll-menu.items.trolls.giveAllBadEffects.options.effectDuration") * 20, 0)));
        });
    }
}
