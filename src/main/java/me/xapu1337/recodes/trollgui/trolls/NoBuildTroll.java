//package me.xapu1337.recodes.trollgui.trolls;
//
//import com.cryptomorin.xseries.XMaterial;
//
//
//public class NoBuildTroll extends Troll {
//
//
//    @Override
//    public TrollMetaData setMetaData() {
//        return (
//                new TrollMetaData(XMaterial.GRASS_BLOCK)
//                        .setTrollName("noBuild")
//                        .setToggable(true)
//                        .setToggled(
//                                () -> Singleton.getSingleInstance().noBuildPlayers.containsKey(Utilities.getSingleInstance().uuidOrName(victim, TrollCore.instance.usingUUID))
//                        )
//
//        );
//    }
//
//
//    @Override
//    public void execute() {
//        Utilities.getSingleInstance().addOrRemove(Singleton.getSingleInstance().noBuildPlayers, victim);
//    }
//}
