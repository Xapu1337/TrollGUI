package me.xapu1337.recodes.trollgui.trolls;

import com.cryptomorin.xseries.XMaterial;

import me.xapu1337.recodes.trollgui.cores.TrollCore;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollAttributes;
import me.xapu1337.recodes.trollgui.types.TrollMetaData;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class PostedCringeTroll extends Troll {

    // Variables
    List<String> postedCringeMessages;
    @Override
    public TrollMetaData setMetaData() {
        return (
                new TrollMetaData( XMaterial.COMMAND_BLOCK_MINECART )
                        .setTrollName("postedCringe")
                        .setAttributes( TrollAttributes.POSSIBLE_DEATH_OR_ITEM_LOSS )
        );
    }



    /**
     * The method that gets executed on item click
     */
    @Override
    public void execute() {

        postedCringeMessages = TrollCore.getInstance().getConfig().getStringList("messages.sequences.postedCringe");

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
                    getVictim().chat(postedCringeMessages.get(count[0]));
                    count[0]++;
                } else {
                    getVictim().setHealth(0.0);
                    this.cancel();
                }
            }
        }.runTaskTimer(TrollCore.getInstance(), 0, 2 * 20);

    }


}
