package Main.Util;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class invTools {


    /**
     * @param Inv
     * @param Item
     * @param Ammount
     */
    public static void remove(Inventory Inv, ItemStack Item, int Ammount){
        Arrays.stream(Inv.getContents()).filter(P -> P.getType().equals(Item.getType())).collect(Collectors.groupingBy((k) -> k));
    }

}
