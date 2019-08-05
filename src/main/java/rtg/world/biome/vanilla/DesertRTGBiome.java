package rtg.world.biome.vanilla;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.common.extensions.IForgeBlockState;
import rtg.api.config.BiomeConfig;
import rtg.api.util.noise.SimplexNoise;
import rtg.api.world.RTGWorld;
import rtg.api.world.biome.RTGBiomeBase;
import rtg.api.world.gen.RTGChunkGenSettings;
import rtg.api.world.gen.RTGChunkWrapper;
import rtg.api.world.surface.SurfaceBase;
import rtg.api.world.terrain.TerrainBase;

import java.util.Random;


public class DesertRTGBiome extends RTGBiomeBase {

    public static Biome biome = Biomes.DESERT;
    public static Biome river = Biomes.RIVER;

    public DesertRTGBiome() {

        super(biome);
    }

    @Override
    public void initConfig() {
        this.getConfig().SURFACE_WATER_LAKE_MULT.set(0.0f);
        this.getConfig().ALLOW_VILLAGES.set(true);
        this.getConfig().ALLOW_SCENIC_LAKES.set(false);
        this.getConfig().SURFACE_FILLER_BLOCK.set("minecraft:sandstone");
        this.getConfig().addProperty(this.getConfig().ALLOW_CACTUS).set(true);
    }

    @Override
    public TerrainBase initTerrain() {

        return new TerrainVanillaDesert();
    }

    @Override
    public SurfaceBase initSurface() {

        return new SurfaceVanillaDesert(getConfig(), biome.getSurfaceBuilderConfig().getTop(), biome.getSurfaceBuilderConfig().getUnder());
    }

    @Override
    public void rReplace(RTGChunkWrapper chunk, int i, int j, int x, int y, int depth, RTGWorld rtgWorld, float[] noise, float river, Biome[] base) {

        this.rReplaceWithRiver(chunk, i, j, x, y, depth, rtgWorld, noise, river, base);
    }

    @Override
    public void initDecos() {

    }

    @Override
    public void overrideDecorations() {
        //baseBiome().decorator.cactiPerChunk = -999;
    }

    public static class TerrainVanillaDesert extends TerrainBase {

        public TerrainVanillaDesert() {

            super(64);
        }

        @Override
        public float generateNoise(RTGWorld rtgWorld, int x, int y, float border, float river) {

            RTGChunkGenSettings settings = rtgWorld.getGeneratorSettings();
            float duneHeight = (minDuneHeight + settings.sandDuneHeight);

            duneHeight *= (1f + rtgWorld.simplexInstance(2).noise2f(x / 330f, y / 330f)) / 2f;

            float stPitch = 200f;    // The higher this is, the more smoothly dunes blend with the terrain
            float stFactor = duneHeight;
            float hPitch = 70;    // Dune scale
            float hDivisor = 40;

            return terrainPolar(x, y, rtgWorld, river, stPitch, stFactor, hPitch, hDivisor, base) + groundNoise(x, y, 1f, rtgWorld);
        }
    }

    public static class SurfaceVanillaDesert extends SurfaceBase {

        public SurfaceVanillaDesert(BiomeConfig config, IForgeBlockState top, IForgeBlockState fill) {

            super(config, top, fill);
        }

        @Override
        public void paintTerrain(RTGChunkWrapper chunk, int i, int j, int x, int z, int depth, RTGWorld rtgWorld, float[] noise, float river, Biome[] base) {

            Random rand = rtgWorld.rand();
            SimplexNoise simplex = rtgWorld.simplexInstance(0);
            boolean water = false;
            boolean riverPaint = false;
            boolean grass = false;

            if (river > 0.05f && river + (simplex.noise2f(i / 10f, j / 10f) * 0.1f) > 0.86f) {
                riverPaint = true;

                if (simplex.noise2f(i / 12f, j / 12f) > 0.25f) {
                    grass = true;
                }
            }

            Block b;
            for (int k = 255; k > -1; k--) {
                b = chunk.getBlockState(x, k, z).getBlock();
                if (b == Blocks.AIR) {
                    depth = -1;
                }
                else if (b == Blocks.STONE) {
                    depth++;

                    if (riverPaint) {
                        if (grass && depth < 4) {
                            //primer.setBlockState(x, k, z, Blocks.GRASS.getDefaultState());
                            chunk.setBlockState(x, k, z, fillerBlock);
                        }
                        else if (depth == 0) {
                            chunk.setBlockState(x, k, z, rand.nextInt(2) == 0 ? topBlock : Blocks.SANDSTONE.getDefaultState());
                        }
                    }
                    else if (depth > -1 && depth < 5) {
                        chunk.setBlockState(x, k, z, topBlock);
                    }
                    else if (depth < 8) {
                        chunk.setBlockState(x, k, z, fillerBlock);
                    }
                }
            }
        }
    }
}