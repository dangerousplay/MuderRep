package Main.listeners;

import Main.Main;
import Main.Runnable.BukkitRunnable;
import Main.Util.NPCSpawn;
import Main.Util.SpawnUtil;
import Main.ItemsEdit;
import br.com.tlcm.cc.API.CoreD;
import br.com.tlcm.cc.API.CoreDPlayer;
import br.com.tlcm.cc.events.CoreDStoreEvent;
import net.minecraft.server.v1_7_R2.EntityPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by GMDAV on 20/04/2017.
 */
public class PlayerUseItems implements Listener {
    private Map<String,Map<Location,List<String>>> PlayersUseTNT = new Hashtable<>();
    private Map<Location,Map<String,List<String>>> LocationUsedTNT = new Hashtable<>();
    private Map<String,List<String>> PlayersAtingidos = new Hashtable<>();


    private static PlayerUseItems Instance = new PlayerUseItems();

    public static PlayerUseItems getInstance(){
        return Instance;
    }


    @EventHandler
    private void PlayerBuyItem(CoreDStoreEvent event){

        if(event.getStoreItem().getItemMeta().equals(ItemsEdit.PocaoInvisibilidade().getItemMeta())){
            ((BukkitRunnable) () -> event.getPlayer().getInventory().remove(event.getStoreItem())).runTaskLater(Main.getInstance(),1);


            ItemMeta Meta = ItemsEdit.PocaoInvisibilidade().getItemMeta();
            Meta.setDisplayName("§aPoção de Invisibilidade");
            Meta.setLore(Arrays.asList("§aUse para ficar invisível,", "Você só será revelado quando Atacar!"));

            ItemStack Potion = ItemsEdit.PocaoInvisibilidade();
            Potion.setItemMeta(Meta);


            event.getPlayer().getInventory().addItem(Potion);

        }

        if(event.getStoreItem().getItemMeta().equals(ItemsEdit.PocaoVelocidade().getItemMeta())){
            ((BukkitRunnable) () -> event.getPlayer().getInventory().remove(event.getStoreItem())).runTaskLater(Main.getInstance(),1);


            ItemMeta Meta = ItemsEdit.PocaoInvisibilidade().getItemMeta();
            Meta.setDisplayName("§bPoção de Velocidade");
            Meta.setLore(Collections.singletonList("§aUse para andar mais rápido,"));

            ItemStack Potion = ItemsEdit.PocaoInvisibilidade();
            Potion.setItemMeta(Meta);


            event.getPlayer().getInventory().addItem(Potion);

        }

        if(event.getStoreItem().getType().equals(Material.TNT)){
           ItemMeta Meta = event.getStoreItem().getItemMeta();

           Meta.setLore(Collections.singletonList("§cUse para explodir players"));
           Meta.setDisplayName("§4C4 BOMB");

           event.getStoreItem().setItemMeta(Meta);
        }

    }



    @EventHandler(ignoreCancelled = true)
    private void PlayerUseItem(PlayerItemConsumeEvent event){
        if(event.getItem().getType().equals(Material.GLASS_BOTTLE)){
            SpawnUtil.removeplayerblood(event.getPlayer());
        }
    }


    @EventHandler(ignoreCancelled = true)
    private void PlayerKillInvisible(EntityDamageByEntityEvent event){
        if(CoreD.isRunning()){
            if(event.getEntity() instanceof Player && event.getDamager() instanceof Player){
                Player Victim = (Player)event.getEntity();

                if(CoreDPlayer.isSpectator(Victim)) {
                    return;
                }

                Player Damager = (Player)event.getDamager();
                if(Damager.hasPotionEffect(PotionEffectType.INVISIBILITY)){
                    Damager.sendMessage("§2Você perdeu a sua camuflagem");
                    Damager.removePotionEffect(PotionEffectType.INVISIBILITY);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void PlayerUseTNT(EntityDamageByBlockEvent event) throws Exception{
        if(CoreD.isRunning()) {
            if (event.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) {
                if (event.getEntity() instanceof Player) {
                    double damage = 0;
                    Field Damage = event.getClass().getSuperclass().getDeclaredField("damage");
                    Damage.setAccessible(true);
                    damage = Damage.getDouble(event);
                    event.setDamage(damage * 2);
                }
            }
        }
    }

    @EventHandler
    private void BlockPlace(BlockPlaceEvent event){
        if(CoreD.isRunning()) {
            TNTPrimed TNT;

            if (event.getBlock().getType().equals(Material.TNT)) {
                if(event.getPlayer().getItemInHand().getAmount() == 1){
                    event.getPlayer().getInventory().remove(event.getPlayer().getItemInHand());
                }else {
                    event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount() - 1);
                }

                event.getBlockPlaced().setType(Material.AIR);
                TNT = (TNTPrimed) event.getBlockPlaced().getLocation().getWorld().spawnEntity(event.getBlockPlaced().getLocation(), EntityType.PRIMED_TNT);
                TNT.setFireTicks(20);

//                Map<Location,List<String>> PlayersAfetados = new HashMap<>();
//                Map<String,List<String>> PlayersAfecct = new HashMap<>();
//
//                if(PlayersUseTNT.containsKey(event.getPlayer().getName())){
//                    PlayersAfetados = PlayersUseTNT.get(event.getPlayer().getName());
//
//                    PlayersAfetados.put(TNT.getLocation(),TNT.getNearbyEntities(4,4,4).stream().filter(P -> P instanceof Player).map(P -> ((Player)P).getName()).collect(Collectors.toList()));
//
//                    PlayersUseTNT.replace(event.getPlayer().getName(),PlayersAfetados);
//                }else {
//                    PlayersAfetados.put(TNT.getLocation(), TNT.getNearbyEntities(4, 4, 4).stream().filter(P -> P instanceof Player).map(P -> ((Player) P).getName()).collect(Collectors.toList()));
//                    PlayersUseTNT.put(event.getPlayer().getName(), PlayersAfetados);
//                }
//
//                if(LocationUsedTNT.containsKey(TNT.getLocation())){
//                    PlayersAfecct = LocationUsedTNT.get(TNT.getLocation());
//
//                    PlayersAfecct.put(event.getPlayer().getName(),TNT.getNearbyEntities(4,4,4).stream().filter(P -> P instanceof Player).map(P -> ((Player)P).getName()).collect(Collectors.toList()));
//
//                    LocationUsedTNT.replace(TNT.getLocation(),PlayersAfecct);
//                }else {
//                    PlayersAfecct.put(event.getPlayer().getName(), TNT.getNearbyEntities(4, 4, 4).stream().filter(P -> P instanceof Player).map(P -> ((Player) P).getName()).collect(Collectors.toList()));
//                    LocationUsedTNT.put(TNT.getLocation(), PlayersAfecct);
//                }

            }


        }
    }

    @EventHandler
    private void PlayerUseTnt(PlayerInteractEvent event){
        if(CoreD.isRunning()){
            if(event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                if(event.getPlayer().getItemInHand().getType().equals(Material.TNT)) {

                    if(event.getPlayer().getItemInHand().getAmount() == 1){
                        event.getPlayer().getInventory().remove(event.getPlayer().getItemInHand());
                    }else {
                        event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount() - 1);
                    }

                    TNTPrimed TNT = (TNTPrimed) event.getPlayer().getLocation().add(0, 1, 0).getWorld().spawnEntity(event.getPlayer().getLocation(), EntityType.PRIMED_TNT);
                    TNT.setFireTicks(20);
                    TNT.getNearbyEntities(4,4,4);
                    TNT.setVelocity(event.getPlayer().getEyeLocation().getDirection().multiply(2));
                    TNT.setMetadata("player", new FixedMetadataValue(Main.getInstance(),event.getPlayer().getName()));


//                    Map<Location,List<String>> PlayersAfetados = new HashMap<>();
//
//                    if(PlayersUseTNT.containsKey(event.getPlayer().getName())){
//                        PlayersAfetados = PlayersUseTNT.get(event.getPlayer().getName());
//
//                        PlayersAfetados.put(TNT.getLocation(),TNT.getNearbyEntities(4,4,4).stream().filter(P -> P instanceof Player).map(P -> ((Player)P).getName()).collect(Collectors.toList()));
//
//                        PlayersUseTNT.replace(event.getPlayer().getName(),PlayersAfetados);
//                    }else {
//                        PlayersAfetados.put(TNT.getLocation(), TNT.getNearbyEntities(4, 4, 4).stream().filter(P -> P instanceof Player).map(P -> ((Player) P).getName()).collect(Collectors.toList()));
//                        PlayersUseTNT.put(event.getPlayer().getName(), PlayersAfetados);
//                    }
                }
            }
        }
    }
//
//    public boolean playerKill(String Killer, String Victim, Location Distance){
//
//        if(PlayersUseTNT.containsKey(Killer)){
//           List<String> Filtrado = PlayersUseTNT.get(Killer).entrySet().stream()
//                    .filter((K) -> K.getKey().distance(Distance) <= 4)
//                   .filter(K -> K.getValue().removeIf(P -> !P.equalsIgnoreCase(Victim)))
//                   .filter(K -> !K.getValue().isEmpty())
//                   .map(K -> K.getValue().contains(Victim) ? Victim : null).collect(Collectors.toList());
//
//           Filtrado.removeIf(Objects::isNull);
//           return !Filtrado.isEmpty();
//        }
//
//        return false;
//    }
//
//    public Optional<List<String>> playerKill(String Victim, Location Distance){
//
//
//        LocationUsedTNT.entrySet().stream().filter(P -> P.getKey().distance(Distance) <= 4)
//
//
//            List<String> Filtrado = PlayersUseTNT.get(Killer).entrySet().stream()
//                    .filter((K) -> K.getKey().distance(Distance) <= 4)
//                    .filter(K -> K.getValue().removeIf(P -> !P.equalsIgnoreCase(Victim)))
//                    .filter(K -> !K.getValue().isEmpty())
//                    .map(K -> K.getValue().contains(Victim) ? Victim : null).collect(Collectors.toList());
//
//            Filtrado.removeIf(Objects::isNull);
//            return Optional.ofNullable()
//        }
//
//        return false;
//    }


//    @EventHandler
//    private void PlayerUseBow(EntityDamageByEntityEvent event){
//        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player){
//            Player Victim = (Player)event.getEntity();
//            Player Killer = (Player)event.getDamager();
//
//            if(event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)){
//                event.setDamage(CoreDPlayer.getHealth(Victim));
//
//                if(!Victim.isDead()){
//                    CoreDPlayer.setHealth(Victim,0);
//                }
//
//                Victimain.getInstance().getTeams().forEach(P -> {
//                    if(P.equalsIgnoreCase(((Player) event.getEntity()).getName())){
//                        ((Player) event.getDamager()).sendMessage("§4Você matou um inocente!");
//                        Random random = new Random();
//
//                        double Vida = CoreDPlayer.getHealth((Player)event.getEntity());
//
//                        double Damage = random.nextInt(30);
//
//                        double Dano = Vida*(Damage/100);
//
//                        ((Player) event.getDamager()).damage(Dano);
//
//                        ((Player) event.getDamager()).sendMessage("§cVocê perdeu " + Dano + "% de sua vida total.");
//                    }
//                });
//            }
//        }
//    }

}
