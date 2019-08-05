package rtg.world.biome.vanilla;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.common.extensions.IForgeBlockState;
import rtg.api.config.BiomeConfig;
import rtg.api.util.PlateauUtil;
import rtg.api.util.WorldUtil;
import rtg.api.world.RTGWorld;
import rtg.api.world.biome.RTGBiomeBase;
import rtg.api.world.gen.RTGChunkWrapper;
import rtg.api.world.surface.SurfaceBase;
import rtg.api.world.terrain.TerrainBase;

import java.util.Random;

public class ShatteredSavannaRTGBiome extends RTGBiomeBase {

  public static Biome biome = Biomes.SHATTERED_SAVANNA;
  public static Biome river = Biomes.RIVER;

  public ShatteredSavannaRTGBiome() {
    super(biome);
  }

  @Override
  public void initConfig() {
    this.getConfig().SURFACE_WATER_LAKE_MULT.set(0.0f);
    this.getConfig().ALLOW_SCENIC_LAKES.set(false);
    this.getConfig().addProperty(this.getConfig().ALLOW_CACTUS).set(true);
    this.getConfig().addProperty(this.getConfig().SURFACE_MIX_BLOCK).set("");
    this.getConfig().addProperty(this.getConfig().ALLOW_PLATEAU_MODIFICATIONS).set(true);
    this.getConfig().addProperty(this.getConfig().PLATEAU_GRADIENT_BLOCK_LIST).set(PlateauUtil.getSavannaPlateauBlocks());
  }

  @Override
  public TerrainBase initTerrain() {

    return new TerrainVanillaSavannaPlateau(true, 35f, 160f, 60f, 40f, 69f);
  }

  @Override
  public SurfaceBase initSurface() {

    return new SurfaceVanillaSavannaPlateau(getConfig(), Blocks.GRASS_BLOCK.getDefaultState(), Blocks.DIRT.getDefaultState(), 0);
  }

  @Override
  public void initDecos() {

  }

  public static class TerrainVanillaSavannaPlateau extends TerrainBase {

    private boolean booRiver;
    private float[] height;
    private int heightLength;
    private float strength;
    private float smooth;
    private float cWidth;
    private float cHeigth;
    private float cStrength;
    private float base;

    /*
     * Example parameters:
     *
     * allowed to generate rivers?
     * riverGen = true
     *
     * canyon jump heights
     * heightArray = new float[]{2.0f, 0.5f, 6.5f, 0.5f, 14.0f, 0.5f, 19.0f, 0.5f}
     *
     * strength of canyon jump heights
     * heightStrength = 35f
     *
     * canyon width (cliff to cliff)
     * canyonWidth = 160f
     *
     * canyon heigth (total heigth)
     * canyonHeight = 60f
     *
     * canyon strength
     * canyonStrength = 40f
     *
     */
    public TerrainVanillaSavannaPlateau(boolean riverGen, float heightStrength, float canyonWidth, float canyonHeight, float canyonStrength, float baseHeight) {

      booRiver = true;
      /*    Values come in pairs per layer. First is how high to step up.
       * 	Second is a value between 0 and 1, signifying when to step up.
       */
      height = new float[]{12.0f, 0.5f, 6f, 0.7f};
      strength = 10f;
      heightLength = height.length;
    }

    @Override
    public float generateNoise(RTGWorld rtgWorld, int x, int y, float border, float river) {

      return terrainPlateau(x, y, rtgWorld, river, height, border, strength, heightLength, 50f, true);
    }
  }

  public class SurfaceVanillaSavannaPlateau extends SurfaceBase {

    private int grassRaise = 0;
    private IForgeBlockState mixBlock;

    public SurfaceVanillaSavannaPlateau(BiomeConfig config, IForgeBlockState top, IForgeBlockState fill, int grassHeight) {

      super(config, top, fill);
      grassRaise = grassHeight;

      this.mixBlock = this.getConfigBlock(config.SURFACE_MIX_BLOCK.get(), Blocks.COARSE_DIRT.getDefaultState());
    }

    @Override
    public void paintTerrain(RTGChunkWrapper chunk, int i, int j, int x, int z, int depth, RTGWorld rtgWorld, float[] noise, float river, Biome[] base) {

      Random rand = rtgWorld.rand();
      float c = WorldUtil.Terrain.calcCliff(x, z, noise);
      boolean cliff = c > 1.3f;
      Block b;

      for (int k = 255; k > -1; k--) {
        b = chunk.getBlockState(new BlockPos(x, k, z)).getBlock();
        if (b == Blocks.AIR) {
          depth = -1;
        } else if (b == Blocks.STONE) {
          depth++;

          if (cliff) {
            if (biomeConfig.ALLOW_PLATEAU_MODIFICATIONS.get()) {
              chunk.setBlockState(x, k, z, PlateauUtil.getPlateauBand(rtgWorld, ShatteredSavannaRTGBiome.this, i, k, j));
            } else {
              if (depth > -1 && depth < 2) {
                if (rand.nextInt(3) == 0) {

                  chunk.setBlockState(x, k, z, hcCobble());
                } else {

                  chunk.setBlockState(x, k, z, hcStone());
                }
              } else if (depth < 10) {
                chunk.setBlockState(x, k, z, hcStone());
              }
            }
          } else {

            if (k > 74 + grassRaise) {
              if (depth == 0) {
                if (rand.nextInt(5) == 0) {
                  chunk.setBlockState(x, k, z, mixBlock);
                } else {
                  chunk.setBlockState(x, k, z, topBlock);
                }
              } else if (depth < 4) {
                chunk.setBlockState(x, k, z, fillerBlock);
              }
            } else if (depth == 0 && k > 61) {
              int r = (int) ((k - (62 + grassRaise)) / 2f);
              if (rand.nextInt(r + 2) == 0) {
                chunk.setBlockState(x, k, z, topBlock);
              } else if (rand.nextInt((int) (r / 2f) + 2) == 0) {
                chunk.setBlockState(x, k, z, mixBlock);
              } else {
                chunk.setBlockState(x, k, z, topBlock);
              }
            } else if (depth < 4) {
              chunk.setBlockState(x, k, z, fillerBlock);
            }
          }
        }
      }
    }
  }
}
