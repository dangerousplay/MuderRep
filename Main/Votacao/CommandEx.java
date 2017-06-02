package Main.Votacao;

import Main.teams.Assasino.AssasinMain;
import Main.teams.Detetive.detetiveMain;
import Main.teams.PacketControl;
import Main.teams.PlayerType;
import Main.teams.Vítima.Victimain;
import br.com.tlcm.cc.API.CoreD;
import br.com.tlcm.cc.API.CoreDPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by GMDAV on 18/05/2017.
 */
public class CommandEx implements CommandExecutor {
    /** Será executado o comando desconfiar para depois ser condenado no Manager
     * @param Sender é quem enviou o comando.
     * @param cmd é o comando enviado.
     * @param label é o nome do comando.
     * @param args são os argumentos enviados.
     * @return é o tipo de retorno, false ou true.
     */

    @Override
    public boolean onCommand(CommandSender Sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if(CoreD.isRunning()) {

            if (Manager.getInstance().containsPlayerConde(Sender.getName())) {
                Sender.sendMessage("§cAguarde para desconfiar novamente!");
                return false;
            }

            if (args.length != 1) {
                Sender.sendMessage("§cuse /desconfiar [player]");
                return false;
            }

            if (detetiveMain.getInsance().hasplayerinside(args[0])) {
                Sender.sendMessage("Você não pode desconfiar do detetive");
                return false;
            }

            if (Sender.getName().equalsIgnoreCase(args[0])) {
                Sender.sendMessage("§cVocê não pode desconfiar de si mesmo!");
                return false;
            }

            if(CoreDPlayer.isSpectator(Sender.getName())){
                Sender.sendMessage("§cVocê não pode desconfiar como espectador");
                return false;
            }

            if(detetiveMain.getInsance().isRevelado(args[0])){
                Sender.sendMessage("§cEste player já foi revelado!");
                return false;
            }

            if (AssasinMain.getInstance().containsignorecase(args[0]).isPresent() || Victimain.getInstance().containsignorecase(args[0]).isPresent()) {

                String player;
                PlayerType Type;


                if(AssasinMain.getInstance().containsignorecase(args[0]).isPresent()){
                    player = AssasinMain.getInstance().containsignorecase(args[0]).get();
                    Type = PlayerType.Assasino;
                }else {
                    player = Victimain.getInstance().containsignorecase(args[0]).get();
                    Type = PlayerType.Vitima;
                }

                if(Manager.getInstance().desconfiou(Sender.getName(),player)){
                    Sender.sendMessage("§cVocê já desconfiou do(a) " + player);
                    return false;
                }



                AssasinMain.getInstance().getBukkitPlayers().forEach(P -> {

                    if(AssasinMain.getInstance().hasplayerinside((Sender).getName())){

                        if(Type.equals(PlayerType.Assasino)) {

                            P.sendMessage("§c" + Sender.getName() + "§a acha que §c" + player + " §cé um assasino");
                        }else {
                            P.sendMessage("§c" + Sender.getName() + "§a acha que §a" + player + " §cé um assasino");
                        }
                    }

                    if(Victimain.getInstance().hasplayerinside(Sender.getName())){

                        if(Type.equals(PlayerType.Assasino)) {
                            P.sendMessage("§a" + Sender.getName() + "§a acha que §c" + player + " §cé um assasino");
                        }else {
                            P.sendMessage("§a" + Sender.getName() + "§a acha que §a" + player + " §cé um assasino");
                        }
                    }

                    if(detetiveMain.getInsance().hasplayerinside(Sender.getName())){
                        if(Type.equals(PlayerType.Assasino)) {
                            P.sendMessage("§9" + Sender.getName() + "§a acha que §c" + player + " §cé um assasino");
                        }else {
                            P.sendMessage("§9" + Sender.getName() + "§a acha que §a" + player + " §cé um assasino");
                        }
                    }

                });

                Victimain.getInstance().getBukkitPlayers().forEach(P -> {

                    if(!Victimain.getInstance().isRevelado(Sender.getName(),player)){
                        P.sendMessage("§e" + Sender.getName() + "§a acha que §4" + player + " §cé um assasino");
                    }


                    if(AssasinMain.getInstance().hasplayerinside(Sender.getName())){

                        if(Type.equals(PlayerType.Assasino)) {

                            P.sendMessage("§c" + Sender.getName() + "§a acha que §c" + player + " §cé um assasino");
                        }else {
                            P.sendMessage("§c" + Sender.getName() + "§a acha que §a" + player + " §cé um assasino");
                        }
                    }

                    if(Victimain.getInstance().hasplayerinside(Sender.getName())){

                        if(Type.equals(PlayerType.Assasino)) {
                            P.sendMessage("§a" + Sender.getName() + "§a acha que §c" + player + " §cé um assasino");
                        }else {
                            P.sendMessage("§a" + Sender.getName() + "§a acha que §a" + player + " §cé um assasino");
                        }
                    }

                    if(detetiveMain.getInsance().hasplayerinside(Sender.getName())){
                        if(Type.equals(PlayerType.Assasino)) {
                            P.sendMessage("§1" + Sender.getName() + "§a acha que §c" + player + " §cé um assasino");
                        }else {
                            P.sendMessage("§1" + Sender.getName() + "§a acha que §a" + player + " §cé um assasino");
                        }
                    }

                });


                Manager.getInstance().addDesconfiou(Sender.getName(),player);

                Manager.getInstance().addPlayer(player);
                Manager.getInstance().addPlayerUsed(Sender.getName());
                return true;
            }
        }

        Sender.sendMessage("§cVocê não pode usar esse comando agora.");

        return false;
    }
}
