package me.xapu1337.recodes.trollgui.trolls;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;

import me.xapu1337.recodes.trollgui.inventories.PlayerSelectorInventory;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollAttributes;
import me.xapu1337.recodes.trollgui.types.TrollMetaData;
import me.xapu1337.recodes.trollgui.utilities.ConfigUtils;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SwapPlayerInventoryTroll extends Troll {



    @Override
    public TrollMetaData setMetaData() {
        return (
                new TrollMetaData( XMaterial.CHEST )
                        .setTrollName( "swapPlayerInventory" )
                        .setAttributes( TrollAttributes.POSSIBLE_DEATH_OR_ITEM_LOSS )
        );
    }

    @Override
    public void execute() {
        PlayerSelectorInventory playerSelectorInventory = new PlayerSelectorInventory(
                ((caller, selectedPlayer) -> {
                    Inventory inv1 = getVictim().getInventory();
                    Inventory inv2 = selectedPlayer.getInventory();
                    ItemStack[] contents1 = inv1.getContents();
                    ItemStack[] contents2 = inv2.getContents();

                    getVictim().playSound(getVictim().getLocation(), XSound.ENTITY_ENDER_PEARL_THROW.parseSound(), .2f, 1);
                    selectedPlayer.playSound(selectedPlayer.getLocation(), XSound.ENTITY_ENDER_PEARL_THROW.parseSound(), .2f, 1);
                    inv1.clear();
                    inv2.clear();
                    getVictim().getInventory().setContents(contents2);
                    selectedPlayer.getInventory().setContents(contents1);
                    getVictim().updateInventory();
                    selectedPlayer.updateInventory();

                    ConfigUtils.getInstance().setClassPlaceholders(this.getClass(), "caller.name", caller.getName());
                    ConfigUtils.getInstance().setClassPlaceholders(this.getClass(), "victim.name", getVictim().getName());

                    caller.sendMessage(ConfigUtils.getInstance().$("{config:messages.swapPlayerInventorySuccess}"));
                })
        );

        playerSelectorInventory.setPreviousInventory(getCallingGUI().getInventory());
        playerSelectorInventory.open(getCaller());
    }
}
