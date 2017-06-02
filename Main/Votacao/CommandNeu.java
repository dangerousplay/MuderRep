package Main.Votacao;

import Main.teams.Assasino.AssasinMain;
import Main.teams.Detetive.detetiveMain;
import Main.teams.PacketControl;
import Main.teams.PlayerType;
import Main.teams.Vítima.Victimain;
import br.com.tlcm.cc.API.CoreD;
import br.com.tlcm.cc.API.CoreDPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandNeu implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender Sender, Command command, String label, String[] args) {

            if(CoreD.isRunning()) {


                if (args.length != 1) {
                    Sender.sendMessage("§cuse /neutro [player]");
                    return false;
                }

                if(!Manager.getInstance().containsVote(Sender.getName(),args[0])){
                    Sender.sendMessage("§cVocê não votou neste player.");
                    return false;
                }

                if (detetiveMain.getInsance().hasplayerinside(args[0])) {
                    Sender.sendMessage("Você não pode neutralizar o detetive");
                    return false;
                }

                if (Sender.getName().equalsIgnoreCase(args[0])) {
                    Sender.sendMessage("§cVocê não pode neutralizar a si mesmo!");
                    return false;
                }

                if(CoreDPlayer.isSpectator(Sender.getName())){
                    Sender.sendMessage("§cVocê não pode neutralizar como espectador");
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


                    AssasinMain.getInstance().getBukkitPlayers().forEach(P -> {

                        if(AssasinMain.getInstance().hasplayerinside(Sender.getName())){

                            if(Type.equals(PlayerType.Assasino)) {

                                P.sendMessage("§c" + Sender.getName() + "§3 removeu o seu voto de §b" + player);
                            }else {
                                P.sendMessage("§c" + Sender.getName() + "§3 removeu o seu voto de §b" + player );
                            }
                        }

                        if(Victimain.getInstance().hasplayerinside(Sender.getName())){

                            if(Type.equals(PlayerType.Assasino)) {
                                P.sendMessage("§a" + Sender.getName() + "§3 removeu o seu voto de §b" + player );
                            }else {
                                P.sendMessage("§a" + Sender.getName() + "§3 removeu o seu voto de §b" + player);
                            }
                        }

                        if(detetiveMain.getInsance().hasplayerinside(Sender.getName())){
                            if(Type.equals(PlayerType.Assasino)) {
                                P.sendMessage("§1" + Sender.getName() + "§3 removeu o seu voto de §b" + player);
                            }else {
                                P.sendMessage("§1" + Sender.getName() + "§3 removeu o seu voto de §b" + player);
                            }
                        }

                    });

                    Victimain.getInstance().getBukkitPlayers().forEach(P -> {

                        if(AssasinMain.getInstance().hasplayerinside(Sender.getName())){

                            if(Type.equals(PlayerType.Assasino)) {

                                P.sendMessage("§4" + Sender.getName() + "§3 removeu o seu voto de §b" + player);
                            }else {
                                P.sendMessage("§4" + Sender.getName() + "§3 removeu o seu voto de §b" + player);
                            }
                        }

                        if(Victimain.getInstance().hasplayerinside(Sender.getName())){

                            if(Type.equals(PlayerType.Assasino)) {
                                P.sendMessage("§a" + Sender.getName() + "§3 removeu o seu voto de §b" + player);
                            }else {
                                P.sendMessage("§a" + Sender.getName() + "§3 removeu o seu voto de §b" + player);
                            }
                        }

                        if(detetiveMain.getInsance().hasplayerinside(Sender.getName())){
                            if(Type.equals(PlayerType.Assasino)) {
                                P.sendMessage("§1" + Sender.getName() + "§3 removeu o seu voto de §b" + player);
                            }else {
                                P.sendMessage("§1" + Sender.getName() + "§3 removeu o seu voto de §b" + player);
                            }
                        }

                    });

                    Manager.getInstance().addPlayerUsed(Sender.getName());
                    Manager.getInstance().removeVote(Sender.getName(),player);

                    return true;
                }
            }

        Sender.sendMessage("§cVocê não pode usar esse comando agora.");

        return false;
    }
}
