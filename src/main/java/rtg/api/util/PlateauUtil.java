package rtg.api.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.BadlandsBiome;
import net.minecraft.world.biome.Biome;

import net.minecraft.world.biome.Biomes;
import net.minecraftforge.common.extensions.IForgeBlockState;
import rtg.api.RTGAPI;
import rtg.api.config.BiomeConfig;
import rtg.api.world.RTGWorld;
import rtg.api.world.biome.IRTGBiome;


/**
 * A Utility for storing and retrieving plateau band configurations for specific biomes.
 *
 * @author WhichOnesPink 2017-01-07
 * @author srs-bsns 2018-03-21
 * @since 1.0.0
 */
@UtilityClass
public final class PlateauUtil {

  private static final HashMap<IRTGBiome, List<IForgeBlockState>> BIOME_PLATEAU_BANDS = Maps.newHashMap();
  private static final IForgeBlockState DEFAULT_PLATEAU_BLOCK = Blocks.TERRACOTTA.getDefaultState();
  private static final Collection<Biome> PLATEAU_BIOMES;
  private static final Collection<String> BADLANDS_PLATEAU_BLOCKS;
  private static final Collection<String> SAVANNA_PLATEAU_BLOCKS;

  static {
    PLATEAU_BIOMES = Collections.unmodifiableCollection(Arrays.asList(
        Biomes.BADLANDS,
        Biomes.BADLANDS_PLATEAU,
        Biomes.ERODED_BADLANDS,
        Biomes.MODIFIED_BADLANDS_PLATEAU,
        Biomes.MODIFIED_WOODED_BADLANDS_PLATEAU,
        Biomes.WOODED_BADLANDS_PLATEAU,
        Biomes.SAVANNA_PLATEAU,
        Biomes.SHATTERED_SAVANNA_PLATEAU
    ));

    BADLANDS_PLATEAU_BLOCKS = Collections.unmodifiableCollection(Arrays.asList(
        "minecraft:stained_hardened_clay[color=yellow]",
        "minecraft:stained_hardened_clay[color=yellow]",
        "minecraft:stained_hardened_clay[color=yellow]",
        "minecraft:hardened_clay",
        "minecraft:hardened_clay",
        "minecraft:stained_hardened_clay[color=orange]",
        "minecraft:stained_hardened_clay[color=red]",
        "minecraft:stained_hardened_clay[color=red]",
        "minecraft:hardened_clay",
        "minecraft:stained_hardened_clay[color=silver]",
        "minecraft:stained_hardened_clay[color=white]",
        "minecraft:hardened_clay",
        "minecraft:hardened_clay",
        "minecraft:hardened_clay",
        "minecraft:stained_hardened_clay[color=orange]",
        "minecraft:hardened_clay",
        "minecraft:hardened_clay",
        "minecraft:hardened_clay",
        "minecraft:stained_hardened_clay[color=orange]",
        "minecraft:stained_hardened_clay[color=silver]",
        "minecraft:stained_hardened_clay[color=white]",
        "minecraft:stained_hardened_clay[color=brown]",
        "minecraft:stained_hardened_clay[color=brown]",
        "minecraft:stained_hardened_clay[color=brown]",
        "minecraft:hardened_clay",
        "minecraft:stained_hardened_clay[color=orange]",
        "minecraft:hardened_clay",
        "minecraft:stained_hardened_clay[color=orange]",
        "minecraft:hardened_clay",
        "minecraft:stained_hardened_clay[color=orange]",
        "minecraft:hardened_clay",
        "minecraft:hardened_clay",
        "minecraft:hardened_clay"
    ));
    SAVANNA_PLATEAU_BLOCKS = Collections.unmodifiableCollection(Arrays.asList(
        "minecraft:stained_hardened_clay[color=white]",
        "minecraft:stained_hardened_clay[color=white]",
        "minecraft:stained_hardened_clay[color=white]",
        "minecraft:stained_hardened_clay[color=white]",
        "minecraft:stained_hardened_clay[color=silver]",
        "minecraft:stained_hardened_clay[color=silver]",
        "minecraft:stained_hardened_clay[color=brown]",
        "minecraft:stained_hardened_clay[color=brown]",
        "minecraft:stained_hardened_clay[color=silver]",
        "minecraft:stained_hardened_clay[color=white]",
        "minecraft:stained_hardened_clay[color=silver]",
        "minecraft:stained_hardened_clay[color=brown]",
        "minecraft:stained_hardened_clay[color=brown]",
        "minecraft:stained_hardened_clay[color=silver]",
        "minecraft:stained_hardened_clay[color=brown]",
        "minecraft:stained_hardened_clay[color=silver]",
        "minecraft:stained_hardened_clay[color=white]",
        "minecraft:stained_hardened_clay[color=white]",
        "minecraft:stained_hardened_clay[color=silver]",
        "minecraft:stained_hardened_clay[color=brown]",
        "minecraft:stained_hardened_clay[color=brown]"
    ));
  }

  private PlateauUtil() {
  }

  public static String[] getMesaPlateauBlocks() {
    return BADLANDS_PLATEAU_BLOCKS.toArray(new String[0]);
  }

  public static String[] getSavannaPlateauBlocks() {
    return SAVANNA_PLATEAU_BLOCKS.toArray(new String[0]);
  }

  private static Collection<String> getConfigBlocks(IRTGBiome rtgBiome) {
    BiomeConfig config = rtgBiome.getConfig();
    return (config.hasProperty(config.PLATEAU_GRADIENT_BLOCK_LIST) && config.PLATEAU_GRADIENT_BLOCK_LIST.getValues().length > 0)
        ? config.PLATEAU_GRADIENT_BLOCK_LIST.getAsCollection()
        : (rtgBiome.baseBiome() instanceof BadlandsBiome) ? BADLANDS_PLATEAU_BLOCKS : SAVANNA_PLATEAU_BLOCKS;
  }

  public static void init() {
    PLATEAU_BIOMES.stream()
        .map(RTGAPI.RTG_BIOMES::get)
        .filter(Objects::nonNull)
        .forEach(rtgBiome -> {
          Collection<String> blocks = getConfigBlocks(rtgBiome);
          List<IForgeBlockState> bands = blocks.stream()
              .map(BlockUtil::getBlockStateFromCfgString)
              .filter(Objects::nonNull)
              .collect(Collectors.toList());
          if (bands.isEmpty()) {
            bands.add(DEFAULT_PLATEAU_BLOCK);
          }
          BIOME_PLATEAU_BANDS.put(rtgBiome, bands);
        });
  }

  public static IForgeBlockState getPlateauBand(final RTGWorld rtgWorld, final IRTGBiome rBiome, final int x, final int y, final int z) {
    return getBand(rBiome, y);
  }

  public static float stepIncrease(final float simplexVal, final float start, final float finish, final float height) {
    return (simplexVal <= start) ? 0 : (simplexVal >= finish) ? height : ((simplexVal - start) / (finish - start)) * height;
  }

  private static IForgeBlockState getBand(IRTGBiome rBiome, int index) {
    List<IForgeBlockState> bands = BIOME_PLATEAU_BANDS.get(rBiome);
    return bands != null ? bands.get(index % bands.size()) : DEFAULT_PLATEAU_BLOCK;
  }
}
