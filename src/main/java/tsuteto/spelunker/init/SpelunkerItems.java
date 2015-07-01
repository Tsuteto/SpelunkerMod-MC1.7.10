package tsuteto.spelunker.init;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.item.*;

/**
 * Created by Tsuteto on 15/06/30.
 */
public class SpelunkerItems
{
    public static Item itemEnergy;
    public static Item itemSpeedPotion;
    public static Item itemDollar;
    public static Item item1up;
    public static Item itemCoin;
    public static Item itemMiracle;
    public static Item item2xScore;
    public static Item itemInvincible;
    public static Item itemDynamiteDrop;
    public static Item itemFlashDrop;
    public static Item itemGunWood;
    public static Item itemGunSteel;
    public static Item itemGunSpelunker;
    public static Item itemGunSpeWorld;
    public static Item itemHelmet;
    public static Item itemDynamite;
    public static Item itemFlash;
    public static Item itemGoldenStatue;
    public static Item itemLevelBuilder;
    public static Item itemEntityPlacer;
    public static Item itemGateKey;
    public static Item itemGateKeyDrop;
    public static Item itemMapPieceGuide;
    public static Item itemHiddenItemPointPlacer;
    public static Item itemSpelunkart;
    public static Item itemSpeBoat;

    public static void load()
    {
        // Spelunker Drop Items
        itemEnergy = $("energy", new ItemEnergy()).register();
        itemSpeedPotion = $("speedPotion", new ItemSpeedPotion()).register();
        itemDollar = $("dollarBag", new ItemDoller()).register();
        item1up = $("1up", new Item1up()).register();
        itemCoin = $("coin", new ItemCoin()).register();
        itemMiracle = $("miracle", new ItemMiracle()).register();
        item2xScore = $("2xScore", new Item2xScore()).register();
        itemInvincible = $("invincible", new ItemInvincible()).register();
        itemDynamiteDrop = $("dynamiteDrop", new ItemDynamiteDrop()).register();
        itemFlashDrop = $("flashDrop", new ItemFlashDrop()).register();

        // Guns
        itemGunWood = $("woodenGun", new ItemGun(EnumGunMaterial.WOOD))
                .register()
                .setFull3D();
        itemGunSteel = $("steelGun", new ItemGun(EnumGunMaterial.IRON))
                .register()
                .setFull3D();
        itemGunSpelunker = $("spelunkerGun", new ItemGun(EnumGunMaterial.ORIGINAL))
                .register()
                .setFull3D();
        itemGunSpeWorld = $("speWorldGun", new ItemSpeWorldBlaster())
                .withResource("spelunkerGun")
                .register()
                .setFull3D()
                .setCreativeTab(null);

        // Tools
        itemDynamite = $("dynamite", new ItemDynamite()).register().setCreativeTab(CreativeTabs.tabTools);
        itemFlash = $("flash", new ItemFlash()).register().setCreativeTab(CreativeTabs.tabTools);

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

        // Level Construction Components
        itemLevelBuilder = $("levelBuilder", new ItemLevelBuilder()).register()
                .setCreativeTab(SpelunkerMod.tabLevelComponents);
        itemEntityPlacer = $("entityPlacer", new ItemEntityPlacer()).register()
                .setCreativeTab(SpelunkerMod.tabLevelComponents);

        itemGateKey = $("gateKey", new ItemGateKey()).register();

        itemGateKeyDrop = $("gateKeyDrop", new ItemGateKeyDrop())
                .withResource("gateKey")
                .register()
                .setCreativeTab(SpelunkerMod.tabLevelComponents);

        itemMapPieceGuide = $("mapPieceGuide", new ItemMapPieceGuide())
                .register()
                .setCreativeTab(SpelunkerMod.tabLevelComponents);

        itemHiddenItemPointPlacer = $("hispPlacer", new ItemHiddenItemPointPlacer())
                .register()
                .setCreativeTab(SpelunkerMod.tabLevelComponents);

        itemSpelunkart = $("spelunkart", new ItemSpelunkart())
                .register()
                .setCreativeTab(SpelunkerMod.tabLevelComponents);

        itemSpeBoat = $("speBoat", new ItemSpeBoat())
                .register()
                .setCreativeTab(SpelunkerMod.tabLevelComponents);

        Item.ToolMaterial killer = EnumHelper.addToolMaterial("KILLER", 3, 0xffff, 8.0F, 32768F, 255);

        $("enderkiller", new ItemSword(killer)
        {
            @Override
            public int getColorFromItemStack(ItemStack p_82790_1_, int p_82790_2_)
            {
                return 0xff0000;
            }
        }).register()
        .setTextureName("minecraft:diamond_sword")
        .setCreativeTab(CreativeTabs.tabCombat);

        // Register dispense behavior
        BlockDispenser.dispenseBehaviorRegistry.putObject(itemFlash, new BehaviorDispenseFlash());
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
}
