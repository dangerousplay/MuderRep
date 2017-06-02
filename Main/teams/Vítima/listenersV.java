package Main.teams.Vítima;

import Main.ItemsEdit;
import Main.teams.Detetive.detetiveMain;
import br.com.tlcm.cc.API.CoreD;
import br.com.tlcm.cc.API.CoreDPlayer;
import com.sun.deploy.security.EnhancedJarVerifier;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.Random;

/**
 * Created by GMDAV on 18/04/2017.
 */
public class listenersV implements Listener {

    @EventHandler(ignoreCancelled = true)
    private void PlayerUseItem(PlayerItemConsumeEvent event) {
        if (event.getItem().getType().equals(Material.GLASS_BOTTLE)) {
            Victimain.getInstance().addUsedItems(event.getPlayer().getName(), ItemsEdit.ItemsToConsume.PoçãoVelocidade);
        }
    }


    @EventHandler(ignoreCancelled = true)
    private void PlayerUseBow(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && !CoreDPlayer.isSpectator((Player)event.getEntity()) && event.getDamager() instanceof Arrow) {
            Player Victim = (Player) event.getEntity();
            @SuppressWarnings("deprecated")
            Player Killer = (Player)((Arrow) event.getDamager()).getShooter();

            if(Victim.getName().equalsIgnoreCase(Killer.getName())){
                event.setCancelled(true);
                return;
            }

            if (Victimain.getInstance().hasplayerinside(Killer.getName())) {

                    event.setDamage(CoreDPlayer.getHealth(Victim));

                    if (!Victim.isDead()) {
                        CoreDPlayer.setHealth(Victim, 0);
                    }

                    if (Victimain.getInstance().getTeams().contains(((Player) event.getEntity()).getName()) || detetiveMain.getInsance().hasplayerinside(((Player) event.getEntity()).getName())) {

                        Killer.sendMessage("§4Você matou um inocente!");
                        Random random = new Random();

                        double Vida = CoreDPlayer.getHealth(Killer);

                        double Damage = 10 + random.nextInt(40);

                        double Dano = Vida * (Damage / 100);

                        Killer.damage(Dano);

                        ((Player) event.getDamager()).sendMessage("§cVocê perdeu " + Dano + "% de sua vida total.");

                        if(Victimain.getInstance().hasplayerinside(Victim.getName())) {
                            Victimain.getInstance().removePlayer(Victim.getName());
                        }else {
                            detetiveMain.getInsance().setPlayer(null);
                        }

                        return;

                    }

                    ((Player) event.getEntity()).sendMessage("§aVocê matou um assasino!");
            }
        }



    }

    @EventHandler(ignoreCancelled = true)
    private void PlayerKillinocent(PlayerDeathEvent event) {
        Player Victim = event.getEntity();
        Player Killer = event.getEntity().getKiller();


        if (Killer != null && Victimain.getInstance().hasplayerinside(Killer.getName())) {


            if (Victimain.getInstance().getTeams().contains(Victim.getName())
                || detetiveMain.getInsance().getPlayer()!= null && detetiveMain.getInsance().hasplayerinside(event.getEntity().getName())) {

                event.getEntity().getKiller().sendMessage("§4Você matou um inocente!");
                Random random = new Random();

                double Vida = event.getEntity().getHealth();

                double Damage = random.nextInt(30);

                double Dano = Vida * (Damage / 100);

                event.getEntity().getKiller().damage(Dano);

                event.getEntity().getKiller().sendMessage("§cVocê perdeu " + Dano + "% de sua vida total.");

                return;
            }

             event.getEntity().sendMessage("§aVocê matou um assasino!");
        }

        if(event.getEntity().getLastDamageCause() != null && event.getEntity().getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)){
            if(Victimain.getInstance().hasplayerinside(event.getEntity().getName())){
                if(event.getEntity().getLastDamageCause().getEntity().hasMetadata("player")){
                    if(Bukkit.getPlayer(event.getEntity().getLastDamageCause().getEntity().getMetadata("player").get(0).asString()) != null) {

                        Killer = Bukkit.getPlayer(event.getEntity().getLastDamageCause().getEntity().getMetadata("player").get(0).asString());

                        if(Victimain.getInstance().hasplayerinside(Killer.getName())){

                            Killer.sendMessage("§4Você matou um §ainocente!");
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

    @EventHandler
    private void VictimDeath(PlayerDeathEvent event){
        if(Victimain.getInstance().hasplayerinside(event.getEntity().getName())){
            Victimain.getInstance().removePlayer(event.getEntity().getName());
        }
    }

}
