package tsuteto.spelunker.init;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import tsuteto.spelunker.Settings;
import tsuteto.spelunker.SpelunkerMod;

public class SpelunkerRecipes
{
    public static void load()
    {
        Settings settings = SpelunkerMod.settings();

        GameRegistry.addRecipe(new ItemStack(SpelunkerItems.itemGunWood),
                "XXX",
                "  X",

                'X', Blocks.planks);

        GameRegistry.addRecipe(new ItemStack(SpelunkerItems.itemGunSteel),
                "XX#",
                "  Y",

                'X', Items.iron_ingot,
                'Y', Blocks.planks,
                '#', new ItemStack(Items.dye, 1, 4));

        GameRegistry.addRecipe(new ItemStack(SpelunkerItems.itemGunSpelunker),
                "XX#",
                "  /",

                'X', Items.iron_ingot,
                '/', Items.blaze_rod,
                '#', Items.ender_pearl);

        GameRegistry.addRecipe(new ItemStack(SpelunkerItems.itemHelmet, 1),
                "HCG",

                'H', Items.iron_helmet,
                'C', new ItemStack(Items.coal, 1, 0),
                'G', Blocks.glass_pane);

        GameRegistry.addRecipe(new ItemStack(SpelunkerItems.itemFlash, 8),
                "L",
                "G",
                "B",

                'L', Items.glowstone_dust,
                'G', Items.gunpowder,
                'B', Items.glass_bottle);

        GameRegistry.addRecipe(new ItemStack(SpelunkerItems.itemFlash, 8),
                "G",
                "L",
                "B",

                'G', Items.gunpowder,
                'L', Items.glowstone_dust,
                'B', Items.glass_bottle);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SpelunkerBlocks.blockSpelunkerPortal2),
                "R",
                "B",
                "I",

                'R', "dyeRed",
                'B', "dyeBlue",
                'I', "blockIron"
        ));

        if (settings.goldenSpelunkerRecipe)
        {
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(SpelunkerItems.itemGoldenStatue),
                    "dyeYellow",
                    new ItemStack(Blocks.dirt)
            ));
        }
    }
}
