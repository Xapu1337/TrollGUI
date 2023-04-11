//package me.xapu1337.recodes.trollgui.trolls;
//import com.cryptomorin.xseries.XMaterial;
//import com.cryptomorin.xseries.XSound;
//
//import me.xapu1337.recodes.trollgui.Handlers.Troll;
//import me.xapu1337.recodes.trollgui.Inventorys.PlayerSelector;
//import me.xapu1337.recodes.trollgui.Types.TrollMetaData;
//import me.xapu1337.recodes.trollgui.Utilities.Utilities;
//import org.bukkit.Location;
//
//public class SwapPlayersTroll extends Troll {
//
//
//
//    @Override
//    public TrollMetaData setMetaData() {
//        return (
//                new TrollMetaData( XMaterial.EMERALD )
//                        .setTrollName( "swapPlayers" )
//                        .setAttributes( TrollAttributes.POSSIBLE_DEATH_OR_ITEM_LOSS, TrollAttributes.POSSIBLE_CRASH_OR_FREEZE )
//        );
//    }
//
//    @Override
//    public void execute() {
//        new PlayerSelector(caller, Utilities.getSingleInstance().getConfigPath("MenuItems.playerSelector.extras.actions.swapPlayer"), getGUI().GUI, victim).setOnPlayerSelect(
//                ($, caller, selectedPlayer) ->
//                {
//                    final Location loc1 = victim.getLocation();
//                    final Location loc2 = selectedPlayer.getLocation();
//                    // Play ender pearl sound for both players.
//                    victim.playSound(loc1, XSound.ENTITY_ENDER_PEARL_THROW.parseSound(), .2f, 1);
//                    selectedPlayer.playSound(loc2, XSound.ENTITY_ENDER_PEARL_THROW.parseSound(), .2f, 1);
//                    victim.teleport(loc2);
//                    selectedPlayer.teleport(loc1);
//
//                    caller.sendMessage(Utilities.getSingleInstance().getConfigPath("Messages.swapPlayersSuccess", true).replaceAll("%PLAYER1%", victim.getName()).replaceAll("%PLAYER2%", selectedPlayer.getName()));
//                }
//        ).openForPlayer(caller);
//    }
//}
