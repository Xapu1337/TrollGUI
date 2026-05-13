package me.xapu1337.recodes.trollgui.trolls;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;

import me.xapu1337.recodes.trollgui.cores.TrollCore;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollName;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@TrollName("lavaBed")
public class LavaBedTroll extends Troll {

    @Override
    public void execute() {
        Location bottom = getVictim().getLocation();
        List<Location> lavaLocations = new ArrayList<>();

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Location loc = bottom.clone().add(x, -1, z);
                lavaLocations.add(loc.clone());
                loc.getBlock().setType(Objects.requireNonNull(XMaterial.LAVA.parseMaterial()));
            }
        }

        getVictim().playSound(getVictim().getLocation(),
                Objects.requireNonNull(XSound.BLOCK_FIRE_EXTINGUISH.parseSound()), 3f, 1f);

        Bukkit.getScheduler().runTaskLater(TrollCore.getInstance(),
                () -> lavaLocations.forEach(loc -> loc.getBlock().setType(Material.AIR)),
                5 * 20L);
    }

}
