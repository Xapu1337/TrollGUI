package me.xapu1337.recodes.trollgui.Trolls;

import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.Objects;

public class DropAllTroll extends TrollHandler {


    public DropAllTroll(Player caller, Player victim) {
        super(caller, victim);
    }

    /**
     * Executed from the TrollGUI Class everything inside this function gets executed.
     */
    @Override
    public void execute() {
        PlayerInventory inv =  victim.getInventory();
        for(int i = 0; i <= 36; i++){
            try {
                victim.getWorld().dropItem(victim.getLocation(), Objects.requireNonNull(inv.getItem(i))).setPickupDelay(40);
            } catch(Exception ignored){}
            try {
                switch (i) {
                    case 0:
                        break;
                    case 1:
                        victim.getWorld().dropItem(victim.getLocation(), Objects.requireNonNull(inv.getChestplate())).setPickupDelay(40);
                        break;
                    case 2:
                        victim.getWorld().dropItem(victim.getLocation(), Objects.requireNonNull(inv.getLeggings())).setPickupDelay(40);
                        break;
                    case 3:
                        victim.getWorld().dropItem(victim.getLocation(), Objects.requireNonNull(inv.getHelmet())).setPickupDelay(40);
                        break;
                }
            } catch(Exception ignored) {}
        }
        victim.getInventory().clear();
    }
}
