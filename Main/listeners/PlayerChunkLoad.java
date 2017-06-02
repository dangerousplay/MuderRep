package Main.listeners;

import Main.Main;
import Main.Runnable.BukkitRunnable;
import Main.Util.NPCSpawn;
import br.com.tlcm.cc.API.CoreD;
import net.minecraft.server.v1_7_R2.PacketPlayOutEntityEquipment;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftItem;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerChunkLoad implements Listener {

    public PlayerChunkLoad(){

    }

    @EventHandler
    private void PlayerEquip(PlayerItemHeldEvent event){
        if(CoreD.isRunning()){
            int ID = ((CraftPlayer)event.getPlayer()).getHandle().getId();

            List<PacketPlayOutEntityEquipment> Equipment = new ArrayList<>();


            if(event.getPlayer().getInventory().getBoots() != null){
                ItemStack Item = new ItemStack(Material.AIR);
                Equipment.add(new PacketPlayOutEntityEquipment(ID,2, CraftItemStack.asNMSCopy(Item)));
            }

            if(event.getPlayer().getInventory().getHelmet() != null){
                ItemStack Item = new ItemStack(Material.AIR);
                Equipment.add(new PacketPlayOutEntityEquipment(ID,5, CraftItemStack.asNMSCopy(Item)));
            }

            if(event.getPlayer().getInventory().getLeggings() != null){
                ItemStack Item = new ItemStack(Material.AIR);
                Equipment.add(new PacketPlayOutEntityEquipment(ID,3, CraftItemStack.asNMSCopy(Item)));
            }

            if(event.getPlayer().getInventory().getChestplate() != null){
                ItemStack Item = new ItemStack(Material.AIR);
                Equipment.add(new PacketPlayOutEntityEquipment(ID,4, CraftItemStack.asNMSCopy(Item)));
            }

            Arrays.stream(Bukkit.getOnlinePlayers()).filter(P -> !P.getName().equalsIgnoreCase(event.getPlayer().getName())).forEach(P -> {
                Equipment.forEach(T -> ((CraftPlayer)P).getHandle().playerConnection.sendPacket(T));
            });
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void PlayerLoadArmor(EntityDamageByEntityEvent event){
        if(CoreD.isRunning()){
            if(event.getEntity() instanceof Player && event.getDamager() instanceof Player){

                int ID = ((CraftPlayer)event.getEntity()).getHandle().getId();

                Player Victim = (Player)event.getEntity();

                List<PacketPlayOutEntityEquipment> Equipment = new ArrayList<>();


                if(Victim.getInventory().getBoots() != null){
                    ItemStack Item = new ItemStack(Material.AIR);
                    Equipment.add(new PacketPlayOutEntityEquipment(ID,2, CraftItemStack.asNMSCopy(Item)));
                }

                if(Victim.getInventory().getHelmet() != null){
                    ItemStack Item = new ItemStack(Material.AIR);
                    Equipment.add(new PacketPlayOutEntityEquipment(ID,5, CraftItemStack.asNMSCopy(Item)));
                }

                if(Victim.getInventory().getLeggings() != null){
                    ItemStack Item = new ItemStack(Material.AIR);
                    Equipment.add(new PacketPlayOutEntityEquipment(ID,3, CraftItemStack.asNMSCopy(Item)));
                }

                if(Victim.getInventory().getChestplate() != null){
                    ItemStack Item = new ItemStack(Material.AIR);
                    Equipment.add(new PacketPlayOutEntityEquipment(ID,4, CraftItemStack.asNMSCopy(Item)));
                }

                Equipment.forEach(P -> {
                    ((CraftPlayer)event.getDamager()).getHandle().playerConnection.sendPacket(P);
                });

            }
        }
    }


    @EventHandler
    private void PlayerTeleport(PlayerTeleportEvent event){
        if(CoreD.isRunning()) {
            ((BukkitRunnable) () -> {
                NPCSpawn.getInstance().getFakesPlayers().forEach((P, K) -> {
                    K.send(event.getPlayer());
                });

            }).runTaskLater(Main.getInstance(), 20);
        }
    }


    @EventHandler
    private void ChunkUnload(ChunkUnloadEvent event){
       if(CoreD.isRunning()) {
           event.setCancelled(true);
       }
    }



}
