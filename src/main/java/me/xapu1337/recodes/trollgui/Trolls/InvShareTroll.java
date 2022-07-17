package me.xapu1337.recodes.trollgui.Trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Cores.Core;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Handlers.TrollItemMetaData;
import org.bukkit.entity.Player;

public class InvShareTroll extends TrollHandler {

    @Override
    public TrollItemMetaData setMetaData() {
        return (
                new TrollItemMetaData()
                        .setItem(XMaterial.ENDER_CHEST)
                        .setConfigData("invShare")

        );
    }


    @Override
    public void execute() {
        Core.instance.getServer().getOnlinePlayers().forEach((player -> {
            // Check if any of the players are currently trolling the victim.
            if (Core.instance.singletons.currentPlayersTrolling.containsKey(player)) {
                // If so, check if the player is trolling the victim.
                if (Core.instance.singletons.currentPlayersTrolling.get(player).getVictim().getUniqueId().equals(victim.getUniqueId())) {
                    // If so, abort the troll.
                    return;
                }
            }

            if(player != victim) player.openInventory(victim.getInventory());
        }));
    }
}
