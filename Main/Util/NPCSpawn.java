package Main.Util;

import Main.Main;
import net.minecraft.server.v1_7_R2.*;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by GMDAV on 20/04/2017.
 */
public class NPCSpawn {
    private static final NPCSpawn Instance = new NPCSpawn();

    private final Map<EntityPlayer,Hologram> FakesPlayers = new Hashtable<>();

    public static NPCSpawn getInstance(){
        return Instance;
    }

    private static int currentEntId = 1337;

    private List<Integer> entityid = new ArrayList<>();

    public EntityPlayer spawnCorpse(Player player) {
        entityid.add(currentEntId);

        MinecraftServer nms = MinecraftServer.getServer();
        WorldServer nmsWorld = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
        UUID randomUUID = UUID.randomUUID();

        while(Bukkit.getPlayer(randomUUID) != null){
            randomUUID = UUID.randomUUID();
        }

        EntityPlayer Fake = new EntityPlayer(nms, nmsWorld, new GameProfile(randomUUID.toString(), player.getName()), new PlayerInteractManager(nmsWorld));

        Fake.setLocation(player.getLocation().getX(),player.getLocation().getY(),player.getLocation().getZ(),0,0);

        //TODO precisa ocultar o nome do player quando ele morre.
        Hologram name = new Hologram(Fake.getBukkitEntity().getLocation(),0.6,"§4§lMorto" + "§4 " + Fake.displayName);

        Arrays.asList(Bukkit.getServer().getOnlinePlayers()).forEach(name::send);

        this.addFakesPlayer(Fake,name);

        PacketPlayOutNamedEntitySpawn npc = new PacketPlayOutNamedEntitySpawn(Fake);

        PacketPlayOutBed sleep = new PacketPlayOutBed();
        PacketPlayOutRelEntityMove move = new PacketPlayOutRelEntityMove(
                currentEntId, (byte)0,
                (byte)(int)((player.getLocation().getY() - 1.65D - player
                        .getLocation().getY()) * 32.0D), (byte)0);

        Location yUsed;

        try { Location locUnder = getNonClippableBlockUnderPlayer(player, 1);
            yUsed = locUnder != null ? locUnder : player.getLocation();
            Field npca = npc.getClass().getDeclaredField("a");
            npca.setAccessible(true);
            npca.setInt(npc, currentEntId);
            npca.setAccessible(!npca.isAccessible());
            Field npcd = npc.getClass().getDeclaredField("d");
            npcd.setAccessible(true);
            npcd.setInt(npc, MathHelper.floor((yUsed.getY() + 2.0D) * 32.0D));
            npcd.setAccessible(!npcd.isAccessible());
            Field npch = npc.getClass().getDeclaredField("h");
            npch.setAccessible(true);
            npch.setInt(npc, 0);
            Field sleepa = sleep.getClass().getDeclaredField("a");
            sleepa.setAccessible(true);
            sleepa.setInt(sleep, currentEntId);
            sleepa.setAccessible(!sleepa.isAccessible());
            Field sleepb = sleep.getClass().getDeclaredField("b");
            sleepb.setAccessible(true);
            sleepb.setInt(sleep, (int)player.getLocation().getX());
            sleepb.setAccessible(!npca.isAccessible());
            Field sleepc = sleep.getClass().getDeclaredField("c");
            sleepc.setAccessible(true);
            sleepc.setInt(sleep, (int)player.getLocation().getY());
            sleepc.setAccessible(!sleepc.isAccessible());
            Field sleepd = sleep.getClass().getDeclaredField("d");
            sleepd.setAccessible(true);
            sleepd.setInt(sleep, (int)player.getLocation().getZ());
            sleepd.setAccessible(!sleepd.isAccessible());
            Field npcf = npc.getClass().getDeclaredField("f");
            npcf.setAccessible(true);
            npcf.setByte(npc, (byte)103);
            npcf.setAccessible(!npcf.isAccessible());
            Field npcg = npc.getClass().getDeclaredField("g");
            npcg.setAccessible(true);
            npcg.setByte(npc, (byte)0);
            npcg.setAccessible(!npcg.isAccessible());
        } catch (Exception x) {
            x.printStackTrace();
        }

        for (Player p : player.getWorld().getPlayers())
        {
                ((CraftPlayer)p).getHandle().playerConnection.sendPacket(npc);
                ((CraftPlayer)p).getHandle().playerConnection.sendPacket(sleep);
                ((CraftPlayer)p).getHandle().playerConnection.sendPacket(move);
                //((CraftPlayer)p).getHandle().playerConnection.sendPacket(Time);
        }

        currentEntId += 1;
        return Fake;
    }

    private static Location getNonClippableBlockUnderPlayer(Player p, int addToYPos)
    {
        Location loc = p.getLocation();
        if (loc.getBlockY() <= 1) {
            return null;
        }
        for (int y = loc.getBlockY(); y >= 1; y--) {
            Material m = loc.getWorld()
                    .getBlockAt(loc.getBlockX(), y, loc.getBlockZ()).getType();
            if (m.isSolid()) {
                return new Location(loc.getWorld(), loc.getX(), y + addToYPos,
                        loc.getZ());
            }
        }
        return null;
    }

    public Map<EntityPlayer,Hologram> getFakesPlayers(){
        return this.FakesPlayers;
    }

    public void addFakesPlayer(EntityPlayer player, Hologram Holograma){
        this.FakesPlayers.put(player,Holograma);
    }


}
