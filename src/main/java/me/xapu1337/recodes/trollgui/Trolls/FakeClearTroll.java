package me.xapu1337.recodes.trollgui.Trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Cores.Core;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Handlers.TrollItemMetaData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class FakeClearTroll extends TrollHandler {


    @Override
    public TrollItemMetaData setMetaData() {
        return (
                new TrollItemMetaData()
                        .setItem(XMaterial.PUFFERFISH)
                        .setConfigData("fakeClear")
                        .formatPlaceholders("%TIME%", String.valueOf(Core.instance.getConfig().getInt("MenuItems.trollMenu.fakeClear.options.fakeClearDelay")))

        );
    }


    /**
     * the:
     * <br />
     * <code>
     * if(Core.instance.utils.uuidOrName(victim.getPlayer(), Core.instance.getServer().getOnlineMode()))...
     * </code>
     * <br />
     * may seem weird, but basically I'm making it offline & online mode friendly and avoiding issues.
     */
    @Override
    public void execute() {
        if(!Core.instance.singletons.clearedPlayerInventories.containsKey(Core.instance.utils.uuidOrName(victim.getPlayer(), Core.instance.getServer().getOnlineMode()))) {
            ItemStack[] savedInventory = victim.getInventory().getContents();
            victim.getInventory().clear();
            Core.instance.singletons.clearedPlayerInventories.put(Core.instance.utils.uuidOrName(victim.getPlayer(), Core.instance.getServer().getOnlineMode()), "1");
            int seconds = 10;

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Core.instance, () -> {
                victim.getInventory().setContents(savedInventory);
                Core.instance.singletons.clearedPlayerInventories.remove(Core.instance.utils.uuidOrName(victim.getPlayer(), Core.instance.getServer().getOnlineMode()));
            }, ((long) seconds * Core.instance.config.getInt("MenuItems.trollMenu.fakeClear.options.fakeClearDelay")));
        }
    }
}
