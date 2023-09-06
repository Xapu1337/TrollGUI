package me.xapu1337.recodes.trollgui.trolls;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;

import me.xapu1337.recodes.trollgui.inventories.PlayerSelectorInventory;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollAttributes;
import me.xapu1337.recodes.trollgui.types.TrollMetaData;
import me.xapu1337.recodes.trollgui.utilities.MessageUtils;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SwapPlayersTroll extends Troll {



    @Override
    public TrollMetaData setMetaData() {
        return (
                new TrollMetaData( XMaterial.EMERALD )
                        .setTrollName( "swapPlayers" )
                        .setAttributes( TrollAttributes.POSSIBLE_DEATH_OR_ITEM_LOSS, TrollAttributes.POSSIBLE_CRASH_OR_FREEZE )
        );
    }

    @Override
    public void execute() {
        PlayerSelectorInventory playerSelectorInventory = new PlayerSelectorInventory(
                ((caller, selectedPlayer) -> {
                    final Location loc1 = getVictim().getLocation();
                    final Location loc2 = selectedPlayer.getLocation();
                    // Play ender pearl sound for both players.
                    getVictim().playSound(loc1, XSound.ENTITY_ENDER_PEARL_THROW.parseSound(), .2f, 1);
                    selectedPlayer.playSound(loc2, XSound.ENTITY_ENDER_PEARL_THROW.parseSound(), .2f, 1);
                    getVictim().teleport(loc2);
                    selectedPlayer.teleport(loc1);

                    MessageUtils.getInstance().setClassPlaceholders(this.getClass(), "caller.name", caller.getName());
                    MessageUtils.getInstance().setClassPlaceholders(this.getClass(), "victim.name", getVictim().getName());

                    caller.sendMessage(MessageUtils.getInstance().$("{config:messages.swapPlayerInventorySuccess}"));
                })
        );

        playerSelectorInventory.setPreviousInventory(getCallingGUI().getInventory());
        playerSelectorInventory.open(getCaller());
    }
}
