package me.xapu1337.recodes.trollgui.inventories;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.cores.TrollCore;
import me.xapu1337.recodes.trollgui.handlers.PaginationHandler;
import me.xapu1337.recodes.trollgui.types.PaginationItemType;
import me.xapu1337.recodes.trollgui.utilities.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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

public class PlayerSelectorInventory implements Listener, InventoryHolder {
    private final InventoryBuilder builder = new InventoryBuilder(this);
    private final Inventory inventory;
    private final List<Player> players;
    private final BiConsumer<Player, Player> onClick;
    private final PaginationHandler paginationHandler;

    private WeakReference<Inventory> previousInventory;
    public PlayerSelectorInventory(BiConsumer<Player, Player> onClick) {
        this.players = List.of(Bukkit.getOnlinePlayers().toArray(new Player[0]));
        this.onClick = onClick;
        this.paginationHandler = new PaginationHandler(45);
        Bukkit.getPluginManager().registerEvents(this, TrollCore.getInstance());

        builder.setSize(54)
        .setPattern(
                "XXXXXXXXX",
                "XXXXXXXXX",
                "XXXXXXXXX",
                "XXXXXXXXX",
                "XXXXXXXXX",
                "BBB<C>BBB"
        )
        .withDefaults()
        .setItem('>', PaginationItemType.NEXT_PAGE.getItemStack())
        .setItem('<', PaginationItemType.PREVIOUS_PAGE.getItemStack())
        .setItem('X', new ItemStackBuilder(XMaterial.AIR).withDisplayName(" ").build())
        .setItem('C', previousInventory == null || previousInventory.get() == null ? new ItemStackBuilder(XMaterial.BARRIER).withDisplayName("Close").build() : new ItemStackBuilder(XMaterial.BARRIER).withDisplayName("Go Back").build())
        .setItem('B', new ItemStackBuilder(XMaterial.BLACK_STAINED_GLASS_PANE).withDisplayName(" ").build())
                .setInventoryContents((inventory) -> {

                    paginationHandler.setMaxPage((int) Math.ceil(players.size() / 45.0));
                    if (paginationHandler.getCurrentPage() > paginationHandler.getMaxPage()) paginationHandler.setCurrentPage(paginationHandler.getMaxPage());
                    if (paginationHandler.getCurrentPage() < 1) paginationHandler.setCurrentPage(paginationHandler.getCurrentPage() + 1);

                    int startIndex = (paginationHandler.getCurrentPage() - 1) * 45;
                    int endIndex = startIndex + 45;
                    if (endIndex > players.size()) endIndex = players.size();
                    String displayName;
                    ItemStack item;
                    ItemMeta meta;

                    for (int i = 0; i < 45; i++) {
                        int index = startIndex + i;

                        if (index < endIndex) {
                            Player player = players.get(index);
                            displayName = player.getDisplayName();
                            if (displayName.length() > 32) {
                                displayName = displayName.substring(0, 32);
                            }
                            item = new ItemStackBuilder(XMaterial.PLAYER_HEAD).withPlayerHead(player.getUniqueId()).withDisplayName(displayName).build();
                            meta = item.getItemMeta();

                            meta.setLore(Stream.of(
                                    "&7Health: &f" + player.getHealth() + "&c❤",
                                    player.isOp() ? "hasop" : ""
                            ).filter(s -> !s.isEmpty()).map(MessageUtils.getInstance()::$).toList());
                            item.setItemMeta(meta);
                            inventory.setItem(i, item);
                        }
                    }

                    inventory.setItem(53, paginationHandler.getCurrentPage() < paginationHandler.getMaxPage() && paginationHandler.getCurrentPage() > 1
                            ? PaginationItemType.NEXT_PAGE.getItemStack()
                            : createPoppyItem("Last Page"));

                    inventory.setItem(45, paginationHandler.getCurrentPage() > 1 && paginationHandler.getCurrentPage() > 1
                            ? PaginationItemType.PREVIOUS_PAGE.getItemStack()
                            : createPoppyItem("First Page"));


                    return inventory;
                });
        this.inventory = builder.build();
        paginationHandler.setOnPageChange( (page, maxPage) -> {
            builder.build();
        });
    }

    public void setPreviousInventory(Inventory previousInventory) {
        this.previousInventory = new WeakReference<>(previousInventory);
    }

    public Inventory getPreviousInventory() {
        return previousInventory.get();
    }
    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    private ItemStack createPoppyItem(String displayName) {
        return new ItemStackBuilder(XMaterial.POPPY)
                .withDisplayName(displayName)
                .withInvisibleEnchantmentGlint()
                .build();
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!Utils.getInstance().checkUniqueInventory(event, this)) return;
        event.setCancelled(true);


        ItemStack item = event.getCurrentItem();

        if (item == null || item.getItemMeta() == null || item.getType() == Material.AIR || item.getType() == Material.BLACK_STAINED_GLASS_PANE || item.getType() == Material.GRAY_STAINED_GLASS_PANE) return;
        if (previousInventory != null && previousInventory.get() != null && item.getType() == Material.BARRIER && item.getItemMeta().getDisplayName().equals("Go Back")) {
            event.getWhoClicked().openInventory(previousInventory.get());
            return;
        }


        if (item.getItemMeta().getPersistentDataContainer().isEmpty()) return;

        Player player = (Player) event.getWhoClicked();
        DebuggingUtil.getInstance().l("Clicked on " + item.getItemMeta().getDisplayName());
        UUID uuid = UUID.fromString(item.getItemMeta().getPersistentDataContainer().get(Utils.getInstance().UUID_KEY, PersistentDataType.STRING));
        if (uuid == null) return;
        Player target = Bukkit.getPlayer(uuid);
        if (target == null) return;
        player.closeInventory();
        onClick.accept((Player) event.getWhoClicked(), target);
    }
}


