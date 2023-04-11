package me.xapu1337.recodes.trollgui.trolls;

import com.cryptomorin.xseries.XMaterial;

import me.xapu1337.recodes.trollgui.cores.TrollCore;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollAttributes;
import me.xapu1337.recodes.trollgui.types.TrollMetaData;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class FakeBlockTroll extends Troll {

    // Variables

    XMaterial customMatForFakeBlock = XMaterial.matchXMaterial(TrollCore.getInstance().getConfig().getString("menus.troll-menu.items.trolls.fakeBlock.options.block")).orElse(XMaterial.TNT);

    @Override
    public TrollMetaData setMetaData() {
        return (
                new TrollMetaData(customMatForFakeBlock)
                        .setTrollName("fakeBlock")
                        .setAttributes( TrollAttributes.POSSIBLE_CRASH_OR_FREEZE)

        );
    }


    /**
     * Executed from the TrollGUI Class everything inside this function gets executed.
     */
    @Override
    public void execute() {
        int rad = TrollCore.getInstance().getConfig().getInt("menus.troll-menu.items.trolls.fakeBlock.options.radius");
        String v = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        int subVersion = Integer.parseInt(v.replace("1_", "").replaceAll("_R\\d", "").replace("v", ""));

        try{
            for(double x = getVictim().getLocation().getX() - rad; x <= getVictim().getLocation().getX() + rad; x++){
                for(double y = getVictim().getLocation().getY() - rad; y <= getVictim().getLocation().getY() + rad; y++){
                    for(double z = getVictim().getLocation().getZ() - rad; z <= getVictim().getLocation().getZ() + rad; z++){
                        Location l = new Location(getVictim().getWorld(), x,y,z);
                        if(l.getBlock().getType() != XMaterial.AIR.parseMaterial()){
                            if(subVersion >= 13){
                                getVictim().sendBlockChange(l, customMatForFakeBlock.parseMaterial().createBlockData());
                            } else {
                                getVictim().sendBlockChange(l, customMatForFakeBlock.parseMaterial(), (byte) 0);
                            }
                        }
                    }
                }

            }

        }catch (Exception ignored){

        }
    }
}
