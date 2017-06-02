package Main;

import Main.Util.*;
import Main.Votacao.CommandEx;
import Main.Votacao.CommandIno;
import Main.Votacao.CommandNeu;
import Main.Votacao.Manager;
import Main.espectador.EspecInteract;
import Main.listeners.*;
import Main.teams.Assasino.AssasinMain;
import Main.teams.Assasino.listenersA;
import Main.teams.Detetive.detetiveMain;
import Main.teams.Detetive.listenersD;
import Main.teams.Vítima.Victimain;
import Main.teams.Vítima.listenersV;
import br.com.tlcm.cc.API.CoreD;
import br.com.tlcm.cc.API.CoreDPlayer;
import br.com.tlcm.cc.API.MiniGame;
import br.com.tlcm.cc.API.enumerators.TeleportToLobbyReason;
import net.minecraft.server.v1_7_R2.*;
import net.minecraft.server.v1_7_R2.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static Main.Util.headScoreboard.scoreboardPacket;
import static Main.Util.headScoreboard.sendPacket;

/**
 * Created by GMDAV on 18/04/2017.
 */
public class Main extends JavaPlugin {
    private static Main Instance;

    private final listenersD DetetiveList = new listenersD();
    private final listenersA AssasinoListener = new listenersA();
    private Material ScrapLocation;
    private final List<Location> ScrapsLocation = new ArrayList<>();


    @Override
    public void onEnable() {
        Instance = this;

        getConfig().set("Minimum_players", getConfig().get("Minimum_players",12));
        getConfig().set("Assasino_Ward", getConfig().get("Assasino_Ward",15));
        getConfig().set("Vitimas_Ward", getConfig().get("Vitimas_Ward",10));
        getConfig().set("Detetive_Ward",getConfig().get("Detetive_Ward",12));

        CoreD.setCancelBlockBreakEvent(true);
        CoreD.setCancelBlockPlaceEvent(true);
        CoreD.setCancelPlayerInteractEvent(true);
        CoreD.setCancelPlayerInteractEntityEvent(true);
        CoreD.setDisableDamage(true);
        CoreD.setDisableDeathScreen(true);
        CoreD.setAllowSpectatorsNearPlayers(false);
        CoreD.setToSpectatorOnRespawn(true);
        CoreD.setAllowJoinSpectators(true);
        CoreD.setAllowTeamChat(false);
        CoreD.setStore(true);


        CoreD.setMiniGame(new MiniGame("Murders","Start", getConfig().getInt("Minimum_players")));

        Bukkit.getServer().getPluginManager().registerEvents(AssasinoListener,this);
        Bukkit.getServer().getPluginManager().registerEvents(DetetiveList, this);
        Bukkit.getServer().getPluginManager().registerEvents(new listenersV(),this);
        Bukkit.getServer().getPluginManager().registerEvents(PlayerDeath.getInstance(),this);
        Bukkit.getServer().getPluginManager().registerEvents(new disabledevents(),this);
        Bukkit.getServer().getPluginManager().registerEvents(PlayerUseItems.getInstance(),this);
        Bukkit.getServer().getPluginManager().registerEvents(new SpawnAdd(),this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerRespawn(),this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDamageHologram(),this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoin(),this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerChunkLoad(),this);
        Bukkit.getServer().getPluginManager().registerEvents(new EspecInteract(),this);

        getCommand("desconfiar").setExecutor(new CommandEx());
        getCommand("inocentar").setExecutor(new CommandIno());
        getCommand("neutro").setExecutor(new CommandNeu());

        CoreD.getMiniGame().setFastStart(true);

        killmobs();

        ItemsEdit.registerArcoSalvacao();
        ItemsEdit.registerFlechaRecipe();
        ItemsEdit.registerSword();

    }



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.isOp()){
            if(label.equalsIgnoreCase("start")){
                CoreD.setRunning();
                Start();
                return true;
            }

            if(label.equalsIgnoreCase("npc") && args.length == 2 && sender instanceof Player){
               Player p = (Player)sender;
                NPCSpawn.getInstance().spawnCorpse(p);
               return true;
            }

            if(label.equalsIgnoreCase("npcs") && args.length == 2 && sender instanceof Player){
                Player p = (Player)sender;
                PlayerDeath.getInstance().addPlayerDeath(SpawnUtil.spawndeathplayer(p,Integer.parseInt(args[0]),Integer.parseInt(args[1])));
                return true;
            }

            if(label.equalsIgnoreCase("addscraploc") && args.length == 3){

                int X = Integer.parseInt(args[0]);
                int Y = Integer.parseInt(args[1]);
                int Z = Integer.parseInt(args[2]);

                ScrapsLocation.add(new Location(Bukkit.getWorlds().get(0),X,Y,Z));
            }

            if(label.equalsIgnoreCase("scrapitem") && args.length == 1){
                @SuppressWarnings("deprecated")
                ItemStack BlockType = new ItemStack(Integer.parseInt(args[0]));
                ScrapLocation = BlockType.getType();
                Bukkit.getServer().getLogger().info("O bloco foi adicionado " + BlockType.getType());
                return true;
            }

            if(label.equalsIgnoreCase("sbt")){
                Scoreboard sb = new Scoreboard();

                if(sb.getObjective("Vitima") == null){
                    sb.registerObjective("Vitima", new ScoreboardBaseCriteria("Vitima"));
                }

                ScoreboardScore SBScore = sb.getPlayerScoreForObjective("§4§lVitima",sb.getObjective("Vitima"));
                SBScore.setScore(1);


                PacketPlayOutScoreboardDisplayObjective Dysplay = new PacketPlayOutScoreboardDisplayObjective(2,sb.getObjective("Vitima"));
                PacketPlayOutScoreboardObjective Objetive = new PacketPlayOutScoreboardObjective(sb.getObjective("Vitima"),0);
                PacketPlayOutScoreboardScore Score = new PacketPlayOutScoreboardScore(SBScore,0);

//                Arrays.asList(Bukkit.getServer().getOnlinePlayers()).forEach(P -> {
//
//                    ((CraftPlayer)P).getHandle().playerConnection.sendPacket(Objetive);
//                    ((CraftPlayer)P).getHandle().playerConnection.sendPacket(Dysplay);
//                    ((CraftPlayer)P).getHandle().playerConnection.sendPacket(Score);
//
//                });

                List<Player> lista = new ArrayList<>();

                lista.add(Bukkit.getServer().getOnlinePlayers()[0]);

                for(Player p: lista){
                    Scoreboard TesteScore = ((CraftPlayer)p).getHandle().getScoreboard();
                    ScoreboardTeam time;


                    List<String> Players = Arrays.stream(Bukkit.getServer().getOnlinePlayers()).filter(P -> !lista.contains(P)).map(Player::getName).collect(Collectors.toList());

                    Random r = new Random();

                    if(TesteScore.getObjective("Suspeito") == null){
                        TesteScore.registerObjective("Suspeito", new ScoreboardBaseCriteria("dummy"));
                    }

                    time = TesteScore.getTeam("Vitimas") == null ? TesteScore.createTeam("Vitimas"):TesteScore.getTeam("Vitimas");

                    ScoreboardScore SBScore2 = TesteScore.getPlayerScoreForObjective("Suspeito",sb.getObjective("Suspeito"));
                    SBScore.setScore(1);

                    //Arrays.stream(Bukkit.getServer().getOnlinePlayers()).filter(P -> !lista.contains(P)).map(Player::getName).collect(Collectors.toList());

                    PacketPlayOutScoreboardDisplayObjective Dysplay2 = new PacketPlayOutScoreboardDisplayObjective(2,TesteScore.getObjective("Suspeito"));
                    PacketPlayOutScoreboardObjective Objetive2 = new PacketPlayOutScoreboardObjective(TesteScore.getObjective("Suspeito"),3);
                    //PacketPlayOutScoreboardScore Score2 = new PacketPlayOutScoreboardScore(SBScore2,0);
                    PacketPlayOutScoreboardTeam Team = new PacketPlayOutScoreboardTeam(time,time.getPlayerNameSet().size());

                    PacketUtil.setTeamDisplayName(Team,"Vitima");
                    PacketUtil.setTeamName(Team,"Vitima");
                    PacketUtil.setTeamPlayers(Team,Players);
                    PacketUtil.setTeamMode(Team,0);
                    PacketUtil.setTeamFriendlyFire(Team,1);





                    Arrays.stream(Bukkit.getServer().getOnlinePlayers()).filter(P -> !lista.contains(P)).forEach(P -> {

                    ((CraftPlayer)P).getHandle().playerConnection.sendPacket(Team);
                   ((CraftPlayer)P).getHandle().playerConnection.sendPacket(Objetive2);
                    ((CraftPlayer)P).getHandle().playerConnection.sendPacket(Dysplay2);
                    //((CraftPlayer)P).getHandle().playerConnection.sendPacket(Score2);

                });


                }




//                Player P = Arrays.asList(Bukkit.getServer().getOnlinePlayers()).get(0);
//
//                    ((CraftPlayer)P).getHandle().playerConnection.sendPacket(Objetive);
//                    ((CraftPlayer)P).getHandle().playerConnection.sendPacket(Dysplay);
//                    ((CraftPlayer)P).getHandle().playerConnection.sendPacket(Score);

                return true;

            }

            if(label.equalsIgnoreCase("bug") && args.length == 2){

                Player p = Bukkit.getPlayer(args[0]);




//                PacketPlayOutEntityStatus Status = new PacketPlayOutEntityStatus(((CraftPlayer)p).getHandle(),Byte.parseByte(args[1]));
//                PacketPlayOutKeepAlive Statuss = new PacketPlayOutKeepAlive(((CraftPlayer)p).getHandle().getId());

                //Arrays.stream(Bukkit.getOnlinePlayers()).filter(P -> !p.getName().equalsIgnoreCase(P.getName())).forEach(P -> PlayerUtil.sendPacket(P,packet));



            }

            if(label.equalsIgnoreCase("sbm") && args.length == 1){



//                PacketPlayOutBed Bed = new PacketPlayOutBed(((CraftPlayer)p).getHandle(),1,1,1);
//
//                Arrays.stream(Bukkit.getServer().getOnlinePlayers()).filter(P -> !P.getName().equalsIgnoreCase(p.getName())).forEach(T -> ((CraftPlayer)T).getHandle().playerConnection.sendPacket(Bed));

//                List<Player> lista = new ArrayList<>();
//
//                lista.add(Bukkit.getServer().getOnlinePlayers()[0]);
//
//                lista.get(0).getScoreboard().getObjective("");
//
//                List<String> scoreboards = Arrays.stream(Bukkit.getServer().getOnlinePlayers()).filter(P -> !lista.contains(P)).map(Player::getName).collect(Collectors.toList());
//
//
//                for (String sbPlayer : scoreboards){
//                    sendPacket(Bukkit.getPlayer(sbPlayer), scoreboardPacket(lista.get(0), headScoreboard.SB.CREATE));
//                }
//
//                for (String sbPlayer : scoreboards){
//                    if (!sbPlayer.equals(lista.get(0).getName()))
//                        sendPacket(lista.get(0), scoreboardPacket(Bukkit.getPlayer(sbPlayer), headScoreboard.SB.CREATE));
//                }

                return true;
            }

        }
     return false;
    }

    private void killmobs(){
        Bukkit.getWorlds().forEach(W -> W.getEntities().forEach(Entity::remove));
    }

    private void Start(){

        setartimes();
        habilitareventos();
        Manager.getInstance().start();

       SpawnUtil.SpawnBloodStart();

       new ClearItems(this);

        DetetiveList.Delaytouse();
        AssasinoListener.Delaytouse();
        Finalizador();



        //Setar Lugar para o Spawn dos Scraps
        if(ScrapsLocation.isEmpty()) {
            SpawnUtil.SpawnScraps(new ScrapSpawnPoint(ScrapLocation).getScrapsLocation(), new ItemStack(Material.GOLD_INGOT, 1));
        }else {
            int ScrapLimit;

            if(Bukkit.getServer().getOnlinePlayers().length > 10){
                ScrapLimit = Bukkit.getOnlinePlayers().length*3;
            }else {
                ScrapLimit = Bukkit.getOnlinePlayers().length*2;
            }

            if(ScrapsLocation.size() < ScrapLimit){

                while(ScrapsLocation.size() < ScrapLimit){
                   ScrapsLocation.add(ScrapsLocation.get((int)(Math.random()*(ScrapsLocation.size()-1))));
                }

            }else {
                while(ScrapsLocation.size() > ScrapLimit){
                    ScrapsLocation.remove(0);
                }
            }

            SpawnUtil.SpawnScraps(ScrapsLocation,new ItemStack(Material.GOLD_INGOT,1));
        }
    }

    private void habilitareventos(){
        CoreD.setCancelPlayerInteractEvent(false);
        CoreD.setCancelPlayerInteractEntityEvent(false);
        CoreD.setDisableDamage(false);
        CoreD.setDisableDeathScreen(false);
    }

    private void setartimes(){
        Random random = new Random();
        List<String> players = Arrays.stream(Bukkit.getServer().getOnlinePlayers()).map(Player::getName).collect(Collectors.toList());

        detetiveMain.getInsance().setPlayer(players.get(random.nextInt(players.size())));
        players.remove(detetiveMain.getInsance().getPlayer());

        int Vitimas = (int)Math.floor(Bukkit.getServer().getOnlinePlayers().length-1 * 0.7);

        List<String> VitimasList = new ArrayList<>();

        for(int i = 1; i < Vitimas; i++){
            VitimasList.add(players.get(random.nextInt(players.size())));
        }

        Victimain.getInstance().setTeams(VitimasList);

        players.removeAll(VitimasList);

        AssasinMain.getInstance().setPlayer(players);



    }

    private void Finalizador(){
        new BukkitRunnable(){
            @Override
            public void run() {
                if(CoreD.isRunning()){
                    if(AssasinMain.getInstance().getPlayer().isEmpty()){

                        Effects.PlaySoundAll(Sound.ENDERDRAGON_DEATH);

                        Bukkit.broadcastMessage("                                  ");
                        Bukkit.broadcastMessage("§aOs inocentes venceram!");
                        Bukkit.broadcastMessage("                                  ");
                        Bukkit.broadcastMessage("§cOs assasinos eram: " + AssasinMain.getInstance().toString());
                        Bukkit.broadcastMessage("                                  ");

                        new BukkitRunnable(){
                            @Override
                            public void run() {

                                Victimain.getInstance().getTeams().forEach(P -> {
                                    CoreD.sendToLobby(Bukkit.getPlayer(P),
                                            TeleportToLobbyReason.WINNER);
                                    CoreD.award(Bukkit.getPlayer(P),getConfig().getInt("Vitimas_Ward"));

                                });

                                if(detetiveMain.getInsance().getPlayer() != null && Bukkit.getPlayer(detetiveMain.getInsance().getPlayer()) != null) {
                                    CoreD.award(Bukkit.getPlayer(detetiveMain.getInsance().getPlayer()), getConfig().getInt("Detetive_Ward"));
                                    CoreD.sendToLobby(Bukkit.getPlayer(detetiveMain.getInsance().getPlayer()), TeleportToLobbyReason.WINNER);
                                }

                                if(!CoreDPlayer.getSpectators().isEmpty()){
                                    CoreDPlayer.getSpectators().forEach(P -> CoreD.sendToLobby(Bukkit.getPlayer(P),TeleportToLobbyReason.SPECTATOR));
                                }

                            }
                        }.runTaskLater(Main.getInstance(),20*10);

                        cancel();
                        return;
                    }

                    if(Victimain.getInstance().getTeams().isEmpty() && detetiveMain.getInsance().getPlayer() == null){

                        Effects.PlaySoundAll(Sound.ENDERDRAGON_DEATH);

                        Bukkit.broadcastMessage("                                  ");
                        Bukkit.broadcastMessage("§cOs assasinos venceram!");
                        Bukkit.broadcastMessage("                                  ");
                        Bukkit.broadcastMessage("§cOs assasinos eram: " + AssasinMain.getInstance().toString());
                        Bukkit.broadcastMessage("                                  ");

                        new BukkitRunnable(){
                            @Override
                            public void run() {

                                AssasinMain.getInstance().getPlayer().forEach(P -> {
                                    CoreD.sendToLobby(Bukkit.getPlayer(P), TeleportToLobbyReason.WINNER);
                                    CoreD.award(Bukkit.getPlayer(P), getConfig().getInt("Assasino_Ward"));
                                });

                                if(!CoreDPlayer.getSpectators().isEmpty()){
                                    CoreDPlayer.getSpectators().forEach(P -> CoreD.sendToLobby(Bukkit.getPlayer(P),TeleportToLobbyReason.SPECTATOR));
                                }

                            }
                        }.runTaskLater(Main.getInstance(),20*10);



                        cancel();
                    }
                }
            }
        }.runTaskTimer(this,0,20);


    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public static Main getInstance(){
        return Instance;
    }
}
