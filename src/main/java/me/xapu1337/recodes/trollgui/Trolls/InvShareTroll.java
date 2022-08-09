package me.xapu1337.recodes.trollgui.Trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Cores.TrollCore;
import me.xapu1337.recodes.trollgui.Enums.TrollAttributes;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Types.TrollItemMetaData;
import me.xapu1337.recodes.trollgui.Utilities.Singleton;

public class InvShareTroll extends TrollHandler {

    @Override
    public TrollItemMetaData setMetaData() {
        return (
                new TrollItemMetaData()
                        .setItem(XMaterial.ENDER_CHEST)
                        .setConfigData("invShare")
                        .setAttributes( TrollAttributes.POSSIBLE_DEATH_OR_ITEM_LOSS )

        );
    }


    @Override
    public void execute() {
        TrollCore.instance.getServer().getOnlinePlayers().forEach((player -> {
            if (Singleton.getSingleInstance().currentPlayersTrolling.containsKey(player)) {
                if (Singleton.getSingleInstance().currentPlayersTrolling.get(player).getVictim().getUniqueId().equals(victim.getUniqueId())) {
                    return;
                }
            }

            if(player != victim) player.openInventory(victim.getInventory());
        }));
    }
}
