package me.xapu1337.recodes.trollgui.Utilities;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Cores.TrollCore;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.*;
import java.util.regex.Pattern;



public class Utilities {

    private volatile static Utilities instance;
    private Utilities() {}

    public static Utilities getSingleInstance() {
        if (instance == null) {
            synchronized (Utilities.class) {
                if (instance == null) {
                    instance = new Utilities();
                }
            }
        }
        return instance;
    }


    public ItemStack createItem(final XMaterial xMat, final String name) {
        final ItemStack item = new ItemStack(xMat.parseMaterial(), 1);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);


        item.setItemMeta(meta);

        return item;
    }

    public ItemStack createItem(final XMaterial xMat, final String name, final String ...lore) {
        final ItemStack item = new ItemStack(xMat.parseMaterial(), 1);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);

        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    public String getConfigPath(String path){
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(TrollCore.instance.getConfig().getString(path)));
    }



    public String getConfigPath(String path, boolean withPrefix){
        return ChatColor.translateAlternateColorCodes('&', (withPrefix ? ( getConfigPath("Variables.prefix") + getConfigPath(path)) : (getConfigPath(path))));
    }

    public String uuidOrName(Player who, boolean useUUID) {
        return useUUID ? who.getUniqueId().toString() : who.getName();
    }

    private final static Pattern UUID_REGEX_PATTERN =
            Pattern.compile("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$");

    public boolean isValidUUID(String str) {
        if (str == null) {
            return false;
        }
        return UUID_REGEX_PATTERN.matcher(str).matches();
    }

    public void addOrRemove(HashMap<String, Player> collection, Player player){
        String uOrN = uuidOrName(player, TrollCore.instance.usingUUID);
        if(collection.containsKey(uOrN))
            collection.remove(uOrN);
        else
            collection.put(uOrN, player);
    }

    public boolean advancedPermissionsChecker(Player player, String extraPermissions){
        if(TrollCore.instance.getConfig().getBoolean("variables.advancedPermissions.enabled")){
            if(TrollCore.instance.usingUUID){
                OfflinePlayer pp = Bukkit.getPlayerExact(Objects.requireNonNull(TrollCore.instance.getConfig().getString("variables.advancedPermissions.name")));
                if(pp != null && pp.isOnline() && pp.hasPlayedBefore()){
                    return player.getUniqueId().equals(pp.getUniqueId());
                } else {
                    return false;
                }
            } else {
                return player.getName().equals(TrollCore.instance.getConfig().getString("variables.advancedPermissions.name"));
            }
        } else {
            return player.hasPermission(extraPermissions);
        }
    }


    public String reverseMessage(String i){
        StringBuilder res = new StringBuilder();
        int length = i.length();

        for (int i1 = length - 1 ; i1 >= 0 ; i1--)
            res.append(i.charAt(i1));
        return res.toString();
    }

    private Location getProperLocationOverWorldEnd(World world,
                                                          Player player, double x, double z) {

        Location location = null;
        Block block = world.getHighestBlockAt((int)x, (int)z);
        location = new Location(world, x, (double)block.getY(), z);
        return location;
    }


    private boolean chkRelativeBlock(Block block, BlockFace face, int distance){

        Block relativeBlock = block.getRelative(face, distance);

        Block footBlock = relativeBlock.getRelative(BlockFace.UP);
        Block headBlock = relativeBlock.getRelative(BlockFace.UP, 2);

        if(!relativeBlock.isLiquid() && !relativeBlock.isEmpty()){
            return footBlock.isEmpty() && headBlock.isEmpty();
        }

        return false;
    }

    private Location getProperLocationNether(World world, double x, double y, double z){

        int y2 = (int)y;
        Location location = null;
        Block centerBlock = null;
        if (y2 <= 120) do {
            centerBlock = world.getBlockAt((int) x, y2, (int) z);
            if (chkRelativeBlock(centerBlock, BlockFace.SELF, 0)) {
                location = new Location(world, x, (double) y2 + 2d, z);
                break;
            }
            y2++;
        } while (y2 <= 120);

        return location;
    }
    public boolean teleportTo(World world, Player player, double x, double z){
        String worldName = world.getName();
        Location location;

        switch (worldName) {
            case "world", "world_the_end" -> {
                location = getProperLocationOverWorldEnd(world, player, x, z);
                return player.teleport(location);
            }
            case "world_nether" -> {
                location = getProperLocationNether(world, x, 3, z);
                if (location == null) {
                    return false;
                }
                return player.teleport(location);
            }
            default -> {
                return false;
            }
        }
    }


    /**
     * Determines if the specified string array contains any of the strings in the specified list.
     *
     * @param stringArray the string array to check
     * @param list the list of strings to check for
     * @return whether the specified string array contains any of the strings in the specified list
     */
    public boolean containsAny(String stringArray, Iterable<String> list) {
        if (stringArray == null) {
            return false;
        }
        for (String piece : list) {
            if (piece != null && stringArray.contains(piece)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Returns the value of the specified key in the configuration as the specified type. Throws an exception if the key
     * is not found or the value is not of the specified type.
     *
     * @param key the key to get the value for
     * @param type the expected type of the value
     * @param <T> the type parameter
     * @return the value of the specified key in the configuration as the specified type
     */
    public <T> T getConfig(String key, Class<T> type) {
        Object value = TrollCore.instance.getConfig().get(key);
        if (value == null) {
            throw new IllegalArgumentException("Key not found: " + key);
        }
        if (type.isInstance(value)) {
            return type.cast(value);
        } else {
            throw new IllegalArgumentException("Invalid type for key: " + key);
        }
    }
    /**
     * Returns the value of the specified key in the configuration as the specified type. Throws an exception if the key
     * is not found or the value is not of the specified type.
     *
     * @param key the key to get the value for
     * @param type the expected type of the value
     * @param prefixed whether to apply the prefix specified in the configuration to the value
     * @param <T> the type parameter
     * @return the value of the specified key in the configuration as the specified type
     */
    public <T> T getConfig(String key, Class<T> type, boolean prefixed) {
        Object value = TrollCore.instance.config.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Key not found: " + key);
        }
        if (type.isInstance(value)) {
            if (type == String.class) {
                value = ChatColor.translateAlternateColorCodes('&', (String) value);
                if (prefixed) {
                    value = TrollCore.instance.getConfig().getString("variables.prefix") + value;
                }
            }
            return type.cast(value);
        } else {
            throw new IllegalArgumentException("Invalid type for key: " + key);
        }
    }


    /**

     Cleans the specified collection if possible.
     @param collection the collection to clean
     */
    public void cleanCollectionIfPossible(HashMap<?, ?> collection){
        if(collection != null && collection.size() > 0){
            collection.clear();
        }
    }
    /**

     Cleans the specified collection if possible.
     @param collection the collection to clean
     */
    public void cleanCollectionIfPossible(Map<?, ?> collection){
        if(collection != null && collection.size() > 0){
            collection.clear();
        }
    }
    /**

     Cleans the specified collection if possible.
     @param collection the collection to clean
     */
    public void cleanCollectionIfPossible(IndexableMap<?, ?> collection){
        if(collection != null && collection.size() > 0){
            collection.clear();
        }
    }

    /**

     Cleans the specified collection if possible.
     @param collection the collection to clean
     */
    public void cleanCollectionIfPossible(List<?> collection){
        if(collection != null && collection.size() > 0){
            collection.clear();
        }
    }
}
