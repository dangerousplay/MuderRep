package Main.Util;

import Main.Main;
import Main.Runnable.BukkitRunnable;
import br.com.tlcm.cc.API.CoreDPlayer;
import net.minecraft.server.v1_7_R2.*;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Created by GMDAV on 18/04/2017.
 */
public abstract class SpawnUtil {

    private static final Set<String> PlayersSangrando = new HashSet<>();
    private static final List<Location> BloodLocation = new ArrayList<>();

    public static void SpawnScraps(Collection<? extends Location> locations, ItemStack Item){

        ((BukkitRunnable) () -> {
            Random random = new Random();
            locations.forEach(Location ->
                    ((BukkitRunnable) () -> Bukkit.getWorlds().get(0).dropItem(Location, Item))
                            .runTaskLater(Main.getInstance(), 20 * random.nextInt(15))
            );
        }).runTaskTimer(Main.getInstance(),0,20*30);
    }

    public static List<Location> SpawnBlood(Location loc){
        List<Location> Locations = new ArrayList<>();

        for(int x = loc.getBlockX()-1; x <= loc.getBlockX(); x++){
            for(int Z = loc.getBlockZ(); Z <= loc.getBlockZ()+1;Z++){
                Locations.add(new Location(loc.getWorld(),x,loc.getBlockY(),Z));
            }
        }

        //Locations.forEach(P -> P.getBlock().setType(Material.REDSTONE_WIRE));
        return Locations;
    }

    public static void SpawnBloodStart(){

        RemoveBlooc();

        new BukkitRunnable(){
            @Override
            public void run() {
                synchronized (PlayersSangrando) {
                    if (!PlayersSangrando.isEmpty()) {
                        PlayersSangrando.forEach(P -> {
                            Player pl = Bukkit.getPlayer(P);
                            if(CoreDPlayer.isSpectator(P)){
                                PlayersSangrando.remove(P);
                                return;
                            }
                            Entity Item = pl.getWorld().dropItem(pl.getLocation().add(0,1.5,0), new ItemStack(Material.REDSTONE));
                            ((BukkitRunnable) () -> {
                                if (pl.getLocation().getBlock().isEmpty()) {
                                    pl.getLocation().getBlock().setType(Material.REDSTONE_WIRE);
                                    BloodLocation.add(pl.getLocation());
                                } else {
                                    if(pl.getLocation().add(0, 1, 0).getBlock().isEmpty()) {
                                        pl.getLocation().getBlock().setType(Material.REDSTONE_WIRE);
                                        BloodLocation.add(pl.getLocation());
                                    }

                                }
                                Item.remove();
                            }).runTaskLater(Main.getInstance(),10);
                        });
                    }
                }
            }
        }.runTaskTimer(Main.getInstance(),0,20);
    }

    private static synchronized void RemoveBlooc(){

        new BukkitRunnable(){
            @Override
            public synchronized void run() {
                BloodLocation.forEach(P -> P.getBlock().setType(Material.AIR));
             BloodLocation.removeIf(BloodLocation::contains);
            }
        }.runTaskTimer(Main.getInstance(),0,20*15);

    }

    public static synchronized void addplayertoblood(Player player){
        PlayersSangrando.add(player.getName());
    }

    public static synchronized void removeplayerblood(Player player){
        if(PlayersSangrando.contains(player.getName())){
            PlayersSangrando.remove(player.getName());
        }
    }


    public static EntityPlayer spawndeathplayer(Player player){
        MinecraftServer nms = MinecraftServer.getServer();
        WorldServer nmsWorld = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
        UUID randomUUID = UUID.randomUUID();

        while(Bukkit.getPlayer(randomUUID) != null){
            randomUUID = UUID.randomUUID();
        }

        EntityPlayer npc = new EntityPlayer(nms, nmsWorld, new GameProfile(randomUUID.toString(), player.getName()), new PlayerInteractManager(nmsWorld));

        npc.setLocation(player.getLocation().getX(),player.getLocation().getY(),player.getLocation().getZ(),0,0);

        npc.setHealth(0);

        PacketPlayOutSpawnEntity PKT = new PacketPlayOutSpawnEntity(npc,2);

        Arrays.asList(Bukkit.getServer().getOnlinePlayers()).forEach( P -> ((CraftPlayer)P).getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(npc)));
        Arrays.asList(Bukkit.getServer().getOnlinePlayers()).forEach( P -> ((CraftPlayer)P).getHandle().playerConnection.sendPacket(PKT));
        return npc;
    }

    public static EntityPlayer spawndeathplayer(Player player, int x, int y){
        MinecraftServer nms = MinecraftServer.getServer();
        WorldServer nmsWorld = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
        UUID randomUUID = UUID.randomUUID();

        while(Bukkit.getPlayer(randomUUID) != null){
            randomUUID = UUID.randomUUID();
        }

        EntityPlayer npc = new EntityPlayer(nms, nmsWorld, new GameProfile(randomUUID.toString(), player.getName()), new PlayerInteractManager(nmsWorld));

        npc.setLocation(player.getLocation().getX(),player.getLocation().getY(),player.getLocation().getZ(),x,y);

        npc.setHealth(0);

        PacketPlayOutSpawnEntity PKT = new PacketPlayOutSpawnEntity(npc,2);

        Arrays.asList(Bukkit.getServer().getOnlinePlayers()).forEach( P -> ((CraftPlayer)P).getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(npc)));
        Arrays.asList(Bukkit.getServer().getOnlinePlayers()).forEach( P -> ((CraftPlayer)P).getHandle().playerConnection.sendPacket(PKT));
        return npc;
    }





}
