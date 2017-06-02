package Main.teams.Detetive;

import Main.ItemsEdit;
import Main.Main;
import Main.Runnable.BukkitRunnable;
import Main.Util.Deatharea;
import Main.Util.Effects;
import Main.Util.PacketUtil;
import Main.teams.Assasino.AssasinMain;
import Main.teams.PacketControl;
import Main.teams.PlayerType;
import Main.teams.Vítima.Victimain;
import br.com.tlcm.cc.API.CoreD;
import br.com.tlcm.cc.API.CoreDPlayer;
import me.confuser.barapi.BarAPI;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

/**
 * Created by GMDAV on 18/04/2017.
 */
public class listenersD implements Listener {

    private Set<Deatharea> AnalisouCaso = new HashSet<>();

    private Random random = new Random();
    private boolean podeusar = true;
    private final int DelaySeconds = 5;


    @EventHandler(ignoreCancelled = true)
    private void Detetiveclick(PlayerInteractEvent event){
                if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
                    if(detetiveMain.getInsance().hasdeath(event.getClickedBlock().getLocation()) != null){
                    if(event.getItem() != null && event.getItem().hasItemMeta()) {
                        if (event.getItem().getItemMeta().equals(ItemsEdit.getTesouraDetetive().getItemMeta())) {
                            if (detetiveMain.getInsance().hasplayerinside(event.getPlayer().getName())) {
                                if (podeusar) {


                                    event.getPlayer().sendMessage("§1Analisando o corpo...");

                                    new org.bukkit.scheduler.BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            if (random.nextInt(100) >= 50) {

                                                detetiveMain.getInsance().hasdeath(event.getClickedBlock().getLocation())
                                                        .forEach(P -> P.getDamagesTakenByPlayer().forEach(event.getPlayer()::sendMessage));

                                                podeusar = false;

                                                event.getPlayer().setLevel(20 * DelaySeconds);
                                                event.getPlayer().setExp(1F);



                                                Random random = new Random();
                                                List<Deatharea> locations = detetiveMain.getInsance().hasdeath(event.getClickedBlock().getLocation());

                                                event.getPlayer().openInventory(locations.get(random.nextInt(locations.size())).getDamagesTakenInventory());


                                                ((BukkitRunnable) () -> {
                                                        if (!podeusar) {
                                                            podeusar = true;
                                                        }
                                                }).runTaskLater(Main.getInstance(), 20 * DelaySeconds);

                                                short Durab = (short)Math.floor(event.getPlayer().getItemInHand().getDurability() - (event.getPlayer().getItemInHand().getType().getMaxDurability()*0.3));
                                                event.getPlayer().getItemInHand().setDurability(Durab);


                                            } else {
                                                event.getPlayer().sendMessage("§4Você não conseguiu analisar o corpo");
                                                podeusar = false;
                                                event.getPlayer().setLevel(20 * DelaySeconds);
                                                event.getPlayer().setExp(1F);

                                                ((BukkitRunnable) () -> {

                                                        if (!podeusar) {
                                                            podeusar = true;
                                                        }

                                                }).runTaskLater(Main.getInstance(), 20 * DelaySeconds);

                                                short Durab = (short)Math.floor(event.getPlayer().getItemInHand().getDurability() - (event.getPlayer().getItemInHand().getType().getMaxDurability()*0.3));
                                                event.getPlayer().getItemInHand().setDurability(Durab);

                                            }
                                        }
                                    }.runTaskLater(Main.getInstance(), 20 * DelaySeconds);


                                }else {
                                    event.getPlayer().sendMessage("§4Você já está analisando um corpo");
                                }
                            }
                        }
                    }
                }
            }

        }


    /** Este é um listener feito para capturar toda a vez que um detetive analisar um player usando a tesoura.
     * @param event é o evento capturado
     */

    @EventHandler(ignoreCancelled = true)
        private void DetetiveScanPlayer(PlayerInteractEntityEvent event) {
            if (event.getRightClicked() instanceof Player && !CoreDPlayer.isSpectator(((Player) event.getRightClicked()).getName())) {
                    if (event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().hasItemMeta()) {
                        if (event.getPlayer().getItemInHand().getItemMeta().equals(ItemsEdit.getTesouraDetetive().getItemMeta())) {
                            Player Victim = (Player)event.getRightClicked();

                            if (podeusar) {

                                if(detetiveMain.getInsance().isRevelado(Victim.getName())){
                                    event.getPlayer().sendMessage("§cEste player já foi relevado");
                                    return;
                                }


                                event.getPlayer().sendMessage("§bAnalisando o " + Victim.getName());

                                Effects.SendParticle(Effects.ParticleEffects.FLAME,Victim.getLocation(),50,true);

                                new org.bukkit.scheduler.BukkitRunnable(){
                                    int timer = 5;

                                    @Override
                                    public void run() {

                                        if(timer == 0){
                                            if(AssasinMain.getInstance().hasplayerinside(Victim.getName())){
                                                Bukkit.getServer().broadcastMessage("§c" + event.getPlayer().getName() + " descobriu que §c" + Victim.getName() + " §cé um assasino");
                                                PacketControl.getInstance().UpdateTeamSafe(Victim.getName(),PlayerType.Assasino,"§4");
                                                detetiveMain.getInsance().addRelevados(Victim.getName(), PlayerType.Assasino);
                                                Victimain.getInstance().addrevelado(Victim.getName(),PlayerType.Assasino);
                                            }else {
                                                Bukkit.getServer().broadcastMessage("§a" + event.getPlayer().getName() + " descobriu que §a" + Victim.getName() + " §aé um inocente");
                                                PacketControl.getInstance().UpdateTeamSafe(Victim.getName(),PlayerType.Vitima,"§a");
                                                detetiveMain.getInsance().addRelevados(Victim.getName(), PlayerType.Vitima);
                                                Victimain.getInstance().addrevelado(Victim.getName(),PlayerType.Vitima);
                                            }


                                            cancel();
                                        }

                                        if(timer == 5){
                                            Victim.sendMessage("§aO detetive está te analisando !");

                                            PotionEffect PE = new PotionEffect(PotionEffectType.BLINDNESS,2,1);
                                            PotionEffect PR = new PotionEffect(PotionEffectType.SLOW,2,5);

                                            Victim.addPotionEffect(PE);
                                            Victim.addPotionEffect(PR);


                                            podeusar = false;
                                            event.getPlayer().setLevel(20 * DelaySeconds);
                                            event.getPlayer().setExp(1F);

                                            ((BukkitRunnable) () -> {

                                                if (!podeusar) {
                                                    podeusar = true;
                                                }

                                            }).runTaskLater(Main.getInstance(), 20 * DelaySeconds);
                                        }

                                        Arrays.asList(Bukkit.getOnlinePlayers()).forEach(P -> {
                                            switch (timer){
                                                case 5:
                                                    P.playNote(P.getLocation(), Instrument.BASS_DRUM, Note.natural(1, Note.Tone.D));
                                                    break;
                                                case 4:
                                                    P.playNote(P.getLocation(), Instrument.BASS_DRUM, Note.natural(1, Note.Tone.B));
                                                    break;
                                                case 3:
                                                    P.playNote(P.getLocation(), Instrument.BASS_DRUM, Note.natural(1, Note.Tone.G));
                                                    break;
                                                case 2:
                                                    P.playNote(P.getLocation(), Instrument.BASS_DRUM, Note.natural(1, Note.Tone.A));
                                                    break;
                                                case 1:
                                                    P.playNote(P.getLocation(), Instrument.BASS_DRUM, Note.natural(1, Note.Tone.B));
                                                    break;
                                                default:
                                                    break;
                                            }
                                        });


                                        timer--;
                                    }
                                }.runTaskTimer(Main.getInstance(),0,20);

                                short Durab = (short)Math.floor(event.getPlayer().getItemInHand().getDurability() - (event.getPlayer().getItemInHand().getType().getMaxDurability()*0.3));
                                event.getPlayer().getItemInHand().setDurability(Durab);

                            } else {
                                event.getPlayer().sendMessage("§4Aguarde para examinar");
                            }

                        }
                    }
            }
        }

        @EventHandler(ignoreCancelled = true)
        private void DetetiveDead(PlayerDeathEvent event){

            if(detetiveMain.getInsance().getPlayer() != null && event.getEntity().getKiller() != null) {

                if (Victimain.getInstance().hasplayerinside(event.getEntity().getName()) && detetiveMain.getInsance().hasplayerinside(event.getEntity().getKiller().getName())) {

                    event.getEntity().getKiller().sendMessage("§4Você matou um §ainocente!");
                    Random random = new Random();

                    double Vida =  event.getEntity().getHealth();

                    double Damage = random.nextInt(30);

                    double Dano = Vida * (Damage / 100);

                    event.getEntity().getKiller().damage(Dano);

                    event.getEntity().getKiller().sendMessage("§cVocê perdeu " + Dano + "% de sua vida total.");
                    return;
                }

                if (detetiveMain.getInsance().getPlayer().equalsIgnoreCase(event.getEntity().getName())) {
                    detetiveMain.getInsance().removePlayer(event.getEntity().getName());
                }
            }

            if(detetiveMain.getInsance().getPlayer() != null
                    && event.getEntity().getLastDamageCause() != null && event.getEntity().getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)){
                if(detetiveMain.getInsance().hasplayerinside(event.getEntity().getName())){
                    if(event.getEntity().getLastDamageCause().getEntity().hasMetadata("player")){
                        if(Bukkit.getPlayer(event.getEntity().getLastDamageCause().getEntity().getMetadata("player").get(0).asString()) != null) {

                            Player Killer = Bukkit.getPlayer(event.getEntity().getLastDamageCause().getEntity().getMetadata("player").get(0).asString());

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

    /**
     * Aqui foi posto um Runnable feito para remover o delay quando passar o tempo determinado.
     */

    public void Delaytouse(){
            (new org.bukkit.scheduler.BukkitRunnable() {
                @Override
                public void run() {
                    if(detetiveMain.getInsance().getPlayer() == null ||
                            Bukkit.getServer().getPlayer(detetiveMain.getInsance().getPlayer()) == null
                            || CoreDPlayer.isSpectator(detetiveMain.getInsance().getPlayer())){
                        cancel();
                    }

                    if(!podeusar) {
                        Player player = Bukkit.getPlayer(detetiveMain.getInsance().getPlayer());
                        player.setExp(player.getExp() - 1F/(DelaySeconds*20));
                        player.setLevel(player.getLevel()-1);
                    }

                }

            }).runTaskTimer(Main.getInstance(),0,1);
        }

    /**
     * Capura do evento de quando um detetive usar um arco, matar o alvo de forma instantânea.
     * @param event é o evento capturado.
     */

    @EventHandler(ignoreCancelled = true)
    private void PlayerUseBow(EntityDamageByEntityEvent event){
            if(CoreD.isRunning() && detetiveMain.getInsance().getPlayer() != null) {
                if (event.getEntity() instanceof Player && !CoreDPlayer.isSpectator((Player)event.getEntity()) && event.getDamager() instanceof Arrow) {
                    Player Victim = (Player) event.getEntity();
                    @SuppressWarnings("deprecated")
                    Player Killer = (Player)((Arrow) event.getDamager()).getShooter();

                    if (detetiveMain.getInsance().hasplayerinside(Killer.getName())) {

                            event.setDamage(CoreDPlayer.getHealth(Victim));

                            if (!Victim.isDead()) {
                                CoreDPlayer.setHealth(Victim, 0);
                            }

                            if (Victimain.getInstance().getTeams().contains(((Player) event.getEntity()).getName())) {
                                ((Player) event.getDamager()).sendMessage("§4Você matou um inocente!");
                                Random random = new Random();

                                double Vida = CoreDPlayer.getHealth((Player) event.getDamager());

                                double Damage = random.nextInt(30);

                                double Dano = Vida * (Damage / 100);

                                ((Player) event.getDamager()).damage(Dano);

                                ((Player) event.getDamager()).sendMessage("§cVocê perdeu " + Dano + "% de sua vida total.");
                            }

                    }
                }
            }
    }


}
