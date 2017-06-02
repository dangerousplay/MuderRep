package Main.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by GMDAV on 18/05/2017.
 */
public class PlayerJoin implements Listener {

    @EventHandler
    private void PlayerJoin(PlayerJoinEvent event){
        event.getPlayer().sendMessage("§a§oEste Minigame é semelhante ao detetive, os assasinos precisam matar as vítimas" +
                " e o detetive para ganhar, as vítimas precisam matar os assasinos.");

        event.getPlayer().sendMessage("§cUse /desconfiar [player] para marcar um player como suspeito.");
        event.getPlayer().sendMessage("§bUse /inocentar [player] para deixar um voto de confiança.");
        event.getPlayer().sendMessage("§3Use /neutralizar [player] para remove o seu voto.");
    }

}
