package me.xapu1337.recodes.trollgui.trolls;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import me.xapu1337.recodes.trollgui.inventories.MenuSelectionInventory;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollAttributes;
import me.xapu1337.recodes.trollgui.types.TrollMetaData;
import me.xapu1337.recodes.trollgui.utilities.MessageUtils;
import me.xapu1337.recodes.trollgui.utilities.ItemStackBuilder;
import me.xapu1337.recodes.trollgui.utilities.Utils;
import org.bukkit.Bukkit;
import org.bukkit.World;

import javax.swing.*;
import java.util.List;

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
        getCaller().sendMessage(MessageUtils.getInstance().$("{config:messages.selectDimensionToTeleport}"));
        getCaller().sendMessage(" \n ");
        getCaller().openInventory(
                new MenuSelectionInventory
                        .Builder()
                        .item(new ItemStackBuilder(XMaterial.GRASS)
                                .withDisplayName("{config:menus.dimension-selector.overworld.name}")
                                .withLore(List.of("{config:menus.dimension-selector.overworld.lore"))
                                .build(), "world"
                        )
                        .item(new ItemStackBuilder(XMaterial.NETHERRACK)
                                .withDisplayName("{config:menus.dimension-selector.nether.name}")
                                .withLore(List.of("{config:menus.dimension-selector.nether.lore"))
                                .build(), "world_nether"
                        )
                        .item(new ItemStackBuilder(XMaterial.END_STONE)
                                .withDisplayName("{config:menus.dimension-selector.end.name}")
                                .withLore(List.of("{config:menus.dimension-selector.end.lore"))
                                .build(), "world_theend"
                        )
                        .onClick( (caller, clickedItemName) -> {
                            MessageUtils.getInstance().setClassPlaceholders(this.getClass(), "WORLD_NAME", clickedItemName);
                            World selectedWorld = Bukkit.getWorld(clickedItemName);
                            if (selectedWorld == null) {
                                getCaller().playSound(getCaller().getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 0.5f, 0.5f);
                                getCaller().sendMessage(MessageUtils.getInstance().$("The world {WORLD_NAME} does not exist. (has it been loaded yet?)"));
                                return;
                            }
                            if (Utils.getInstance().teleportTo(selectedWorld, getVictim(), getVictim().getLocation().getX(), getVictim().getLocation().getZ()))
                                getCaller().sendMessage(MessageUtils.getInstance().$("{config:messages.teleportedToDimension}").replaceAll("%PLAYER%", getVictim().getDisplayName()).replaceAll("%DIMENSION%", selectedWorld.getName().toUpperCase()));
                            else
                                getCaller().sendMessage(MessageUtils.getInstance().$("{config:messages.teleportFailed}"));
                        })
                        .build("test")
                        .getInventory()
        );
    }
}

