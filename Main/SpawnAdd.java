package Main;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by GMDAV on 02/05/2017.
 */
public class SpawnAdd implements Listener {

    private static ItemStack Wand = new ItemStack(Material.BLAZE_ROD);



    @EventHandler
    private void Click(PlayerInteractEvent event){
        if(event.getPlayer().isOp()&& event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getPlayer().getItemInHand().getType().equals(Material.BLAZE_ROD)){
            Location Lugar = event.getPlayer().getLocation();

            try {
                List<String> Locations = Main.getInstance().getConfig().getStringList("Spawns");
                Locations.add("addscraploc " + Lugar.getBlockX() + " " + Lugar.getBlockY() + " " + Lugar.getBlockZ());
                Main.getInstance().getConfig().set("Spawns",Locations);
                event.getPlayer().sendMessage(Lugar.toString());
            }catch (Exception Ex){
                Ex.printStackTrace();
            }


            Main.getInstance().saveConfig();

        }
    }


}
