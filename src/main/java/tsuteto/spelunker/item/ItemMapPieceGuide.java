package tsuteto.spelunker.item;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tsuteto.spelunker.gui.GuiMapPieceBook;

public class ItemMapPieceGuide extends Item
{
    @SidedProxy(serverSide = "tsuteto.spelunker.item.ItemMapPieceGuide$GuiProxyCommon", clientSide = "tsuteto.spelunker.item.ItemMapPieceGuide$GuiProxyClient")
    private static GuiProxyCommon guiProxy;

    @Override
    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_)
    {
        if (p_77659_2_.isRemote)
        {
            guiProxy.openGui(p_77659_1_, p_77659_2_, p_77659_3_);
        }
        return p_77659_1_;
    }

    public static class GuiProxyCommon
    {
        public void openGui(ItemStack stack, World world, EntityPlayer player) {}
    }

    @SideOnly(Side.CLIENT)
    public static class GuiProxyClient extends GuiProxyCommon
    {
        @Override
        public void openGui(ItemStack stack, World world, EntityPlayer player)
        {
            Minecraft mc = FMLClientHandler.instance().getClient();
            mc.displayGuiScreen(new GuiMapPieceBook(player, stack));
        }
    }
}
