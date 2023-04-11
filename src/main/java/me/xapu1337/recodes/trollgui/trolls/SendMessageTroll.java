//package me.xapu1337.recodes.trollgui.trolls;
//
//import com.cryptomorin.xseries.XMaterial;
//import me.xapu1337.recodes.trollgui.Cores.TrollCore;
//
//import me.xapu1337.recodes.trollgui.Utilities.MessageCollector;
//import me.xapu1337.recodes.trollgui.Utilities.Utilities;
//import me.xapu1337.recodes.trollgui.types.Troll;
//import me.xapu1337.recodes.trollgui.types.TrollMetaData;
//import org.bukkit.Bukkit;
//
//import java.util.concurrent.atomic.AtomicReference;
//
//public class SendMessageTroll extends Troll {
//
//
//    /**
//     * @return
//     */
//    @Override
//    public TrollMetaData setMetaData() {
//        return (
//                new TrollMetaData(XMaterial.PAPER)
//                        .setTrollName("sendMessage")
//                );
//    }
//
//    @Override
//    public void execute() {
//        getCaller().sendMessage(" \n ");
//        caller.sendMessage(Utilities.getSingleInstance().getConfigPath("Messages.enterMessage", true).replaceAll("%VICTIM%", victim.getName()));
//        caller.sendMessage(" \n ");
//        MessageCollector.getSingleInstance()
//                .registerHandler(
//                        caller,
//                        (player, playerReply) -> {
//                            Bukkit.getScheduler().runTask(TrollCore.instance, () ->
//                                    victim.chat(playerReply));
//                        }
//                );
//    }
//
//}
