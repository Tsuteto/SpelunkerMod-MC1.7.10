package tsuteto.spelunker.item;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tsuteto.spelunker.Settings;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.constants.SpelunkerDifficulty;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

import java.util.ArrayList;

public abstract class SpelunkerItem extends Item
{
    public static Item itemEnergy;
    public static Item itemGunWood;
    public static Item itemGunSteel;
    public static Item itemGunSpelunker;
    public static Item itemSpeedPotion;
    public static Item itemDollar;
    public static Item item1up;
    public static Item itemCoin;
    public static Item itemMiracle;
    public static Item item2xScore;
    public static Item itemInvincible;
    public static Item itemDynamite;
    public static Item itemFlashDrop;
    public static Item itemHelmet;
    public static Item itemFlash;
    public static Item itemGoldenStatue;

    public static ArrayList<SpelunkerItem> dropItemRegistry = new ArrayList<SpelunkerItem>();

    public static void load()
    {
        Settings settings = SpelunkerMod.settings();

        itemGunWood = $("woodenGun", new ItemGun(EnumGunMaterial.WOOD))
                .register()
                .setFull3D();
        itemGunSteel = $("steelGun", new ItemGun(EnumGunMaterial.IRON))
                .register()
                .setFull3D();
        itemGunSpelunker = $("spelunkerGun", new ItemGun(EnumGunMaterial.ORIGINAL))
                .register()
                .setFull3D();

        itemEnergy = $("energy", new ItemEnergy()).register();
        itemSpeedPotion = $("speedPotion", new ItemSpeedPotion()).register();
        itemDollar = $("dollarBag", new ItemDoller()).register();
        item1up = $("1up", new Item1up()).register();
        itemCoin = $("coin", new ItemCoin()).register();
        itemMiracle = $("miracle", new ItemMiracle()).register();
        item2xScore = $("2xScore", new Item2xScore()).register();
        itemInvincible = $("invincible", new ItemInvincible()).register();
        itemDynamite = $("dynamite", new ItemDynamite()).register();
        itemFlashDrop = $("flashDrop", new ItemFlashDrop()).register();

        itemFlash = $("flash", new ItemFlash())
                .register()
                .setCreativeTab(CreativeTabs.tabTools);
        itemHelmet = $("helmet", new ItemHelmet(ItemArmor.ArmorMaterial.IRON, 2, 0))
                .register()
                .setLightRange(32)
                .setTargetLightValue(14)
                .setHeadLightValue(8)
                .setCreativeTab(CreativeTabs.tabTools);

        itemGoldenStatue = $("goldenStatueF", new ItemGoldenSpelunker())
                .withResource("goldenSpelunker")
                .register()
                .setMaxStackSize(1)
                .setCreativeTab(CreativeTabs.tabMisc);

        // Register dispense behavior
        BlockDispenser.dispenseBehaviorRegistry.putObject(itemFlash, new BehaviorDispenseFlash());

        // Recipes
        GameRegistry.addRecipe(new ItemStack(itemGunWood),
                "XXX",
                "  X",

                'X', Blocks.planks);

        GameRegistry.addRecipe(new ItemStack(itemGunSteel),
                "XX#",
                "  Y",

                'X', Items.iron_ingot,
                'Y', Blocks.planks,
                '#', new ItemStack(Items.dye, 1, 4));

        GameRegistry.addRecipe(new ItemStack(itemGunSpelunker),
                "XX#",
                "  /",

                'X', Items.iron_ingot,
                '/', Items.blaze_rod,
                '#', Items.ender_pearl);

        GameRegistry.addRecipe(new ItemStack(itemHelmet, 1),
                "HCG",

                'H', Items.iron_helmet,
                'C', new ItemStack(Items.coal, 1, 0),
                'G', Blocks.glass_pane);

        GameRegistry.addRecipe(new ItemStack(itemFlash, 8),
                "L",
                "G",
                "B",

                'L', Items.glowstone_dust,
                'G', Items.gunpowder,
                'B', Items.glass_bottle);

        GameRegistry.addRecipe(new ItemStack(itemFlash, 8),
                "G",
                "L",
                "B",

                'G', Items.gunpowder,
                'L', Items.glowstone_dust,
                'B', Items.glass_bottle);

        if (settings.goldenSpelunkerRecipe)
        {
            GameRegistry.addShapelessRecipe(new ItemStack(itemGoldenStatue),
                    new ItemStack(Items.dye, 1, 11),
                    new ItemStack(Blocks.dirt));
        }
    }

    public static void modsLoaded()
    {
    }

    private static <T extends Item> ItemRegister<T> $(String name, T item)
    {
        return new ItemRegister<T>(name, item);
    }

    private static class ItemRegister<T extends Item>
    {
        T item;
        String uniqueName;
        String resourceName;

        public ItemRegister(String name, T item)
        {
            this.item = item;
            this.uniqueName = name;
            this.resourceName = name;
        }

        public ItemRegister<T> withResource(String name)
        {
            this.resourceName = name;
            return this;
        }

        public T register()
        {
            item.setUnlocalizedName(SpelunkerMod.resourceDomain + resourceName);
            item.setTextureName(SpelunkerMod.resourceDomain + resourceName);
            GameRegistry.registerItem(item, uniqueName);
            return item;
        }

    }


    public SpelunkerItem()
    {
        super();
        dropItemRegistry.add(this);
    }

    @Override
    public Item setUnlocalizedName(String par1Str)
    {
        this.setTextureName(par1Str);
        return super.setUnlocalizedName(par1Str);
    }

    public abstract void giveEffect(World world, SpelunkerPlayerMP spelunker);

    public int getRarity(SpelunkerDifficulty difficulty)
    {
        return 10;
    }

    public boolean spawnCheck(SpelunkerPlayerMP spelunker, SpelunkerDifficulty difficulty, boolean isDarkPlace, double posY)
    {
        return true;
    }

    public void animate() {}
}
