package me.xapu1337.recodes.trollgui.Trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Cores.TrollCore;
import me.xapu1337.recodes.trollgui.Enums.TrollAttributes;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Types.TrollItemMetaData;
import me.xapu1337.recodes.trollgui.Utilities.Utilities;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class PostedCringeTroll extends TrollHandler {

    // Variables
    List<String> postedCringeMessages;
    @Override
    public TrollItemMetaData setMetaData() {
        return (
                new TrollItemMetaData()
                        .setItem( XMaterial.COMMAND_BLOCK_MINECART )
                        .setConfigData("postedCringe")
                        .setAttributes( TrollAttributes.POSSIBLE_DEATH_OR_ITEM_LOSS )
        );
    }


    @Override
    public void onServerDisable() {
        super.onServerDisable();
        Utilities.getSingleInstance().cleanCollectionIfPossible(postedCringeMessages);
    }

    /**
     * The method that gets executed on item click
     */
    @Override
    public void execute() {

        postedCringeMessages = TrollCore.instance.config.getStringList("Messages.sequences.postedCringe");

        // If the list is empty, insert the default messages
        if(postedCringeMessages.size() == 0) {
            postedCringeMessages.add("Oh no! I think...");
            postedCringeMessages.add("...I just posted cringe!");
        }

        final int[] count = {0};
        new BukkitRunnable() {
            @Override
            public void run() {
                if (count[0] < postedCringeMessages.size()) {
                    victim.chat(postedCringeMessages.get(count[0]));
                    count[0]++;
                } else {
                    victim.setHealth(0.0);
                    this.cancel();
                }
            }
        }.runTaskTimer(TrollCore.instance, 0, 2 * 20);

    }


}
