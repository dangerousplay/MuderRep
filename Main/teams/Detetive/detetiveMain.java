package Main.teams.Detetive;

import Main.ItemsEdit;
import Main.Util.Deatharea;
import Main.Util.PacketUtil;
import Main.teams.PlayerType;
import net.minecraft.server.v1_7_R2.PacketPlayOutScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by GMDAV on 18/04/2017.
 */
public class detetiveMain {
    private String player;
    private  List<Deatharea> locaisdocrime = new ArrayList<>();
    private final Map<String,PlayerType> Revelados = new Hashtable<>();

    private final static detetiveMain Instance = new detetiveMain();

    public static detetiveMain getInsance(){
        return Instance;
    }

    public synchronized void setPlayer(String player) {
        this.player = player;

        Bukkit.getPlayer(player).sendMessage("§1§lVocê é o detetive!");
        Bukkit.getPlayer(player).sendMessage("                             ");
        Bukkit.getPlayer(player).sendMessage("§a§lObjetivos:");
        Bukkit.getPlayer(player).sendMessage("                             ");
        Bukkit.getPlayer(player).sendMessage("§f- Use sua tesoura para descobrir os assasinos");
        Bukkit.getPlayer(player).sendMessage("§f- Proteja os inocentes");
        Bukkit.getPlayer(player).sendMessage("§f- Condene os assasinos e absolva os inocentes");


        Arrays.stream(Bukkit.getServer().getOnlinePlayers()).filter(P -> !P.getName().equalsIgnoreCase(player))
                .forEach(P -> P.sendMessage("§1§l" + player + " §9é o detetive"));

        Bukkit.getPlayer(player).getInventory().addItem(ItemsEdit.getTesouraDetetive());


        PacketPlayOutScoreboardTeam ScoreBoard;

        ScoreBoard = new PacketPlayOutScoreboardTeam();

        PacketUtil.setTeamName(ScoreBoard,player);
        PacketUtil.setTeamDisplayName(ScoreBoard,player);
        PacketUtil.setTeamPrefix(ScoreBoard,"§1§lDetetive §9");
        PacketUtil.setTeamSuffix(ScoreBoard," §a§oAmigo");
        PacketUtil.setTeamFriendlyFire(ScoreBoard,1);
        PacketUtil.addPlayer(ScoreBoard,Bukkit.getPlayer(player));
        PacketUtil.setTeamMode(ScoreBoard,0);

        ((CraftPlayer)Bukkit.getPlayer(player)).getHandle().playerConnection.sendPacket(ScoreBoard);


        for(Player P : Bukkit.getServer().getOnlinePlayers()){
            ScoreBoard = new PacketPlayOutScoreboardTeam();

            PacketUtil.setTeamName(ScoreBoard,P.getName());
            PacketUtil.setTeamDisplayName(ScoreBoard,P.getName());
            PacketUtil.setTeamPrefix(ScoreBoard,"§6§lSuspeito §e");
            PacketUtil.setTeamFriendlyFire(ScoreBoard,1);
            PacketUtil.addPlayer(ScoreBoard,P);
            PacketUtil.setTeamMode(ScoreBoard,0);

            if(!P.getName().equalsIgnoreCase(player))
                    ((CraftPlayer)Bukkit.getPlayer(player)).getHandle().playerConnection.sendPacket(ScoreBoard);

        }

//        ScoreBoard = new PacketPlayOutScoreboardTeam();
//
//        PacketUtil.setTeamName(ScoreBoard,player);
//        PacketUtil.setTeamDisplayName(ScoreBoard,player);
//        PacketUtil.setTeamPrefix(ScoreBoard,"§a§lInocente §r");
//        PacketUtil.setTeamFriendlyFire(ScoreBoard,1);
//        PacketUtil.addPlayer(ScoreBoard,Bukkit.getPlayer(player));
//        PacketUtil.setTeamMode(ScoreBoard,0);
//
//        ((CraftPlayer)Bukkit.getPlayer(player)).getHandle().playerConnection.sendPacket(ScoreBoard);


    }

    public String getPlayer(){
        return this.player;
    }

    public boolean hasplayerinside(String player) {
        return this.player != null && this.player.equalsIgnoreCase(player);
    }

    public void removePlayer(String player) {
        this.player = null;
    }

    public void addlocalcrime(Deatharea crime){
        this.locaisdocrime.add(crime);
    }

    public List<Deatharea> hasdeath(Location loc){

        List<Deatharea> Return = new ArrayList<>();

        for (Deatharea locations : locaisdocrime) {
            for (Location location : locations) {
                if(loc.equals(location)){
                    Return.add(locations);
                }
            }
        }
        return Return.size() > 0 ? Return:null;
    }

    public Optional<Deatharea> getlocalcrime(String player){

        List<Deatharea> Player = locaisdocrime.stream().filter(P -> P.getPlayer().equalsIgnoreCase(player)).collect(Collectors.toList());

        return Optional.ofNullable(Player.isEmpty() ? null : Player.get(0));
    }

    public Player getBukkitPlayer(){
        return Bukkit.getPlayer(player);
    }

    public void removelocalcrime(Deatharea crime){
        if(locaisdocrime.contains(crime)) {
            locaisdocrime.remove(crime);
        }
    }

    public void addRelevados(String Revelado, PlayerType type){
        Revelados.put(Revelado,type);
    }

    public boolean isRevelado(String player){
        return Revelados.containsKey(player);
    }


}
