package tsuteto.spelunker.eventhandler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.achievement.AchievementMgr;
import tsuteto.spelunker.constants.SpelunkerPacketType;
import tsuteto.spelunker.damage.SpelunkerDamageSource;
import tsuteto.spelunker.data.SpelunkerPlayerInfo;
import tsuteto.spelunker.dimension.SpelunkerLevelManagerClient;
import tsuteto.spelunker.init.SpeAchievementList;
import tsuteto.spelunker.init.SpelunkerItems;
import tsuteto.spelunker.item.SpelunkerWorldItem;
import tsuteto.spelunker.network.SpelunkerPacketDispatcher;
import tsuteto.spelunker.player.ISpelunkerPlayer;
import tsuteto.spelunker.player.SpelunkerPlayerMP;
import tsuteto.spelunker.player.SpelunkerPlayerSP;
import tsuteto.spelunker.potion.SpelunkerPotion;
import tsuteto.spelunker.world.WorldProviderSpelunker;

/**
 * Handles events on Spelunker
 *
 * @author Tsuteto
 *
 */
public class PlayerEventHandler
{
    @SubscribeEvent
    public void onSmelting(PlayerEvent.ItemSmeltedEvent event)
    {
        EntityPlayer player = event.player;
        ItemStack item = event.smelting;

        if (!player.worldObj.isRemote)
        {
            SpelunkerPlayerMP spelunker = SpelunkerMod.getSpelunkerPlayer(player);
            if (spelunker != null)
            {
                // Obtained gold
                if (item.getItem() == Items.gold_ingot) {
                    // Passes twice when shift-clicking
                    spelunker.addSpelunkerScore(item.stackSize == 0 ? 1500 : 3000 * item.stackSize);
                }
            }
        }

        AchievementMgr.achieveSmeltingItem(item, player);
    }

    @SubscribeEvent
    public void onCrafting(PlayerEvent.ItemCraftedEvent event)
    {
        EntityPlayer player = event.player;
        ItemStack itemStack = event.crafting;
        AchievementMgr.achieveCraftingItem(itemStack, player);
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
    {
        //SpelunkerMod.sidedProxy.showSoundNotice(player);
        new SpelunkerPacketDispatcher(SpelunkerPacketType.CHECK_POTION_ID)
                .addInt(SpelunkerPotion.choked.id)
                .addInt(SpelunkerPotion.heatStroke.id)
                .sendPacketPlayer(event.player);
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event)
    {
        if (event.player.worldObj.isRemote)
        {
            SpelunkerPlayerSP spelunker = SpelunkerMod.getSpelunkerPlayer(event.player);
            if (spelunker != null)
            {
                spelunker.isReady = false;
                spelunker.isInitializing = false;
            }

            SpelunkerLevelManagerClient.unregisterAll();
        }
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event)
    {
        WorldProvider worldProviderFrom = DimensionManager.getProvider(event.fromDim);
        WorldProvider worldProviderTo = DimensionManager.getProvider(event.toDim);

        if (worldProviderFrom instanceof WorldProviderSpelunker || worldProviderTo instanceof WorldProviderSpelunker)
        {
            EntityPlayer player = event.player;
            SpelunkerPlayerMP spelunker = SpelunkerMod.getSpelunkerPlayer(player);

            if (!event.player.capabilities.isCreativeMode)
            {
                // Remove spelunker world items from player's inventory
                InventoryPlayer inventory = event.player.inventory;
                for (int i = 0; i < inventory.mainInventory.length; i++)
                {
                    if (inventory.mainInventory[i] != null && inventory.mainInventory[i].getItem() instanceof SpelunkerWorldItem)
                    {
                        inventory.mainInventory[i] = null;
                    }
                }

                // Entering Spelunker World
                if (worldProviderTo instanceof WorldProviderSpelunker)
                {
                    // Give a blaster
                    event.player.inventory.addItemStackToInventory(new ItemStack(SpelunkerItems.itemGunSpeWorld));
                    AchievementMgr.achieve(event.player, SpeAchievementList.Key.speWorld);
                }
            }

            SpelunkerPlayerInfo worldInfo = spelunker.getWorldInfo();
            if (worldProviderTo instanceof WorldProviderSpelunker)
            {
                // Set info
                worldInfo.createSpeLevelInfo(player);
                SpelunkerPacketDispatcher.of(SpelunkerPacketType.DIM_CHANGE)
                        .addLong(player.worldObj.getTotalWorldTime());
            }
            else
            {
                // Discard info
                worldInfo.discardSpeLevelInfo();
                SpelunkerPacketDispatcher.of(SpelunkerPacketType.DIM_CHANGE)
                        .addLong(-1);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event)
    {
        EntityPlayer player = event.player;

        if (!player.worldObj.isRemote)
        {
            SpelunkerPlayerMP spelunker = SpelunkerMod.getSpelunkerPlayer(player);

            if (!MinecraftServer.getServer().isSinglePlayer() && SpelunkerMod.isGameToBeFinished())
            {
                spelunker.banByGameover();
            }

            if (spelunker != null)
            {
                EntityPlayerMP deadPlayer = SpelunkerMod.deadPlayerStorage.get(player.getUniqueID());

                if (deadPlayer != null)
                {
                    spelunker.onRespawn(deadPlayer);
                    SpelunkerMod.deadPlayerStorage.remove(player.getUniqueID());
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerDestroyItem(PlayerDestroyItemEvent event)
    {
        ISpelunkerPlayer spelunker = SpelunkerMod.getSpelunkerPlayer(event.entityPlayer);
        if (spelunker != null && spelunker.isHardcore())
        {
            Item item = event.original.getItem();
            if (item != null && item.isDamageable() && !(item instanceof ItemArmor))
            {
                EntityPlayer player = event.entityPlayer;
                player.attackEntityFrom(SpelunkerDamageSource.itemDestroy, 1.0f);
            }
        }
    }
}
