package me.xapu1337.recodes.trollgui.trolls;


import me.xapu1337.recodes.trollgui.cores.TrollCore;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollName;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

@TrollName("postedCringe")
public class PostedCringeTroll extends Troll {

    List<String> postedCringeMessages;

    @Override
    public void execute() {

        postedCringeMessages = services.config().getStringList("messages.sequences.postedCringe");

        // If the list is empty, insert the default messages
        if (postedCringeMessages.size() == 0) {
            postedCringeMessages.add("Oh no! I think...");
            postedCringeMessages.add("...I just posted cringe!");
        }

        final int[] count = { 0 };
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
