package me.xapu1337.recodes.trollgui.Trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Enums.TrollAttributes;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Types.TrollItemMetaData;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

public class LaunchPlayerTroll extends TrollHandler {

    @Override
    public TrollItemMetaData setMetaData() {
        return (
                new TrollItemMetaData()
                        .setItem(XMaterial.FIREWORK_ROCKET)
                        .setConfigData("launchPlayer")
                        .setAttributes( TrollAttributes.POSSIBLE_DEATH_OR_ITEM_LOSS )

        );
    }


    @Override
    public void execute() {
        Vector upVec = new Vector(0, 25, 0);

        Location fireworkSpawn = victim.getLocation().add(upVec);

        victim.setVelocity(upVec);

        Firework firework = (Firework) fireworkSpawn.getWorld().spawnEntity(fireworkSpawn, EntityType.FIREWORK);

        FireworkMeta fireworkMeta = firework.getFireworkMeta();

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
