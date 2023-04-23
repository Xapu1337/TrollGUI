package me.xapu1337.recodes.trollgui.utilities;

import me.xapu1337.recodes.trollgui.cores.TrollCore;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.Objects;
import java.util.Random;

public class Utils {


    private static final SingletonBase<Utils> instance = new SingletonBase<>(Utils.class);

    public static Utils getInstance() {
        return instance.get();
    }

    public final NamespacedKey UUID_KEY = new NamespacedKey(TrollCore.getInstance(), "uuid");
    public final NamespacedKey SELECTION_ITEM_ID = new NamespacedKey(TrollCore.getInstance(), "selectionItemID");

   public boolean checkUniqueInventory(InventoryClickEvent clickEvent, InventoryHolder inventoryHolder) {
       return Objects.equals(clickEvent.getInventory().getHolder(), inventoryHolder);
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


    public String reverseMessage(String i){
        StringBuilder res = new StringBuilder();
        int length = i.length();

        for (int i1 = length - 1 ; i1 >= 0 ; i1--)
            res.append(i.charAt(i1));
        return res.toString();
    }
    public int getRandomNumberInRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
