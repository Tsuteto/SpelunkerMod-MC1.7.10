package tsuteto.spelunker.player;

import net.minecraft.entity.Entity;


public interface ISpelunkerExtraSP
{
    void beforeOnLivingUpdate();
    void afterOnLivingUpdate();
    void onPlayerDigging();
    void onPlayerNotDigging();
    boolean onParticleCollision(Entity entity);
}
