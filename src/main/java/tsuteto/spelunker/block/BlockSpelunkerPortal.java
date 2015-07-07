package tsuteto.spelunker.block;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.block.tileentity.TileEntitySpelunkerPortalStage;
import tsuteto.spelunker.block.tileentity.TileEntitySpelunkerPortalStatue;
import tsuteto.spelunker.dimension.DimensionRegenerator;
import tsuteto.spelunker.dimension.SpelunkerDimensionTeleportation;
import tsuteto.spelunker.gui.SpelunkerGuiHandler;
import tsuteto.spelunker.init.SpelunkerItems;
import tsuteto.spelunker.levelmap.SpelunkerMapInfo;
import tsuteto.spelunker.levelmap.SpelunkerMapManager;
import tsuteto.spelunker.util.PlayerUtils;
import tsuteto.spelunker.world.WorldProviderSpelunker;

import java.util.ArrayList;

public class BlockSpelunkerPortal extends BlockContainer4Directions
{
    public enum Type
    {
        SURVIVAL, CUSTOM
    }

    public final Type type;

    public BlockSpelunkerPortal(Type type)
    {
        super(Material.iron);
        this.type = type;
        this.setBlockBounds(
                0.1825F, 0.0F, 0.1875F,
                0.8125F, 1.0F, 0.8125F);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        if (player instanceof EntityPlayerMP)
        {
            TileEntitySpelunkerPortalStage te = this.getTileEntity(world, x, y, z);
            if (player.dimension == 0)
            {
                if (this.type == Type.CUSTOM)
                {
                    if (te.levelInfo == null)
                    {
                        this.openMapSelectorGui(world, te.xCoord, te.yCoord, te.zCoord, player, te);
                    }
                    else
                    {
                        if (player.isSneaking())
                        {
                            this.openMapSelectorGui(world, te.xCoord, te.yCoord, te.zCoord, player, te);
                        }
                        else
                        {
                            teleportPlayerToLevel(world, te.xCoord, te.yCoord, te.zCoord, player, te);
                        }
                    }
                }
                else if (this.type == Type.SURVIVAL)
                {
                    if (te.levelInfo == null)
                    {
                        int dimId = DimensionManager.getNextFreeDimId();
                        SpelunkerMapInfo info = SpelunkerMapManager.SPELUNKER_MAP;
                        te.registerLevel(dimId, info);
                    }
                    else
                    {
                        if (this.canRegenerate(te.levelInfo.dimId))
                        {
                            DimensionRegenerator.regenerateDimension(te.levelInfo.dimId);
                        }
                    }
                    teleportPlayerToLevel(world, te.xCoord, te.yCoord, te.zCoord, player, te);
                }
                return true;
            }
            else
            {
                this.openLeaveSpeWorldGui(world, te.xCoord, te.yCoord, te.zCoord, player, te);
            }
            return true;
        }
        else
        {
            return true;
        }

    }

    public boolean canRegenerate(int dimId)
    {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        int playerCount = 0;
        for (Object obj : server.getConfigurationManager().playerEntityList)
        {
            EntityPlayerMP player = (EntityPlayerMP)obj;

            if (player.dimension == dimId)
            {
                playerCount++;
            }
        }
        return playerCount == 0;
    }

    public void openMapSelectorGui(World world, int x, int y, int z, EntityPlayer player, TileEntitySpelunkerPortalStage te)
    {
        if (te != null)
        {
            if (te.isOwner(player))
            {
                player.openGui(SpelunkerMod.instance, SpelunkerGuiHandler.GUIID_MAP_SELECTOR, world, x, y, z);
            }
            else
            {
                EntityLivingBase ownerEntity = te.getOwner();
                if (ownerEntity != null)
                {
                    player.addChatComponentMessage(new ChatComponentTranslation("tile.spelunker:spelunkerPortal.denied", ownerEntity.getCommandSenderName()));
                }
                else if (te.ownerName != null && !te.ownerName.isEmpty())
                {
                    player.addChatComponentMessage(new ChatComponentTranslation("tile.spelunker:spelunkerPortal.denied", te.ownerName));
                }
                else
                {
                    player.addChatComponentMessage(new ChatComponentTranslation("tile.spelunker:spelunkerPortal.denied.unknown"));
                }
            }
        }
    }

    public void openLeaveSpeWorldGui(World world, int x, int y, int z, EntityPlayer player, TileEntitySpelunkerPortalStage te)
    {
        if (te != null)
        {
            player.openGui(SpelunkerMod.instance, SpelunkerGuiHandler.GUIID_LEAVE_SPE_WORLD, world, x, y, z);
        }
    }

    public static void teleportPlayerToLevel(World world, int x, int y, int z, EntityPlayer player, TileEntitySpelunkerPortalStage te)
    {
        if (!player.capabilities.isCreativeMode)
        {
            if (!checkInventory(player))
            {
                return;
            }
        }

        SpelunkerMod.levelManager().syncLevel(te.levelInfo.dimId, player);
        PlayerUtils.updatePlayerSpawnPoint(world, x, y, z, player);
        SpelunkerDimensionTeleportation.transferPlayerToDimension((EntityPlayerMP) player, te.levelInfo.dimId);
    }

    public static boolean checkInventory(EntityPlayer player)
    {
        // Inventory
        boolean isInventoryValid = true;
        for (ItemStack itemStack : player.inventory.mainInventory)
        {
            if (itemStack != null && itemStack.getItem() != SpelunkerItems.itemGoldenStatue)
            {
                isInventoryValid = false;
                break;
            }
        }

        if (!isInventoryValid)
        {
            player.addChatComponentMessage(new ChatComponentTranslation("tile.spelunker:spelunkerPortal.denied.inventory"));
        }

        // Armor
        boolean isArmorValid = true;
        for (int i = 0; i < player.inventory.armorInventory.length; i++)
        {
            ItemStack itemStack = player.inventory.armorInventory[i];
//            if (i == 3)
//            {
//                if (itemStack != null && itemStack.getItem() != SpelunkerItems.itemHelmet)
//                {
//                    isArmorValid = false;
//                }
//            }
//            else
//            {
//                if (itemStack != null) isArmorValid = false;
//            }
            if (itemStack != null) isArmorValid = false;
        }
        if (!isArmorValid)
        {
            player.addChatComponentMessage(new ChatComponentTranslation("tile.spelunker:spelunkerPortal.denied.armor"));
        }

        return isInventoryValid && isArmorValid;
    }


    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemStack)
    {
        super.onBlockPlacedBy(world, x, y, z, entityLivingBase, itemStack);

        world.setBlock(x, y + 1, z, this, 8 | world.getBlockMetadata(x, y, z), 2);
        world.notifyBlocksOfNeighborChange(x, y + 1, z, this);

        if (!world.isRemote)
        {
            TileEntitySpelunkerPortalStage te = this.getTileEntity(world, x, y, z);
            if (itemStack.getItemDamage() != 0)
            {
                te.levelInfo = SpelunkerMod.levelManager().getLevelInfo(itemStack.getItemDamage());
            }
            if (this.type == Type.CUSTOM
                    && entityLivingBase instanceof EntityPlayer)
            {
                te.setOwner((EntityPlayer) entityLivingBase);
            }
        }
    }

    @Override
    public void onBlockHarvested(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer)
    {
        int bodyPosY = (par5 & 8) == 0 ? par3 : par3 - 1;

        if (par6EntityPlayer.capabilities.isCreativeMode)
        {
            // Set 8 for no drops
            par5 |= 8;
            par1World.setBlockMetadataWithNotify(par2, bodyPosY, par4, par5, 4);
        }

        // Force to drop items here before removing tileentity
        dropBlockAsItem(par1World, par2, bodyPosY, par4, par5, 0);
        super.onBlockHarvested(par1World, par2, par3, par4, par5, par6EntityPlayer);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

        if ((metadata & 8) == 0) // no drops when the metadata is 8
        {
            // Called twice, but drops items once when the tileentity is alive. Based on BlockSkull
            TileEntitySpelunkerPortalStage te = this.getTileEntity(world, x, y, z);
            if (te != null)
            {
                Item item = getItemDropped(metadata, world.rand, fortune);
                if (item != null)
                {
                    ItemStack stack;
                    if (te.levelInfo != null)
                    {
                        stack = new ItemStack(item, 1, te.levelInfo.dimId);
                    }
                    else
                    {
                        stack = new ItemStack(item, 1, 0);
                    }
                    ret.add(stack);
                }
            }
        }
        return ret;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        Item item = Item.getItemFromBlock(this);
        TileEntitySpelunkerPortalStage te = this.getTileEntity(world, x, y, z);
        if (te.levelInfo != null)
        {
            return new ItemStack(item, 1, te.levelInfo.dimId);
        }
        else
        {
            return new ItemStack(item, 1, 0);
        }
    }

    public TileEntitySpelunkerPortalStage getTileEntity(IBlockAccess blockAccess, int x, int y, int z)
    {
        return (TileEntitySpelunkerPortalStage)blockAccess.getTileEntity(x, isBody(blockAccess, x, y, z) ? y : y - 1, z);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return (p_149915_2_ & 8) == 0 ? new TileEntitySpelunkerPortalStage() : new TileEntitySpelunkerPortalStatue();
    }

    public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_)
    {
        if (isBody(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_))
        {
            if (p_149695_1_.getBlock(p_149695_2_, p_149695_3_ + 1, p_149695_4_) != this)
            {
                if (!p_149695_1_.isRemote)
                {
                    this.dropBlockAsItem(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, 0, 0);
                }
                p_149695_1_.setBlockToAir(p_149695_2_, p_149695_3_, p_149695_4_);
            }
        }
        else
        {
            if (p_149695_1_.getBlock(p_149695_2_, p_149695_3_ - 1, p_149695_4_) != this)
            {
                p_149695_1_.setBlockToAir(p_149695_2_, p_149695_3_, p_149695_4_);
            }

            if (p_149695_5_ != this)
            {
                this.onNeighborBlockChange(p_149695_1_, p_149695_2_, p_149695_3_ - 1, p_149695_4_, p_149695_5_);
            }
        }
    }

    public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_)
    {
//        if (isBody(p_149719_1_, p_149719_2_, p_149719_3_, p_149719_4_))
//        {
//            this.setBlockBounds(
//                    0.1825F, 0.0F, 0.1875F,
//                    0.8125F, 1.0F, 0.8125F);
//        }
//        else
//        {
//            int l = p_149719_1_.getBlockMetadata(p_149719_2_, p_149719_3_, p_149719_4_) & 3;
//
//            if (l != 3 && l != 1)
//            {
//                this.setBlockBounds(
//                        0.0625F * 7.0F, 0.0F, 0.1875F,
//                        0.0625F * 9.0F, 1.0F, 0.8125F);
//            }
//            else
//            {
//                this.setBlockBounds(
//                        0.1875F, 0.0F, 0.0625F * 7.0F,
//                        0.8125F, 1.0F, 0.0625F * 9.0F);
//            }
//        }
    }

    public float getBlockHardness(World world, int x, int y, int z)
    {
        if (world.provider instanceof WorldProviderSpelunker)
        {
            return -1;
        }
        else
        {
            return this.blockHardness;
        }
    }


    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    public static boolean isBody(IBlockAccess blockAccess, int x, int y, int z)
    {
        return (blockAccess.getBlockMetadata(x, y, z) & 8) == 0;
    }

    @Override
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.blockIcon = p_149651_1_.registerIcon(
                SpelunkerMod.resourceDomain + "spelunkerPortal_" + this.type.name().toLowerCase());
    }
}
