package me.xapu1337.recodes.trollgui.Trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Cores.TrollCore;
import me.xapu1337.recodes.trollgui.Enums.TrollAttributes;
import me.xapu1337.recodes.trollgui.Handlers.MenuSelectionHandler;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Types.ClutchItem;
import me.xapu1337.recodes.trollgui.Types.TrollItemMetaData;
import me.xapu1337.recodes.trollgui.Utilities.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ClutchesTroll extends TrollHandler {


    @Override
    public TrollItemMetaData setMetaData() {
        return (
                new TrollItemMetaData()
                        .setItem(XMaterial.WATER_BUCKET)
                        .setConfigData("clutches")
                        .setAttributes( TrollAttributes.POSSIBLE_DEATH_OR_ITEM_LOSS )
                );
    }

    /**
     * The method that gets executed on item click
     */
    @Override
    public void execute() {
        MenuSelectionHandler clutchMenu = new MenuSelectionHandler()
                .setTitle(Utilities.getSingleInstance().getConfigPath("MenuItems.clutchesMenu.title"));

        List<ClutchItem> clutchItems = new ArrayList<>();

        List<ClutchItem> finalClutchItems = clutchItems;
        // O(n^(n * 3)) AHHHHHH! NOOOOOOOOOOOOOO!
        // remove duplicates from the list.

        for (LinkedHashMap<String, LinkedHashMap<String, String>> clutch : TrollCore.instance.config.getList("MenuItems.clutchesMenu.clutches").stream().distinct().map(o -> (LinkedHashMap<String, LinkedHashMap<String, String>>) o).toList()) {
            clutch.forEach(
                    (clutchName, clutchProperties) -> {
                        ClutchItem item = new ClutchItem();
                        item.name = clutchProperties.get("name");
                        item.lore = clutchProperties.get("lore");
                        item.type = clutchProperties.get("type");

                        clutchMenu.addNewItemFixed(
                                ChatColor.stripColor(item.name).toUpperCase() + item.type,
                                item.name,
                                XMaterial.matchXMaterial(item.type).orElse(XMaterial.WATER_BUCKET),
                                item.lore
                        );

                        if (finalClutchItems.contains(item)) {
                            return;
                        } else {
                            finalClutchItems.add(item);
                        }
                    }
            );
        };


        // trim clutchItems to only show the first 9 items
        if (clutchItems.size() > 9) {
            clutchItems = clutchItems.subList(0, 9);
        }

        clutchMenu.setCallback(
                (itemStack, itemID) -> {
                    // Launch player up to the height of the world
                    victim.teleport(new Location(victim.getWorld(), victim.getLocation().getX(), victim.getWorld().getMaxHeight(), victim.getLocation().getZ()));
                    // Set player selected item to the first item hotbar slot
                    victim.getInventory().setHeldItemSlot(0);
                    // Strip name and lore of itemstack meta
                    ItemStack itemStackTrimmed = new ItemStack(itemStack);
                    itemStackTrimmed.setItemMeta(null);

                    victim.getInventory().setItem(0, itemStackTrimmed);
                }
        );

        clutchMenu.build();

        clutchMenu.openForPlayer(caller);

    }
}
