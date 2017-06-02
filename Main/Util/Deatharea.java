package Main.Util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Skull;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by GMDAV on 21/04/2017.
 */
public class Deatharea implements Iterable<Location> {

    private final List<Location> CrimeLocations = new ArrayList<>();
    private String player;
    private Map<String,Double> Damagestaken;

    public Deatharea(Collection<? extends Location> locations, String player, Map<String,Double> damagestaken){
        this.CrimeLocations.addAll(locations);
        this.player = player;
        this.Damagestaken = damagestaken;
    }

    @Override
    public Iterator<Location> iterator() {
      return CrimeLocations.iterator();
    }

    @Override
    public void forEach(Consumer<? super Location> action) {
        Objects.requireNonNull(action);
        for(Location loc : CrimeLocations){
            action.accept(loc);
        }
    }

    public boolean hasblockinside(Block b){
        return this.CrimeLocations.contains(b.getLocation());
    }

    public boolean hasplayerinside(Player p){
        return this.CrimeLocations.contains(p.getLocation());
    }


    public List<Location> getCrimeLocations() {
        return CrimeLocations;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public List<String> getDamagesTakenByPlayer() {

        List<String> Return = new ArrayList<>();

        List<Double> Sumof = new ArrayList<>();
        Damagestaken.forEach((P,K) -> Sumof.add(K));

        Double somatudo = Sumof.stream().mapToDouble(P -> P).sum();

        if(somatudo <= 0){
            Return.add("§1§lInvestigação do player " + "§b" + player);
            Return.add("§eMorreu por causas desconhecidas!");
        }else {

            Return.add("§1§lInvestigação do player " + "§b" + player);

            Damagestaken.forEach((K, V) -> Return.add("§b" + K + " causou " + getpercentageint(somatudo, V) + "% de dano ao player"));

            Return.add("                 ");
        }
        return Return;
    }

    public Inventory getDamagesTakenInventory(){
        Inventory inventory = Bukkit.createInventory(null,Damagestaken.size() % 9 == 0 ? Damagestaken.size():(int)(Math.ceil(Damagestaken.size()/9)*9),"§lAnalise: " + player);

        List<Double> Sumof = new ArrayList<>();
        Damagestaken.forEach((P,K) -> Sumof.add(K));

        Double somatudo = Sumof.stream().mapToDouble(P -> P).sum();


        Damagestaken.forEach((K,V) ->{

            ItemStack Skull = new ItemStack(Material.SKULL,1,(short) SkullType.PLAYER.ordinal());

            SkullMeta MetaSkull = (SkullMeta)Skull.getItemMeta();

            MetaSkull.setOwner(K);
            MetaSkull.setDisplayName(K);

            List<String> Lore = new ArrayList<>();

            Lore.add("§b" + K + " causou §4" + getpercentageint(somatudo, V) + "§c% de dano ao player " + "");
            Lore.add(getcoracao(getpercentageint(somatudo, V)));
            MetaSkull.setLore(Lore);

            Skull.setItemMeta(MetaSkull);

            inventory.addItem(Skull);

        });

        return inventory;
    }

    private String getcoracao(int Percentage){
        StringBuilder SB = new StringBuilder();

        for(int i = 5; i <= Percentage; i*=5){
            SB.append("♥");
        }

        return SB.toString();
    }

    private Double getpercentage(double max, double value){
        return (100*value)/max;
    }

    private int getpercentageint(double max, double value){
        return (int)Math.ceil(100*value/max);
    }
}
