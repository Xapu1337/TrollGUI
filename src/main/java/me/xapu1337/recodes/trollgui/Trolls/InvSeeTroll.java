package me.xapu1337.recodes.trollgui.Trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Handlers.TrollItemMetaData;
import org.bukkit.entity.Player;

public class InvSeeTroll extends TrollHandler {

    @Override
    public TrollItemMetaData setMetaData() {
        return (
                new TrollItemMetaData()
                        .setItem(XMaterial.TRAPPED_CHEST)
                        .setConfigData("invSee")

        );
    }


    @Override
    public void execute() {
        caller.openInventory(victim.getInventory());
    }
}
