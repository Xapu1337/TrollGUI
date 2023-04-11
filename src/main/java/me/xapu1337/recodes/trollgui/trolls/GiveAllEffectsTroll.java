package me.xapu1337.recodes.trollgui.trolls;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XPotion;
import me.xapu1337.recodes.trollgui.cores.TrollCore;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollAttributes;
import me.xapu1337.recodes.trollgui.types.TrollMetaData;


import java.util.Arrays;
import java.util.Objects;

public class GiveAllEffectsTroll extends Troll {


    @Override
    public TrollMetaData setMetaData() {
        return (
                new TrollMetaData(XMaterial.POTION)
                        .setTrollName("giveAllEffects")
                        .setAttributes( TrollAttributes.POSSIBLE_DEATH_OR_ITEM_LOSS )


        );
    }


    @Override
    public void execute() {
        Arrays.stream(XPotion.VALUES).toList().forEach(effect -> {

            if (effect == null || !effect.isSupported()) return;

            getVictim().addPotionEffect(Objects.requireNonNull(effect.buildPotionEffect(TrollCore.getInstance().getConfig().getInt("menus.troll-menu.items.trolls.giveAllBadEffects.options.effectDuration") * 20, 0)));
        });
    }
}
