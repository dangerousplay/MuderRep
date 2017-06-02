package Main;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GMDAV on 21/04/2017.
 */
public abstract class ItemsEdit {

    public enum ItemsToConsume{

        Tesoura(getTesouraDetetive()),
        ArcoSalvacao(getArcoSalvacao()),
        LaminaDoran(getLaminadoran()),
        PoçãoVelocidade(PocaoVelocidade()),
        PoçãoInvisibilidade(PocaoInvisibilidade());

        private ItemStack item;

         ItemsToConsume(ItemStack item){
            this.item = item;
        }

        public ItemStack getItem(){
             return item;
        }




    }


    public static ItemStack getTesouraDetetive(){
        ItemStack TesouraDetetive = new ItemStack(Material.SHEARS);
        ItemMeta TesouraMeta = TesouraDetetive.getItemMeta();

        TesouraMeta.setDisplayName("§bTesoura inspetora");

        List<String> lore = new ArrayList<>();
        lore.add("§aUse esta tesoura em um player que morreu e você");
        lore.add("Verá quem matou ele");

        TesouraMeta.setLore(lore);

        TesouraDetetive.setItemMeta(TesouraMeta);

        return TesouraDetetive;
    }

    public static ItemStack getLaminadoran(){
        ItemStack LaminaAss = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta LaminaAssItemMeta = LaminaAss.getItemMeta();

        LaminaAssItemMeta.setDisplayName("§4Lâmina de Doran!");

        List<String> lore = new ArrayList<>();
        lore.add("§aUse essa lâmina para matar um player");
        lore.add("Rapidamente");

        LaminaAssItemMeta.setLore(lore);

        LaminaAss.setItemMeta(LaminaAssItemMeta);

        return LaminaAss;
    }

    public static ItemStack getArcoSalvacao(){
        ItemStack ArcoSalvacao = new ItemStack(Material.BOW);
        ItemMeta ArcosalvaItemMeta = ArcoSalvacao.getItemMeta();

        ArcosalvaItemMeta.setDisplayName("§1Arco da Salvação");

        List<String> lore = new ArrayList<>();
        lore.add("§aUse esse arco para matar o Sabotador");
        lore.add("§aRapidamente");

        ArcosalvaItemMeta.setLore(lore);

        ArcoSalvacao.setItemMeta(ArcosalvaItemMeta);

        return ArcoSalvacao;
    }

    public static void registerArcoSalvacao(){

        ShapedRecipe ArcoReceita = new ShapedRecipe(getArcoSalvacao());
        ArcoReceita.shape(
                "@##",
                "#@#",
                "@##");

        ArcoReceita.setIngredient('#',Material.GOLD_INGOT);
        Bukkit.getServer().addRecipe(ArcoReceita);

    }

    public static void registerFlechaRecipe(){


        ShapedRecipe Flecha = new ShapedRecipe(new ItemStack(Material.ARROW,1));
        Flecha.shape("@","@");
        Flecha.setIngredient('@',Material.GOLD_INGOT);

        Bukkit.getServer().addRecipe(Flecha);

    }

    public static void registerSword(){

        ShapedRecipe Espada = new ShapedRecipe(new ItemStack(Material.GOLD_SWORD,1));
        Espada.shape("@","@","@");
        Espada.setIngredient('@',Material.GOLD_INGOT);

        Bukkit.getServer().addRecipe(Espada);

    }

    public static ItemStack PocaoInvisibilidade(){
        Potion pocao = new Potion(PotionType.INVISIBILITY);
        pocao.setLevel(1);
        pocao.setHasExtendedDuration(false);
        pocao.setSplash(false);
        return pocao.toItemStack(1);
    }


    public static ItemStack PocaoVelocidade(){
        Potion pocao = new Potion(PotionType.SPEED);
        pocao.setLevel(1);
        return pocao.toItemStack(1);
    }


}
