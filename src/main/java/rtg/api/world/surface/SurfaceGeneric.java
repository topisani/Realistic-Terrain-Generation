package rtg.api.world.surface;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.extensions.IForgeBlockState;
import rtg.api.config.BiomeConfig;
import rtg.api.world.RTGWorld;
import rtg.api.world.gen.RTGChunkWrapper;

import java.util.Random;


public class SurfaceGeneric extends SurfaceBase {

    public SurfaceGeneric(BiomeConfig config, IForgeBlockState top, IForgeBlockState filler) {

        super(config, top, filler);
    }

    @Override
    public void paintTerrain(RTGChunkWrapper chunk, int i, int j, int x, int z, int depth, RTGWorld rtgWorld, float[] noise, float river, Biome[] base) {

        Random rand = rtgWorld.rand();

        for (int k = 255; k > -1; k--) {
            Block b = chunk.getBlockState(new BlockPos(x, k, z)).getBlock();

            if (b == Blocks.AIR) {
                depth = -1;
            }
            else if (b == Blocks.STONE) {
                depth++;

                if (depth == 0 && k > 61) {
                    chunk.setBlockState(new BlockPos(x, k, z), topBlock.getBlockState(), false);
                }
                else if (depth < 4) {
                    chunk.setBlockState(new BlockPos(x, k, z), fillerBlock.getBlockState(), false);
                }
            }
        }
    }
}
