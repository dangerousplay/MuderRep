package Main.Util;

import net.minecraft.server.v1_7_R2.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by GMDAV on 12/05/2017.
 */
public abstract class Effects {

    public enum ParticleEffects {

        HUGE_EXPLOSION("hugeexplosion"),
        LARGE_EXPLODE("largeexplode"),
        FIREWORKS_SPARK("fireworksSpark"),
        BUBBLE("bubble"),
        SUSPEND("suspend"),
        DEPTH_SUSPEND("depthSuspend"),
        TOWN_AURA("townaura"),
        CRIT("crit"),
        MAGIC_CRIT("magicCrit"),
        MOB_SPELL("mobSpell"),
        MOB_SPELL_AMBIENT("mobSpellAmbient"),
        SPELL("spell"),
        INSTANT_SPELL("instantSpell"),
        WITCH_MAGIC("witchMagic"),
        NOTE("note"),
        PORTAL("portal"),
        ENCHANTMENT_TABLE("enchantmenttable"),
        EXPLODE("explode"),
        FLAME("flame"),
        LAVA("lava"),
        FOOTSTEP("footstep"),
        SPLASH("splash"),
        LARGE_SMOKE("largesmoke"),
        CLOUD("cloud"),
        RED_DUST("reddust"),
        SNOWBALL_POOF("snowballpoof"),
        DRIP_WATER("dripWater"),
        DRIP_LAVA("dripLava"),
        SNOW_SHOVEL("snowshovel"),
        SLIME("slime"),
        HEART("heart"),
        ANGRY_VILLAGER("angryVillager"),
        HAPPY_VILLAGER("happerVillager"),
        ICONCRACK("iconcrack_"),
        TILECRACK("tilecrack_");

        private String particleName;

        ParticleEffects(String particleName) {
            this.particleName = particleName;
        }


    }

    public static void SendParticle(ParticleEffects effects,Location loc, int Particles,boolean random){

        if(random){
            Random R = new Random();
            Location Base = loc;

            double X,Y,Z;

            for(int i = 0; i <= Particles; i++) {

                if (R.nextBoolean()) {
                    X = R.nextDouble() * -1;
                } else {
                    X = R.nextDouble();
                }

                if (R.nextBoolean()) {
                    Y = R.nextDouble() * -1;
                } else {
                    Y = R.nextDouble();
                }

                if (R.nextBoolean()) {
                    Z = R.nextDouble() * -1;
                } else {
                    Z = R.nextDouble();
                }

                Base.add(X, Y, Z);

                final Location LocFinal = Base;

                Arrays.asList(Bukkit.getServer().getOnlinePlayers()).forEach(P ->
                        ((CraftPlayer) P).getHandle().playerConnection.sendPacket(toPacket(effects, LocFinal, 2)));
                Base = loc;
            }

        }else {
            Arrays.asList(Bukkit.getServer().getOnlinePlayers()).forEach(P ->
                    ((CraftPlayer)P).getHandle().playerConnection.sendPacket(toPacket(effects,loc,Particles)));
        }


    }

    public static void PlaySoundAll(Sound Sound){
        Arrays.asList(Bukkit.getServer().getOnlinePlayers()).forEach(P -> P.playSound(P.getLocation(),Sound,100,100));
    }

    public static PacketPlayOutWorldParticles toPacket(ParticleEffects effects, Location Location,int Ammount) {

    return new PacketPlayOutWorldParticles(effects.particleName,(float)Location.getX(),(float)Location.getY(),(float)Location.getZ(),(float)0,(float)0,(float)0,(float)0,Ammount);

    }




}
