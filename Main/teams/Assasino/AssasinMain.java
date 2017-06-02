package Main.teams.Assasino;

import Main.ItemsEdit;
import Main.Util.PacketUtil;
import Main.teams.Detetive.detetiveMain;
import Main.teams.PacketControl;
import Main.teams.Vítima.Victimain;
import com.sun.org.apache.regexp.internal.RE;
import Main.Main;
import net.minecraft.server.v1_7_R2.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by GMDAV on 18/04/2017.
 */
public class AssasinMain {
    private final static AssasinMain Instance = new AssasinMain();
    private final Set<String> player = new HashSet<>();

    private final List<String> AssasinosNomes = new ArrayList<>();

    public static AssasinMain getInstance(){
        return Instance;
    }

    public synchronized void setPlayer(Collection<? extends String> Player){


        PacketPlayOutScoreboardTeam ScoreBoard;

        this.player.addAll(Player);
        this.AssasinosNomes.addAll(Player);

//        removeScoreBoard(player);

        for(String P : Player){
            Bukkit.getPlayer(P).sendMessage("§4§lVocê é um assasino!");
            Bukkit.getPlayer(P).getInventory().addItem(ItemsEdit.getLaminadoran());
            Bukkit.getServer().getLogger().info(P + " assasino");

            Bukkit.getPlayer(P).sendMessage("                             ");
            Bukkit.getPlayer(P).sendMessage("§a§lObjetivos:");
            Bukkit.getPlayer(P).sendMessage("                             ");
            Bukkit.getPlayer(P).sendMessage("§f- Use sua espada para matar os inocentes");
            Bukkit.getPlayer(P).sendMessage("§f- Proteja os assasinos");
            Bukkit.getPlayer(P).sendMessage("§f- Engane os inocentes com o seu voto de desconfiar");
            Bukkit.getPlayer(P).sendMessage("§f- Mate o detetive");

            ScoreBoard = new PacketPlayOutScoreboardTeam();

                PacketUtil.setTeamName(ScoreBoard,P);
                PacketUtil.setTeamDisplayName(ScoreBoard,P);
                PacketUtil.setTeamPrefix(ScoreBoard,"§4§lAssasino §c");
                PacketUtil.setTeamSuffix(ScoreBoard," §a§oAmigo");
                PacketUtil.setTeamFriendlyFire(ScoreBoard,1);
                PacketUtil.addPlayer(ScoreBoard,Bukkit.getPlayer(P));
                PacketUtil.setTeamMode(ScoreBoard,0);

            ((CraftPlayer)Bukkit.getPlayer(P)).getHandle().playerConnection.sendPacket(ScoreBoard);

        }


        for(Player P : Bukkit.getServer().getOnlinePlayers()){

            ScoreBoard = new PacketPlayOutScoreboardTeam();

            if(detetiveMain.getInsance().hasplayerinside(P.getName())){
                PacketUtil.setTeamName(ScoreBoard,P.getName());
                PacketUtil.setTeamDisplayName(ScoreBoard,P.getName());
                PacketUtil.setTeamPrefix(ScoreBoard,"§1§lDetetive §9");
                PacketUtil.setTeamSuffix(ScoreBoard," §c§oInimigo");
                PacketUtil.setTeamFriendlyFire(ScoreBoard,1);
                PacketUtil.addPlayer(ScoreBoard,Bukkit.getPlayer(P.getName()));
                PacketUtil.setTeamMode(ScoreBoard,0);


                for(String R : player){
                    if(!P.getName().equalsIgnoreCase(R))
                        if(!PacketControl.getInstance().PlayerContainsPacket(R,P.getName())) {
                            PacketControl.getInstance().add(R, P.getName());
                            ((CraftPlayer) Bukkit.getPlayer(R)).getHandle().playerConnection.sendPacket(ScoreBoard);
                        }
                }

                continue;
            }

            if(AssasinMain.getInstance().hasplayerinside(P.getName())){
                PacketUtil.setTeamName(ScoreBoard,P.getName());
                PacketUtil.setTeamDisplayName(ScoreBoard,P.getName());
                PacketUtil.setTeamPrefix(ScoreBoard,"§4§lAssasino §c");
                PacketUtil.setTeamSuffix(ScoreBoard," §a§oAmigo");
                PacketUtil.setTeamFriendlyFire(ScoreBoard,1);
                PacketUtil.addPlayer(ScoreBoard,Bukkit.getPlayer(P.getName()));
                PacketUtil.setTeamMode(ScoreBoard,0);


                for(String R : player){
                    if(!P.getName().equalsIgnoreCase(R))
                        if(!PacketControl.getInstance().PlayerContainsPacket(R,P.getName())) {
                            PacketControl.getInstance().add(R, P.getName());
                            ((CraftPlayer) Bukkit.getPlayer(R)).getHandle().playerConnection.sendPacket(ScoreBoard);
                        }
                }


                continue;
            }

            PacketUtil.setTeamName(ScoreBoard,P.getName());
            PacketUtil.setTeamDisplayName(ScoreBoard,P.getName());
            PacketUtil.setTeamPrefix(ScoreBoard,"§2§lInocente §a");
            PacketUtil.setTeamSuffix(ScoreBoard," §c§oInimigo");
            PacketUtil.setTeamFriendlyFire(ScoreBoard,1);
            PacketUtil.addPlayer(ScoreBoard,Bukkit.getPlayer(P.getName()));
            PacketUtil.setTeamMode(ScoreBoard,0);


            for(String R : player){
                if(!P.getName().equalsIgnoreCase(R))
                    if(!PacketControl.getInstance().PlayerContainsPacket(R,P.getName())) {
                        PacketControl.getInstance().add(R, P.getName());
                        ((CraftPlayer) Bukkit.getPlayer(R)).getHandle().playerConnection.sendPacket(ScoreBoard);
                    }
            }

        }

        new BukkitRunnable(){
            @Override
            public void run() {
                synchronized (player){
                    player.removeIf(P -> Bukkit.getPlayer(P) == null);
                }
            }
        }.runTaskTimer(Main.getInstance(),0,20);




    }

    public Set<String> getPlayer() {
        return player;
    }

    public boolean hasplayerinside(String player) {
        return this.player.contains(player);
    }

    public void removePlayer(String player) {
       this.player.remove(player);
    }

    private void sendToall(Collection<? extends String> Players, Packet packet){
        Players.forEach(P -> ((CraftPlayer)Bukkit.getPlayer(P)).getHandle().playerConnection.sendPacket(packet));
    }

    private void SendIfNotPresent(Collection<? extends String> Players, String Player, Packet packet){
        Players.removeIf(Player::equalsIgnoreCase);
        Players.forEach(P -> ((CraftPlayer)Bukkit.getPlayer(P)).getHandle().playerConnection.sendPacket(packet));
    }

    private void removeScoreBoard(Collection<? extends String> Players){

        PacketPlayOutScoreboardTeam ScoreBoard;

        for(Player P : Bukkit.getServer().getOnlinePlayers()){

            ScoreBoard = new PacketPlayOutScoreboardTeam();

            if(detetiveMain.getInsance().hasplayerinside(P.getName())){
                PacketUtil.setTeamName(ScoreBoard,P.getName());
                PacketUtil.setTeamDisplayName(ScoreBoard,P.getName());
                PacketUtil.setTeamPrefix(ScoreBoard,"§1§lDetetive §r");
                PacketUtil.setTeamFriendlyFire(ScoreBoard,1);
                PacketUtil.addPlayer(ScoreBoard,Bukkit.getPlayer(P.getName()));
                PacketUtil.setTeamMode(ScoreBoard,1);

                SendIfNotPresent(Players,P.getName(),ScoreBoard);
                continue;
            }

            if(Victimain.getInstance().hasplayerinside(P.getName())) {
                PacketUtil.setTeamName(ScoreBoard, P.getName());
                PacketUtil.setTeamDisplayName(ScoreBoard, P.getName());
                PacketUtil.setTeamPrefix(ScoreBoard, "§6§lSuspeito §e");
                PacketUtil.setTeamFriendlyFire(ScoreBoard, 1);
                PacketUtil.addPlayer(ScoreBoard, Bukkit.getPlayer(P.getName()));
                PacketUtil.setTeamMode(ScoreBoard, 1);

                SendIfNotPresent(Players, P.getName(), ScoreBoard);
            }
        }

    }

    public Optional<String> containsignorecase(String player){
        List<String> Set = this.player.stream().filter(P -> P.equalsIgnoreCase(player)).collect(Collectors.toList());
        return Optional.ofNullable(Set.isEmpty() ? null:Set.get(0));
    }

    public Set<Player> getBukkitPlayers(){
        return player.stream().map(Bukkit::getPlayer).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        StringBuilder Retorno = new StringBuilder();

        for(int i = 0; i < AssasinosNomes.size(); i++){

            final int Size = AssasinosNomes.size()-1;
            if(i == AssasinosNomes.size()-1) {
                Retorno.append(AssasinosNomes.get(i)).append(".");
                continue;
            }

            if(i == AssasinosNomes.size()-2){
                Retorno.append(AssasinosNomes.get(i)).append(" e ");
                continue;
            }

            Retorno.append(AssasinosNomes.get(i)).append(", ");
        }

        return Retorno.toString();
    }
}
