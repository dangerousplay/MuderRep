package Main.listeners;

import Main.Main;
import Main.Util.Hologram;
import br.com.tlcm.cc.API.CoreD;
import net.minecraft.server.v1_7_R2.EntityWitherSkull;
import net.minecraft.server.v1_7_R2.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_7_R2.PacketPlayOutRelEntityMove;
import net.minecraft.server.v1_7_R2.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by GMDAV on 14/05/2017.
 */
public class PlayerDamageHologram implements Listener {

    @EventHandler(ignoreCancelled = true)
    private void PlayerDamage(EntityDamageByEntityEvent event){
        if(CoreD.isRunning()){
            if(event.getEntity() instanceof Player && event.getDamager() instanceof Player){

                try{
                    Field DamageField = event.getClass().getSuperclass().getDeclaredField("damage");
                    DamageField.setAccessible(true);

                    Double Damage = DamageField.getDouble(event);

                    Hologram Tag = new Hologram(event.getEntity().getLocation().add(0,2,0),0,"§c-" + Damage + " ❤");

                    Arrays.asList(Bukkit.getServer().getOnlinePlayers()).forEach(Tag::send);
                    List<String> Players = Arrays.stream(Bukkit.getServer().getOnlinePlayers()).map(Player::getName).collect(Collectors.toList());

                    new BukkitRunnable(){

                        int Time = 9;

                        @Override
                        public void run() {

                            if(Time > 0) {

                                Tag.getAllSkullsHorses().forEach((P,K) -> {
                                    Players.stream().filter(Player -> Bukkit.getPlayer(Player) != null).forEach(R -> {
                                        Tag.getLocation().forEach(L -> {
                                            PacketPlayOutAttachEntity Attach = new PacketPlayOutAttachEntity(0,P,K);

                                            ((CraftPlayer)Bukkit.getServer().getPlayer(R)).getHandle().playerConnection.sendPacket(Attach);

                                        });
                                    });
                                });
                                Time -= 3;
                            }else {
                                try {
                                    Tag.remove();
                                }catch (Exception ex){
                                    ex.printStackTrace();
                                }
                                cancel();
                            }
                        }
                    }.runTaskTimer(Main.getInstance(),0,5);

                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        }
    }
}
