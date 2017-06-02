package Main.Votacao;

import Main.Runnable.BukkitRunnable;
import Main.teams.Assasino.AssasinMain;
import Main.teams.Detetive.detetiveMain;
import Main.teams.PacketControl;
import Main.teams.Vítima.Victimain;
import br.com.tlcm.cc.API.CoreD;
import br.com.tlcm.cc.API.CoreDPlayer;
import Main.Main;
import me.confuser.barapi.BarAPI;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by GMDAV on 18/05/2017.
 */
public class Manager {
    private final Map<String, Integer> PlayersDesconfiados = new Hashtable<>();
    private final List<String> PlayersCondenaram = new ArrayList<>();
    private final List<String> PlayersInocentaram = new ArrayList<>();


    private final Map<String,List<String>> CondenouQuem = new Hashtable<>();
    private final Map<String,List<String>> InocentouQuem = new Hashtable<>();

    private final int TempoCondenar = 30;
    private final int TempoParaVotar = 3;

    private static Manager ourInstance = new Manager();

    public static Manager getInstance() {
        return ourInstance;
    }

    private Manager() {
    }

    /**
     * Inicia o contador
     */

    public void start() {

        ((BukkitRunnable) this::atualizarScoreBoard).runTaskTimer(Main.getInstance(), 0, 10);

       // ((BukkitRunnable) this::condenar).runTaskTimer(Main.getInstance(), 0, 20*TempoCondenar);

        //((BukkitRunnable)() -> Arrays.asList(Bukkit.getServer().getOnlinePlayers()).forEach(P -> BarAPI.setMessage(P,"§4§lPróxima condenação",20)) ).runTaskLater(Main.getInstance(),20);

    }


    /**
     * Condena os players que foram acusados
     */

//    private void condenar(){
//        List<Integer> Lista = new ArrayList<>();
//        PlayersDesconfiados.forEach((K,V) -> Lista.add(V));
//
//         OptionalInt VotoMaior = Lista.stream().mapToInt(Integer::intValue).max();
//
//         if(VotoMaior.isPresent() && VotoMaior.getAsInt() >= 3){
//             List<String> Filtrado = PlayersDesconfiados.entrySet().stream().filter((K) -> K.getValue() >= 4).map(Map.Entry::getKey).collect(Collectors.toList());
//
//             Arrays.asList(Bukkit.getServer().getOnlinePlayers()).forEach(P -> P.playSound(P.getLocation(), Sound.ENDERDRAGON_GROWL,100,100));
//
//             Filtrado.forEach(P -> {
//                 if(AssasinMain.getInstance().hasplayerinside(P)){
//                     Bukkit.getServer().broadcastMessage("§c" + P + " foi condenado.");
//                     CoreDPlayer.setSpectator(P);
//                     PlayersDesconfiados.remove(P);
//                 }else {
//                     Bukkit.getServer().broadcastMessage("§a" + P + " foi condenado injustamente!");
//                     PacketControl.getInstance().UpdateTeamSafe(P,"Inocente","§1");
//                     PlayersDesconfiados.remove(P);
//                 }
//             });
//
//             atualizarScoreBoard(Filtrado);
//         }
//
//        Arrays.asList(Bukkit.getServer().getOnlinePlayers()).forEach(P -> BarAPI.setMessage(P,"§4§lPróxima condenação",TempoCondenar));
//
//    }

    /**
     * Atualiza o Scoreboard com os acusados
     */

    @SuppressWarnings("deprecated")
    public void atualizarScoreBoard() {
        if(CoreD.isRunning()) {

            for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                Objective obj = online.getScoreboard().getObjective(DisplaySlot.SIDEBAR);

                if (obj == null) {
                    obj = online.getScoreboard().registerNewObjective("status", "dummy");
                    obj.setDisplayName("§4§lSuspeitos");
                    obj.setDisplaySlot(DisplaySlot.SIDEBAR);
                }

                final Objective FinalObj = obj;

                PlayersDesconfiados.forEach((K, V) -> {
                    if (V > 0) {
                        FinalObj.getScore(Bukkit.getOfflinePlayer("§c" + K)).setScore(V);
                        return;
                    }
                    if (V == 0|| detetiveMain.getInsance().isRevelado(K)|| Bukkit.getPlayer(K) == null|| CoreDPlayer.isSpectator(K)) {
                        online.getScoreboard().resetScores(Bukkit.getOfflinePlayer("§c" + K));
                        online.getScoreboard().resetScores(Bukkit.getOfflinePlayer("§a" + K));
                        PlayersDesconfiados.remove(K);
                    } else {
                        FinalObj.getScore(Bukkit.getOfflinePlayer("§a" + K)).setScore(V);
                    }
                });

            }

            List<Player> Allplayers = new ArrayList<>(Victimain.getInstance().getBukkitPlayers());
            if (detetiveMain.getInsance().getPlayer() != null) {
                Allplayers.add(detetiveMain.getInsance().getBukkitPlayer());
            }

            PlayersDesconfiados.forEach((K, V) -> {
                if (V > 0) {
                    PacketControl.getInstance().UpdateTeamSafe(K, "§4§lSuspeito §c", " §cDesconfiado", Allplayers);
                    return;
                }
                if (V == 0) {
                    PacketControl.getInstance().UpdateTeamSafe(K, "§6§lSuspeito §e", "", Allplayers);
                } else {
                    PacketControl.getInstance().UpdateTeamSafe(K, "§3§lSuspeito ", " §bConfiável", Allplayers);
                }
            });
        }

    }

    @SuppressWarnings("deprecated")
    public void atualizarScoreBoard(String player){
        Arrays.asList(Bukkit.getServer().getOnlinePlayers()).forEach(online -> {
            online.getScoreboard().resetScores(Bukkit.getOfflinePlayer(player));
        });
    }

    /**
     * Remove uma coleção de players do ScoreBoard
     * @param player os players a serem removidos
     */

    public void atualizarScoreBoard(Collection<? extends String> player){

        Arrays.asList(Bukkit.getServer().getOnlinePlayers()).forEach(online -> {
            player.forEach(P -> {
                online.getScoreboard().resetScores(Bukkit.getOfflinePlayer(P));
            });
        });

        removePlayer(player);
    }

    /**
     * Adiciona um player ao ScoreBoard.
     * @param Player o player a ser adicionado
     */

    public void addPlayer(String Player) {

        if(PlayersDesconfiados.containsKey(Player)){
            PlayersDesconfiados.replace(Player,PlayersDesconfiados.get(Player) + 1);
        }else {
            PlayersDesconfiados.put(Player,1);
        }

    }

    /**
     * Remove um player da lista de quem desconfiou
     * @param Player o player a ser removido
     */

    public void removePlayer(String Player){
        if(PlayersDesconfiados.containsKey(Player)){
            PlayersDesconfiados.replace(Player,PlayersDesconfiados.get(Player) - 1);
        }else {
            PlayersDesconfiados.put(Player,-1);
        }
    }



    public void removePlayer(Collection<? extends String> Player){
        PlayersCondenaram.removeAll(Player);
    }



    public void addPlayerUsed(String player){
        this.PlayersCondenaram.add(player);
        ((BukkitRunnable) () -> PlayersCondenaram.remove(player)).runTaskLater(Main.getInstance(),20*TempoParaVotar);

    }

    public void addDesconfiou(String Playe, String Voto){
        if(CondenouQuem.containsKey(Playe)){
            List<String> Condeou = CondenouQuem.get(Playe);
            Condeou.add(Voto);

            CondenouQuem.replace(Playe,Condeou);
        }
    }

    public void addconfiou(String Playe, String Voto){
        if(InocentouQuem.containsKey(Playe)){
            List<String> Condeou = InocentouQuem.get(Playe);
            Condeou.add(Voto);

            InocentouQuem.replace(Playe,Condeou);
        }
    }

    public boolean desconfiou(String Player, String Desconfiado){
        return CondenouQuem.containsKey(Player) && !CondenouQuem.get(Player).stream().filter(Desconfiado::equalsIgnoreCase).collect(Collectors.toList()).isEmpty();
    }

    public boolean inocentou(String Player, String Inocente){
        return InocentouQuem.containsKey(Player) && !InocentouQuem.get(Player).stream().filter(Inocente::equalsIgnoreCase).collect(Collectors.toList()).isEmpty();
    }

    public void removeVote(String Player, String Voto){
        if(inocentou(Player,Voto)){
            List<String> Remover = this.InocentouQuem.get(Player);
            Remover.remove(Voto);
            InocentouQuem.replace(Player,Remover);

            addPlayer(Voto);
        }else {
            List<String> Remover = this.CondenouQuem.get(Player);
            Remover.remove(Voto);
            CondenouQuem.replace(Player,Remover);

            removePlayer(Voto);
        }

    }


    public boolean containsPlayerConde(String player){
        return this.PlayersCondenaram.contains(player);
    }

    public boolean containsPlayerInoce(String player){
        return this.PlayersInocentaram.contains(player);
    }

    public boolean containsVote(String player, String voto){
        return desconfiou(player,voto) || inocentou(player,voto);
    }
}
