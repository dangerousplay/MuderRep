package Main.espectador;

import Main.Util.Deatharea;
import Main.teams.Detetive.detetiveMain;
import br.com.tlcm.cc.API.CoreD;
import br.com.tlcm.cc.API.CoreDPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Optional;

public class EspecInteract implements Listener {


    @EventHandler(ignoreCancelled = true)
    private void Use(PlayerInteractEvent event){
        if(CoreD.isRunning() && CoreDPlayer.isSpectator(event.getPlayer())){
            if(event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().hasItemMeta()){
                if(event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(event.getPlayer().getName())){
                    Optional<Deatharea> Death = detetiveMain.getInsance().getlocalcrime(event.getPlayer().getName());

                    Death.ifPresent(P -> event.getPlayer().openInventory(P.getDamagesTakenInventory()));
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void Throw(PlayerDropItemEvent event){
        if(CoreDPlayer.isSpectator(event.getPlayer()))
            event.setCancelled(true);
    }




}
