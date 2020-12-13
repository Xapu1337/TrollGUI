package me.xapu.trollgui.main;


import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import me.xapu.trollgui.other.*;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;


public class Core extends JavaPlugin implements Listener {


    public FileConfiguration config = getConfig();
    public HashMap<String, String> AntiBuildPlayers = new HashMap<>();
    public HashMap<String, String> AntiPlacePlayers = new HashMap<>();
    public HashMap<String, String> FreezePlayers = new HashMap<>();
    public HashMap<String, String> reverseChatMessages = new HashMap<String, String>();
    static Boolean usingUUID = false;
    private File file = null;

    public static Core instance;
    public Core(){

        if((instance) == null)
            instance = this;

    }


    public static String tcc(String i){
        i = ChatColor.translateAlternateColorCodes('&', i);
        return i;
    }
    public ShapedRecipe recipe;
    @Override
    public void reloadConfig() {
        super.reloadConfig();
        saveDefaultConfig();
        config = getConfig();
        config.options().copyDefaults(true);
    }
    public static Boolean uid(){
        return usingUUID;
    }

    public String getServerVersion() {
        return Bukkit.getServer().getVersion().split("MC: ")[1].replaceAll("\\)", "").trim();
    }

    public ItemStack getSkull(){
        ItemStack skull = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGY0YzY3MzZjYjFjNjg4MGQ4MGEyMDYyZDdhYTRhYWIxZWEwYzU1YmYxNDJhZDMwZmQ1MmM1NzUxNWYwYzJkMSJ9fX0=");
        ItemMeta skullmeta = skull.getItemMeta();
        skullmeta.setDisplayName("§7§l§kYou scum if you can read this idk how but hi.");
        skullmeta.setLore(Arrays.asList("§0§k000000000", "§7§k777777", "§9§k999999", "§a§k§l133742069YEET!"));
        skullmeta.addEnchant(XEnchantment.DURABILITY.parseEnchantment(), 1, false);
        skullmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        skull.setItemMeta(skullmeta);
        return skull;
    }




    @Override
    public void onEnable() {
        Metrics metrics = new Metrics(this, 9617);
//        if(getDataFolder().exists()) {
//                try {
//                    this.saveResource("noteblockfiles/megalovania.nbs", true);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//        }
//        else
//        {
//            File nbsFolder  = new File(getDataFolder().getAbsolutePath() +File.separator+ "noteblockfiles");
//            if(nbsFolder.mkdirs())
//                try {
//                    this.saveResource("noteblockfiles/megalovania.nbs", false);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//        }
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getServer().getPluginManager().registerEvents(new EventListener(), this);
        super.onEnable();
        reloadConfig();
        usingUUID = getConfig().getBoolean("values.using-uuid");
        if (Integer.parseInt(getServerVersion().split("\\.")[1]) < 7) {
            usingUUID = false;
            this.getServer().getLogger().info("using UUID disable because server is version " + getServerVersion());
        }
        try {

            Field f = null;
            f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            CommandMap commandMap = (CommandMap) f.get(Bukkit.getServer());

            new TrollCommand(commandMap, this);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            getLogger().severe(e.getLocalizedMessage());
            e.printStackTrace();
            this.getPluginLoader().disablePlugin(this);
        }
        usingUUID = getServer().getOnlineMode();
        NamespacedKey key = new NamespacedKey(this, "ms_item");
        recipe = new ShapedRecipe(key, getSkull());
        recipe.shape("FIF", "LGL", "FLF");
        recipe.setIngredient('G', XMaterial.GOLD_BLOCK.parseMaterial());
        recipe.setIngredient('I', XMaterial.IRON_BLOCK.parseMaterial());
        recipe.setIngredient('F', XMaterial.FEATHER.parseMaterial());
        recipe.setIngredient('L', XMaterial.GLASS.parseMaterial());
        Bukkit.addRecipe(recipe);
    }
    public String getP(){
        return tcc(this.config.getString("prefix"));
    }

    public String getPrefixedMessage(String configPath){
        return tcc(this.config.getString("prefix")+this.config.get(configPath));
    }

    public static String getPathCC(String path){
        String c = Core.instance.getConfig().getString(path);
        assert c != null;
        c = ChatColor.translateAlternateColorCodes('&', c);
        return c;
    }

    public static Boolean advCheck(String perm, Player p){
        if(instance.getConfig().getBoolean("values.advanced-perms.enabled")){
            if(usingUUID){
                OfflinePlayer pp = Bukkit.getPlayerExact(instance.getConfig().getString("values.advanced-perms.playername"));
                if(pp.isOnline() && pp.hasPlayedBefore()){
                    return p.getUniqueId().equals(pp.getUniqueId());
                }
            } else {
                return p.getName().equals(instance.getConfig().getString("values.advanced-perms.playername"));
            }
        } else {
            return p.hasPermission(perm);
        }
        return false;
    }

    public static String reverseMessage(String i){
        StringBuilder res = new StringBuilder();
        int length = i.length();

        for (int i1 = length - 1 ; i1 >= 0 ; i1--)
            res.append(i.charAt(i1));
        return res.toString();
    }



    public static void sendGameStateChange(Player Victim, int type, float state) {
        try {
            Object entityPlayer = Victim.getClass().getMethod("getHandle").invoke(Victim);
            Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
            Object packet = Packets.getNMSClass("PacketPlayOutGameStateChange").getConstructor(int.class, float.class)
                    .newInstance(type, state);

            playerConnection.getClass().getMethod("sendPacket", Packets.getNMSClass("Packet")).invoke(playerConnection,
                    packet);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException | NoSuchFieldException | InstantiationException e) {
            System.out.println("Your Server Version isn't Supporting this Packet! (PacketPlayOutGameStateChange)");
        }
    }

    @Override
    public void onDisable() {
        this.getServer().getLogger().info("§cDisabling");
    }
    @EventHandler
    public void preCraft(PrepareItemCraftEvent event) {
        boolean equal = CraftUtils.areEqual(event.getRecipe(), recipe);

        if (equal) {
            HumanEntity p = event.getView().getPlayer();
            if(!Core.advCheck("ms3.use", (Player) p)){
                event.getInventory().setResult(null);
            }
        }
    }
}