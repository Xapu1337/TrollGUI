//package me.xapu1337.recodes.trollgui.trolls;
//
//import com.cryptomorin.xseries.XMaterial;
//
//import me.xapu1337.recodes.trollgui.types.Troll;
//import me.xapu1337.recodes.trollgui.types.TrollMetaData;
//import org.bukkit.Bukkit;
//import org.bukkit.inventory.ItemStack;
//
//public class FakeClearTroll extends Troll {
//
//    // Variables
//    ItemStack[] savedInventory;
//
//    @Override
//    public TrollMetaData setMetaData() {
//        return (
//                new TrollMetaData(XMaterial.PUFFERFISH)
//                        .setTrollName("fakeClear")
//
//        );
//    }
//
//
//    /**
//     * the:
//     * <br />
//     * <code>
//     * if(Utilities.getSingleInstance().uuidOrName(victim.getPlayer(), Core.instance.getServer().getOnlineMode()))...
//     * </code>
//     * <br />
//     * may seem weird, but basically I'm making it offline & online mode friendly and avoiding issues.
//     */
//    @Override
//    public void execute() {
//        if(!Singleton.getSingleInstance().clearedPlayerInventories.containsKey(Utilities.getSingleInstance().uuidOrName(victim.getPlayer(), TrollCore.instance.getServer().getOnlineMode()))) {
//            savedInventory = victim.getInventory().getContents();
//            victim.getInventory().clear();
//            Singleton.getSingleInstance().clearedPlayerInventories.put(Utilities.getSingleInstance().uuidOrName(victim.getPlayer(), TrollCore.instance.getServer().getOnlineMode()), victim);
//            int seconds = 10;
//
//            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TrollCore.instance, () -> {
//                victim.getInventory().setContents(savedInventory);
//                Singleton.getSingleInstance().clearedPlayerInventories.remove(Utilities.getSingleInstance().uuidOrName(victim.getPlayer(), TrollCore.instance.getServer().getOnlineMode()));
//            }, ((long) seconds * TrollCore.instance.config.getInt("MenuItems.trollMenu.trolls.fakeClear.options.fakeClearDelay")));
//        }
//    }
//}
