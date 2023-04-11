package me.xapu1337.recodes.trollgui.trolls;

import com.cryptomorin.xseries.XMaterial;


import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollAttributes;
import me.xapu1337.recodes.trollgui.types.TrollMetaData;
import org.bukkit.entity.EntityType;

public class ThunderPlayerTroll extends Troll {


    @Override
    public TrollMetaData setMetaData() {
        return (
                new TrollMetaData(XMaterial.PRISMARINE_SHARD)
                        .setTrollName("thunder")
                        .setAttributes( TrollAttributes.POSSIBLE_DEATH_OR_ITEM_LOSS, TrollAttributes.POSSIBLE_DESTRUCTION )

        );
    }


    @Override
    public void execute() {
        getVictim().getWorld().spawnEntity(getVictim().getLocation(), EntityType.LIGHTNING);
    }
}
