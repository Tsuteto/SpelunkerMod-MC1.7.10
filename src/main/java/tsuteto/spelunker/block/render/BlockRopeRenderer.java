package tsuteto.spelunker.block.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import tsuteto.spelunker.block.BlockRope;

@SideOnly(Side.CLIENT)
public class BlockRopeRenderer implements ISimpleBlockRenderingHandler
{
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
    {
        Tessellator tessellator = Tessellator.instance;
        block.setBlockBoundsForItemRender();
        //GL11.glTranslatef(-0.2F, -0.2F, -0.2F);
        float f1 = 0.0625F;
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(block, 0));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(block, 1));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        tessellator.addTranslation(0.0F, 0.0F, f1);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(block, 2));
        tessellator.addTranslation(0.0F, 0.0F, -f1);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        tessellator.addTranslation(0.0F, 0.0F, -f1);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(block, 3));
        tessellator.addTranslation(0.0F, 0.0F, f1);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        tessellator.addTranslation(f1, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(block, 4));
        tessellator.addTranslation(-f1, 0.0F, 0.0F);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        tessellator.addTranslation(-f1, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(block, 5));
        tessellator.addTranslation(f1, 0.0F, 0.0F);
        tessellator.draw();
        //GL11.glTranslatef(0.2F, 0.2F, 0.2F);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        Tessellator tessellator = Tessellator.instance;
        float f19 = 0.0625F;
        int l = block.getMixedBrightnessForBlock(world, x, y, z);
        tessellator.setBrightness(l);

        if (renderer.renderAllFaces || block.shouldSideBeRendered(world, x, y - 1, z, 0))
        {
            renderer.renderFaceYNeg(block, (double)x, (double)y, (double)z, renderer.getBlockIcon(block, world, x, y, z, 0));
        }

        if (renderer.renderAllFaces || block.shouldSideBeRendered(world, x, y + 1, z, 1))
        {
            renderer.renderFaceYPos(block, (double)x, (double)y, (double)z, renderer.getBlockIcon(block, world, x, y, z, 1));
        }

        tessellator.addTranslation(0.0F, 0.0F, f19);
        renderer.renderFaceZNeg(block, (double)x, (double)y, (double)z, renderer.getBlockIcon(block, world, x, y, z, 2));
        tessellator.addTranslation(0.0F, 0.0F, -f19);
        tessellator.addTranslation(0.0F, 0.0F, -f19);
        renderer.renderFaceZPos(block, (double)x, (double)y, (double)z, renderer.getBlockIcon(block, world, x, y, z, 3));
        tessellator.addTranslation(0.0F, 0.0F, f19);
        tessellator.addTranslation(f19, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, (double)x, (double)y, (double)z, renderer.getBlockIcon(block, world, x, y, z, 4));
        tessellator.addTranslation(-f19, 0.0F, 0.0F);
        tessellator.addTranslation(-f19, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, (double)x, (double)y, (double)z, renderer.getBlockIcon(block, world, x, y, z, 5));
        tessellator.addTranslation(f19, 0.0F, 0.0F);
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return true;
    }

    @Override
    public int getRenderId()
    {
        return BlockRope.renderIdRope;
    }
}
