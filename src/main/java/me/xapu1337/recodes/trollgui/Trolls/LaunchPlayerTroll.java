package me.xapu1337.recodes.trollgui.Trolls;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.cryptomorin.xseries.particles.XParticle;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Handlers.TrollItemMetaData;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class LaunchPlayerTroll extends TrollHandler {

    @Override
    public TrollItemMetaData setMetaData() {
        return (
                new TrollItemMetaData()
                        .setItem(XMaterial.FIREWORK_ROCKET)
                        .setConfigData("launchPlayer")

        );
    }


    @Override
    public void execute() {
        victim.setVelocity(new Vector(0f, 5f, 0f));
        victim.playSound(victim.getLocation(), XSound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST.parseSound(), 3f, 1f);
        victim.spawnParticle(XParticle.getParticle("EXPLOSION_HUGE"), victim.getLocation(), 1, 0.11, 1, 1, 1);
    }
}
