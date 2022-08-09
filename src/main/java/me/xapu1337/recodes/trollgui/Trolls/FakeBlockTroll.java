package me.xapu1337.recodes.trollgui.Trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Cores.TrollCore;
import me.xapu1337.recodes.trollgui.Enums.TrollAttributes;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Types.TrollItemMetaData;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class FakeBlockTroll extends TrollHandler {

    // Variables

    XMaterial customMatForFakeBlock = XMaterial.matchXMaterial(TrollCore.instance.getConfig().getString("MenuItems.trollMenu.trolls.fakeBlock.options.block")).orElse(XMaterial.TNT);

    @Override
    public TrollItemMetaData setMetaData() {
        return (
                new TrollItemMetaData()
                        .setItem(customMatForFakeBlock)
                        .setConfigData("fakeBlock")
                        .setAttributes( TrollAttributes.POSSIBLE_CRASH_OR_FREEZE)

        );
    }

    @Override
    public void onServerDisable() {
        super.onServerDisable();
        customMatForFakeBlock = null;
    }

    /**
     * Executed from the TrollGUI Class everything inside this function gets executed.
     */
    @Override
    public void execute() {
        int rad = TrollCore.instance.config.getInt("MenuItems.trollMenu.fakeBlock.options.radius");
        String v = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        int subVersion = Integer.parseInt(v.replace("1_", "").replaceAll("_R\\d", "").replace("v", ""));


        try{
            for(double x = victim.getLocation().getX() - rad; x <= victim.getLocation().getX() + rad; x++){
                for(double y = victim.getLocation().getY() - rad; y <= victim.getLocation().getY() + rad; y++){
                    for(double z = victim.getLocation().getZ() - rad; z <= victim.getLocation().getZ() + rad; z++){
                        Location l = new Location(victim.getWorld(), x,y,z);
                        if(l.getBlock().getType() != XMaterial.AIR.parseMaterial()){
                            if(subVersion >= 13){
                                victim.sendBlockChange(l, customMatForFakeBlock.parseMaterial().createBlockData());
                            } else {
                                victim.sendBlockChange(l, customMatForFakeBlock.parseMaterial(), (byte) 0);
                            }
                        }
                    }
                }

            }

        }catch (Exception ignored){

        }
    }
}
