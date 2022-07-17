package me.xapu1337.recodes.trollgui.Trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Handlers.TrollItemMetaData;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class ThunderPlayerTroll extends TrollHandler {


    @Override
    public TrollItemMetaData setMetaData() {
        return (
                new TrollItemMetaData()
                        .setItem(XMaterial.PRISMARINE_SHARD)
                        .setConfigData("thunder")

        );
    }


    @Override
    public void execute() {
        victim.getWorld().spawnEntity(victim.getLocation(), EntityType.LIGHTNING);
    }
}
