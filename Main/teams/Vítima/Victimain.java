package Main.teams.Vítima;

import Main.ItemsEdit;
import Main.Main;
import Main.Util.PacketUtil;
import Main.teams.Detetive.detetiveMain;
import Main.teams.PacketControl;
import Main.teams.PlayerType;
import net.minecraft.server.v1_7_R2.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by GMDAV on 18/04/2017.
 */
public class Victimain  {
    private final Set<String> playersvitimas = new HashSet<>();
    private final static Victimain Instance = new Victimain();
    private final Map<String,List<ItemsEdit.ItemsToConsume>> UsedItems = new Hashtable<>();
    private final Map<String,PlayerType> Revelados = new Hashtable<>();

    public static Victimain getInstance(){
        return Instance;
    }


    public synchronized Set<? extends String> getTeams() {
        return playersvitimas;
    }


    public synchronized void setTeams(Collection<? extends String> player) {
       playersvitimas.addAll(player);
       playersvitimas.forEach(P -> {
           Bukkit.getPlayer(P).sendMessage("§a§lVocê é um inocente");
           Bukkit.getPlayer(P).sendMessage("                             ");
           Bukkit.getPlayer(P).sendMessage("§a§lObjetivos:");
           Bukkit.getPlayer(P).sendMessage("                             ");
           Bukkit.getPlayer(P).sendMessage("§f- Descubra e condene os assasinos");
           Bukkit.getPlayer(P).sendMessage("§f- Proteger o detevive");
           Bukkit.getPlayer(P).sendMessage("§f- Colete ouros para fazer um arco");
           Bukkit.getPlayer(P).sendMessage("§f- Use o ouro para fazer flechas");


           Bukkit.getServer().getLogger().info(P + " inocente");
       });

       Random random = new Random();

       List<String> Escolher = new ArrayList<>(playersvitimas);
        List<Player> Escolhidos = new ArrayList<>();

        for(int i = 0; i < 3; i++) {

            if(Escolher.size() == 0) {
                break;
            }

            int value = Escolher.size() == 0 ? 0:Escolher.size()-1;

            if(value == 0){
                break;
            }

            int index =  random.nextInt((value));
            Escolhidos.add(Bukkit.getPlayer(Escolher.get(index)));
            Escolher.remove(index);
        }

        PacketPlayOutScoreboardTeam ScoreBoard;

        Player[] PlayersTotal = Bukkit.getOnlinePlayers();

        for(Player P : PlayersTotal){
            ScoreBoard = new PacketPlayOutScoreboardTeam();

            if(detetiveMain.getInsance().hasplayerinside(P.getName())){
                PacketUtil.setTeamName(ScoreBoard,P.getName());
                PacketUtil.setTeamDisplayName(ScoreBoard,P.getName());
                PacketUtil.setTeamPrefix(ScoreBoard,"§1§lDetetive §9");
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
            PacketUtil.setTeamPrefix(ScoreBoard,"§6§lSuspeito §e");
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



        for(String P : player) {

            ScoreBoard = new PacketPlayOutScoreboardTeam();

            PacketUtil.setTeamName(ScoreBoard,P);
            PacketUtil.setTeamDisplayName(ScoreBoard,P);
            PacketUtil.setTeamPrefix(ScoreBoard,"§2§lInocente §a");
            PacketUtil.setTeamSuffix(ScoreBoard," §a§oAmigo");
            PacketUtil.setTeamFriendlyFire(ScoreBoard,1);
            PacketUtil.addPlayer(ScoreBoard,Bukkit.getPlayer(P));
            PacketUtil.setTeamMode(ScoreBoard,0);

            if(!PacketControl.getInstance().PlayerContainsPacket(P,P)) {
                PacketControl.getInstance().add(P, P);
                ((CraftPlayer) Bukkit.getPlayer(P)).getHandle().playerConnection.sendPacket(ScoreBoard);
            }
        }



        Escolhidos.forEach(P -> {
            P.sendMessage("§aVocê ganhou o §bArco da Salvação §a:)");
            P.getInventory().addItem(ItemsEdit.getArcoSalvacao());
            P.getInventory().addItem(new ItemStack(Material.ARROW));
        });

        new BukkitRunnable(){
            @Override
            public void run() {
                synchronized (playersvitimas){
                    playersvitimas.removeIf(P -> Bukkit.getPlayer(P) == null);
                }
            }
        }.runTaskTimer(Main.getInstance(),0,20);

    }


    public int getTeamsSize() {
        return playersvitimas.size();
    }

    public boolean hasplayerinside(String player) {
        return playersvitimas.contains(player);
    }

    public Optional<String> containsignorecase(String player){
        List<String> Set = this.playersvitimas.stream().filter(P -> P.equalsIgnoreCase(player)).collect(Collectors.toList());

        return Optional.ofNullable(Set.isEmpty() ? null: Set.get(0));
    }
    public void putPlayer(String player) {
        playersvitimas.add(player);
    }

    public void removePlayer(String player) {
        playersvitimas.remove(player);
    }

    public Map<String, List<ItemsEdit.ItemsToConsume>> getUsedItems() {
        return UsedItems;
    }

    public void addUsedItems(String player,ItemsEdit.ItemsToConsume Item){
        if(UsedItems.containsKey(player)){
            UsedItems.forEach((K,V) -> {
                if(K.equalsIgnoreCase(player)){
                    V.add(Item);
                    UsedItems.replace(K,V);
                }
            });

        }else {
            List<ItemsEdit.ItemsToConsume> Return = new ArrayList<>();
            Return.add(Item);
            this.UsedItems.put(player,Return);
        }

    }

    public Set<Player> getBukkitPlayers(){
        return playersvitimas.stream().map(Bukkit::getPlayer).collect(Collectors.toSet());
    }

    private void SendToOthers(Collection<? extends String> Players,String Player,Packet Packet){
        Players.stream().filter(P -> !Player.equalsIgnoreCase(P))
                .forEach(P -> ((CraftPlayer)Bukkit.getPlayer(P)).getHandle().playerConnection.sendPacket(Packet));
    }

    public void addrevelado(String Revelado, PlayerType Type){
        Revelados.put(Revelado,Type);
    }

    public boolean isRevelado(String player,String revelado){
        return Revelados.containsKey(player);
    }


    @Override
    public String toString() {
        StringBuilder Retorno = new StringBuilder();
        playersvitimas.forEach(P -> Retorno.append(P).append(", "));
        return Retorno.toString();
    }

}
