package me.xapu1337.recodes.trollgui.Trolls;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XPotion;
import me.xapu1337.recodes.trollgui.Cores.TrollCore;
import me.xapu1337.recodes.trollgui.Enums.TrollAttributes;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Types.TrollItemMetaData;

import java.util.Arrays;
import java.util.Objects;

public class GiveAllEffectsTroll extends TrollHandler {


    @Override
    public TrollItemMetaData setMetaData() {
        return (
                new TrollItemMetaData()
                        .setItem(XMaterial.POTION)
                        .setConfigData("giveAllEffects")
                        .setAttributes( TrollAttributes.POSSIBLE_DEATH_OR_ITEM_LOSS )


        );
    }


    @Override
    public void execute() {
        Arrays.stream(XPotion.VALUES).toList().forEach(effect -> {

            if (effect == null || !effect.isSupported()) return;

            victim.addPotionEffect(Objects.requireNonNull(effect.buildPotionEffect(TrollCore.instance.config.getInt("MenuItems.trollMenu.trolls.giveAllBadEffects.options.effectDuration") * 20, 0)));
        });
    }
}
