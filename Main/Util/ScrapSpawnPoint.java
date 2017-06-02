package Main.Util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by GMDAV on 27/04/2017.
 */
public class ScrapSpawnPoint {

    private List<Location> ScrapsLocation = new ArrayList<>();


    public ScrapSpawnPoint(Material Material){
        int Radius = Bukkit.getSpawnRadius();
        Location Spawn = Bukkit.getWorlds().get(0).getSpawnLocation();

        Location PointMax = new Location(Spawn.getWorld(),Spawn.getX()+Radius,Spawn.getY()+Radius,Spawn.getZ()+Radius);
        Location PointMin = new Location(Spawn.getWorld(),Spawn.getX()-Radius,Spawn.getY()-Radius,Spawn.getZ()-Radius);

        List<Location> AllLocations = new ArrayList<>();

        for(int x = PointMin.getBlockX(); x <= PointMax.getBlockX(); x++) {
            for (int y = PointMin.getBlockY(); y <= PointMax.getBlockY(); y++) {
                for (int z = PointMin.getBlockZ(); z <= PointMax.getBlockZ(); z++) {
                    AllLocations.add(new Location(Spawn.getWorld(), x, y, z));
                }
            }
        }

        ScrapsLocation = AllLocations.stream().filter(P -> P.getBlock().getType().equals(Material)).collect(Collectors.toList());
    }

    public ScrapSpawnPoint(Material Material, int Radius){
        Location Spawn = Bukkit.getWorlds().get(0).getSpawnLocation();

        Location PointMax = new Location(Spawn.getWorld(),Spawn.getX()+Radius,Spawn.getY()+Radius,Spawn.getZ()+Radius);
        Location PointMin = new Location(Spawn.getWorld(),Spawn.getX()-Radius,Spawn.getY()-Radius,Spawn.getZ()-Radius);

        List<Location> AllLocations = new ArrayList<>();

        for(int x = PointMin.getBlockX(); x <= PointMax.getBlockX(); x++) {
            for (int y = PointMin.getBlockY(); y <= PointMax.getBlockY(); y++) {
                for (int z = PointMin.getBlockZ(); z <= PointMax.getBlockZ(); z++) {
                    AllLocations.add(new Location(Spawn.getWorld(), x, y, z));
                }
            }
        }

        ScrapsLocation = AllLocations.stream().filter(P -> P.getBlock().getType().equals(Material)).collect(Collectors.toList());
    }


    public List<Location> getScrapsLocation() {
        return ScrapsLocation;
    }
}
