package me.xapu1337.recodes.trollgui.Trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Enums.TrollAttributes;
import me.xapu1337.recodes.trollgui.Handlers.MenuSelectionHandler;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Types.TrollItemMetaData;
import me.xapu1337.recodes.trollgui.Utilities.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class DimensionTeleportTroll extends TrollHandler{

    /**
     * @return
     */
    @Override
    public TrollItemMetaData setMetaData() {
        return (
                new TrollItemMetaData()
                        .setItem(XMaterial.ENDER_PEARL)
                        .setConfigData("dimensionTeleport")
                        .setAttributes( TrollAttributes.POSSIBLE_DEATH_OR_ITEM_LOSS )
                );
    }

    /**
     * The method that gets executed on item click
     */
    @Override
    public void execute() {
        caller.sendMessage(" \n ");
        caller.sendMessage(Utilities.getSingleInstance().getConfigPath("Messages.selectDimensionToTeleport", true));
        caller.sendMessage(" \n ");
        new MenuSelectionHandler()
                .newItem()
                .setMaterial(
                        XMaterial.GRASS
                )
                .setDisplayName(
                        Utilities.getSingleInstance().getConfigPath("MenuItems.dimensionSelector.overworld.name")
                )
                .setLore(
                        Utilities.getSingleInstance().getConfigPath("MenuItems.dimensionSelector.overworld.lore")
                )
                .setItemID("world")
                .finishItem()
                .newItem()
                .setMaterial(
                        XMaterial.NETHERRACK
                )
                .setDisplayName(
                        Utilities.getSingleInstance().getConfigPath("MenuItems.dimensionSelector.nether.name")
                )
                .setLore(
                        Utilities.getSingleInstance().getConfigPath("MenuItems.dimensionSelector.nether.lore")
                )
                .setItemID("world_nether")
                .finishItem()
                .newItem()
                .setMaterial(
                        XMaterial.ENDER_EYE
                )
                .setDisplayName(
                        Utilities.getSingleInstance().getConfigPath("MenuItems.dimensionSelector.end.name")
                )
                .setLore(
                        Utilities.getSingleInstance().getConfigPath("MenuItems.dimensionSelector.end.lore")
                )
                .setItemID("world_the_end")
                .finishItem()
                .setTitle(
                        Utilities.getSingleInstance().getConfigPath("MenuItems.dimensionSelector.title")
                )
                .setCallback( (itemStack, id) ->
                {
                    World selectedWorld = Bukkit.getWorld(id);
                    if (selectedWorld == null)
                        Bukkit.getLogger().warning("[MS3] The world " + id + " does not exist!");
                    else
                        if(Utilities.getSingleInstance().teleportTo(selectedWorld, victim, victim.getLocation().getX(), victim.getLocation().getZ()))
                            caller.sendMessage(Utilities.getSingleInstance().getConfigPath("Messages.teleportedToDimension", true).replaceAll("%PLAYER%", victim.getDisplayName()).replaceAll("%DIMENSION%", selectedWorld.getName().toUpperCase()));
                        else
                            caller.sendMessage(Utilities.getSingleInstance().getConfigPath("Messages.teleportFailed", true));
                }
                ).openForPlayer(caller).setInventoryHolderClass(this.getGUI());
    }
}

