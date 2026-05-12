package me.xapu1337.recodes.trollgui.trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollName;
import org.bukkit.Location;
import org.bukkit.entity.Item;

@TrollName("dropItem")
public class DropItemTroll extends Troll {

    /**
     * Executed from the TrollGUI Class everything inside this function gets
     * executed.
     */
    @Override
    public void execute() {
        if (getVictim().getInventory().getItemInMainHand() == null
                || getVictim().getInventory().getItemInMainHand().getType() == XMaterial.AIR.parseMaterial()) {
            services.messages().setClassPlaceholders(DropItemTroll.class, "player", getVictim().getName());
            getCaller().sendMessage(services.messages().$("{config:Messages.noItemInHand}"));
        } else {
            Location loc = getVictim().getLocation();
            loc.setY(loc.getY() + 1.45);

            Item dropped = getVictim().getWorld().dropItem(loc, getVictim().getInventory().getItemInMainHand());
            getVictim().swingMainHand();

            dropped.setVelocity(loc.getDirection().multiply(0.39));
            dropped.setPickupDelay(40);

            getVictim().getInventory().setItemInMainHand(null);

        }
    }
}
