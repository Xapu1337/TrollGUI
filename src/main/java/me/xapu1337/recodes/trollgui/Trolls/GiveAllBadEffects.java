package me.xapu1337.recodes.trollgui.Trolls;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XPotion;
import me.xapu1337.recodes.trollgui.Cores.TrollCore;
import me.xapu1337.recodes.trollgui.Enums.TrollAttributes;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Types.TrollItemMetaData;

import java.util.Arrays;
import java.util.stream.Collectors;

public class GiveAllBadEffects extends TrollHandler {


    @Override
    public TrollItemMetaData setMetaData() {
        return (
                new TrollItemMetaData()
                        .setItem(XMaterial.SPLASH_POTION)
                        .setConfigData("giveAllBadEffects")
                        .setAttributes( TrollAttributes.POSSIBLE_DEATH_OR_ITEM_LOSS )


        );
    }


    @Override
    public void execute() {
        XPotion.DEBUFFS.stream().toList().forEach(effect -> {

            if (effect == null || !effect.isSupported()) return;

            victim.addPotionEffect(effect.buildPotionEffect(TrollCore.instance.config.getInt("MenuItems.trollMenu.giveAllBadEffects.options.effectDuration") * 20, 0));
        });
    }
}
