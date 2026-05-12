package me.xapu1337.recodes.trollgui.trolls;

import com.cryptomorin.xseries.XMaterial;

import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollAttributes;
import me.xapu1337.recodes.trollgui.types.TrollMetaData;
import me.xapu1337.recodes.trollgui.utilities.Services;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

public class FakeBlockTroll extends Troll {

    private XMaterial customMatForFakeBlock;
    private Material resolvedMaterial;

    @Override
    public void injectServices(Services services) {
        super.injectServices(services);
        String configBlock = services.config()
                .getString("menus.troll-menu.items.trolls.fakeBlock.options.block", "TNT");
        this.customMatForFakeBlock = XMaterial.matchXMaterial(configBlock != null ? configBlock : "TNT")
                .orElse(XMaterial.TNT);
        Material parsed = customMatForFakeBlock.parseMaterial();
        this.resolvedMaterial = parsed != null ? parsed : Material.TNT;
    }

    @Override
    public TrollMetaData setMetaData() {
        return (new TrollMetaData(customMatForFakeBlock, services)
                .setTrollName("fakeBlock")
                .setAttributes(TrollAttributes.POSSIBLE_CRASH_OR_FREEZE));
    }

    @Override
    public void execute() {
        int rad = services.config().getInt("menus.troll-menu.items.trolls.fakeBlock.options.radius");
        BlockData fakeBlockData = resolvedMaterial.createBlockData();

        try {
            for (double x = getVictim().getLocation().getX() - rad; x <= getVictim().getLocation().getX() + rad; x++) {
                for (double y = getVictim().getLocation().getY() - rad; y <= getVictim().getLocation().getY()
                        + rad; y++) {
                    for (double z = getVictim().getLocation().getZ() - rad; z <= getVictim().getLocation().getZ()
                            + rad; z++) {
                        Location l = new Location(getVictim().getWorld(), x, y, z);
                        if (l.getBlock().getType() != Material.AIR) {
                            getVictim().sendBlockChange(l, fakeBlockData);
                        }
                    }
                }
            }
        } catch (Exception e) {
            services.debug().log("FakeBlockTroll failed for " + getVictim().getName() + ": " + e.getMessage());
        }
    }
}
