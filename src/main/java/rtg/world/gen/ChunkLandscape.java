package rtg.world.gen;

import net.minecraft.world.biome.Biome;

/**
 * A cache of a chunks heights, river and biomes.
 */
public class ChunkLandscape {
  public float[] noise = new float[256];
  public Biome[] biomes = new Biome[256];
  public float[] river = new float[256];
  /**
   * Weight of non-rtg biomes. 0-1
   */
  public float[] vanillaWeight = new float[256];

  boolean hasVanillaWeight() {
    for (int i = 0; i < 256; ++i) {
      if (vanillaWeight[i] != 0) return true;
    }
    return false;
  }
}
