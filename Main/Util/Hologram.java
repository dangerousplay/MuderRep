package Main.Util;

import java.util.*;

import net.minecraft.server.v1_7_R2.*;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
 
public class Hologram {
    private final Location loc;
    private final double dy;
    private final String message;
    private final World world;
    private final EntityWitherSkull skull;
    private HashMap<String, Integer> tags = new HashMap<String, Integer>();
    private final List<Location> AllLocations = new ArrayList<>();
    private final Map<EntityHorse,EntityWitherSkull> AllSkullsHorses = new HashMap<>();
    private boolean lock = false;
    
    public Hologram(Location loc, double dy, String message) {
        this.loc = loc;
        this.dy = dy;
        this.message = message;
        this.world = ((CraftWorld) loc.getWorld()).getHandle();
        skull = new EntityWitherSkull(world);
        skull.setLocation(loc.getX(), loc.getY() + dy + 55, loc.getZ(), 0, 0);
        ((CraftWorld) loc.getWorld()).getHandle().addEntity(skull);
    }

    public Hologram(String world, double x, double y, double z, double dy, String message) {
        this(new Location(Bukkit.getWorld(world), x, y, z), dy, message);
    }
    
    public void send(Player player) {
        if (!lock) {
            EntityHorse horse = new EntityHorse(world);
            horse.setLocation(loc.getX(), loc.getY() + dy + 55.25, loc.getZ(), 0, 0);
            horse.setAge(-1700000);
            horse.setCustomName(message);
            horse.setCustomNameVisible(true);
            PacketPlayOutSpawnEntityLiving spawn = new PacketPlayOutSpawnEntityLiving(horse);
            sendPacket(player, spawn);
            
            PacketPlayOutAttachEntity pa = new PacketPlayOutAttachEntity(0, horse, skull);
            sendPacket(player, pa);
            tags.put(player.getName(), horse.getId());
            AllSkullsHorses.put(horse,skull);
            AllLocations.add(horse.getBukkitEntity().getLocation());
        } else {
           Bukkit.getLogger().info("Hologram lock");
        }
    }

    public Set<Integer> getEntityCurrentID(){
        Set<Integer> Numbers = new HashSet<>();
        tags.forEach((K,V) -> Numbers.add(V));

        return Numbers;
    }

    public Map<EntityHorse, EntityWitherSkull> getAllSkullsHorses() {
        return AllSkullsHorses;
    }

    public List<Location> getLocation(){

        return AllLocations;
    }

    public void remove(Player player) throws Exception {
        if (!lock) {
            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(tags.get(player.getName()));
            sendPacket(player, packet);
            tags.remove(player.getName());
        } else {
            throw new Exception("There is a lock on this hologram!");
        }
    }
    public void remove() throws Exception {
        if (!lock) {
            lock = true;
            for (String s : tags.keySet()) {
                PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(tags.get(s));
                sendPacket(Bukkit.getPlayer(s), packet);
            }
            tags.clear();
        } else {
            throw new Exception("There is a lock on this hologram!");
        }
    }
    private void sendPacket(Player p, Packet packet) {
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}