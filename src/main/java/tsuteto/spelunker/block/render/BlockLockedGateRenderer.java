package tsuteto.spelunker.block.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import tsuteto.spelunker.block.BlockLockedGate;

@SideOnly(Side.CLIENT)
public class BlockLockedGateRenderer implements ISimpleBlockRenderingHandler
{
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
    {
        Tessellator tessellator = Tessellator.instance;

        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(block, 1));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(block, 1));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(block, 0));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(block, 0));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(block, 1));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(block, 1));
        tessellator.draw();

        int j = ((BlockLockedGate)block).getRenderColorOverlay(metadata);

        float f1 = (float)(j >> 16 & 255) / 255.0F;
        float f2 = (float)(j >> 8 & 255) / 255.0F;
        float f3 = (float)(j & 255) / 255.0F;
        GL11.glColor4f(f1, f2, f3, 1.0F);

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, BlockLockedGate.getFrontOverlayIcon());
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, BlockLockedGate.getFrontOverlayIcon());
        tessellator.draw();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        int color;
        Tessellator tessellator = Tessellator.instance;
        int meta = world.getBlockMetadata(x, y, z);
        int side = meta & 7;

        if (BlockLockedGate.isLocked(meta))
        {
            renderer.renderStandardBlock(block, x, y, z);

            // Render overlay
            int l = block.getMixedBrightnessForBlock(world, x, y, z);
            color = ((BlockLockedGate) block).colorMultiplierOverlay(world, x, y, z);
            this.setColorAO(color, renderer);
            tessellator.setBrightness(l);
            renderer.enableAO = true;

            this.renderFaceControlled(world, block, x, y, z, ForgeDirection.getOrientation(side), BlockLockedGate.getFrontOverlayIcon(), renderer);

            return true;
        }
        else
        {
            return false;
        }
    }

    public void setColorAO(int color, RenderBlocks renderer)
    {
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;

        renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = f;
        renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = f1;
        renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = f2;
    }

    public void renderFaceControlled(IBlockAccess world, Block block, int x, int y, int z, ForgeDirection dir, RenderBlocks renderer)
    {
        if (renderer.renderAllFaces || block.shouldSideBeRendered(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, dir.ordinal()))
        {
            this.renderFace(world, block, x, y, z, dir, renderer);
        }
    }

    public void renderFaceControlled(IBlockAccess world, Block block, int x, int y, int z, ForgeDirection dir, IIcon icon, RenderBlocks renderer)
    {
        if (renderer.renderAllFaces || block.shouldSideBeRendered(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, dir.ordinal()))
        {
            this.renderFace(block, x, y, z, dir, icon, renderer);
        }
    }

    public void renderFace(IBlockAccess world, Block block, int x, int y, int z, ForgeDirection dir, RenderBlocks renderer)
    {
        this.renderFace(block, x, y, z, dir, renderer.getBlockIcon(block, world, x, y, z, dir.ordinal()), renderer);
    }

    public void renderFace(Block block, int x, int y, int z, ForgeDirection dir, IIcon icon, RenderBlocks renderer)
    {
        switch (dir)
        {
            case DOWN:
                renderer.renderFaceYNeg(block, (double) x, (double) y, (double) z, icon);
                break;
            case UP:
                renderer.renderFaceYPos(block, (double) x, (double) y, (double) z, icon);
                break;
            case NORTH:
                renderer.renderFaceZNeg(block, (double) x, (double) y, (double) z, icon);
                break;
            case SOUTH:
                renderer.renderFaceZPos(block, (double) x, (double) y, (double) z, icon);
                break;
            case WEST:
                renderer.renderFaceXNeg(block, (double) x, (double) y, (double) z, icon);
                break;
            case EAST:
                renderer.renderFaceXPos(block, (double) x, (double) y, (double) z, icon);
                break;
        }
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return true;
    }

    @Override
    public int getRenderId()
    {
        return BlockLockedGate.renderId;
    }
}
