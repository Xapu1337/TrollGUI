//package me.xapu1337.recodes.trollgui.trolls;
//
//import com.cryptomorin.xseries.XMaterial;
//
//
//import me.xapu1337.recodes.trollgui.Utilities.Utilities;
//import me.xapu1337.recodes.trollgui.types.Troll;
//import me.xapu1337.recodes.trollgui.types.TrollAttributes;
//import me.xapu1337.recodes.trollgui.types.TrollMetaData;
//
//public class FreezeTroll extends Troll {
//
//    @Override
//    public TrollMetaData setMetaData() {
//        return (
//                new TrollMetaData(XMaterial.SNOWBALL)
//                        .setTrollName("freezePlayer")
//                        .setAttributes( TrollAttributes.POSSIBLE_DESTRUCTION )
//                        .setToggable(true)
//                        .setToggled(
//                                () -> Singleton.getSingleInstance().frozenPlayers.containsKey(Utilities.getSingleInstance().uuidOrName(victim, TrollCore.instance.usingUUID))
//                        )
//
//        );
//    }
//
//
//    @Override
//    public void execute() {
//        Utilities.getSingleInstance().addOrRemove(Singleton.getSingleInstance().frozenPlayers, victim);
//    }
//}
