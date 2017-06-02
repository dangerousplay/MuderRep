package Main.Util;

import net.minecraft.server.v1_7_R2.EntityPlayer;
import net.minecraft.server.v1_7_R2.Packet;
import net.minecraft.server.v1_7_R2.PacketPlayOutScoreboardTeam;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public abstract class PacketUtil
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //
   
    public static PacketPlayOutScoreboardTeam constructTeamCreatePacket(String name, String prefix, String suffix, Player player)
    {
        PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
       
        setTeamName(packet, name);
        setTeamDisplayName(packet, name);
        setTeamPrefix(packet, prefix);
        setTeamSuffix(packet, suffix);
        setTeamPlayers(packet, player);
        setTeamMode(packet, 0); // If 0 then the team is created.
        setTeamFriendlyFire(packet, 1); // 1 for on
       
        return packet;
    }
   
    public static PacketPlayOutScoreboardTeam constructTeamUpdatePacket(String name, String prefix, String suffix)
    {
        PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
       
        setTeamName(packet, name);
        setTeamDisplayName(packet, name);
        setTeamPrefix(packet, prefix);
        setTeamSuffix(packet, suffix);
        //setPlayers(packet, player);
        setTeamMode(packet, 2); // If 2 the team team information is updated.
        setTeamFriendlyFire(packet, 1); // 1 for on
       
        return packet;
    }
   
    // -------------------------------------------- //
    // SPECIFIC FIELDS UTIL
    // -------------------------------------------- //
   
    // http://wiki.vg/Protocol#Teams
    // String a: Team Name
    // String b: Team Display Name
    // String c: Team Prefix
    // String d: Team Suffix
    // ArrayList<String> e: Players
    // int f: Mode
    // int g: Friendly fire
   
    public final static Field TEAM_NAME = getAccessibleField(PacketPlayOutScoreboardTeam.class, "a");
    public final static Field TEAM_DISPLAY_NAME = getAccessibleField(PacketPlayOutScoreboardTeam.class, "b");
    public final static Field TEAM_PREFIX = getAccessibleField(PacketPlayOutScoreboardTeam.class, "c");
    public final static Field TEAM_SUFFIX = getAccessibleField(PacketPlayOutScoreboardTeam.class, "d");
    public final static Field TEAM_PLAYERS = getAccessibleField(PacketPlayOutScoreboardTeam.class, "e");
    public final static Field TEAM_MODE = getAccessibleField(PacketPlayOutScoreboardTeam.class, "f");
    public final static Field TEAM_FRIENDLY_FIRE = getAccessibleField(PacketPlayOutScoreboardTeam.class, "g");
   
    public static void setTeamName(PacketPlayOutScoreboardTeam packet, String teamName)
    {
        set(TEAM_NAME, packet, teamName);
    }
   
    public static void setTeamDisplayName(PacketPlayOutScoreboardTeam packet, String teamDisplayName)
    {
        set(TEAM_DISPLAY_NAME, packet, teamDisplayName);
    }
   
    public static void setTeamPrefix(PacketPlayOutScoreboardTeam packet, String teamPrefix)
    {
        set(TEAM_PREFIX, packet, teamPrefix);
    }
   
    public static void setTeamSuffix(PacketPlayOutScoreboardTeam packet, String teamSuffix)
    {
        set(TEAM_SUFFIX, packet, teamSuffix);
    }
   
    public static void setTeamPlayers(PacketPlayOutScoreboardTeam packet, Collection<? extends String> players)
    {
        set(TEAM_PLAYERS, packet, new ArrayList<String>(players));
    }
   
    public static void setTeamPlayers(PacketPlayOutScoreboardTeam packet, String player)
    {
        setTeamPlayers(packet, Arrays.asList(player));
    }
   
    public static void setTeamPlayers(PacketPlayOutScoreboardTeam packet, Player player)
    {
        setTeamPlayers(packet, player.getName());
    }
   
    public static void setTeamMode(PacketPlayOutScoreboardTeam packet, int mode)
    {
        set(TEAM_MODE, packet, mode);
    }
   
    public static void setTeamFriendlyFire(PacketPlayOutScoreboardTeam packet, int friendlyFire)
    {
        set(TEAM_FRIENDLY_FIRE, packet, friendlyFire);
    }

    @SuppressWarnings("unchecked")
    public static void addPlayer(PacketPlayOutScoreboardTeam packet, Player player){
        ((Collection)get(packet,TEAM_PLAYERS)).add(player.getName());
    }

    @SuppressWarnings("unchecked")
    public static Collection<? extends String> getTeamPlayers(PacketPlayOutScoreboardTeam packet){
            return (Collection<? extends String>)get(packet,TEAM_PLAYERS);
    }
   
    // -------------------------------------------- //
    // BASIC UTIL
    // -------------------------------------------- //
   
    public static void sendPacket(Player player, Packet packet)
    {
        CraftPlayer cplayer = (CraftPlayer)player;
        EntityPlayer eplayer = cplayer.getHandle();
        eplayer.playerConnection.sendPacket(packet);
    }
   
    private static void set(Field field, Object object, Object value)
    {
        try
        {
            field.set(object, value);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static Object get(PacketPlayOutScoreboardTeam packet, Field field) {
        try {
            return field.get(packet);
        }catch (Exception Ex){
            Ex.printStackTrace();
        }
            return null;
    }
   
    public static Field getAccessibleField(Class<?> clazz, String fieldName)
    {
        try
        {
            Field field = clazz.getDeclaredField(fieldName);
            makeAccessible(field);
            return field;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
   
    public static void makeAccessible(Field field)
    {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field.getModifiers())) && !field.isAccessible())
        {
            field.setAccessible(true);
        }
    }
   
}