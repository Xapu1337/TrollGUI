package me.xapu1337.recodes.trollgui.inventories;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.cores.TrollCore;
import me.xapu1337.recodes.trollgui.handlers.PaginationHandler;
import me.xapu1337.recodes.trollgui.types.PaginationItemType;
import me.xapu1337.recodes.trollgui.utilities.ConfigUtils;
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

public class PlayerSelectorInventory implements Listener, InventoryHolder {

    private final Inventory inventory;
    private final NamespacedKey UUID_KEY = new NamespacedKey(TrollCore.instance, "uuid");
    private final List<Player> players;
    private final BiConsumer<Player, Player> onClick;
    private final PaginationHandler paginationHandler;

    public PlayerSelectorInventory(BiConsumer<Player, Player> onClick) {
        this.players = List.of(Bukkit.getOnlinePlayers().toArray(new Player[0]));
        this.onClick = onClick;
        this.paginationHandler = new PaginationHandler(45);
        paginationHandler.setOnPageChange( (page, maxPage) -> { refresh(); });

        Bukkit.getPluginManager().registerEvents(this, TrollCore.getInstance());

        InventoryBuilder builder = new InventoryBuilder();
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
        .setItem('C', PaginationItemType.CLOSE.getItemStack())
        .setItem('B', new ItemStackBuilder(XMaterial.BLACK_STAINED_GLASS_PANE).withDisplayName(" ").build());

        this.inventory = builder.build();
        this.refresh();
    }

    public void refresh() {
        ItemMeta meta;
        ItemStack item;
        String displayName;

        paginationHandler.setMaxPage((int) Math.ceil(players.size() / 45.0));
        if (paginationHandler.getCurrentPage() > paginationHandler.getMaxPage()) paginationHandler.setCurrentPage(paginationHandler.getMaxPage());
        if (paginationHandler.getCurrentPage() < 1) paginationHandler.setCurrentPage(paginationHandler.getCurrentPage() + 1);

        int startIndex = (paginationHandler.getCurrentPage() - 1) * 45;
        int endIndex = startIndex + 45;
        if (endIndex > players.size()) endIndex = players.size();

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

                List<String> lore = new ArrayList<>();
                lore.add("&7" + player.getName());
                lore.add("&7Health: &f" + player.getHealth() + "&c❤");
                lore = lore.stream().map(ConfigUtils.getInstance()::$).toList();
                meta.setLore(lore);
                item.setItemMeta(meta);
                TrollCore.getInstance().debuggingUtil.log("Setting item " + i + " to " + player.getName());
                this.inventory.setItem(i, item);
            }
        }

        if (paginationHandler.getCurrentPage() < paginationHandler.getMaxPage()) {
            this.inventory.setItem(53, PaginationItemType.NEXT_PAGE.getItemStack());
        } else {
            this.inventory.setItem(53, new ItemStackBuilder(XMaterial.GRAY_STAINED_GLASS_PANE).withDisplayName("Last Page").build());
        }
        if (paginationHandler.getCurrentPage() > 1) {
            this.inventory.setItem(45, PaginationItemType.PREVIOUS_PAGE.getItemStack());
        } else {
            this.inventory.setItem(45, new ItemStackBuilder(XMaterial.GRAY_STAINED_GLASS_PANE).withDisplayName("First Page").build());
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (Utils.getInstance().checkUniqueInventory(event, this, this.inventory)) return;

        event.setCancelled(true);

        ItemStack item = event.getCurrentItem();

        if (item == null || item.getItemMeta() == null || item.getItemMeta().getDisplayName() == null || item.getType() == Material.AIR || item.getType() == Material.BLACK_STAINED_GLASS_PANE || item.getType() == Material.GRAY_STAINED_GLASS_PANE) return;

        paginationHandler.handleOnInventoryClick(event);


        if (item.getItemMeta().getPersistentDataContainer().isEmpty()) return;

        Player player = (Player) event.getWhoClicked();
        UUID uuid = UUID.fromString(Objects.requireNonNull(item.getItemMeta().getPersistentDataContainer().get(UUID_KEY, PersistentDataType.STRING)));
        Player target = Bukkit.getPlayer(uuid);
        if (target == null) return;
        onClick.accept((Player) event.getWhoClicked(), target);
    }
}


