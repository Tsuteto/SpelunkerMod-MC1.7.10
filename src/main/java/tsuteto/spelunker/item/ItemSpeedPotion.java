package tsuteto.spelunker.item;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import tsuteto.spelunker.constants.SpelunkerDifficulty;
import tsuteto.spelunker.constants.SpelunkerPacketType;
import tsuteto.spelunker.network.SpelunkerPacketDispatcher;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

public class ItemSpeedPotion extends SpelunkerItem
{
    @Override
    public void giveEffect(World world, SpelunkerPlayerMP spelunker)
    {
        spelunker.player().addPotionEffect(new PotionEffect(Potion.moveSpeed.getId(), 600, 3));
        spelunker.setSpeedPotion(30 * 20);
        spelunker.playSound("spelunker:item", 1.0F, 1.0F);

        SpelunkerPacketDispatcher packet = new SpelunkerPacketDispatcher(SpelunkerPacketType.GOT_POTION);
        packet.sendPacketPlayer(spelunker.player());
    }

    @Override
    public boolean hasEffect(ItemStack par1ItemStack)
    {
        return true;
    }

    @Override
    public int getRarity(SpelunkerDifficulty difficulty)
    {
        return (difficulty == SpelunkerDifficulty.HARD) ? 15 : 10;
    }
}
