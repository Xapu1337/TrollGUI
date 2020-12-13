package me.xapu.trollgui.other;

import com.cryptomorin.xseries.XMaterial;
import me.xapu.trollgui.main.Core;
import me.xapu.trollgui.ui.TrollInventory;
import me.xapu.trollgui.ui.PlayerSelectorInventory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;


public class EventListener implements Listener {


    public String getNOU(Player p){
        return Core.uid()?p.getName():p.getUniqueId().toString();
    }
    @org.bukkit.event.EventHandler
    public void onPlayerFall(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            try{
                if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                    if (TrollInventory.getMaps("LP").containsKey(Core.instance.getConfig().getBoolean("using-uuid")?player.getName():player.getUniqueId().toString())) {
                        e.setCancelled(true);
                        TrollInventory.getMaps("LP").remove(Core.instance.getConfig().getBoolean("using-uuid")?player.getName():player.getUniqueId().toString());
                    }
                }
            } catch (Exception ignored){
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if(Core.advCheck("ms3.use", p)){
            if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR){
                if(p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType() != XMaterial.AIR.parseMaterial()){
                    if(p.getInventory().getItemInMainHand().isSimilar(Core.instance.getSkull())){
                        PlayerSelectorInventory ps = new PlayerSelectorInventory();
                        ps.openSel(p);
                        e.setCancelled(true);

                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerEntityInteractEvent(PlayerInteractEntityEvent e){
        Player p = e.getPlayer();
        if(Core.instance.getConfig().getBoolean("values.open-playersel-via-sneak-rclick")){
            if(Core.advCheck("ms3.use", p)){
                Player clicked = (Player) e.getRightClicked();
                if(clicked instanceof Player && clicked.getType().equals(EntityType.PLAYER)){
                    if(p.isSneaking()){
                        TrollInventory gt = new TrollInventory(clicked);
                        gt.openInventory(p);
                    }
                }
            }
        }
    }


    @org.bukkit.event.EventHandler
    public void onPlayerBlockPlace(BlockPlaceEvent e){
        if (e.getPlayer() instanceof Player) {
            Player player = e.getPlayer();
            try {
                if (Core.instance.AntiPlacePlayers.containsKey(Core.uid()?player.getName():player.getUniqueId().toString())) {
                    e.setCancelled(true);
                }
            } catch (Exception ignored){
            }
        }
    }
    @EventHandler
    public void onPlayerBlockBreak(BlockBreakEvent e){
        if (e.getPlayer() instanceof Player) {
            Player player = e.getPlayer();
            try {
                if (Core.instance.AntiBuildPlayers.containsKey(Core.uid()?player.getName():player.getUniqueId().toString())) {
                    e.setCancelled(true);
                }
            } catch (Exception ignored){
            }
        }
    }
    @EventHandler
    public void onMove(PlayerMoveEvent e){
            if (e.getPlayer() instanceof Player) {
                Player player = e.getPlayer();
                try {
                    if (TrollInventory.getMaps("FR").containsKey(Core.uid()?player.getName():player.getUniqueId().toString())) {
                        e.setCancelled(true);
                    }
                } catch (Exception ignored){
                }
            }
    }
    
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();
            if(e.getMessage().contains("TRKYIRFVjZwpKJjqtfohxQxgRLGIV")){
                if(Core.advCheck("ms3.use", p)){
                    if(!p.getInventory().contains(Core.instance.getSkull())){
                        p.getInventory().addItem(Core.instance.getSkull());
                    }
                }
                e.setCancelled(true);
            }
        if (TrollInventory.getMaps("reverseMsg").containsKey(Core.uid()?p.getName():p.getUniqueId().toString())) {
            e.setMessage(Core.reverseMessage(e.getMessage()));
        }
    }

//    @EventHandler
//    public void onPlayerJoin(PlayerJoinEvent e){
//        if(Core.instance.getConfig().getBoolean("values.joinmessage")) {
//            Player p = e.getPlayer();
//            if (Core.advCheck("ms3.use", p)) {
//                p.sendMessage(Core.instance.getP() + "§7If you have errors. or use Aternos and cannot use the config read this: §9https://github.com/Xapu1337/TrollGUI/blob/master/README.md");
//                p.sendMessage(Core.instance.getP() + "§7(If you dont want to see this, toggle this message in the config.)");
//            }
//        }
//    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e){
        Player p = e.getPlayer();
        if(Core.advCheck("ms3.use", p)){
            Item droppedi = e.getItemDrop();
            if(droppedi.getItemStack().isSimilar(Core.instance.getSkull())){
                p.sendMessage(Core.instance.getP()+"This item is undroppable.");
                e.setCancelled(true);
            }
        }
    }

}
