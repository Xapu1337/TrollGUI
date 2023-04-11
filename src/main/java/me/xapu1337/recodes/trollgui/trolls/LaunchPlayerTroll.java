package me.xapu1337.recodes.trollgui.trolls;

import com.cryptomorin.xseries.XMaterial;


import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollAttributes;
import me.xapu1337.recodes.trollgui.types.TrollMetaData;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

public class LaunchPlayerTroll extends Troll {


    // Variables
    private Firework firework;
    private FireworkMeta fireworkMeta;
    @Override
    public TrollMetaData setMetaData() {
        return (
                new TrollMetaData(XMaterial.FIREWORK_ROCKET)
                        .setTrollName("launchPlayer")
                        .setAttributes( TrollAttributes.POSSIBLE_DEATH_OR_ITEM_LOSS )

        );
    }



    @Override
    public void execute() {
        Vector upVec = new Vector(0, 25, 0);

        Location fireworkSpawn = getVictim().getLocation().add(upVec);

        getVictim().setVelocity(upVec);

        firework = (Firework) fireworkSpawn.getWorld().spawnEntity(fireworkSpawn, EntityType.FIREWORK);

        fireworkMeta = firework.getFireworkMeta();

        fireworkMeta.addEffect(
                FireworkEffect
                        .builder()
                        .trail(true)
                        .withColor( Color.fromRGB(0xFF5F0F)   )
                        .withColor( Color.fromRGB(0xD7FF0F)   )
                        .withColor( Color.fromRGB(0xFFB259)   )
                        .withFade ( Color.fromRGB(0xFFD7B0)   )
                        .with     ( FireworkEffect.Type.STAR  )
                        .build()
        );

        firework.setFireworkMeta(fireworkMeta);

        firework.setVelocity(new Vector(0, Math.E - 1, 0));
    }
}
