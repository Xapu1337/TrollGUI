package me.xapu1337.recodes.trollgui.Trolls;

import com.cryptomorin.xseries.XSound;
import com.cryptomorin.xseries.particles.XParticle;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import org.bukkit.entity.Player;

public class ScareTroll extends TrollHandler {

    public ScareTroll(Player caller, Player victim) {
        super(caller, victim);
    }

    @Override
    public void execute() {
        victim.spawnParticle(XParticle.getParticle("MOB_APPEARANCE"), victim.getLocation(), 1);
        victim.playSound(victim.getLocation(), XSound.ENTITY_ELDER_GUARDIAN_CURSE.parseSound(), 100, 1);
        victim.playSound(victim.getLocation(), XSound.ENTITY_ELDER_GUARDIAN_AMBIENT.parseSound(), 100, 1);
        victim.playSound(victim.getLocation(), XSound.ENTITY_ELDER_GUARDIAN_AMBIENT_LAND.parseSound(), 100, 1);
        victim.playSound(victim.getLocation(), XSound.ENTITY_ELDER_GUARDIAN_DEATH.parseSound(), 100, 1);
        victim.playSound(victim.getLocation(), XSound.ENTITY_ELDER_GUARDIAN_DEATH_LAND.parseSound(), 100, 1);
        victim.playSound(victim.getLocation(), XSound.ENTITY_ELDER_GUARDIAN_FLOP.parseSound(), 100, 1);
        victim.playSound(victim.getLocation(), XSound.ENTITY_ELDER_GUARDIAN_HURT.parseSound(), 100, 1);
        victim.playSound(victim.getLocation(), XSound.ENTITY_ELDER_GUARDIAN_HURT_LAND.parseSound(), 100, 1);
    }
}
