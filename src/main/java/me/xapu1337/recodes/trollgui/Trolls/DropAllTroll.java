package me.xapu1337.recodes.trollgui.Trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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

        for (ItemStack itemStack : victim.getInventory()) {
            if(itemStack == null || itemStack == XMaterial.AIR.parseItem()) continue;
            victim.getWorld().dropItemNaturally(victim.getLocation(), itemStack);
        }
        victim.getInventory().clear();
    }
}
