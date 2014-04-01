package tsuteto.spelunker.player;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import tsuteto.spelunker.SpelunkerMod;


public class SpelunkerNormalSP implements ISpelunkerExtraSP
{
    protected SpelunkerPlayerSP spelunker;
    protected EntityPlayerSP player;

    public SpelunkerNormalSP(SpelunkerPlayerSP spelunker)
    {
        this.spelunker = spelunker;
        this.player = spelunker.player();
    }

    @Override
    public void beforeOnLivingUpdate() {}

    @Override
    public void afterOnLivingUpdate() {}

    @Override
    public void onPlayerDigging()
    {
        spelunker.decreaseEnergy(SpelunkerMod.energyCostDig);
    }

    @Override
    public void onPlayerNotDigging() {}

    @Override
    public boolean onParticleCollision(Entity entity)
    {
        return false;
    }
}
