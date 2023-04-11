package me.xapu1337.recodes.trollgui.trolls;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;


import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollAttributes;
import me.xapu1337.recodes.trollgui.types.TrollMetaData;
import org.bukkit.Location;

import java.util.Objects;

public class LavaBedTroll extends Troll {

    @Override
    public TrollMetaData setMetaData() {
        return (
                new TrollMetaData(XMaterial.LAVA_BUCKET)
                        .setTrollName("lavaBed")
                        .setAttributes( TrollAttributes.POSSIBLE_DEATH_OR_ITEM_LOSS, TrollAttributes.POSSIBLE_DESTRUCTION )

        );
    }

    @Override
    public void execute() {
        Location bottom = getVictim().getLocation();

        // a 3x1 grid of lava blocks below the player
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Location loc = bottom.clone().add(x, -1, z);
                loc.getBlock().setType(Objects.requireNonNull(XMaterial.LAVA.parseMaterial()));
            }
        }

        getVictim().playSound(getVictim().getLocation(), Objects.requireNonNull(XSound.BLOCK_FIRE_EXTINGUISH.parseSound()), 3f, 1f);
    }

}
