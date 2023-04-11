package me.xapu1337.recodes.trollgui.trolls;

import com.cryptomorin.xseries.XMaterial;


import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollAttributes;
import me.xapu1337.recodes.trollgui.types.TrollMetaData;
import org.bukkit.Location;

public class VoidTeleportTroll extends Troll {

    /**
     * @return
     */
    @Override
    public TrollMetaData setMetaData() {
        return (
                new TrollMetaData(XMaterial.BEDROCK)
                        .setTrollName("voidTeleport")
                        .setAttributes( TrollAttributes.POSSIBLE_DEATH_OR_ITEM_LOSS )
                ) ;
    }

    /**
     * The method that gets executed on item click
     */
    @Override
    public void execute() {
        // get minimal height and teleport 3 blocks down (height of the player + 1 block buffer)
        getVictim().teleport(new Location(getVictim().getWorld(), getVictim().getLocation().getX(), getVictim().getWorld().getMinHeight() - 3, getVictim().getLocation().getZ()));
    }
}
