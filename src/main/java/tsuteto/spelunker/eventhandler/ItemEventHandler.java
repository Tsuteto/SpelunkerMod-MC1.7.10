package tsuteto.spelunker.eventhandler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.damage.SpelunkerDamageSource;
import tsuteto.spelunker.init.SpelunkerItems;
import tsuteto.spelunker.player.ISpelunkerPlayer;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

public class ItemEventHandler
{
    @SubscribeEvent
    public void onItemTossed(ItemTossEvent event) {
        EntityItem entity = event.entityItem;
        ItemStack stack = entity.getEntityItem();
        EntityPlayer player = event.player;

        if (stack.getItem() == SpelunkerItems.itemGoldenStatue)
        {
            ISpelunkerPlayer spelunker = SpelunkerMod.getSpelunkerPlayer(player);
            if (spelunker != null && spelunker.isHardcore())
            {
                spelunker.killInstantly(SpelunkerDamageSource.worthyItemDropped);
                entity.lifespan = 0;
            }
        }
    }

    @SubscribeEvent
    public void onEntityItemPickup(EntityItemPickupEvent event)
    {
        EntityItem entity = event.item;
        ItemStack stack = entity.getEntityItem();
        EntityPlayer player = event.entityPlayer;

        if (stack.getItem() == SpelunkerItems.itemGoldenStatue)
        {
            SpelunkerPlayerMP spelunker = SpelunkerMod.getSpelunkerPlayer(player);
            if (spelunker != null && spelunker.isHardcore())
            {
                spelunker.addGoldenSpelunker();
            }
        }
    }
}
