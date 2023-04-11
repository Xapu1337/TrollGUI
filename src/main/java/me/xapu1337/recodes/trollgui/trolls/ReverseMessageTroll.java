//package me.xapu1337.recodes.trollgui.trolls;
//
//import com.cryptomorin.xseries.XMaterial;
//
//
//public class ReverseMessageTroll extends Troll {
//
//
//    @Override
//    public TrollMetaData setMetaData() {
//        return (
//                new TrollMetaData(XMaterial.PAPER)
//                        .setTrollName("reverseMessage")
//                        .setToggable(true)
//                        .setToggled(
//                                () -> Singleton.getSingleInstance().reverseMessagePlayers.containsKey(Utilities.getSingleInstance().uuidOrName(victim, TrollCore.instance.usingUUID))
//                        )
//
//        );
//    }
//
//
//    @Override
//    public void execute() {
//        Utilities.getSingleInstance().addOrRemove(Singleton.getSingleInstance().reverseMessagePlayers, victim);
//    }
//}
