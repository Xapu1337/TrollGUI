package me.xapu.trollgui.ui;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import me.xapu.trollgui.main.Core;

import java.util.*;

import static me.xapu.trollgui.main.Core.sendGameStateChange;

public class TrollInventory implements Listener, InventoryHolder {
    HashMap<String, String> launchedPlayers = new HashMap<String, String>();
    HashMap<String, String> clearedFPlayers = new HashMap<String, String>();
    //HashMaps for Toggabels
    private final Inventory inv;
    static TrollInventory main;
    Player VictimPlayer;
    public String getNOU(Player p){
        return Core.uid()?p.getName():p.getUniqueId().toString();
    }
    public String centerTitle(String title) {
        StringBuilder result = new StringBuilder();
        int spaces = (27 - ChatColor.stripColor(title).length());

        for (int i = 0; i < spaces; i++) {
            result.append(" ");
        }

        return result.append(title).toString();
    }
    public TrollInventory(Player vic) {

        VictimPlayer = vic;
        main = this;
        Bukkit.getPluginManager().registerEvents(this, Core.instance);
        // Create a new inventory, with "this" owner for comparison with other inventories, a size of nine, called example
        inv = Bukkit.createInventory(this, 45, centerTitle(Core.getPathCC("menu.menu-title")));
        // Put the items into the inventory
        initializeItems();

    }
    //To use method beyond this codeA
    public static TrollInventory getGUI() {
        return main;
    }
    public static HashMap getMaps(String maps) {
        switch (maps) {
            case "LP":
                return TrollInventory.getGUI().launchedPlayers;
            case "cFP":
                return TrollInventory.getGUI().clearedFPlayers;
            case "TM":
                return Core.instance.AntiBuildPlayers;
            case "TP":
                return Core.instance.AntiPlacePlayers;
            case "FR":
                return Core.instance.FreezePlayers;
            case "reverseMsg":
                return Core.instance.reverseChatMessages;
        }
        // FrZLayers... creative.
        return null;
    }
    @Override
    public Inventory getInventory() {
        return inv;
    }

    // You can call this whenever you want to put the items in

    ItemStack mainPage = createGuiItem(XMaterial.REDSTONE_BLOCK, true, Core.getPathCC("items.Playerselector-name"), Core.getPathCC("items.Playerselector-lore"));
    public void initializeItems() {
        ItemStack ToggableAB = createGuiItem(XMaterial.GRASS_BLOCK,false,  Core.tcc(Core.instance.getConfig().getString("items.nobuild-name")), Core.tcc(Core.instance.getConfig().getString("items.nobuild-lore")), !Core.instance.AntiBuildPlayers.containsKey(getNOU(VictimPlayer))? Core.getPathCC("items.messages.disabled"): Core.getPathCC("items.messages.enabled"));
        ItemStack ToggableAP = createGuiItem(XMaterial.STONE,false,  Core.tcc(Core.instance.getConfig().getString("items.noplace-name")), Core.tcc(Core.instance.getConfig().getString("items.noplace-lore")), !Core.instance.AntiPlacePlayers.containsKey(getNOU(VictimPlayer))? Core.getPathCC("items.messages.disabled"): Core.getPathCC("items.messages.enabled"));
        ItemStack ToggableFR = createGuiItem(XMaterial.SNOWBALL,false,  Core.tcc(Core.instance.getConfig().getString("items.freezeplayer-name")), Core.tcc(Core.instance.getConfig().getString("items.freezeplayer-lore")), !Core.instance.FreezePlayers.containsKey(getNOU(VictimPlayer))? Core.getPathCC("items.messages.disabled"): Core.getPathCC("items.messages.enabled"));
        ItemStack ToggableRM = createGuiItem(XMaterial.BARRIER,false,  Core.tcc(Core.instance.getConfig().getString("items.reversemessage-name")), Core.tcc(Core.instance.getConfig().getString("items.reversemessage-lore")), !Core.instance.reverseChatMessages.containsKey(getNOU(VictimPlayer))? Core.getPathCC("items.messages.disabled"): Core.getPathCC("items.messages.enabled"));
        if(Core.instance.FreezePlayers.containsKey(getNOU(VictimPlayer))){
            ItemMeta meta = ToggableFR.getItemMeta();
            if(meta.hasEnchants()){
                meta.removeEnchant(XEnchantment.DURABILITY.parseEnchantment());
            } else{
                meta.addEnchant(XEnchantment.DURABILITY.parseEnchantment(), 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            ToggableFR.setItemMeta(meta);
        }
        if(Core.instance.AntiPlacePlayers.containsKey(getNOU(VictimPlayer))){
            ItemMeta meta = ToggableAP.getItemMeta();
            if(meta.hasEnchants()){
                meta.removeEnchant(XEnchantment.DURABILITY.parseEnchantment());
            } else{
                meta.addEnchant(XEnchantment.DURABILITY.parseEnchantment(), 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            ToggableAP.setItemMeta(meta);
        }
        if(Core.instance.AntiBuildPlayers.containsKey(getNOU(VictimPlayer))){
            ItemMeta meta = ToggableAB.getItemMeta();
            if(meta.hasEnchants()){
                meta.removeEnchant(XEnchantment.DURABILITY.parseEnchantment());
            } else{
                meta.addEnchant(XEnchantment.DURABILITY.parseEnchantment(), 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            ToggableAB.setItemMeta(meta);
        }
        if(Core.instance.reverseChatMessages.containsKey(getNOU(VictimPlayer))){
            ItemMeta meta = ToggableRM.getItemMeta();
            if(meta.hasEnchants()){
                meta.removeEnchant(XEnchantment.DURABILITY.parseEnchantment());
            } else{
                meta.addEnchant(XEnchantment.DURABILITY.parseEnchantment(), 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            ToggableRM.setItemMeta(meta);
        }
        ItemStack plc = new ItemStack(XMaterial.BLACK_STAINED_GLASS_PANE.parseMaterial(), 1);
        ItemMeta meta = plc.getItemMeta();
        meta.setDisplayName(" ");
        plc.setItemMeta(meta);
        for(Integer i = 0; i < 45; i++){
            inv.setItem(i, plc);
        }
        inv.setItem(10,createGuiItem(XMaterial.GOLDEN_BOOTS, false, Core.tcc(Core.instance.getConfig().getString("items.launch-name")), Core.tcc(Core.instance.getConfig().getString("items.launch-lore"))));
        inv.setItem(11,createGuiItem(XMaterial.PUMPKIN, false, Core.tcc(Core.instance.getConfig().getString("items.scare-name")), Core.tcc(Core.instance.getConfig().getString("items.scare-lore"))));
        inv.setItem(12,createGuiItem(XMaterial.REDSTONE_BLOCK, false, Core.tcc(Core.instance.getConfig().getString("items.randomlook-name")), Core.tcc(Core.instance.getConfig().getString("items.randomlook-lore"))));
        inv.setItem(13,createGuiItem(XMaterial.PUFFERFISH, false, Core.tcc(Core.instance.getConfig().getString("items.fakeclear-name")), Core.tcc(Core.instance.getConfig().getString("items.fakeclear-lore").replace("%TIME%", Core.instance.getConfig().getString("values.fake-clear.fake-clear-delay")))));
        inv.setItem(14,createGuiItem(XMaterial.IRON_BARS, false, Core.tcc(Core.instance.getConfig().getString("items.demo-name")), Core.tcc(Core.instance.getConfig().getString("items.demo-lore"))));
        inv.setItem(15,createGuiItem(XMaterial.TRIDENT, false, Core.tcc(Core.instance.getConfig().getString("items.thunder-name")), Core.tcc(Core.instance.getConfig().getString("items.thunder-lore"))));
        inv.setItem(16,createGuiItem(XMaterial.FIRE_CHARGE, false, Core.tcc(Core.instance.getConfig().getString("items.burn-name")), Core.tcc(Core.instance.getConfig().getString("items.burn-lore"))));
        inv.setItem(19,createGuiItem(XMaterial.TNT, false, Core.tcc(Core.instance.getConfig().getString("items.explode-name")), Core.tcc(Core.instance.getConfig().getString("items.explode-lore"))));
        inv.setItem(20,createGuiItem(XMaterial.DIAMOND, false, Core.tcc(Core.instance.getConfig().getString("items.fakeoperator-name")), Core.tcc(Core.instance.getConfig().getString("items.fakeoperator-lore"))));
        inv.setItem(21,createGuiItem(XMaterial.STONE_PRESSURE_PLATE, false, Core.tcc(Core.instance.getConfig().getString("items.dropitem-name")), Core.tcc(Core.instance.getConfig().getString("items.dropitem-lore"))));
        inv.setItem(22,createGuiItem(XMaterial.HEAVY_WEIGHTED_PRESSURE_PLATE, false, Core.tcc(Core.instance.getConfig().getString("items.dropall-name")), Core.tcc(Core.instance.getConfig().getString("items.dropall-lore"))));
        inv.setItem(23,createGuiItem(XMaterial.CHEST, false, Core.tcc(Core.instance.getConfig().getString("items.invsee-name")), Core.tcc(Core.instance.getConfig().getString("items.invsee-lore"))));
        inv.setItem(24,createGuiItem(XMaterial.BARRIER,false,  Core.tcc(Core.instance.getConfig().getString("items.closegui-name")), Core.tcc(Core.instance.getConfig().getString("items.closegui-lore"))));
        try {
        inv.setItem(25,createGuiItem(XMaterial.valueOf(Core.instance.config.getString("values.fakeBlocks.material")),false,  Core.tcc(Core.instance.getConfig().getString("items.fakeblock-name")), Core.tcc(Core.instance.getConfig().getString("items.fakeblock-lore"))));
        } catch (Exception e){
            Core.instance.config.set("values.fakeBlocks.material", "TNT");
        }
        inv.setItem(30, ToggableAB);
        inv.setItem(31, ToggableAP);
        inv.setItem(32, ToggableFR);
        inv.setItem(33, ToggableRM);
        inv.setItem(44, mainPage);
    }

    int task = 0;
    Random rand = new Random();
    //Idk, I feel kinda clunky about this
    public void RndLook(Player vic){
        Location loc = vic.getLocation();
        float retoreYaw = loc.getYaw();
        float restorePitch = loc.getPitch();
        new BukkitRunnable(){
            private int i = 0;
            public void run() {
                if(i >= Core.instance.getConfig().getInt("values.random-look.random-look-time") * 20) {
                    loc.setPitch(restorePitch);
                    loc.setYaw(retoreYaw);
                    vic.teleport(loc);
                    cancel();
                }
                ++i;
                loc.setYaw(rand.nextInt(360));
                loc.setPitch(rand.nextInt(180));
                vic.teleport(loc);
            }
        }.runTaskTimer(Core.instance, 5L, 1L);
    }
    public void InvSee(Player vic, Player sender){
        Inventory vici = vic.getInventory();
        sender.openInventory(vici);
    }
    //Tried randomizing the inventory then in the next morning i said Frick it, im gonna do a Fake Clear (Was easy!)
    public void fakeclear(Player vic){
        if(vic != null && vic instanceof Player){
            if(!clearedFPlayers.containsKey(vic.getUniqueId().toString())) {
                ItemStack[] i3 = vic.getInventory().getContents();
                vic.getInventory().clear();
                clearedFPlayers.put(vic.getUniqueId().toString(), "1");
                int seconds = 10;

                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Core.instance, new Runnable() {
                    public void run() {
                        vic.getInventory().setContents(i3);
                        clearedFPlayers.remove(vic.getUniqueId().toString());
                    }
                }, (seconds * 20)); // Always multiply by twenty because that's the amount of ticks in Minecraft
            }
        }
    }
    public void closeInv(Player vic){
        vic.closeInventory();
    }
    //Would be easier with vic.getWorld().createExplosion() Though?
    public void ExplodePlayer(Player vic){
        if(vic != null && vic instanceof Player){
            World toX = vic.getWorld();
            if(Core.instance.getConfig().getBoolean("values.explode-item.EnableExplodeRandomness")){
                toX.createExplosion(vic.getLocation(),rand.nextInt(Core.instance.getConfig().getInt("values.explode-item.explode-radius") +1 ), false);
            } else {
                toX.createExplosion(vic.getLocation(), Core.instance.getConfig().getInt("values.explode-item.explode-radius") + 1, false);
            }

        }
    }
    //Searched
    public void burnPlayer(Player vic){
        if(vic != null && vic instanceof Player){
            int ticks = Core.instance.getConfig().getInt("values.burn-item.burn-time") * 20;
            vic.setFireTicks(ticks);
        }
    }

    protected ItemStack createGuiItem(final XMaterial XMaterial, final Boolean isEnchanted , final String name, final String... lore) {
        final ItemStack item = new ItemStack(XMaterial.parseMaterial(), 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);
        if(isEnchanted){
            meta.addEnchant(XEnchantment.DURABILITY.parseEnchantment(), 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }


    public void fakeBlock(Material mat, Player vic){ // Ty Garkolym (https://www.youtube.com/channel/UCukS5iWNa60iBC60MCdETBw) for the idea!
        int rad = Core.instance.config.getInt("values.fakeBlocks.radius");
        String v = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        int subVersion = Integer.parseInt(v.replace("1_", "").replaceAll("_R\\d", "").replace("v", ""));
        try{
            for(double x = vic.getLocation().getX() - rad; x <= vic.getLocation().getX() + rad; x++){
                for(double y = vic.getLocation().getY() - rad; y <= vic.getLocation().getY() + rad; y++){
                    for(double z = vic.getLocation().getZ() - rad; z <= vic.getLocation().getZ() + rad; z++){
                        Location l = new Location(vic.getWorld(), x,y,z);
                        if(l.getBlock().getType() != Material.AIR){
                            if(subVersion >= 13){
                                vic.sendBlockChange(l, mat.createBlockData());
                            } else {
                                vic.sendBlockChange(l, mat, (byte) 0);
                            }
                        }
                    }
                }

            }

        }catch (Exception ignored){

        }
    }

    //First feature. still, kinda proud
    public void launchPlayer(final Player p) {
        p.setVelocity(new Vector(0f, 5f, 0f));
        if(!launchedPlayers.containsKey(getNOU(p))) launchedPlayers.put(getNOU(p), "true");
        p.playSound(p.getLocation(), XSound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST.parseSound(), 3f, 1f);
        p.spawnParticle(Particle.EXPLOSION_HUGE, p.getLocation(), 1, 0.11, 1, 1, 1);
    }

    // You can open the inventory with this
    public void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
    }

    public void demoTroll(Player p){
        sendGameStateChange(p, 5, 0);
    }
    //Easy
    public void fakeOP(Player vic){
        vic.sendMessage(Core.tcc("&7&o[Server: Made "+vic.getName()+" a Server operator"));
    }
    //Needed to search
    public void dropItem( Player vic, Player sender){
        if(vic.getInventory().getItemInMainHand() == null || vic.getInventory().getItemInMainHand().getType() == XMaterial.AIR.parseMaterial()){
            sender.sendMessage(Core.instance.getP()+ Core.tcc(Core.instance.getConfig().getString("menu.messages.no-item-in-hand").replace("%player%", vic.getName())));
            return;
        }else {
            ItemStack hand = vic.getInventory().getItemInMainHand();
            vic.getInventory().clear(vic.getInventory().getHeldItemSlot()); // .remove removed ALL items of the same TYPE
            vic.getWorld().dropItemNaturally(vic.getLocation(), hand).setPickupDelay(20);
            sender.sendMessage(hand.getItemMeta().getDisplayName());
            sender.sendMessage(hand.getItemMeta().getLocalizedName());
            sender.sendMessage(Core.instance.getPrefixedMessage("menu.messages.items-dropped-single").replace("%ITEM_NAME%", hand.getItemMeta().getDisplayName()).replace("%ITEM_AMOUNT%", String.valueOf(hand.getAmount())).replace("%PLAYER%", vic.getDisplayName())); // Prefixed?
        }
    }
    //Thanks "Lubdan" - https://www.spigotmc.org/threads/drop-a-players-inventory.358762/
    public void setAntiBuild(Player vic){
        if (Core.instance.AntiBuildPlayers.containsKey(getNOU(vic))) {
            Core.instance.AntiBuildPlayers.remove(getNOU(vic));
        } else {
            Core.instance.AntiBuildPlayers.put(getNOU(vic), "1");
        }
        initializeItems();
    }
    public void setAntiPlace(Player vic){
        if (Core.instance.AntiPlacePlayers.containsKey(getNOU(vic))) {
            Core.instance.AntiPlacePlayers.remove(getNOU(vic));
        } else {
            Core.instance.AntiPlacePlayers.put(getNOU(vic), "1");
        }
        initializeItems();
    }
    public void setFreezePlayer(Player vic){
        if (Core.instance.FreezePlayers.containsKey(getNOU(vic))) {
            Core.instance.FreezePlayers.remove(getNOU(vic));
        } else {
            Core.instance.FreezePlayers.put(getNOU(vic), "1");
        }
        initializeItems();
    }
    public void setReverseMsg(Player vic){
        if (Core.instance.reverseChatMessages.containsKey(getNOU(vic))) {
            Core.instance.reverseChatMessages.remove(getNOU(vic));
        } else {
            Core.instance.reverseChatMessages.put(getNOU(vic), "1");
        }
        initializeItems();
    }
    public void dropAllItems( Player vic, Player sender){
        Inventory inv =  vic.getInventory();
        for(int i = 0; i <= 36; i++){
            try {
                vic.getWorld().dropItem(vic.getLocation(), inv.getItem(i)).setPickupDelay(40);
            }
            catch(Exception a){
            }
            try {
                switch (i) {
                    case 0:
                        break;
                    case 1:
                        vic.getWorld().dropItem(vic.getLocation(), ((PlayerInventory) inv).getChestplate()).setPickupDelay(40);
                        break;
                    case 2:
                        vic.getWorld().dropItem(vic.getLocation(), ((PlayerInventory) inv).getLeggings()).setPickupDelay(40);
                        break;
                    case 3:
                        vic.getWorld().dropItem(vic.getLocation(), ((PlayerInventory) inv).getHelmet()).setPickupDelay(40);
                        break;
                }
            }
            catch(Exception d) {
            }
        }
        vic.getInventory().clear();
    }
    //Easy
    public void thorPlayer(Player p){
        Location lb = p.getLocation().getBlock().getLocation();
        World w = lb.getWorld();
        if(w != null)
            w.spawnEntity(lb, EntityType.LIGHTNING);
    }
    //Ha, v = Volume, v1 = Pitch
    public void scare(Player p){
        p.spawnParticle(Particle.MOB_APPEARANCE, p.getLocation(), 1);
        p.playSound(p.getLocation(), XSound.ENTITY_ELDER_GUARDIAN_CURSE.parseSound(), 100, 1);
        p.playSound(p.getLocation(), XSound.ENTITY_ELDER_GUARDIAN_AMBIENT.parseSound(), 100, 1);
        p.playSound(p.getLocation(), XSound.ENTITY_ELDER_GUARDIAN_AMBIENT_LAND.parseSound(), 100, 1);
        p.playSound(p.getLocation(), XSound.ENTITY_ELDER_GUARDIAN_DEATH.parseSound(), 100, 1);
        p.playSound(p.getLocation(), XSound.ENTITY_ELDER_GUARDIAN_DEATH_LAND.parseSound(), 100, 1);
        p.playSound(p.getLocation(), XSound.ENTITY_ELDER_GUARDIAN_FLOP.parseSound(), 100, 1);
        p.playSound(p.getLocation(), XSound.ENTITY_ELDER_GUARDIAN_HURT.parseSound(), 100, 1);
        p.playSound(p.getLocation(), XSound.ENTITY_ELDER_GUARDIAN_HURT_LAND.parseSound(), 100, 1);
    }
    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getInventory().getHolder() != this) return;
        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType() == XMaterial.AIR.parseMaterial()) return;

        final Player p = (Player) e.getWhoClicked();
        if(VictimPlayer != null){
            if(e.getSlot() < 45){
                switch(e.getRawSlot()){
                    case 10:
                        launchPlayer(VictimPlayer);
                        break;
                    case 11:
                        scare(VictimPlayer);
                        break;
                    case 12:
                        RndLook(VictimPlayer);
                        break;
                    case 13:
                        fakeclear(VictimPlayer); // Changed
                        break;
                    case 14:
                        demoTroll(VictimPlayer);
                        break;
                    case 15:
                        thorPlayer(VictimPlayer);
                        break;
                    case 16:
                        burnPlayer(VictimPlayer);
                        break;
                    case 19:
                        ExplodePlayer(VictimPlayer);
                        break;
                    case 20:
                        fakeOP(VictimPlayer);
                        break;
                    case 21:
                        dropItem(VictimPlayer, p);
                        break;
                    case 22:
                        dropAllItems(VictimPlayer, p);
                        break;
                    case 23:
                        InvSee(VictimPlayer, p);
                        break;
                    case 24:
                        closeInv(VictimPlayer);
                        break;
                    case 25:
                        fakeBlock(XMaterial.valueOf(Core.instance.config.getString("values.fakeBlocks.material")).parseMaterial(), p);
                        break;
                    case 30:
                        setAntiBuild(VictimPlayer);
                        break;
                    case 31:
                        setAntiPlace(VictimPlayer);
                        break;
                    case 32:
                        setFreezePlayer(VictimPlayer);
                        break;
                    case 33:
                        setReverseMsg(VictimPlayer);
                        break;
                    case 44:
                        PlayerSelectorInventory ps = new PlayerSelectorInventory();
                        ps.openSel(p);
                        break;
                }
            }
        }
    }
}

