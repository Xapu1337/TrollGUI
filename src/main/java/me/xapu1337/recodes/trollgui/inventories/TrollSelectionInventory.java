package me.xapu1337.recodes.trollgui.inventories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.cryptomorin.xseries.XMaterial;
import com.google.gson.Gson;
import me.xapu1337.recodes.trollgui.cores.TrollCore;
import me.xapu1337.recodes.trollgui.handlers.PaginationHandler;
import me.xapu1337.recodes.trollgui.loaders.TrollLoader;
import me.xapu1337.recodes.trollgui.types.PaginationItemType;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollMetaData;
import me.xapu1337.recodes.trollgui.utilities.DebuggingUtil;
import me.xapu1337.recodes.trollgui.utilities.InventoryBuilder;
import me.xapu1337.recodes.trollgui.utilities.ItemStackBuilder;
import me.xapu1337.recodes.trollgui.utilities.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class TrollSelectionInventory implements Listener, InventoryHolder {

    private final Inventory inventory;
    private final PaginationHandler paginationHandler;
    public Player caller, victim;

    private final InventoryBuilder builder = new InventoryBuilder();
    public TrollSelectionInventory(Player caller, Player target)  {
        this.paginationHandler = new PaginationHandler(54);

        this.caller = caller;
        this.victim = target;

        Bukkit.getPluginManager().registerEvents(this, TrollCore.getInstance());

        builder.setSize(54)
                .setPattern(
                        "BBBBBBBBB",
                        "BXXXXXXXB",
                        "BXXXXXXXB",
                        "BXXXXXXXB",
                        "BXXXXXXXB",
                        "BBB<C>BBR"
                )
                .withDefaults()
                .setItem('>', PaginationItemType.NEXT_PAGE.getItemStack())
                .setItem('<', PaginationItemType.PREVIOUS_PAGE.getItemStack())
                .setItem('C', PaginationItemType.CLOSE.getItemStack())
                .setItem('R', new ItemStackBuilder(XMaterial.JUKEBOX).withDisplayName("Random").build())
                .setInventoryContents((inventory) -> {
                    DebuggingUtil.getInstance().l(Arrays.toString(inventory.getContents()));
                    paginationHandler.setMaxPage((int) Math.ceil((double) TrollLoader.getInstance().getTrolls().size() / 28));
                    if (paginationHandler.getCurrentPage() > paginationHandler.getMaxPage()) paginationHandler.setCurrentPage(paginationHandler.getMaxPage());
                    if (paginationHandler.getCurrentPage() < 1) paginationHandler.setCurrentPage(paginationHandler.getCurrentPage() + 1);

                    int startIndex = (paginationHandler.getCurrentPage() - 1) * 28;
                    int endIndex = startIndex + 28;
                    DebuggingUtil.getInstance().l("Start: " + startIndex + " End: " + endIndex);
                    List<ItemStack> items = TrollLoader.getInstance().getTrolls().stream()
                            .map(Troll::getTrollMetaData)
                            .map(TrollMetaData::getItem)
                            .filter(Objects::nonNull)
                            .toList();
                    DebuggingUtil.getInstance().l(Arrays.toString(items.toArray()));
                    int totalPages = (int) Math.ceil((double) items.size() / 28);
                    DebuggingUtil.getInstance().l("Total pages: " + totalPages);
                    if (paginationHandler.getCurrentPage() > totalPages) paginationHandler.setCurrentPage(totalPages);
                    DebuggingUtil.getInstance().l("Current page: " + paginationHandler.getCurrentPage());
                    ItemStack[] inventoryContents = inventory.getContents();
                    for (int i = startIndex; i < endIndex && i < inventoryContents.length; i++) {
                        DebuggingUtil.getInstance().l("Index: " + i);
                        ItemStack item = items.size() > i ? items.get(i) : null;

                        DebuggingUtil.getInstance().l("Fetching item... " + item + " " + i);
                        if (item == null) continue;
                        DebuggingUtil.getInstance().l("Item: " + item);
                        int[] emptySlots = builder.getItemSlots('X');
                        DebuggingUtil.getInstance().l("Slots: " + Arrays.toString(emptySlots) + " Length: " + emptySlots.length);
                        if (emptySlots.length == 0) {
                            DebuggingUtil.getInstance().l("One slot is left, breaking...");
                            emptySlots = builder.getItemSlots('X');
                            DebuggingUtil.getInstance().l("Slots: " + Arrays.toString(emptySlots) + " Length: " + emptySlots.length);
                            if (emptySlots.length == 0) break;
                            DebuggingUtil.getInstance().l("Item: " + item);
                        }
                        inventory.setItem(emptySlots[0], item);
                    }
        //        int endIndex = startIndex + 38;
        //        if (endIndex > TrollLoader.getInstance().getTrolls().size()) endIndex = TrollLoader.getInstance().getTrolls().size();
        //        DebuggingUtil.getInstance().l(String.valueOf(TrollLoader.getInstance().getTrolls().size()));
        //        DebuggingUtil.getInstance().l("Start index: " + startIndex + " End index: " + endIndex);
        //
        //        for (int i = 11; i < 38; i++) {
        //            int index = startIndex + i;
        //            DebuggingUtil.getInstance().l("Index: " + index);
        //            DebuggingUtil.getInstance().l("Index i: " + i);
        //
        //            if (index < endIndex && index >= startIndex) {
        //                Troll trollClass = TrollLoader.getInstance().getTrolls().get(index);
        //
        //                DebuggingUtil.getInstance().l(new Gson().toJson(TrollLoader.getInstance().getTrolls()));
        //                DebuggingUtil.getInstance().l(trollClass.getTrollMetaData().getTrollName());
        //                trollClass.setCaller(caller).setVictim(victim).Init();
        //                DebuggingUtil.getInstance().l("Setting item " + trollClass.getTrollMetaData().getItem().getItemMeta().getDisplayName() + " to slot " + i);
        //                this.inventory.setItem(i, trollClass.getTrollMetaData().getItem());
        //            }
        //        }

                    if (paginationHandler.getCurrentPage() < paginationHandler.getMaxPage()) {
                        inventory.setItem(53, PaginationItemType.NEXT_PAGE.getItemStack());
                    } else {
                        inventory.setItem(53, new ItemStackBuilder(XMaterial.POPPY).withDisplayName("Last Page").withInvisibleEnchantmentGlint().build());
                    }
                    if (paginationHandler.getCurrentPage() > 1) {
                        inventory.setItem(45, PaginationItemType.PREVIOUS_PAGE.getItemStack());
                    } else {
                        inventory.setItem(45, new ItemStackBuilder(XMaterial.POPPY).withDisplayName("First Page").withInvisibleEnchantmentGlint().build());
                    }
                    DebuggingUtil.getInstance().l(Arrays.toString(inventory.getContents()));

                    return inventory;
        });
        this.inventory = builder.build();
        paginationHandler.setOnPageChange( (page, maxPage) -> {
            builder.build();
        });
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (Utils.getInstance().checkUniqueInventory(event, this, this.inventory)) return;
        event.setCancelled(true);


        ItemStack item = event.getCurrentItem();

        if (item == null || item.getItemMeta() == null || item.getItemMeta().getDisplayName() == null ||
                item.getType() == Material.AIR || item.getType() == Material.BLACK_STAINED_GLASS_PANE) {
            return;
        }

        paginationHandler.handleOnInventoryClick(event);

        List<Troll> trolls = new ArrayList<>(TrollLoader.getInstance().getTrolls());
        trolls.removeIf(Objects::isNull);

        if (item.getType() == XMaterial.JUKEBOX.parseMaterial()) {
            Troll randomTroll = trolls.get(Utils.getInstance().getRandomNumberInRange(0, trolls.size() - 1));
            randomTroll.setCaller(caller).setVictim(victim).Init().execute();
            return;
        }

        trolls.stream().findFirst().filter(troll -> troll.getTrollMetaData().getItemMeta().getPersistentDataContainer().get(Troll.trollClassKey, PersistentDataType.STRING).equalsIgnoreCase(item.getItemMeta().getPersistentDataContainer().get(Troll.trollClassKey, PersistentDataType.STRING))).ifPresent(troll -> {
            troll.setCaller(caller).setVictim(victim).Init().execute();
        });



    }

    /**
     * @return
     */
    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void openInventory(Player player) {
        player.openInventory(this.inventory);
    }
}


