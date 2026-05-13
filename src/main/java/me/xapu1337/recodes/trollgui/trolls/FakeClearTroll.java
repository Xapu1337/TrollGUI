package me.xapu1337.recodes.trollgui.trolls;


import me.xapu1337.recodes.trollgui.cores.TrollCore;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollName;
import me.xapu1337.recodes.trollgui.utilities.TrollVariableStorage;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

@TrollName("fakeClear")
public class FakeClearTroll extends Troll {

    @Override
    public void execute() {
        // if(!Singleton.getSingleInstance().clearedPlayerInventories.containsKey(Utilities.getSingleInstance().uuidOrName(victim.getPlayer(),
        // TrollCore.instance.getServer().getOnlineMode()))) {
        // savedInventory = victim.getInventory().getContents();
        // victim.getInventory().clear();
        if (services.toggles().toggle(getVictim().getUniqueId(), getTrollMetaData().getTrollName())) {
            TrollVariableStorage.setPermanentVariable(
                    getVictim().getUniqueId() + "-FC-" + getTrollMetaData().getTrollName(),
                    getVictim().getInventory().getContents());
            getVictim().getInventory().clear();

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TrollCore.getInstance(), () -> {
                getVictim().getInventory().setContents((ItemStack[]) TrollVariableStorage
                        .getPermanentVariable(getVictim().getUniqueId() + "-FC-" + getTrollMetaData().getTrollName()));
                TrollVariableStorage.removePermanentVariable(
                        getVictim().getUniqueId() + "-FC-" + getTrollMetaData().getTrollName());
                services.toggles().removePlayer(getVictim().getUniqueId());
            }, ((long) 10 * services.config()
                    .getInt("menus.troll-menu.items.trolls.fakeClear.options.fakeClearDelay")));
        }

    }
}
