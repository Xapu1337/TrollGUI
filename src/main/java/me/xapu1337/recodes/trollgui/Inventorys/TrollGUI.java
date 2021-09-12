package me.xapu1337.recodes.trollgui.Inventorys;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Cores.Core;
import me.xapu1337.recodes.trollgui.Trolls.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TrollGUI implements Listener, InventoryHolder {
    private final Player caller;
    private final Player victim;
    public Inventory GUI;
    public String centerTitle(String title) {
        StringBuilder result = new StringBuilder();
        int spaces = (27 - ChatColor.stripColor(title).length());

        for (int i = 0; i < spaces; i++) {
            result.append(" ");
        }

        return result.append(title).toString();
    }
    public TrollGUI(Player caller, Player victim) {
        this.victim = victim;
        this.caller = caller;
        Bukkit.getPluginManager().registerEvents(this, Core.instance);
        GUI = Bukkit.createInventory(this, 54, centerTitle(
                Core.instance.utils.getConfigPath("MenuTitles.trollGUI")
                        .replace("%VICTIM%", victim.getName())
                        .replace("%PLAYER%", caller.getName())
                )
        );
        initializeItems();
    }

    public void initializeItems(){
        for(int i = 0; i < GUI.getSize(); i++)
            GUI.setItem(i, Core.instance.utils.createItem(XMaterial.GRAY_STAINED_GLASS_PANE, false, " "));

        GUI.setItem(10, Core.instance.utils.createItem(XMaterial.BLAZE_POWDER, false,
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.burnPlayer.name"),
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.burnPlayer.lore")));

        GUI.setItem(11, Core.instance.utils.createItem(XMaterial.BARRIER, false,
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.closeGUI.name"),
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.closeGUI.lore")));

        GUI.setItem(12, Core.instance.utils.createItem(XMaterial.CAULDRON, false,
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.dropAll.name"),
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.dropAll.lore")));

        GUI.setItem(13, Core.instance.utils.createItem(XMaterial.WATER_BUCKET, false,
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.dropItem.name"),
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.dropItem.lore")));

        GUI.setItem(14, Core.instance.utils.createItem(XMaterial.TNT, false,
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.explodePlayer.name"),
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.explodePlayer.lore")));

        XMaterial customMatForFakeBlock;

        if(XMaterial.matchXMaterial(Core.instance.config.getString("MenuItems.trollMenu.fakeBlock.options.block")).isPresent())
            customMatForFakeBlock = XMaterial.matchXMaterial(Core.instance.config.getString("MenuItems.trollMenu.fakeBlock.options.block")).get();
        else
            customMatForFakeBlock = XMaterial.TNT;

        GUI.setItem(15, Core.instance.utils.createItem(customMatForFakeBlock, false,
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.fakeBlock.name"),
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.fakeBlock.lore")));

        GUI.setItem(16, Core.instance.utils.createItem(XMaterial.PUFFERFISH, false,
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.fakeClear.name"),
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.fakeClear.lore").replace("%TIME%", String.valueOf(Core.instance.config.getInt("MenuItems.trollMenu.fakeClear.options.fakeClearDelay")))));

        GUI.setItem(19, Core.instance.utils.createItem(XMaterial.LIGHT_GRAY_DYE, false,
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.fakeOperator.name"),
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.fakeOperator.lore")));

        GUI.setItem(20, Core.instance.utils.createItem(XMaterial.SNOWBALL, Core.instance.singletons.frozenPlayers.containsKey(Core.instance.utils.uuidOrName(victim, Core.instance.usingUUID)),
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.freezePlayer.name"),
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.freezePlayer.lore"),
                Core.instance.singletons.frozenPlayers.containsKey(Core.instance.utils.uuidOrName(victim, Core.instance.usingUUID))
                        ?
                            Core.instance.utils.getConfigPath("MenuItems.trollMenu.extras.isEnabled")
                        :
                            Core.instance.utils.getConfigPath("MenuItems.trollMenu.extras.isDisabled")
        ));

        GUI.setItem(21, Core.instance.utils.createItem(XMaterial.TRAPPED_CHEST, false,
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.invSee.name"),
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.invSee.lore")));

        GUI.setItem(22, Core.instance.utils.createItem(XMaterial.ENDER_CHEST, false,
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.invShare.name"),
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.invShare.lore")));

        GUI.setItem(23, Core.instance.utils.createItem(XMaterial.FIREWORK_ROCKET, false,
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.launchPlayer.name"),
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.launchPlayer.lore")));

        GUI.setItem(24, Core.instance.utils.createItem(XMaterial.GRASS_BLOCK, Core.instance.singletons.noBuildPlayers.containsKey(Core.instance.utils.uuidOrName(victim, Core.instance.usingUUID)),
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.noBuild.name"),
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.noBuild.lore"),
                Core.instance.singletons.noBuildPlayers.containsKey(Core.instance.utils.uuidOrName(victim, Core.instance.usingUUID))
                        ?
                        Core.instance.utils.getConfigPath("MenuItems.trollMenu.extras.isEnabled")
                        :
                        Core.instance.utils.getConfigPath("MenuItems.trollMenu.extras.isDisabled")
        ));

        GUI.setItem(25, Core.instance.utils.createItem(XMaterial.STONE, Core.instance.singletons.noBreakPlayers.containsKey(Core.instance.utils.uuidOrName(victim, Core.instance.usingUUID)),
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.noBreak.name"),
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.noBreak.lore"),
                Core.instance.singletons.noBreakPlayers.containsKey(Core.instance.utils.uuidOrName(victim, Core.instance.usingUUID))
                        ?
                        Core.instance.utils.getConfigPath("MenuItems.trollMenu.extras.isEnabled")
                        :
                        Core.instance.utils.getConfigPath("MenuItems.trollMenu.extras.isDisabled")
        ));

        GUI.setItem(29, Core.instance.utils.createItem(XMaterial.BARRIER, false,
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.randomLook.name"),
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.randomLook.lore")));

        GUI.setItem(30, Core.instance.utils.createItem(XMaterial.PAPER, Core.instance.singletons.reverseMessagePlayers.containsKey(Core.instance.utils.uuidOrName(victim, Core.instance.usingUUID)),
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.reverseMessage.name"),
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.reverseMessage.lore"),
                Core.instance.singletons.reverseMessagePlayers.containsKey(Core.instance.utils.uuidOrName(victim, Core.instance.usingUUID))
                        ?
                        Core.instance.utils.getConfigPath("MenuItems.trollMenu.extras.isEnabled")
                        :
                        Core.instance.utils.getConfigPath("MenuItems.trollMenu.extras.isDisabled")
        ));

        GUI.setItem(31, Core.instance.utils.createItem(XMaterial.CARVED_PUMPKIN, false,
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.scarePlayer.name"),
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.scarePlayer.lore")));

        GUI.setItem(32, Core.instance.utils.createItem(XMaterial.PRISMARINE_SHARD, true,
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.thunder.name"),
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.thunder.lore")));


        GUI.setItem(53, Core.instance.utils.createItem(XMaterial.BARRIER, true,
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.returnToPlayerSelector.name"),
                Core.instance.utils.getConfigPath("MenuItems.trollMenu.returnToPlayerSelector.lore")));

    }

    @Override
    @NotNull
    public Inventory getInventory() { return GUI; }


    @EventHandler
    public void onInventoryClicked(InventoryClickEvent event) {
        if (event.getInventory().getHolder() != this) return; // IF the inventory belongs not to this class dismiss.
        event.setCancelled(true); // Disable the item to be draggable.

        final ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == XMaterial.AIR.parseMaterial()) return;

        switch (event.getRawSlot()){
            case 10:
                new BurnPlayerTroll(caller, victim).execute();
                break;
            case 11:
                new CloseGUITroll(caller, victim).execute();
                break;
            case 12:
                new DropAllTroll(caller, victim).execute();
                break;
            case 13:
                new DropItemTroll(caller, victim).execute();
                break;
            case 14:
                new ExplodePlayerTroll(caller, victim).execute();
                break;
            case 15:
                new FakeBlockTroll(caller, victim).execute();
                break;
            case 16:
                new FakeClearTroll(caller, victim).execute();
                break;
            case 19:
                new FakeOperatorTroll(caller, victim).execute();
                break;
            case 20:
                new FreezeTroll(caller, victim).execute();
                GUI.clear();
                initializeItems();
                break;
            case 21:
                new InvSeeTroll(caller, victim).execute();
                break;
            case 22:
                new InvShareTroll(caller, victim).execute();
                break;
            case 23:
                new LaunchPlayerTroll(caller, victim).execute();
                break;
            case 24:
                new NoBuildTroll(caller, victim).execute();
                GUI.clear();
                initializeItems();
                break;
            case 25:
                new NoBreakTroll(caller, victim).execute();
                GUI.clear();
                initializeItems();
                break;
            case 29:
                new RandomLookTroll(caller, victim).execute();
                break;
            case 30:
                new ReverseMessageTroll(caller, victim).execute();
                GUI.clear();
                initializeItems();
                break;
            case 31:
                new ScareTroll(caller, victim).execute();
                break;
            case 32:
                new ThunderPlayerTroll(caller, victim).execute();
                break;





            case 53:
                caller.openInventory(new PlayerSelector(caller).getInventory());
                break;
        }


    }
}
