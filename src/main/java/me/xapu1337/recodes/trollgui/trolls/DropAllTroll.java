package me.xapu1337.recodes.trollgui.trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollName;
import org.bukkit.inventory.ItemStack;

@TrollName("dropAll")
public class DropAllTroll extends Troll {

    /**
     * Executed from the TrollGUI Class everything inside this function gets
     * executed.
     */
    @Override
    public void execute() {

        for (ItemStack itemStack : getVictim().getInventory()) {
            if (itemStack == null || itemStack == XMaterial.AIR.parseItem())
                continue;
            getVictim().getWorld().dropItemNaturally(getVictim().getLocation(), itemStack);
        }
        getVictim().getInventory().clear();

    }
}
