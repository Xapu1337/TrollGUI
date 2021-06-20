package me.xapu1337.recodes.trollgui.Trolls;

import com.cryptomorin.xseries.XItemStack;
import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Cores.Core;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class FakeBlockTroll extends TrollHandler {
    public FakeBlockTroll(Player caller, Player victim) {
        super(caller, victim);
    }

    /**
     * Executed from the TrollGUI Class everything inside this function gets executed.
     */
    @Override
    public void execute() {
        int rad = Core.instance.config.getInt("MenuItems.trollMenu.fakeBlock.options.radius");
        String v = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        int subVersion = Integer.parseInt(v.replace("1_", "").replaceAll("_R\\d", "").replace("v", ""));

        XMaterial customMatForFakeBlock;

        if(XMaterial.matchXMaterial(Objects.requireNonNull(Core.instance.config.getString("MenuItems.trollMenu.fakeBlock.options.block"))).isPresent())
            customMatForFakeBlock = XMaterial.matchXMaterial(Objects.requireNonNull(Core.instance.config.getString("MenuItems.trollMenu.fakeBlock.options.block"))).get();
        else
            customMatForFakeBlock = XMaterial.TNT;
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
