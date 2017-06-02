package Main.Util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.server.v1_7_R2.PacketPlayOutScoreboardTeam;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;


@SuppressWarnings("all")
public class headScoreboard implements Listener {
   
    public static ArrayList<Player> scoreboards = new ArrayList<Player>();
   
    @EventHandler (priority = EventPriority.NORMAL)
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        scoreboards.add(player);
        for (Player sbPlayer : scoreboards){
            sendPacket(sbPlayer, scoreboardPacket(player, SB.CREATE));
        }
       
        for (Player sbPlayer : scoreboards){
            if (sbPlayer != player)
                sendPacket(player, scoreboardPacket(sbPlayer, SB.CREATE));
        }
    }
   
    @EventHandler (priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if (scoreboards.contains(player)){
            scoreboards.remove(player);
            for (Player sbPlayer : scoreboards){
                sendPacket(sbPlayer, scoreboardPacket(player, SB.DESTROY));
            }
        }
    }
   
    public static void updateScoreboard(Player player){
        if (scoreboards.contains(player)){
            for (Player sbPlayer : scoreboards){
                sendPacket(sbPlayer, scoreboardPacket(player, SB.UPDATE));
            }
        }
    }
   
    public static PacketPlayOutScoreboardTeam scoreboardPacket(Player player, SB sb){
        String playerName = player.getName();
        String addLevel = "§c ";
        return apply(player, "§e§lSuspeito ", addLevel, sb);
    }
   
    public static void sendPacket(Player player, PacketPlayOutScoreboardTeam packet){
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
       
   
    /// SCOREBOARD PACKET
   
    private static PacketPlayOutScoreboardTeam packet;
   
    private static PacketPlayOutScoreboardTeam apply(Player player, String prefix, String suffix, SB sb){
        packet = new PacketPlayOutScoreboardTeam();
        setField("a", player.getName());
        setField("b", player.getName());
        setField("c", prefix);
        setField("d", suffix);
        setField("g", 1);
        switch(sb){
        case CREATE: addPlayer(player); setField("f", 0); break;
        case DESTROY: setField("f", 1); break;
        case UPDATE: setField("f", 2); break; }
        return packet;
    }
   
   
    private static void setField(String field, Object value) {
        try {
            Field f = packet.getClass().getDeclaredField(field);
            f.setAccessible(true); f.set(packet, value); f.setAccessible(false);
        } catch (Exception ex) {ex.printStackTrace();}
    }
    private static void addPlayer(Player pl){
        try {
            add(pl);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
   
    private static void add(Player pl) throws NoSuchFieldException, IllegalAccessException{
        Field f = packet.getClass().getDeclaredField("e");
        f.setAccessible(true);
        ((Collection) f.get(packet)).add(pl.getName());
    }
    public enum SB { CREATE, DESTROY, UPDATE }

}