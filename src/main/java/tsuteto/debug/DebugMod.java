package tsuteto.debug;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mod(modid = "DEBUGMOD", name = "DEBUG MOD")
public class DebugMod
{
    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        Item testFood = new Item()
        {
            @Override
            public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
            {
                par3EntityPlayer.getFoodStats().addStats(-1, 0f);
                return par1ItemStack;
            }
            @Override
            public int getMaxItemUseDuration(ItemStack par1ItemStack)
            {
                return 1;
            }

            @Override
            public EnumAction getItemUseAction(ItemStack par1ItemStack)
            {
                return EnumAction.eat;
            }

            @Override
            public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
            {
                par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
                return par1ItemStack;
            }
        }
                .setCreativeTab(CreativeTabs.tabFood)
                .setUnlocalizedName("zeroFood");
        LanguageRegistry.instance().addNameForObject(testFood, "en_US", "Zero Food Level");
        LanguageRegistry.instance().addNameForObject(testFood, "ja_JP", "満腹度0");
    }
}
