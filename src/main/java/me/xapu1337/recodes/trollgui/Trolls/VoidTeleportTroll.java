package me.xapu1337.recodes.trollgui.Trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Enums.TrollAttributes;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Types.TrollItemMetaData;
import org.bukkit.Location;

public class VoidTeleportTroll extends TrollHandler {

    /**
     * @return
     */
    @Override
    public TrollItemMetaData setMetaData() {
        return (
                new TrollItemMetaData()
                        .setItem(XMaterial.BEDROCK)
                        .setConfigData("voidTeleport")
                        .setAttributes( TrollAttributes.POSSIBLE_DEATH_OR_ITEM_LOSS )
                ) ;
    }

    /**
     * The method that gets executed on item click
     */
    @Override
    public void execute() {
        // get minimal height and teleport 3 blocks down (height of the player + 1 block buffer)
        victim.teleport(new Location(victim.getWorld(), victim.getLocation().getX(), victim.getWorld().getMinHeight() - 3, victim.getLocation().getZ()));
    }
}
