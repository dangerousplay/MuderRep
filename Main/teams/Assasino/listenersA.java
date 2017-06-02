package Main.teams.Assasino;

import Main.ItemsEdit;
import Main.Main;
import Main.Runnable.BukkitRunnable;
import Main.listeners.PlayerUseItems;
import br.com.tlcm.cc.API.CoreD;
import br.com.tlcm.cc.API.CoreDPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

/**
 * Created by GMDAV on 18/04/2017.
 */
public class listenersA implements Listener {

    private final Map<String,BukkitTask> Podeusar = new Hashtable<>();
    private final int DelaySeconds = 20;

    @EventHandler(ignoreCancelled = true)
    private void PlayerDamage(EntityDamageByEntityEvent event) {
        if (CoreD.isRunning()) {
            if (event.getEntity() instanceof Player && !CoreDPlayer.isSpectator((Player)event.getEntity()) && event.getDamager() instanceof Player) {

                Player damager = (Player) event.getDamager();

                if (damager.getInventory().getItemInHand() != null && damager.getInventory().getItemInHand().hasItemMeta()) {

                    if (damager.getInventory().getItemInHand().getItemMeta().equals(ItemsEdit.getLaminadoran().getItemMeta())) {

                        if (Podeusar.containsKey(damager.getName())) {
                            event.setCancelled(true);
                            damager.sendMessage("§4Você não pode matar agora, limpando espada");
                        }
                    }
                }
            }
        }
    }


    @EventHandler(ignoreCancelled = true)
    private void AssasinTakeItem(PlayerPickupItemEvent event){
        if(CoreD.isRunning()) {
            if (AssasinMain.getInstance().hasplayerinside(event.getPlayer().getName())) {
                if (event.getItem().getItemStack().getType().equals(Material.BOW)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void AssasinDead(PlayerDeathEvent event){

        if(AssasinMain.getInstance().getPlayer() != null && event.getEntity().getKiller() != null) {

            Player damager = event.getEntity().getKiller();

            if (AssasinMain.getInstance().hasplayerinside(event.getEntity().getKiller().getName()) && AssasinMain.getInstance().hasplayerinside(event.getEntity().getName())) {

                event.getEntity().getKiller().sendMessage("§4Você matou um assasino!");
                Random random = new Random();

                double Vida = CoreDPlayer.getHealth(event.getEntity());

                double Damage = random.nextInt(30);

                double Dano = Vida * (Damage / 100);

                event.getEntity().getKiller().damage(Dano);

                event.getEntity().getKiller().sendMessage("§cVocê perdeu " + Dano + "% de sua vida total.");

            }

            damager.setExp(1F);
            damager.setLevel(20*DelaySeconds);
            damager.playSound(damager.getLocation(), Sound.ARROW_HIT,0,100);

            Podeusar.put(damager.getName(),

                    ((BukkitRunnable) () -> Podeusar.remove(damager.getName())
                    ).runTaskLater(Main.getInstance(),20*DelaySeconds));


            if (AssasinMain.getInstance().hasplayerinside(event.getEntity().getName())) {
                AssasinMain.getInstance().removePlayer(event.getEntity().getName());
            }
        }

        if(AssasinMain.getInstance().getPlayer() != null
                && event.getEntity().getLastDamageCause() != null && event.getEntity().getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)){
            if(AssasinMain.getInstance().hasplayerinside(event.getEntity().getName())){
                if(event.getEntity().getLastDamageCause().getEntity().hasMetadata("player")){
                    if(Bukkit.getPlayer(event.getEntity().getLastDamageCause().getEntity().getMetadata("player").get(0).asString()) != null) {

                        Player Killer = Bukkit.getPlayer(event.getEntity().getLastDamageCause().getEntity().getMetadata("player").get(0).asString());

                        if(AssasinMain.getInstance().hasplayerinside(Killer.getName())){

                            Killer.sendMessage("§4Você matou um assasino!");
                            Random random = new Random();

                            double Vida = CoreDPlayer.getHealth(event.getEntity());

                            double Damage = random.nextInt(30);

                            double Dano = Vida * (Damage / 100);

                            Killer.damage(Dano);

                            Killer.sendMessage("§cVocê perdeu " + Dano + "% de sua vida total.");

                        }
                    }
                }
            }
        }
    }

    public void Delaytouse(){
        ((BukkitRunnable) () ->

                Podeusar.forEach((P,K) -> {
                    if(Bukkit.getPlayer(P) == null) {
                        Podeusar.remove(P);
                        return;
                    }

                    Player player = Bukkit.getPlayer(P);
                    player.setExp(player.getExp() - 1F/(DelaySeconds*20));
                    player.setLevel(player.getLevel() - 1);
                })

        ).runTaskTimer(Main.getInstance(),0,1);
    }

}
