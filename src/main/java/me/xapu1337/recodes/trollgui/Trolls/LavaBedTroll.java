package me.xapu1337.recodes.trollgui.Trolls;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import me.xapu1337.recodes.trollgui.Enums.TrollAttributes;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Types.TrollItemMetaData;
import org.bukkit.Location;

public class LavaBedTroll extends TrollHandler {

    @Override
    public TrollItemMetaData setMetaData() {
        return (
                new TrollItemMetaData()
                        .setItem(XMaterial.LAVA_BUCKET)
                        .setConfigData("lavaBed")
                        .setAttributes( TrollAttributes.POSSIBLE_DEATH_OR_ITEM_LOSS, TrollAttributes.POSSIBLE_DESTRUCTION )

        );
    }

    @Override
    public void execute() {
        Location bottom = victim.getLocation();

        // a 3x1 grid of lava blocks below the player
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Location loc = bottom.clone().add(x, -1, z);
                loc.getBlock().setType(XMaterial.LAVA.parseMaterial());
            }
        }

        victim.playSound(victim.getLocation(), XSound.BLOCK_FIRE_EXTINGUISH.parseSound(), 3f, 1f);
    }

}
