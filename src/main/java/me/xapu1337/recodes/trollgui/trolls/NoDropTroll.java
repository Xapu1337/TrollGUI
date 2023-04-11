//package me.xapu1337.recodes.trollgui.trolls;
//
//import com.cryptomorin.xseries.XMaterial;
//
//
//public class NoDropTroll extends Troll {
//
//
//    @Override
//    public TrollMetaData setMetaData() {
//        return (
//                new TrollMetaData(XMaterial.DROPPER)
//                        .setTrollName("noDrop")
//                        .setToggable(true)
//                        .setToggled(
//                                () -> Singleton.getSingleInstance().noDropPlayers.containsKey(Utilities.getSingleInstance().uuidOrName(victim, TrollCore.instance.usingUUID))
//                        )
//
//        );
//    }
//
//
//    @Override
//    public void execute() {
//        Utilities.getSingleInstance().addOrRemove(Singleton.getSingleInstance().noDropPlayers, victim);
//    }
//}
