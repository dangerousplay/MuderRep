package Main.espectador;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.Collections;


public abstract class Items {

    public static ItemStack getSkullLog(String player){
        ItemStack Skull = new ItemStack(Material.SKULL,1,(short) SkullType.PLAYER.ordinal());

        Skull.getItemMeta().setDisplayName("§aDanos Tomados " + player);
        Skull.getItemMeta().setLore(Collections.singletonList("§cUse para ver o seu histórico de dano"));
        return Skull;
    }

}

