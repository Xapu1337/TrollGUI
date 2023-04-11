//package me.xapu1337.recodes.trollgui.trolls;
//
//import com.cryptomorin.xseries.XMaterial;
//import me.xapu1337.recodes.trollgui.types.Troll;
//import me.xapu1337.recodes.trollgui.types.TrollMetaData;
//
//
//public class NoBreakTroll extends Troll {
//
//    @Override
//    public TrollMetaData setMetaData() {
//        return (
//                new TrollMetaData(XMaterial.STONE)
//                        .setTrollName("noBreak")
//                        .setToggable(true)
//                        .setToggled(
//                                () -> Singleton.getSingleInstance().noBreakPlayers.containsKey(Utilities.getSingleInstance().uuidOrName(victim, TrollCore.instance.usingUUID))
//                        )
//
//        );
//    }
//
//
//    @Override
//    public void execute() {
//        Utilities.getSingleInstance().addOrRemove(Singleton.getSingleInstance().noBreakPlayers, victim);
//    }
//}
