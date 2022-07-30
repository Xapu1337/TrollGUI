package me.xapu1337.recodes.trollgui.Trolls;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import me.xapu1337.recodes.trollgui.Enums.TrollAttributes;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Inventorys.PlayerSelector;
import me.xapu1337.recodes.trollgui.Types.TrollItemMetaData;
import me.xapu1337.recodes.trollgui.Utilities.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class SwapPlayerInventoryTroll extends TrollHandler {



    @Override
    public TrollItemMetaData setMetaData() {
        return (
                new TrollItemMetaData()
                        .setItem( XMaterial.TNT )
                        .setConfigData( "swapPlayerInventory" )
                        .setAttributes( TrollAttributes.POSSIBLE_DEATH_OR_ITEM_LOSS )
        );
    }

    @Override
    public void execute() {
        new PlayerSelector(caller, Utilities.getSingleInstance().getConfigPath("MenuItems.playerSelector.extras.actions.swapPlayerInventory"), getGUI().GUI, victim).setOnPlayerSelect(
                ($, caller, selectedPlayer) ->
                {
                    Inventory inv1 = victim.getInventory();
                    Inventory inv2 = selectedPlayer.getInventory();
                    ItemStack[] contents1 = inv1.getContents();
                    ItemStack[] contents2 = inv2.getContents();

                    victim.playSound(victim.getLocation(), XSound.ENTITY_ENDER_PEARL_THROW.parseSound(), .2f, 1);
                    selectedPlayer.playSound(selectedPlayer.getLocation(), XSound.ENTITY_ENDER_PEARL_THROW.parseSound(), .2f, 1);
                    inv1.clear();
                    inv2.clear();
                    victim.getInventory().setContents(contents2);
                    selectedPlayer.getInventory().setContents(contents1);
                    victim.updateInventory();
                    selectedPlayer.updateInventory();

                    caller.sendMessage(Utilities.getSingleInstance().getConfigPath("Messages.swapPlayerInventorySuccess", true).replaceAll("%PLAYER1%", victim.getName()).replaceAll("%PLAYER2%", selectedPlayer.getName()));
                }
        ).openForPlayer(caller);
    }
}
