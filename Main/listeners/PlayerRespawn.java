package Main.listeners;

import Main.Runnable.BukkitRunnable;
import Main.Util.NPCSpawn;
import Main.Util.PacketUtil;
import Main.Main;
import Main.espectador.Items;
import net.minecraft.server.v1_7_R2.PacketPlayOutBed;
import net.minecraft.server.v1_7_R2.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_7_R2.PacketPlayOutRelEntityMove;
import net.minecraft.server.v1_7_R2.PacketPlayOutScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Created by GMDAV on 04/05/2017.
 */
public class PlayerRespawn implements Listener {


    @EventHandler
    private void PlayerRespawn(PlayerRespawnEvent event){


        ((BukkitRunnable) () -> {
            NPCSpawn.getInstance().getFakesPlayers().forEach((P,K) -> {
                //              int EntityID = P.getId();

//                PacketPlayOutRelEntityMove move = new PacketPlayOutRelEntityMove(
//                        EntityID, (byte)0,
//                        (byte)(int)((P.getBukkitEntity().getLocation().getY() - 1.65D - P.getBukkitEntity()
//                                .getLocation().getY()) * 32.0D), (byte)0);

                K.send(event.getPlayer());

                ((CraftPlayer) event.getPlayer()).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(P));
                //((CraftPlayer) event.getPlayer()).getHandle().playerConnection.sendPacket(move);
            });

            event.getPlayer().getInventory().addItem(Items.getSkullLog(event.getPlayer().getName()));

        }).runTaskLater(Main.getInstance(),20*2);


    }

}
