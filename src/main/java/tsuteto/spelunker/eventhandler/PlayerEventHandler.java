package tsuteto.spelunker.eventhandler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.constants.SpelunkerPacketType;
import tsuteto.spelunker.damage.SpelunkerDamageSource;
import tsuteto.spelunker.dimension.SpelunkerLevelManagerClient;
import tsuteto.spelunker.item.SpelunkerItem;
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
        WorldProvider worldProvider1 = DimensionManager.getProvider(event.fromDim);
        WorldProvider worldProvider2 = DimensionManager.getProvider(event.toDim);

        // Remove gate keys from player's inventory
        if (worldProvider1 instanceof WorldProviderSpelunker || worldProvider2 instanceof WorldProviderSpelunker)
        {
            InventoryPlayer inventory = event.player.inventory;
            for (int i = 0; i < inventory.mainInventory.length; i++)
            {
                if (inventory.mainInventory[i] != null && inventory.mainInventory[i].getItem() == SpelunkerItem.itemGateKey)
                {
                    inventory.mainInventory[i] = null;
                }
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
            if (item != null && item.isDamageable())
            {
                EntityPlayer player = event.entityPlayer;
                player.attackEntityFrom(SpelunkerDamageSource.itemDestroy, 1.0f);
            }
        }
    }
}
