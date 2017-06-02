package Main.Votacao;

import Main.teams.Assasino.AssasinMain;
import Main.teams.Detetive.detetiveMain;
import Main.teams.PacketControl;
import Main.teams.PlayerType;
import Main.teams.Vítima.Victimain;
import br.com.tlcm.cc.API.CoreD;
import br.com.tlcm.cc.API.CoreDPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandIno implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender Sender, Command command, String s, String[] args) {


            if(CoreD.isRunning()) {
                if (Manager.getInstance().containsPlayerInoce(Sender.getName())) {
                    Sender.sendMessage("§cAguarde para inocentar novamente!");
                    return false;
                }

                if (args.length != 1) {
                    Sender.sendMessage("§cuse /inocentar [player]");
                    return false;
                }

                if (detetiveMain.getInsance().hasplayerinside(args[0])) {
                    Sender.sendMessage("Você não pode inocentar o detetive");
                    return false;
                }

                if (Sender.getName().equalsIgnoreCase(args[0])) {
                    Sender.sendMessage("§cVocê não pode inocentar a si mesmo!");
                    return false;
                }

                if(CoreDPlayer.isSpectator(Sender.getName())){
                    Sender.sendMessage("§cVocê não pode inocentar como espectador");
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

                    if(Manager.getInstance().inocentou(Sender.getName(),player)){
                        Sender.sendMessage("§cVocê já inocentou o(a) " + player);
                        return false;
                    }





                    AssasinMain.getInstance().getBukkitPlayers().forEach(P -> {

                        if(AssasinMain.getInstance().hasplayerinside(Sender.getName())){

                            if(Type.equals(PlayerType.Assasino)) {

                                P.sendMessage("§c" + Sender.getName() + "§a acha que §c" + player + " §aé um inocente");
                            }else {
                                P.sendMessage("§c" + Sender.getName() + "§a acha que §a" + player + " §aé um inocente");
                            }
                        }

                        if(Victimain.getInstance().hasplayerinside(Sender.getName())){

                            if(Type.equals(PlayerType.Assasino)) {
                                P.sendMessage("§a" + Sender.getName() + "§a acha que §c" + player + " §aé um inocente");
                            }else {
                                P.sendMessage("§a" + Sender.getName() + "§a acha que §a" + player + " §aé um inocente");
                            }
                        }

                        if(detetiveMain.getInsance().hasplayerinside(Sender.getName())){
                            if(Type.equals(PlayerType.Assasino)) {
                                P.sendMessage("§1" + Sender.getName() + "§a acha que §c" + player + " §aé um inocente");
                            }else {
                                P.sendMessage("§1" + Sender.getName() + "§a acha que §a" + player + " §aé um inocente");
                            }
                        }

                    });

                    Victimain.getInstance().getBukkitPlayers().forEach(P -> {

                        if(AssasinMain.getInstance().hasplayerinside(Sender.getName())){

                            if(Type.equals(PlayerType.Assasino)) {

                                P.sendMessage("§4" + Sender.getName() + "§a acha que §4" + player + " §aé um inocente");
                            }else {
                                P.sendMessage("§4" + Sender.getName() + "§a acha que §1" + player + " §aé um inocente");
                            }
                        }

                        if(Victimain.getInstance().hasplayerinside(Sender.getName())){

                            if(Type.equals(PlayerType.Assasino)) {
                                P.sendMessage("§a" + Sender.getName() + "§a acha que §4" + player + " §aé um inocente");
                            }else {
                                P.sendMessage("§a" + Sender.getName() + "§a acha que §1" + player + " §aé um inocente");
                            }
                        }

                        if(detetiveMain.getInsance().hasplayerinside(Sender.getName())){
                            if(Type.equals(PlayerType.Assasino)) {
                                P.sendMessage("§1" + Sender.getName() + "§a acha que §4" + player + " §aé um inocente");
                            }else {
                                P.sendMessage("§1" + Sender.getName() + "§a acha que §1" + player + " §aé um inocente");
                            }
                        }

                    });

                    Manager.getInstance().removePlayer(player);
                    Manager.getInstance().addPlayerUsed(Sender.getName());
                    Manager.getInstance().addconfiou(Sender.getName(),player);
                    return true;
                }
            }

        Sender.sendMessage("§cVocê não pode usar esse comando agora.");

        return false;
    }
}
