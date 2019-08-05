package rtg.world;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.biome.provider.OverworldBiomeProvider;
import net.minecraft.world.biome.provider.OverworldBiomeProviderSettings;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.OverworldGenSettings;
import net.minecraftforge.common.extensions.IForgeWorldType;
import rtg.api.RTGAPI;
import rtg.api.world.biome.RTGBiomeRegistry;
import rtg.api.world.gen.RTGChunkGenSettings;
import rtg.world.biome.vanilla.DesertHillsRTGBiome;
import rtg.world.biome.vanilla.DesertRTGBiome;
import rtg.world.biome.vanilla.PlainsRTGBiome;
import rtg.world.biome.vanilla.ShatteredSavannaRTGBiome;
import rtg.world.gen.RTGChunkGenerator;

public class WorldTypeRTG extends WorldType implements IForgeWorldType {
  private static WorldTypeRTG INSTANCE;

  private static ChunkGeneratorType<RTGChunkGenSettings, RTGChunkGenerator> CHUNK_GENERATOR_TYPE =
        new ChunkGeneratorType<RTGChunkGenSettings, RTGChunkGenerator>(RTGChunkGenerator::new,
      true, () -> (new RTGChunkGenSettings.Factory()).build());

  private WorldTypeRTG() {
    super(RTGAPI.RTG_WORLDTYPE_ID);
  }

  public static WorldTypeRTG getInstance() {
    if (INSTANCE == null) {
      init();
    }
    return INSTANCE;
  }

  public static void init() {
    INSTANCE = new WorldTypeRTG();

    RTGBiomeRegistry.REGISTRY.register(Biomes.PLAINS, new PlainsRTGBiome());
    RTGBiomeRegistry.REGISTRY.register(Biomes.SHATTERED_SAVANNA, new ShatteredSavannaRTGBiome());
    RTGBiomeRegistry.REGISTRY.register(Biomes.DESERT, new DesertRTGBiome());
    RTGBiomeRegistry.REGISTRY.register(Biomes.DESERT_HILLS, new DesertHillsRTGBiome());
    Registry.register(Registry.CHUNK_GENERATOR_TYPE, "rtg", CHUNK_GENERATOR_TYPE);
  }

  @Override
  public ChunkGenerator<?> createChunkGenerator(World world) {
    if (world.getDimension().isSurfaceWorld()) {
      BiomeProviderType<OverworldBiomeProviderSettings, OverworldBiomeProvider> biomeProviderType = BiomeProviderType.VANILLA_LAYERED;
      OverworldBiomeProviderSettings bpSettings = biomeProviderType.createSettings().setGeneratorSettings(new OverworldGenSettings()).setWorldInfo(world.getWorldInfo());
      OverworldBiomeProvider biomeProvider = biomeProviderType.create(bpSettings);
      return CHUNK_GENERATOR_TYPE.create(world, biomeProvider, CHUNK_GENERATOR_TYPE.createSettings());
    }
    return world.getDimension().createChunkGenerator();
  }



  }
