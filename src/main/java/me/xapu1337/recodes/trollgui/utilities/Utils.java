package me.xapu1337.recodes.trollgui.utilities;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.Objects;
import java.util.Random;

public class Utils {


    public static final SingletonBase<Utils> instance = new SingletonBase<>(Utils.class);

    public static Utils getInstance() {
        return instance.get();
    }


   public boolean checkUniqueInventory(InventoryClickEvent clickEvent, InventoryHolder inventoryHolder, Inventory inventory) {
        return Objects.equals(clickEvent.getInventory().getHolder(), inventoryHolder) && clickEvent.getInventory().equals(inventory);
   }


    public int getRandomNumberInRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
