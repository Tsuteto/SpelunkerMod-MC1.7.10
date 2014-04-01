package tsuteto.spelunker.player;

import net.minecraft.entity.Entity;


public class SpelunkerExtraBlankSP implements ISpelunkerExtraSP
{
    @Override
    public void beforeOnLivingUpdate() {}
    @Override
    public void afterOnLivingUpdate() {}
    @Override
    public void onPlayerDigging() {}
    @Override
    public void onPlayerNotDigging() {}
    @Override
    public boolean onParticleCollision(Entity entity)
    {
        return false;
    }
}
