package me.xapu1337.recodes.trollgui.trolls;

import com.cryptomorin.xseries.XPotion;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollName;

import java.util.Objects;

@TrollName("giveAllBadEffects")
public class GiveAllBadEffects extends Troll {

    @Override
    public void execute() {
        XPotion.DEBUFFS.stream().toList().forEach(effect -> {

            if (effect == null || !effect.isSupported())
                return;

            getVictim().addPotionEffect(Objects.requireNonNull(effect.buildPotionEffect(
                    services.config()
                            .getInt("menus.troll-menu.items.trolls.giveAllBadEffects.options.effectDuration") * 20,
                    0)));
        });
    }
}
