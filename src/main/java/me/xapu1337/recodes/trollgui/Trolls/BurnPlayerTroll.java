package me.xapu1337.recodes.trollgui.Trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Cores.Core;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Handlers.TrollItemMetaData;
import org.bukkit.entity.Player;

public class BurnPlayerTroll extends TrollHandler {


    @Override
    public TrollItemMetaData setMetaData() {
        return
                new TrollItemMetaData()
                        .setItem(XMaterial.BLAZE_POWDER)
                        .setConfigData("burnPlayer");
    }

    @Override
    public void execute() {
        victim.setFireTicks(Core.instance.getConfig().getInt("MenuItems.trollMenu.burnPlayer.options.burnTime") * 20);
    }
}
