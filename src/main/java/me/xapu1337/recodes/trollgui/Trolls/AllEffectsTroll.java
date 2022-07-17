package me.xapu1337.recodes.trollgui.Trolls;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XPotion;
import me.xapu1337.recodes.trollgui.Cores.Core;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Handlers.TrollItemMetaData;

import java.util.Arrays;
import java.util.stream.Collectors;

public class AllEffectsTroll extends TrollHandler {


    @Override
    public TrollItemMetaData setMetaData() {
        return (
                new TrollItemMetaData()
                        .setItem(XMaterial.LINGERING_POTION)
                        .setConfigData("allEffects")


        );
    }


    @Override
    public void execute() {
        Arrays.stream(XPotion.VALUES).collect(Collectors.toList()).forEach(effect -> {

            if (effect == null || !effect.isSupported()) return;

            victim.addPotionEffect(effect.buildPotionEffect(Core.instance.config.getInt("MenuItems.trollMenu.allEffects.options.effectDuration") * 20, 0));
        });
    }
}
