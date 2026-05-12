package me.xapu1337.recodes.trollgui.trolls;

import com.cryptomorin.xseries.XSound;

import me.xapu1337.recodes.trollgui.inventories.PlayerSelectorInventory;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollName;
import org.bukkit.Location;

@TrollName("swapPlayers")
public class SwapPlayersTroll extends Troll {

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

                    services.messages().setClassPlaceholders(this.getClass(), "caller.name", caller.getName());
                    services.messages().setClassPlaceholders(this.getClass(), "victim.name", getVictim().getName());

                    caller.sendMessage(services.messages().$("{config:messages.swapPlayerInventorySuccess}"));
                }),
                services);

        playerSelectorInventory.setPreviousInventory(getCallingGUI().getInventory());
        playerSelectorInventory.open(getCaller());
    }
}
