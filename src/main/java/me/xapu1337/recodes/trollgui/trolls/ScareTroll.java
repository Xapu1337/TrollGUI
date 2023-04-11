package me.xapu1337.recodes.trollgui.trolls;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XPotion;
import com.cryptomorin.xseries.XSound;
import com.cryptomorin.xseries.particles.XParticle;

import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollMetaData;
import org.bukkit.SoundCategory;

import java.util.Objects;

public class ScareTroll extends Troll {


    @Override
    public TrollMetaData setMetaData() {
        return (
                new TrollMetaData(XMaterial.CARVED_PUMPKIN)
                        .setTrollName("scarePlayer")

        );
    }


    @Override
    public void execute() {
        getVictim().spawnParticle(XParticle.getParticle("MOB_APPEARANCE"), getVictim().getLocation(), 1, 0, 0, 0, 0);

        getVictim().playSound(getVictim().getLocation(), Objects.requireNonNull(XSound.ENTITY_ELDER_GUARDIAN_CURSE.parseSound()), SoundCategory.MASTER, 1, 1);
        getVictim().addPotionEffect(Objects.requireNonNull(XPotion.BLINDNESS.buildPotionEffect(60, 1)));
        for (int i = 0; i < 32; i++) {
            getVictim().playSound(getVictim().getLocation(), Objects.requireNonNull(XSound.ITEM_TOTEM_USE.parseSound()), SoundCategory.MASTER, 1, 1);
            getVictim().playSound(getVictim().getLocation(), Objects.requireNonNull(XSound.ENTITY_GHAST_HURT.parseSound()), SoundCategory.MASTER, 1, 1);
            getVictim().playSound(getVictim().getLocation(), Objects.requireNonNull(XSound.ENTITY_ELDER_GUARDIAN_DEATH.parseSound()), 1, 1);
            getVictim().playSound(getVictim().getLocation(), XSound.ENTITY_ELDER_GUARDIAN_CURSE.parseSound(), 1, 1);
        }

    }
}
