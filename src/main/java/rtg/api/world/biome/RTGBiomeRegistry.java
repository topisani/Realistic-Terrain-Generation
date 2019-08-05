package rtg.api.world.biome;

import net.minecraft.world.biome.Biome;
import rtg.api.util.storage.BiomeMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RTGBiomeRegistry {

  public static RTGBiomeRegistry REGISTRY = new RTGBiomeRegistry();

  private BiomeMap map = new BiomeMap();

  public IRTGBiome register(@Nonnull Biome baseBiome, @Nonnull IRTGBiome rtgBiome) {
    map.put(baseBiome, rtgBiome);
    return rtgBiome;
  }

  public boolean hasRTGBiomeFor(Biome baseBiome) {
    return map.containsKey(baseBiome);
  }

  @Nullable
  public IRTGBiome getRTGBiomeFor(Biome baseBiome) {
    return map.get(baseBiome);
  }

  public IRTGBiome getById(int biomeId) {
    return map.getValueAt(biomeId);
  }
}
