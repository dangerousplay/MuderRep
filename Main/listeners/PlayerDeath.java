package Main.listeners;

import Main.ItemsEdit;
import Main.Util.Deatharea;
import Main.Util.NPCSpawn;
import Main.Util.SpawnUtil;
import Main.teams.Assasino.AssasinMain;
import Main.teams.Detetive.detetiveMain;
import br.com.tlcm.cc.API.CoreD;
import br.com.tlcm.cc.API.CoreDPlayer;
import net.minecraft.server.v1_7_R2.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by GMDAV on 18/04/2017.
 */
public class PlayerDeath implements Listener {
    private static final PlayerDeath Instance = new PlayerDeath();
    private final List<EntityPlayer> LocationsPlayersDeaths = new ArrayList<>();
    private final Random random = new Random();

    private final Map<String,Integer> PlayersQuaseSangrou = new Hashtable<>();
    private final Map<String,Map<String,Double>> entityDamageTaken = new Hashtable<>();

    @EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
    private void onplayerdeath(PlayerDeathEvent evento){
        if(CoreD.isRunning()) {
            LocationsPlayersDeaths.add(NPCSpawn.getInstance().spawnCorpse(evento.getEntity()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST,ignoreCancelled = true)
    private void Playerkillcloser(PlayerDeathEvent event){
        if(CoreD.isRunning() && event.getEntity().getKiller() != null && event.getEntity() != null) {

            Arrays.stream(Bukkit.getServer().getOnlinePlayers())
                    .filter(P -> !P.getName().equalsIgnoreCase(event.getEntity().getName()))
                    .forEach(P -> P.playSound(event.getEntity().getLocation(), Sound.CAT_MEOW,100,100));

            if (event.getEntity().getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
                if (event.getEntity().getKiller() != null) {
                    event.getEntity().getKiller().sendMessage("§aVocê matou o(a) §e" + event.getEntity().getName());

                    if (!PlayersQuaseSangrou.containsKey(event.getEntity().getKiller().getName())) {
                        PlayersQuaseSangrou.put(event.getEntity().getKiller().getName(), random.nextInt(100));
                    } else {
                        if (PlayersQuaseSangrou.get(event.getEntity().getKiller().getName()) + random.nextInt(100) >= 100) {
                            SpawnUtil.addplayertoblood(event.getEntity().getKiller());
                            event.getEntity().getKiller().sendMessage("§4Você está sujo de §lsangue!");
                        }

                    }

                    entityDamageTaken.forEach((Strin, Map) -> {
                        if (Strin.equalsIgnoreCase(event.getEntity().getName())) {
                            detetiveMain.getInsance().addlocalcrime(
                                    new Deatharea(SpawnUtil.SpawnBlood(event.getEntity().getLocation().add(0,1,0)),
                                            event.getEntity().getKiller().getName(), Map));
                        }
                    });


                    event.setDeathMessage("§a" + event.getEntity().getName() + " §7foi morto por alguém");
                    return;
                }
            }


            entityDamageTaken.forEach((Strin, Map) -> {
                if (Strin.equalsIgnoreCase(event.getEntity().getName())) {
                    detetiveMain.getInsance().addlocalcrime(
                            new Deatharea(SpawnUtil.SpawnBlood(event.getEntity().getLocation().add(0,-1,0)),
                                    event.getEntity().getKiller().getName(), Map));
                }
            });

            event.setDeathMessage("§a" + event.getEntity().getName() + " §7morreu misteriosamente");
            return;
        }

        event.setDeathMessage(null);
    }

    @EventHandler(ignoreCancelled = true)
    private void Playertomardano(EntityDamageByEntityEvent event){
        if(CoreD.isRunning()) {

            if (event.getEntity() instanceof Player && !CoreDPlayer.isSpectator((Player)event.getEntity()) && event.getDamager() instanceof Player) {

                Player Victim = (Player) event.getEntity();
                Player Damager = (Player) event.getDamager();
                double Refl = 0D;



                try {
                    Field Field = event.getClass().getSuperclass().getDeclaredField("damage");
                    Field.setAccessible(true);
                    Refl = Field.getDouble(event);
                    Field.setAccessible(!Field.isAccessible());

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                double Damage = Refl;

                if (entityDamageTaken.containsKey(Victim.getName())) {

                    entityDamageTaken.forEach((Strin, Map) -> {
                        if (Strin.equalsIgnoreCase(Victim.getName())) {
                            if (Map.containsKey(Damager.getName())) {
                                Map.replace(Damager.getName(), Map.get(Damager.getName()) + Damage);
                            } else {
                                Map.put(Damager.getName(), Damage);
                            }
                        }
                    });
                } else {
                    Map<String, Double> newmap = new Hashtable<>();
                    newmap.put(Damager.getName(), Damage);

                    entityDamageTaken.put(Victim.getName(), newmap);
                }


            }
        }

    }

    public static PlayerDeath getInstance(){
        return Instance;
    }

    public List<EntityPlayer> getLocationsPlayersDeaths(){
        return this.LocationsPlayersDeaths;
    }

    public void addPlayerDeath(EntityPlayer player){
        this.LocationsPlayersDeaths.add(player);
    }

    public Map<String,Map<String, Double>> getEntityDamageTaken() {
        return entityDamageTaken;
    }

    public Map<String,Double> getDamageEntity(String player){
        return entityDamageTaken.getOrDefault(player,new HashMap<>());
    }
}
