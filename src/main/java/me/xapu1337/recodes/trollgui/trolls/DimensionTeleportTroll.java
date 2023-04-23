package me.xapu1337.recodes.trollgui.trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.inventories.MenuSelectionInventory;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollAttributes;
import me.xapu1337.recodes.trollgui.types.TrollMetaData;
import me.xapu1337.recodes.trollgui.utilities.ConfigUtils;
import me.xapu1337.recodes.trollgui.utilities.Utils;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class DimensionTeleportTroll extends Troll {

    /**
     * @return
     */
    @Override
    public TrollMetaData setMetaData() {
        return (
                new TrollMetaData(XMaterial.ENDER_PEARL)
                        .setTrollName("dimensionTeleport")
                        .setAttributes( TrollAttributes.POSSIBLE_DEATH_OR_ITEM_LOSS )
                );
    }

    /**
     * The method that gets executed on item click
     */
    @Override
    public void execute() {
        getCaller().sendMessage(" \n ");
        getCaller().sendMessage(ConfigUtils.getInstance().$("{config:messages.selectDimensionToTeleport}"));
        getCaller().sendMessage(" \n ");
        new MenuSelectionInventory()
                .newItem()
                .setMaterial(
                        XMaterial.GRASS
                )
                .setDisplayName(
                       ConfigUtils.getInstance().$("{config:menus.dimension-selector.overworld.name}")
                )
                .setLore(
                        ConfigUtils.getInstance().$("{config:menus.dimension-selector.overworld.lore}")
                )
                .setItemID("world")
                .finishItem()
                .newItem()
                .setMaterial(
                        XMaterial.NETHERRACK
                )
                .setDisplayName(
                        ConfigUtils.getInstance().$("{config:menus.dimension-selector.nether.name}")
                )
                .setLore(
                        ConfigUtils.getInstance().$("{config:menus.dimension-selector.nether.lore}")
                )
                .setItemID("world_nether")
                .finishItem()
                .newItem()
                .setMaterial(
                        XMaterial.ENDER_EYE
                )
                .setDisplayName(
                        ConfigUtils.getInstance().$("{config:menus.dimension-selector.end.name}")
                )
                .setLore(
                        ConfigUtils.getInstance().$("{config:menus.dimension-selector.end.lore}")
                )
                .setItemID("world_the_end")
                .finishItem()
                .setTitle(
                        ConfigUtils.getInstance().$("{config:menus.dimension-selector.title}")
                )
                .setCallback( (itemStack, id) ->
                {
                    World selectedWorld = Bukkit.getWorld(id);
                    if (selectedWorld == null)
                        Bukkit.getLogger().warning("[MS3] The world " + id + " does not exist!");
                    else
                        if (Utils.getInstance().teleportTo(selectedWorld, getVictim(), getVictim().getLocation().getX(), getVictim().getLocation().getZ()))
                            getCaller().sendMessage(ConfigUtils.getInstance().$("{config:messages.teleportedToDimension}").replaceAll("%PLAYER%", getVictim().getDisplayName()).replaceAll("%DIMENSION%", selectedWorld.getName().toUpperCase()));
                        else
                            getCaller().sendMessage(ConfigUtils.getInstance().$("{config:messages.teleportFailed}"));
                }
                )
                .openForPlayer(getCaller())
                .setInventoryHolderClass(this.getCallingGUI());
    }
}

