package rtg.world.biome.vanilla;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.common.extensions.IForgeBlockState;
import rtg.api.config.BiomeConfig;
import rtg.api.util.WorldUtil;
import rtg.api.world.RTGWorld;
import rtg.api.world.biome.RTGBiomeBase;
import rtg.api.world.gen.RTGChunkWrapper;
import rtg.api.world.surface.SurfaceBase;
import rtg.api.world.terrain.TerrainBase;
import rtg.api.world.terrain.heighteffect.GroundEffect;

import java.util.Random;

public class PlainsRTGBiome extends RTGBiomeBase {

  public PlainsRTGBiome() {
    super(Biomes.PLAINS);
  }

  @Override
  public void initDecos() {

  }

  @Override
  public TerrainBase initTerrain() {
    return new TerrainVanillaPlains();
  }

  @Override
  public SurfaceBase initSurface() {
    return new SurfaceVanillaPlains(getConfig(), Blocks.GRASS_BLOCK.getDefaultState(), Blocks.DIRT.getDefaultState());
  }

  @Override
  public void initConfig() {
  }

  public static class TerrainVanillaPlains extends TerrainBase {

    private GroundEffect groundEffect = new GroundEffect(4f);

    public TerrainVanillaPlains() {

    }

    @Override
    public float generateNoise(RTGWorld rtgWorld, int x, int y, float border, float river) {
      //return terrainPlains(x, y, simplex, river, 160f, 10f, 60f, 200f, 66f);
      return riverized(65f + groundEffect.added(rtgWorld, x, y), river);
    }
  }

  public static class SurfaceVanillaPlains extends SurfaceBase {

    public SurfaceVanillaPlains(BiomeConfig config, IForgeBlockState top, IForgeBlockState filler) {
      super(config, top, filler);
    }

    @Override
    public void paintTerrain(RTGChunkWrapper chunk, int i, int j, int x, int z, int depth, RTGWorld rtgWorld, float[] noise, float river, Biome[] base) {

      Random rand = rtgWorld.rand();
      float c = WorldUtil.Terrain.calcCliff(x, z, noise);
      boolean cliff = c > 1.4f;

      for (int k = 255; k > -1; k--) {
        BlockPos bp = new BlockPos(x, k, z);
        Block b = chunk.getBlockState(bp).getBlock();
        if (b == Blocks.AIR) {
          depth = -1;
        }
        else if (b == Blocks.STONE) {
          depth++;

          if (cliff) {
            if (depth > -1 && depth < 2) {
              if (rand.nextInt(3) == 0) {

                chunk.setBlockState(bp, hcCobble());
              }
              else {

                chunk.setBlockState(bp, hcStone());
              }
            }
            else if (depth < 10) {
              chunk.setBlockState(bp, hcStone());
            }
          }
          else {
            if (depth == 0 && k > 61) {
              chunk.setBlockState(bp, topBlock.getBlockState());
            }
            else if (depth < 4) {
              chunk.setBlockState(bp, fillerBlock.getBlockState());
            }
          }
        }
      }
    }
  }

}
