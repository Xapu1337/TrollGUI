//package me.xapu1337.recodes.trollgui.trolls;
//
//import com.cryptomorin.xseries.XMaterial;
//
//import me.xapu1337.recodes.trollgui.Handlers.MenuSelectionHandler;
//import me.xapu1337.recodes.trollgui.Handlers.Troll;
//import me.xapu1337.recodes.trollgui.Types.ClutchItem;
//import me.xapu1337.recodes.trollgui.Types.TrollMetaData;
//import org.bukkit.ChatColor;
//import org.bukkit.Location;
//import org.bukkit.inventory.ItemStack;
//
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Objects;
//
//import static me.xapu1337.recodes.trollgui.Cores.TrollCore.instance;
//import static me.xapu1337.recodes.trollgui.Utilities.Utilities.getSingleInstance;
//
//public class ClutchesTroll extends Troll {
//
//
//    // Variables
//
//    List<LinkedHashMap< String, LinkedHashMap<String, String> > > clutchesList;
//    List<ClutchItem> finalClutchItems = new ArrayList<>();
//    List<ClutchItem> clutchItems = new ArrayList<>();
//    MenuSelectionHandler clutchMenuHandler = new MenuSelectionHandler();
//    @Override
//    public void onServerDisable() {
//        super.onServerDisable();
//        getSingleInstance().cleanCollectionIfPossible(clutchesList    );
//        getSingleInstance().cleanCollectionIfPossible(finalClutchItems);
//        getSingleInstance().cleanCollectionIfPossible(clutchItems     );
//        clutchMenuHandler = null;
//
//    }
//
//    @Override
//    public TrollMetaData setMetaData() {
//        return (
//                new TrollMetaData(XMaterial.WATER_BUCKET)
//                        .setTrollName("clutches")
//                        .setAttributes( TrollAttributes.POSSIBLE_DEATH_OR_ITEM_LOSS )
//        );
//    }
//
//    /**
//     * The method that gets executed on item click
//     */
//    @Override
//    public void execute() {
//        clutchMenuHandler
//                .setTitle(getSingleInstance()
//                        .getConfigPath("MenuItems.clutchesMenu.title"));
//
//        clutchesList =  Objects.requireNonNull(instance.config.getList("MenuItems.clutchesMenu.clutches")).stream().distinct().map(o -> (LinkedHashMap<String, LinkedHashMap<String, String>>) o).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
//
//        finalClutchItems = clutchItems;
//        // O(n^(n * 3)) AHHHHHH! NOOOOOOOOOOOOOO!
//
//        for (LinkedHashMap<String, LinkedHashMap<String, String>> clutch : clutchesList) {
//            clutch.forEach(
//                    (clutchName, clutchProperties) -> {
//                        ClutchItem item = new ClutchItem();
//                        item.name = clutchProperties.get("name");
//                        item.lore = clutchProperties.get("lore");
//                        item.type = clutchProperties.get("type");
//
//                        clutchMenuHandler.addNewItemFixed(
//                                ChatColor.stripColor(item.name).toUpperCase() + item.type,
//                                item.name,
//                                XMaterial.matchXMaterial(item.type).orElse(XMaterial.WATER_BUCKET),
//                                item.lore
//                        );
//
//                        if (finalClutchItems.contains(item)) {
//                            return;
//                        } else {
//                            finalClutchItems.add(item);
//                        }
//                    }
//            );
//        };
//
//
//        // trim clutchItems to only show the first 9 items
//        if (clutchItems.size() > 9) {
//            clutchItems = clutchItems.subList(0, 9);
//        }
//
//        clutchMenuHandler.setCallback(
//                (itemStack, itemID) -> {
//                    // Launch player up to the height of the world
//                    victim.teleport(new Location(victim.getWorld(), victim.getLocation().getX(), victim.getWorld().getMaxHeight(), victim.getLocation().getZ()));
//                    // Set player selected item to the first item hotbar slot
//                    victim.getInventory().setHeldItemSlot(0);
//                    // Strip name and lore of itemstack meta
//                    ItemStack itemStackTrimmed = new ItemStack(itemStack);
//                    itemStackTrimmed.setItemMeta(null);
//
//                    victim.getInventory().setItem(0, itemStackTrimmed);
//                }
//        );
//
//        clutchMenuHandler.build();
//
//        clutchMenuHandler.openForPlayer(caller);
//
//    }
//}
