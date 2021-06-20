package me.xapu1337.recodes.trollgui.Trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Cores.Core;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class DropItemTroll extends TrollHandler {
    public DropItemTroll(Player caller, Player victim) {
        super(caller, victim);
    }

    /**
     * Executed from the TrollGUI Class everything inside this function gets executed.
     */
    @Override
    public void execute() {
        if (victim.getInventory().getItemInMainHand() == null || victim.getInventory().getItemInMainHand().getType() == XMaterial.AIR.parseMaterial())
            caller.sendMessage(Core.instance.utils.getConfigPath("Messages.noItemInHand", true).replace("%player%", victim.getName()));
        else {
            Location loc = victim.getLocation();
            loc.setY(loc.getY() + 1.45);

            Item dropped = victim.getWorld().dropItem(loc, victim.getInventory().getItemInMainHand());
            victim.swingMainHand();

            dropped.setVelocity(loc.getDirection().multiply(0.39));
            dropped.setPickupDelay(40);

            victim.getInventory().setItemInMainHand(null);

        }
    }
}
