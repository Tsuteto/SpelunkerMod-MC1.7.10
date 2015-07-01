package tsuteto.spelunker.world.levelmapper;

import net.minecraft.world.World;
import tsuteto.spelunker.entity.EntityWaterLift;
import tsuteto.spelunker.world.gen.WorldGenSpelunkerLevel;

public class MapPieceWaterLift extends MapPieceEntity
{
    public MapPieceWaterLift(String name, int color)
    {
        super(name, color);
    }

    @Override
    public void place(WorldGenSpelunkerLevel gen, World world, int x, int y, int z, SpelunkerLevelMapper mapper, int mapX, int mapY)
    {
        EntityWaterLift waterLift = (EntityWaterLift)this.spawnEntity(world, "waterLift", x, y, z + 1, 0);
        if (waterLift != null)
        {
            int distance = 0;

            for (int py = mapY - 1; py > 0 && mapper.getMapPiece(mapX, py) == MapPieces.waterLiftPath; py--)
            {
                distance++;
            }

            waterLift.distance = distance;
        }
    }
}
