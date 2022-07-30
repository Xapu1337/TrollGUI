package me.xapu1337.recodes.trollgui.Trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Enums.TrollAttributes;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Types.TrollItemMetaData;
import org.bukkit.entity.EntityType;

public class ThunderPlayerTroll extends TrollHandler {


    @Override
    public TrollItemMetaData setMetaData() {
        return (
                new TrollItemMetaData()
                        .setItem(XMaterial.PRISMARINE_SHARD)
                        .setConfigData("thunder")
                        .setAttributes( TrollAttributes.POSSIBLE_DEATH_OR_ITEM_LOSS, TrollAttributes.POSSIBLE_DESTRUCTION )

        );
    }


    @Override
    public void execute() {
        victim.getWorld().spawnEntity(victim.getLocation(), EntityType.LIGHTNING);
    }
}
