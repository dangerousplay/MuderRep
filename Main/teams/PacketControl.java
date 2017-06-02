package Main.teams;

import Main.Util.PacketUtil;
import Main.teams.Assasino.AssasinMain;
import Main.teams.Detetive.detetiveMain;
import Main.teams.Vítima.Victimain;
import net.minecraft.server.v1_7_R2.Packet;
import net.minecraft.server.v1_7_R2.PacketPlayOutScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by GMDAV on 13/05/2017.
 */
public class PacketControl {

    /**
     * Uma classe manager criada para evitar crash's e packets enviados duas vezes para um mesmo player
     * @sigleton
     */

    private final Map<String,List<String>> PacketsAndPlayers = new Hashtable<>();

    private static PacketControl ourInstance = new PacketControl();

    public static PacketControl getInstance() {
        return ourInstance;
    }

    private PacketControl() {
    }

    /**
     * Compara a existência do player dentro da lista de já enviados.
     * @param player o player a ser comparado.
     * @return a existência do player
     */

    public boolean containsplayer(String player){
        return PacketsAndPlayers.containsKey(player);
    }

    /**
     * Verifica se já foi enviado esse packet ao player em questão.
     * @param player o player
     * @param Packet o packet a ser comparado.
     * @return se já foi enviado.
     */

    public boolean PlayerContainsPacket(String player, String Packet){
        return PacketsAndPlayers.containsKey(player) && PacketsAndPlayers.get(player).contains(Packet);
    }

    /**
     * Adiciona o player a lista de players que já receberam este packet
     * @param player o player que recebeu o packet
     * @param Packet o packet do player
     */

    public void add(String player, String Packet){
        if(containsplayer(player)){
            List<String> Packets = PacketsAndPlayers.get(player);
            Packets.add(Packet);
            PacketsAndPlayers.replace(player,Packets);
        }else {
            List<String> Packets = new ArrayList<>();
            Packets.add(Packet);
            PacketsAndPlayers.put(player,Packets);
        }
    }

    /**
     * Atualiza um time de um player para todos usando packets safe-thread
     * @param PLayer o player que será enviado o packet
     * @param type o nome do time
     * @param color a cor do time
     */

    public void UpdateTeamSafe(String PLayer, PlayerType type, String color){
        PacketPlayOutScoreboardTeam ScoreBoard = new PacketPlayOutScoreboardTeam();

        switch (type){
            case Vitima:
                PacketUtil.setTeamPrefix(ScoreBoard,color + "§2Inocente" + "§a ");
                PacketUtil.setTeamSuffix(ScoreBoard,color + " §a§oAmigo");
                break;
            case Assasino:
                PacketUtil.setTeamPrefix(ScoreBoard,color + "§4Assasino" + "§c ");
                PacketUtil.setTeamSuffix(ScoreBoard,color + " §c§oInimigo");
                break;
        }

        PacketUtil.setTeamName(ScoreBoard,PLayer);
        PacketUtil.setTeamDisplayName(ScoreBoard,PLayer);
        PacketUtil.setTeamFriendlyFire(ScoreBoard,1);
        PacketUtil.addPlayer(ScoreBoard, Bukkit.getPlayer(PLayer));
        PacketUtil.setTeamMode(ScoreBoard,2);

        List<Player> PlayersSeram = new ArrayList<>(Victimain.getInstance().getBukkitPlayers());

        if(detetiveMain.getInsance().getPlayer() != null) {
            PlayersSeram.add(detetiveMain.getInsance().getBukkitPlayer());
        }

        sendpacket(ScoreBoard,PlayersSeram);
    }

    public void UpdateTeamSafe(String PLayer, String Prefix,String Suffix,Player Receiver){
        PacketPlayOutScoreboardTeam ScoreBoard = new PacketPlayOutScoreboardTeam();

        PacketUtil.setTeamName(ScoreBoard,PLayer);
        PacketUtil.setTeamDisplayName(ScoreBoard,PLayer);
        PacketUtil.setTeamPrefix(ScoreBoard,Prefix);
        PacketUtil.setTeamSuffix(ScoreBoard,Suffix);
        PacketUtil.setTeamFriendlyFire(ScoreBoard,1);
        PacketUtil.addPlayer(ScoreBoard, Bukkit.getPlayer(PLayer));
        PacketUtil.setTeamMode(ScoreBoard,2);

        sendpacket(ScoreBoard,Receiver);
    }

    public void UpdateTeamSafe(String PLayer, String Prefix,String Suffix,Collection<Player> Receiver){
        PacketPlayOutScoreboardTeam ScoreBoard = new PacketPlayOutScoreboardTeam();

        PacketUtil.setTeamName(ScoreBoard,PLayer);
        PacketUtil.setTeamDisplayName(ScoreBoard,PLayer);
        PacketUtil.setTeamPrefix(ScoreBoard,Prefix);
        PacketUtil.setTeamSuffix(ScoreBoard,Suffix);
        PacketUtil.setTeamFriendlyFire(ScoreBoard,1);
        PacketUtil.addPlayer(ScoreBoard, Bukkit.getPlayer(PLayer));
        PacketUtil.setTeamMode(ScoreBoard,2);

        sendpacket(ScoreBoard,Receiver);
    }



    /**
     * Envia um packet a um determinado player
     * @param packet o packet a ser enviado
     * @param Player o player que receberá o packet
     */

    private void sendpacket(Packet packet, Player... Player){
        Arrays.asList(Player).forEach(P -> ((CraftPlayer)P).getHandle().playerConnection.sendPacket(packet));
    }

    private void sendpacket(Packet packet, Collection<Player> Player){
        Player.forEach(P -> ((CraftPlayer)P).getHandle().playerConnection.sendPacket(packet));
    }

}
