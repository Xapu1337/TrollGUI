package me.xapu1337.recodes.trollgui.inventories;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.cryptomorin.xseries.XMaterial;
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

import javax.swing.text.html.Option;

public class TrollSelectionInventory implements Listener, InventoryHolder {

    private final Inventory inventory;
    private final PaginationHandler paginationHandler;
    public Player caller, victim;

    public final InventoryBuilder builder = new InventoryBuilder(this);
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
                    int totalPages = (int) Math.ceil((double) items.size() / 28);
                    DebuggingUtil.getInstance().l("Total pages: " + totalPages);
                    if (paginationHandler.getCurrentPage() > totalPages) paginationHandler.setCurrentPage(totalPages);
                    DebuggingUtil.getInstance().l("Current page: " + paginationHandler.getCurrentPage());
                    ItemStack[] inventoryContents = inventory.getContents();
                    for (int i = startIndex; i < endIndex && i < inventoryContents.length; i++) {
                        ItemStack item = items.size() > i ? items.get(i) : null;

                        if (item == null) continue;
                        int[] emptySlots = builder.getItemSlots('X');
                        if (emptySlots.length == 0) {
                            emptySlots = builder.getItemSlots('X');
                            if (emptySlots.length == 0) break;
                        }
                        inventory.setItem(emptySlots[0], item);
                    }

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

                    return inventory;
        });
        this.inventory = builder.build();
        paginationHandler.setOnPageChange( (page, maxPage) -> {
            builder.build();
        });
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!Utils.getInstance().checkUniqueInventory(event, this)) return;
        event.setCancelled(true);

        ItemStack item = event.getCurrentItem();

        if (item == null || item.getItemMeta() == null || item.getItemMeta().getDisplayName() == null ||
                item.getType() == Material.AIR || item.getType() == Material.BLACK_STAINED_GLASS_PANE) {
            return;
        }

        paginationHandler.handleOnInventoryClick(event);

        Optional<Troll> fetchedTroll = TrollLoader.getInstance().getTrolls().stream()
                .filter(Objects::nonNull)
                .filter(troll -> trollContainerFilter(troll.getTrollMetaData().getItem(), item))
                .findFirst();

        if (!fetchedTroll.isPresent() || fetchedTroll == null || fetchedTroll.get().getTrollMetaData() == null) return;

        fetchedTroll
            .get()
            .setVictim(victim)
            .setCaller(caller)
            .setCallingGUI(this)
            .checkToggled()
            .execute();


    }

    private boolean trollContainerFilter(ItemStack a, ItemStack b) {
        return a.getItemMeta().getPersistentDataContainer().get(Troll.trollClassKey, PersistentDataType.STRING).equalsIgnoreCase(b.getItemMeta().getPersistentDataContainer().get(Troll.trollClassKey, PersistentDataType.STRING));
    }


    @NotNull
    @Override
    public Inventory getInventory() {
        return builder.getInventory();
    }

    public void openInventory(Player player) {
        player.openInventory(this.inventory);
    }
}


