package me.xapu1337.recodes.trollgui.Trolls;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XPotion;
import com.cryptomorin.xseries.XSound;
import com.cryptomorin.xseries.particles.XParticle;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Types.TrollItemMetaData;
import org.bukkit.SoundCategory;

import java.util.Objects;

public class ScareTroll extends TrollHandler {


    @Override
    public TrollItemMetaData setMetaData() {
        return (
                new TrollItemMetaData()
                        .setItem(XMaterial.CARVED_PUMPKIN)
                        .setConfigData("scarePlayer")

        );
    }


    @Override
    public void execute() {
        victim.spawnParticle(XParticle.getParticle("MOB_APPEARANCE"), victim.getLocation(), 1, 0, 0, 0, 0);

        victim.playSound(victim.getLocation(), Objects.requireNonNull(XSound.ENTITY_ELDER_GUARDIAN_CURSE.parseSound()), SoundCategory.MASTER, 1, 1);
        victim.addPotionEffect(Objects.requireNonNull(XPotion.BLINDNESS.buildPotionEffect(60, 1)));
        for (int i = 0; i < 32; i++) {
            victim.playSound(victim.getLocation(), Objects.requireNonNull(XSound.ITEM_TOTEM_USE.parseSound()), SoundCategory.MASTER, 1, 1);
            victim.playSound(victim.getLocation(), Objects.requireNonNull(XSound.ENTITY_GHAST_HURT.parseSound()), SoundCategory.MASTER, 1, 1);
            victim.playSound(victim.getLocation(), Objects.requireNonNull(XSound.ENTITY_ELDER_GUARDIAN_DEATH.parseSound()), 1, 1);
            victim.playSound(victim.getLocation(), XSound.ENTITY_ELDER_GUARDIAN_CURSE.parseSound(), 1, 1);
        }

    }
}
