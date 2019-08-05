package rtg.api.world.biome;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import rtg.api.config.BiomeConfig;
import rtg.api.world.RTGWorld;
import rtg.api.world.biome.RTGBiomeBase.BeachType;
import rtg.api.world.biome.RTGBiomeBase.RiverType;
import rtg.api.world.deco.DecoBase;
import rtg.api.world.gen.RTGChunkWrapper;
import rtg.api.world.surface.SurfaceBase;
import rtg.api.world.terrain.TerrainBase;

import java.util.Collection;


public interface IRTGBiome {

  Biome baseBiome();

  ResourceLocation baseBiomeResLoc();

  int baseBiomeId();

  RiverType getRiverType();

  BeachType getBeachType();

  IRTGBiome getRiverBiome();

  IRTGBiome getBeachBiome();

  Biome preferredBeach();

  BiomeConfig getConfig();

  TerrainBase terrain();

  SurfaceBase surface();

  void rReplace(final RTGChunkWrapper chunk, final BlockPos blockPos, final int x, final int y, final int depth, final RTGWorld rtgWorld, final float[] noise, final float river, final Biome[] base);

  void rReplace(RTGChunkWrapper chunk, int i, int j, int x, int y, int depth, RTGWorld rtgWorld, float[] noise, float river, Biome[] base);

  float rNoise(RTGWorld rtgWorld, int x, int y, float border, float river);

  double waterLakeMult();

  double lavaLakeMult();

  float lakePressure(RTGWorld rtgWorld, int x, int y, float border, float lakeInterval, float largeBendSize, float mediumBendSize, float smallBendSize);

  void initDecos();

  Collection<DecoBase> getDecos();

  /**
   * Adds a deco object to the list of biome decos.
   * The 'allowed' parameter allows us to pass biome config booleans dynamically when configuring the decos in the biome.
   */
  default void addDeco(DecoBase deco, boolean allowed) {
    if (allowed) {
      Collection<DecoBase> decos = this.getDecos();
      if (!deco.properlyDefined()) {
        throw new RuntimeException(deco.toString());
      }
      decos.add(deco);
    }
  }

  // Use this method to override a base biome's decorations.
  default void overrideDecorations() {
  }

  /**
   * Convenience method for addDeco() where 'allowed' is assumed to be true.
   */
  default void addDeco(DecoBase deco) {
    if (!deco.properlyDefined()) {
      throw new RuntimeException(deco.toString());
    }
    this.addDeco(deco, true);
  }

  /**
   * Some biomes have hard-coded decorations.
   * If true, RTG will call the biome decorator's decorate() method instead of the biome's decorate() method.
   */
  default boolean overridesHardcoded() {
    return false;
  }

  TerrainBase initTerrain();

  SurfaceBase initSurface();

  void initConfig();
}
