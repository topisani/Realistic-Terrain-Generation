package rtg.api.world.surface;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.extensions.IForgeBlockState;
import rtg.api.RTGAPI;
import rtg.api.config.BiomeConfig;
import rtg.api.util.BlockUtil;
import rtg.api.world.RTGWorld;
import rtg.api.world.gen.RTGChunkWrapper;


public abstract class SurfaceBase {

    public IForgeBlockState shadowStoneBlock;
    public IForgeBlockState shadowDesertBlock;
    protected IForgeBlockState topBlock;
    protected IForgeBlockState fillerBlock;
    protected IForgeBlockState cliffStoneBlock;
    protected IForgeBlockState cliffCobbleBlock;
    protected BiomeConfig biomeConfig;

    public SurfaceBase(BiomeConfig config, Block top, Block fill) {

        this(config, top.getDefaultState(), fill.getDefaultState());
    }

    public SurfaceBase(BiomeConfig config, IForgeBlockState top, IForgeBlockState fill) {

        if (config == null) {
            throw new RuntimeException("Biome config in SurfaceBase is NULL.");
        }

        biomeConfig = config;
        topBlock = top;
        fillerBlock = fill;
        this.initCustomBlocks();
        this.assignUserConfigs(config, top, fill);
    }

    public void paintTerrain(RTGChunkWrapper chunk, int i, int j, int x, int z, int depth, RTGWorld rtgWorld, float[] noise, float river, Biome[] base) {

    }

    protected IForgeBlockState getShadowStoneBlock() {

        return shadowStoneBlock;
    }

    protected IForgeBlockState getShadowDesertBlock() {

        return shadowDesertBlock;
    }

    protected IForgeBlockState hcStone() {

        return cliffStoneBlock;
    }

    protected IForgeBlockState hcCobble() {

        return cliffCobbleBlock;
    }

    public IForgeBlockState getTopBlock() {

        return this.topBlock;
    }

    public IForgeBlockState getFillerBlock() {

        return this.fillerBlock;
    }

    private void assignUserConfigs(BiomeConfig config, IForgeBlockState top, IForgeBlockState fill) {

        this.topBlock = getConfigBlock(config.SURFACE_TOP_BLOCK.get(), top);
        this.fillerBlock = getConfigBlock(config.SURFACE_FILLER_BLOCK.get(), fill);
    }

    protected IForgeBlockState getConfigBlock(String configString, IForgeBlockState defaultState) {
        return BlockUtil.getBlockStateFromCfgString(configString, defaultState);
    }

    private void initCustomBlocks() {
        cliffStoneBlock = getConfigBlock(biomeConfig.SURFACE_CLIFF_STONE_BLOCK.get(), Blocks.STONE.getDefaultState());
        cliffCobbleBlock = getConfigBlock(biomeConfig.SURFACE_CLIFF_COBBLE_BLOCK.get(), Blocks.COBBLESTONE.getDefaultState());
        shadowStoneBlock = RTGAPI.getShadowStoneBlock();
        shadowDesertBlock = RTGAPI.getShadowDesertBlock();
    }
}