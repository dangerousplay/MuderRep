package Main;

import Main.Runnable.BukkitRunnable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.plugin.Plugin;

public class ClearItems {

    public ClearItems(Plugin Instance){
        ((BukkitRunnable) () -> {
            Bukkit.getServer().getWorlds().forEach(P -> P.getEntities().forEach(E -> {
                if(E instanceof Item){
                    if(((Item) E).getItemStack().getType().equals(Material.REDSTONE))
                        E.remove();
                }
            }));
        }).runTaskTimer(Main.getInstance(),0,20*30);
    }


}
