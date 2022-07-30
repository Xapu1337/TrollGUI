package me.xapu1337.recodes.trollgui.Trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Cores.TrollCore;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Types.TrollItemMetaData;
import me.xapu1337.recodes.trollgui.Utilities.Singleton;
import me.xapu1337.recodes.trollgui.Utilities.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public class FakeClearTroll extends TrollHandler {


    @Override
    public TrollItemMetaData setMetaData() {
        return (
                new TrollItemMetaData()
                        .setItem(XMaterial.PUFFERFISH)
                        .setConfigData("fakeClear")
                        .formatPlaceholders("%TIME%", String.valueOf(TrollCore.instance.getConfig().getInt("MenuItems.trollMenu.fakeClear.options.fakeClearDelay")))

        );
    }


    /**
     * the:
     * <br />
     * <code>
     * if(Utilities.getSingleInstance().uuidOrName(victim.getPlayer(), Core.instance.getServer().getOnlineMode()))...
     * </code>
     * <br />
     * may seem weird, but basically I'm making it offline & online mode friendly and avoiding issues.
     */
    @Override
    public void execute() {
        if(!Singleton.getSingleInstance().clearedPlayerInventories.containsKey(Utilities.getSingleInstance().uuidOrName(victim.getPlayer(), TrollCore.instance.getServer().getOnlineMode()))) {
            ItemStack[] savedInventory = victim.getInventory().getContents();
            victim.getInventory().clear();
            Singleton.getSingleInstance().clearedPlayerInventories.put(Utilities.getSingleInstance().uuidOrName(victim.getPlayer(), TrollCore.instance.getServer().getOnlineMode()), "1");
            int seconds = 10;

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TrollCore.instance, () -> {
                victim.getInventory().setContents(savedInventory);
                Singleton.getSingleInstance().clearedPlayerInventories.remove(Utilities.getSingleInstance().uuidOrName(victim.getPlayer(), TrollCore.instance.getServer().getOnlineMode()));
            }, ((long) seconds * TrollCore.instance.config.getInt("MenuItems.trollMenu.fakeClear.options.fakeClearDelay")));
        }
    }
}
