package me.xapu1337.recodes.trollgui.Trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Enums.TrollAttributes;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Types.TrollItemMetaData;
import org.bukkit.inventory.ItemStack;

public class DropAllTroll extends TrollHandler {


    @Override
    public TrollItemMetaData setMetaData() {
        return (
                new TrollItemMetaData()
                        .setItem(XMaterial.CAULDRON)
                        .setConfigData("dropAll")
                        .setAttributes( TrollAttributes.POSSIBLE_DEATH_OR_ITEM_LOSS )

        );
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
