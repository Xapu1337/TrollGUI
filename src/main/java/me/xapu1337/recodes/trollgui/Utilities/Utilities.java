package me.xapu1337.recodes.trollgui.Utilities;

import com.cryptomorin.xseries.XBiome;
import com.cryptomorin.xseries.XMaterial;
import com.google.common.base.Strings;
import me.xapu1337.recodes.trollgui.Cores.TrollCore;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.StringUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
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


    public String getString(String path, String defaultString){
        String configString = TrollCore.instance.getConfig().getString(path);
        if(configString == null || configString.length() == 0 ) return defaultString;
        else return configString;
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

    public void addOrRemove(HashMap<String, String> collection, Player player){
        if(collection.containsKey(uuidOrName(player, TrollCore.instance.usingUUID)))
            collection.remove(uuidOrName(player, TrollCore.instance.usingUUID));
        else
            collection.put(uuidOrName(player, TrollCore.instance.usingUUID), uuidOrName(player, TrollCore.instance.usingUUID));
    }

    public boolean advancedPermissionsChecker(Player player, String extraPermissions){
        if(TrollCore.instance.getConfig().getBoolean("variables.advancedPermissions.enabled")){
            if(TrollCore.instance.usingUUID){
                OfflinePlayer pp = Bukkit.getPlayerExact(TrollCore.instance.getConfig().getString("variables.advancedPermissions.name"));
                if(pp.isOnline() && pp.hasPlayedBefore()){
                    return player.getUniqueId().equals(pp.getUniqueId());
                }
            } else {
                return player.getName().equals(TrollCore.instance.getConfig().getString("variables.advancedPermissions.name"));
            }
        } else {
            return player.hasPermission(extraPermissions);
        }
        return false;
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
        loop: while(y2 <= 120){
            centerBlock = world.getBlockAt((int)x, y2, (int)z);
            if(chkRelativeBlock(centerBlock, BlockFace.SELF, 0)){
                location = new Location(world, x, (double)y2+2d, z);
                break loop;
            }
            y2++;
        }

        return location;
    }

    public boolean teleportTo(World world, Player player, double x, double z){
        String worldName = world.getName();
        Location location = null;

        if(worldName.equals("world") || worldName.equals("world_the_end")){
            location = getProperLocationOverWorldEnd(world, player, x, z);
            return player.teleport(location);
        }
        else if(worldName.equals("world_nether")){

            location = getProperLocationNether(world, x, 3, z);

            if(location == null)
                player.sendMessage("Not a safe location");

            return player.teleport(location);
        }
        else {
            return false;
        }
    }


    /**
     * Return whether the given string contains any of the provided elements.
     *
     * @param str    The string to analyze
     * @param pieces The items to check the string for
     *
     * @return True if the string contains at least one of the items
     */
    public boolean containsAny(String str, Iterable<String> pieces) {
        if (str == null) {
            return false;
        }
        for (String piece : pieces) {
            if (piece != null && str.contains(piece)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Null-safe method for checking whether a string is empty. Note that the string
     * is trimmed, so this method also considers a string with whitespace as empty.
     *
     * @param str The string to verify
     *
     * @return True if the string is empty, false otherwise
     */
    public boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Check that the given needle is in the middle of the haystack, i.e. that the haystack
     * contains the needle and that it is not at the very start or end.
     *
     * @param needle the needle to search for
     * @param haystack the haystack to search in
     *
     * @return true if the needle is in the middle of the word, false otherwise
     */
    public boolean isInsideString(char needle, String haystack) {
        int index = haystack.indexOf(needle);
        return index > 0 && index < haystack.length() - 1;
    }

}
