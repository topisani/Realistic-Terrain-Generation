package rtg.world.gen;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.chunk.UpgradeData;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.OverworldChunkGenerator;
import rtg.api.RTGAPI;
import rtg.api.util.LimitedArrayCacheMap;
import rtg.api.util.WorldUtil;
import rtg.api.world.RTGWorld;
import rtg.api.world.biome.IRTGBiome;
import rtg.api.world.gen.RTGChunkGenSettings;
import rtg.api.world.gen.RTGChunkWrapper;
import rtg.api.world.terrain.TerrainBase;
import rtg.world.biome.BiomeAnalyzer;

import java.util.Random;

public class RTGChunkGenerator extends ChunkGenerator<RTGChunkGenSettings> {

  public final RTGWorld rtgWorld;
  private final OverworldChunkGenerator vanillaChunkGenerator;
  private final LimitedArrayCacheMap<ChunkPos, ChunkLandscape> landscapeCache = new LimitedArrayCacheMap<>(1024);// cache ChunkLandscape objects
  private final LimitedArrayCacheMap<ChunkPos, float[]> vanillaHeightsCache = new LimitedArrayCacheMap<>(1024);// cache ChunkLandscape objects
  private final int sampleSize = 8;
  private final int sampleArraySize = sampleSize * 2 + 5;
  private final int[] biomeData = new int[sampleArraySize * sampleArraySize];
  private final float[] weightedBiomes = new float[256];
  private final float[][] weightings = new float[sampleArraySize * sampleArraySize][256];
  private final BiomeAnalyzer analyzer = new BiomeAnalyzer();
  private final Random rand;

  public RTGChunkGenerator(IWorld worldIn, BiomeProvider biomeProviderIn, RTGChunkGenSettings generationSettingsIn) {
    super(worldIn, biomeProviderIn, generationSettingsIn);
    // TODO: Map our settings to vanilla settings, or extend vanilla settings
    rtgWorld = RTGWorld.getInstance(worldIn.getWorld());
    this.rand = new Random(rtgWorld.seed());
    this.rtgWorld.setRandom(this.rand);
    vanillaChunkGenerator = new OverworldChunkGenerator(worldIn, biomeProviderIn, ChunkGeneratorType.SURFACE.createSettings());
    setWeightings();
  }

  @Override
  public void generateSurface(IChunk chunkIn) {

    boolean hasNonRTGBiomes =
        BlockPos.getAllInBox(0, 0, 0, 15, 0, 15).anyMatch((p) -> !rtgWorld.getBiomeRegistry().hasRTGBiomeFor(chunkIn.getBiome(p)));

    if (hasNonRTGBiomes) {
      vanillaChunkGenerator.generateSurface(chunkIn);
    }

    BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();
    ChunkLandscape landscape = getLandscape(biomeProvider, chunkIn.getPos());
    RTGChunkWrapper rtgChunk = new RTGChunkWrapper(chunkIn);
    for (int x = 0; x < 16; x++) {
      for (int z = 0; z < 16; z++) {
        mpos.setPos(chunkIn.getPos().getXStart() + x, 0, chunkIn.getPos().getZStart() + z);

        float river = -TerrainBase.getRiverStrength(mpos, rtgWorld);
        int depth = -1;
        IRTGBiome rtgBiome = rtgWorld.getBiomeRegistry().getRTGBiomeFor(landscape.biomes[x * 16 + z]);
        if (rtgBiome != null) {
          rtgBiome.rReplace(rtgChunk, mpos, x, z, depth, rtgWorld, landscape.noise, river, landscape.biomes);
        }

      }
    }
  }

  @Override
  public int getGroundHeight() {
    return vanillaChunkGenerator.getGroundHeight();
  }

  @Override
  public void makeBase(IWorld worldIn, IChunk chunkIn) {
    BlockPos.MutableBlockPos bp = new BlockPos.MutableBlockPos();

    boolean hasNonRTGBiomes =
        BlockPos.getAllInBox(0, 0, 0, 15, 0, 15).anyMatch((p) -> !rtgWorld.getBiomeRegistry().hasRTGBiomeFor(chunkIn.getBiome(p)));

    if (hasNonRTGBiomes) {
      // generating vanilla heights also generates the actual landscape when given a real chunkprimer.
      //vanillaChunkGenerator.makeBase(worldIn, chunkIn);
    }
    vanillaChunkGenerator.makeBase(worldIn, chunkIn);

    final ChunkLandscape landscape = getLandscape(biomeProvider, chunkIn.getPos());
    for (int x = 0; x < 16; ++x) {
      for (int z = 0; z < 16; ++z) {
        bp.setPos(x, 0, z);
        if (landscape.vanillaWeight[x * 16 + z] < 1) {
          generateTerrain(new RTGChunkWrapper(chunkIn), x, z, landscape.noise);
        }
      }
    }
    Biome[] xyinverted = new Biome[256];
    for (int i = 0; i < 256; ++i) {
      xyinverted[i] = landscape.biomes[BiomeAnalyzer.XYINVERTER[i]];
    }
    chunkIn.setBiomes(xyinverted);
  }

  private void setWeightings() {
    for (int x = 0; x < 16; x++) {
      for (int z = 0; z < 16; z++) {
        float limit = (float) Math.pow((56f * 56f), 0.7D);
        for (int mapX = 0; mapX < sampleArraySize; mapX++) {
          for (int mapZ = 0; mapZ < sampleArraySize; mapZ++) {
            float xDist = (x - (mapX - sampleSize) * 8);
            float zDist = (z - (mapZ - sampleSize) * 8);
            float distanceSquared = xDist * xDist + zDist * zDist;
            float distance = (float) Math.pow(distanceSquared, 0.7D);
            float weight = 1f - distance / limit;
            if (weight < 0) {
              weight = 0;
            }
            weightings[mapX * sampleArraySize + mapZ][x * 16 + z] = weight;
          }
        }
      }
    }
  }

  public ChunkLandscape getLandscape(final BiomeProvider biomeProvider, final ChunkPos chunkPos) {
    final BlockPos blockPos = new BlockPos(chunkPos.x * 16, 0, chunkPos.z * 16);
    ChunkLandscape landscape = landscapeCache.get(chunkPos);
    if (landscape == null) {
      landscape = generateLandscape(biomeProvider, blockPos);
      landscapeCache.put(chunkPos, landscape);
    }
    return landscape;
  }

  private ChunkLandscape generateLandscape(BiomeProvider biomeProvider, BlockPos blockPos) {
    final ChunkLandscape landscape = new ChunkLandscape();
    getNewerNoise(biomeProvider, blockPos.getX(), blockPos.getZ(), landscape);
    Biome[] biomes = new Biome[256];
    for (int x = 0; x < 16; x++) {
      for (int z = 0; z < 16; z++) {
        biomes[x * 16 + z] = biomeProvider.getBiome(blockPos.add(x, 0, z));
      }
    }
    analyzer.newRepair(biomes, this.biomeData, landscape);
    return landscape;
  }

  public float[] getVanillaHeights(final ChunkPos chunkPos) {
    float[] vanillaHeights = vanillaHeightsCache.get(chunkPos);
    if (vanillaHeights == null) {
      ChunkPrimer chunk = new ChunkPrimer(chunkPos, UpgradeData.EMPTY);
      vanillaHeights = generateVanillaHeights(chunk);
      vanillaHeightsCache.put(chunkPos, vanillaHeights);
    }
    return vanillaHeights;
  }

  private float[] generateVanillaHeights(ChunkPrimer chunk) {
    vanillaChunkGenerator.makeBase(world, chunk);

    float[] heights = new float[256];
    //Heightmap hm = chunk.func_217303_b(Heightmap.Type.OCEAN_FLOOR_WG);
    BlockPos.MutableBlockPos mbp = new BlockPos.MutableBlockPos(0, 0, 0);
    for (int x = 0; x < 16; ++x) {
      for (int z = 0; z < 16; ++z) {
        heights[x * 16 + z] = 255;
        for (int y = 0; y < 256; ++y) {
          mbp.setPos(x, y, z);
          BlockState blockstate = chunk.getBlockState(mbp);
          if (!blockstate.isSolid()) {
            heights[x * 16 + z] = y;
            break;
          }
        }
      }
    }
    return heights;
  }

  private synchronized void getNewerNoise(final BiomeProvider biomeProvider, final int worldX, final int worldZ, ChunkLandscape landscape) {

    // get area biome map
    for (int x = -sampleSize; x < sampleSize + 5; x++) {
      for (int z = -sampleSize; z < sampleSize + 5; z++) {
        biomeData[(x + sampleSize) * sampleArraySize + (z + sampleSize)] = WorldUtil.Biomes.getBiomeID(biomeProvider.getBiome(new BlockPos(worldX + ((x * 8)), 0, worldZ + ((z * 8)))));
      }
    }

    //fill biomes array with biomeData
    for (int x = 0; x < 16; x++) {
      for (int z = 0; z < 16; z++) {
        BlockPos pos = new BlockPos(worldX + (x - 7) * 8 + 4, 0, worldZ + (z - 7) * 8 + 4);
        landscape.biomes[x * 16 + z] = biomeProvider.getBiome(pos);
      }
    }

    // fill the old smallRender
    BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos(worldX, 0, worldZ);
    for (int x = 0; x < 16; x++) {
      for (int z = 0; z < 16; z++) {
        float totalWeight = 0;
        for (int mapX = 0; mapX < sampleArraySize; mapX++) {
          for (int mapZ = 0; mapZ < sampleArraySize; mapZ++) {
            float weight = weightings[mapX * sampleArraySize + mapZ][x * 16 + z];
            if (weight > 0) {
              totalWeight += weight;
              weightedBiomes[biomeData[mapX * sampleArraySize + mapZ]] += weight;
            }
          }
        }

        // normalize biome weights
        for (int biomeIndex = 0; biomeIndex < weightedBiomes.length; biomeIndex++) {
          weightedBiomes[biomeIndex] /= totalWeight;
        }

        // combine mesa biomes
        // TODO: mesaCombiner.adjust(weightedBiomes);

        landscape.noise[x * 16 + z] = 0f;

        float river = TerrainBase.getRiverStrength(mpos.setPos(worldX + x, 0, worldZ + z), rtgWorld);
        landscape.river[x * 16 + z] = -river;

        float vanillaWeight = 0.f;
        for (int i = 0; i < 256; i++) {
          if (weightedBiomes[i] > 0f) {

            IRTGBiome rtgBiome = RTGAPI.getRTGBiome(i);
            if (rtgBiome != null) {
              landscape.noise[x * 16 + z] += rtgBiome.rNoise(this.rtgWorld, worldX + x, worldZ + z, weightedBiomes[i], river + 1f) * weightedBiomes[i];
            } else {
              vanillaWeight += weightedBiomes[i];
            }

            // 0 for the next column
            weightedBiomes[i] = 0f;
          }
        }
        landscape.noise[x * 16 + z] /= (1.f - vanillaWeight);
        landscape.vanillaWeight[x * 16 + z] = vanillaWeight;
        if (vanillaWeight > 0.f) {
          float[] heights = getVanillaHeights(new ChunkPos(worldX / 16, worldZ / 16));
          landscape.noise[x * 16 + z] += (heights[x * 16 + z] - landscape.noise[x * 16 + z]) * vanillaWeight;
        }
      }
    }

  }

  /**
   * Generate a column of terrain blocks from landscape noise
   */
  private void generateTerrain(RTGChunkWrapper chunk, int x, int z, float[] noise) {

    int height;
    height = (int) noise[x * 16 + z];

    Heightmap heightmap = chunk.func_217303_b(Heightmap.Type.OCEAN_FLOOR_WG);
    Heightmap heightmap1 = chunk.func_217303_b(Heightmap.Type.WORLD_SURFACE_WG);

    BlockState water = Blocks.WATER.getDefaultState();
    BlockState air = Blocks.AIR.getDefaultState();
    BlockState stone = Blocks.STONE.getDefaultState();

    for (int y = 0; y < 256; y++) {
      if (y > height) {
        if (y < this.settings.seaLevel) {
          chunk.setBlockState(x, y, z, water);
        } else {
          chunk.setBlockState(x, y, z, air);
        }
      } else {
        chunk.setBlockState(x, y, z, stone);
        heightmap.update(x, y, z, stone);
        heightmap1.update(x, y, z, stone);
      }
    }
  }


  /**
   * No idea what this is, lets hope it gets a better name in MCP at some point
   */
  @Override
  public int func_222529_a(int p_222529_1_, int p_222529_2_, Heightmap.Type p_222529_3_) {
    return vanillaChunkGenerator.func_222529_a(p_222529_1_, p_222529_2_, p_222529_3_);
  }
}
