package me.xapu1337.recodes.trollgui.inventories;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.cores.TrollCore;
import me.xapu1337.recodes.trollgui.handlers.PaginationHandler;
import me.xapu1337.recodes.trollgui.loaders.TrollLoader;
import me.xapu1337.recodes.trollgui.types.PaginationItemType;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.utilities.InventoryBuilder;
import me.xapu1337.recodes.trollgui.utilities.ItemStackBuilder;
import me.xapu1337.recodes.trollgui.utilities.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class TrollSelectionInventory implements Listener, InventoryHolder {

    private final Inventory inventory;
    private final PaginationHandler paginationHandler;
    public Player caller, victim;
    public TrollSelectionInventory(Player caller, Player target)  {
        this.paginationHandler = new PaginationHandler(54);
        paginationHandler.setOnPageChange( (page, maxPage) -> {
            refresh();
        });

        Bukkit.getPluginManager().registerEvents(this, TrollCore.getInstance());

        InventoryBuilder builder = new InventoryBuilder();
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
                .setItem('X', new ItemStackBuilder(XMaterial.AIR).withDisplayName(" ").build())
                .setItem('C', PaginationItemType.CLOSE.getItemStack())
                .setItem('R', new ItemStackBuilder(XMaterial.JUKEBOX).withDisplayName("Random").build())
                .setItem('B', new ItemStackBuilder(XMaterial.BLACK_STAINED_GLASS_PANE).withDisplayName(" ").build());

        this.inventory = builder.build();
        this.refresh();
    }

    public void refresh() {

        paginationHandler.setMaxPage((int) Math.ceil(Bukkit.getOnlinePlayers().size() / 38.0));
        if (paginationHandler.getCurrentPage() > paginationHandler.getMaxPage()) paginationHandler.setCurrentPage(paginationHandler.getMaxPage());
        if (paginationHandler.getCurrentPage() < 1) paginationHandler.setCurrentPage(paginationHandler.getCurrentPage() + 1);

        int startIndex = (paginationHandler.getCurrentPage() - 1) * 38;
        int endIndex = startIndex + 38;
        if (endIndex > TrollLoader.getInstance().getTrolls().size()) endIndex = TrollLoader.getInstance().getTrolls().size();
        TrollCore.getInstance().debuggingUtil.log(String.valueOf(TrollLoader.getInstance().getTrolls().size()));
        TrollCore.getInstance().debuggingUtil.log("Start index: " + startIndex + " End index: " + endIndex);

        for (int i = 11; i < 38; i++) {
            int index = startIndex + i;
            TrollCore.getInstance().debuggingUtil.log("Index: " + index);
            TrollCore.getInstance().debuggingUtil.log("Index i: " + i);

            if (index < endIndex && index >= startIndex) {
                Troll trollClass = TrollLoader.getInstance().getTrolls().get(index);
                TrollCore.getInstance().debuggingUtil.log(trollClass.getTrollMetaData().getTrollName());
                trollClass.setCaller(caller).setVictim(victim).Init();
                TrollCore.getInstance().debuggingUtil.log("Setting item " + trollClass.getTrollMetaData().getItem().getItemMeta().getDisplayName() + " to slot " + i);
                this.inventory.setItem(i, trollClass.getTrollMetaData().getItem());
            }
        }

        if (paginationHandler.getCurrentPage() < paginationHandler.getMaxPage()) {
            this.inventory.setItem(53, PaginationItemType.NEXT_PAGE.getItemStack());
        } else {
            this.inventory.setItem(53, new ItemStackBuilder(XMaterial.POPPY).withDisplayName("Last Page").withInvisibleEnchantmentGlint().build());
        }
        if (paginationHandler.getCurrentPage() > 1) {
            this.inventory.setItem(45, PaginationItemType.PREVIOUS_PAGE.getItemStack());
        } else {
            this.inventory.setItem(45, new ItemStackBuilder(XMaterial.POPPY).withDisplayName("First Page").withInvisibleEnchantmentGlint().build());
        }
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


