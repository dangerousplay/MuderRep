package Main.listeners;

import Main.ItemsEdit;
import Main.teams.Vítima.Victimain;
import br.com.tlcm.cc.API.CoreD;
import org.bukkit.Material;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Cow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;

/**
 * Created by GMDAV on 20/04/2017.
 */
public class disabledevents implements Listener {

    @EventHandler
    private void Achievement(PlayerAchievementAwardedEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    private void MobSpawn(EntitySpawnEvent event) {
        if(!event.getEntity().getType().equals(EntityType.DROPPED_ITEM)){
            event.setCancelled(true);
        }
    }


    @EventHandler
    private void PlayerPickup(PlayerPickupItemEvent event){
        if(event.getItem().getItemStack().getType().equals(Material.SHEARS)
                ||event.getItem().getItemStack().getType().equals(Material.REDSTONE) || event.getItem().getItemStack().getType().equals(Material.DIAMOND_SWORD)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void PlayerDropItem(PlayerDropItemEvent event){
            if(event.getItemDrop().getItemStack().getType().equals(Material.SHEARS)
                    || event.getItemDrop().getItemStack().getType().equals(Material.DIAMOND_SWORD)){
                event.setCancelled(true);
            }
    }

    @EventHandler
    private void Hungry(FoodLevelChangeEvent event){
        if(CoreD.isRunning())
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    private void BlockExplosion(EntityExplodeEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    private void PlayerSprint(PlayerMoveEvent event){

        if(Victimain.getInstance().getUsedItems().containsKey(event.getPlayer().getName()) || event.getPlayer().isSprinting()) {
            Victimain.getInstance().getUsedItems().forEach((K, V) -> {
                if (K.equalsIgnoreCase(event.getPlayer().getName())) {
                    if(!V.contains(ItemsEdit.ItemsToConsume.PoçãoVelocidade))
                        event.getPlayer().setSprinting(false);

                }
            });
        }else {
            if(Victimain.getInstance().hasplayerinside(event.getPlayer().getName()))
                event.getPlayer().setSprinting(false);
        }
    }


}
