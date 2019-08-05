package rtg.api;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.item.DyeColor;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.extensions.IForgeBlockState;
import rtg.api.util.BlockUtil;
import rtg.api.util.UtilityClass;
import rtg.api.util.storage.BiomeMap;
import rtg.api.world.biome.IRTGBiome;
import rtg.api.world.biome.RTGBiomeRegistry;
import rtg.world.WorldTypeRTG;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.Set;


@UtilityClass
public final class RTGAPI {

    public static final String   RTG_API_ID       = "rtgapi";
    public static final String   VERSION          = "@API_VERSION@";
    public static final String   RTG_MOD_ID       = "rtg";
    public static final String   RTG_WORLDTYPE_ID = "RTG";
    public static final BiomeMap RTG_BIOMES       = new BiomeMap();

    private static final Set<DimensionType> ALLOWED_DIMENSION_TYPES = new ObjectArraySet<>();

    private static Path            configPath;
    private static IForgeBlockState     shadowStoneBlock  = null;
    private static IForgeBlockState     shadowDesertBlock = null;

    private RTGAPI() {

    }

    public static Path getConfigPath() {
        return configPath;
    }

    public static void setConfigPath(Path path) {
        if (configPath == null) {
            configPath = path;
        }
    }

    public static boolean checkWorldType(WorldType worldType) {
        return WorldTypeRTG.getInstance().equals(worldType);
    }

    public static void addAllowedDimensionType(DimensionType dimType) {
        ALLOWED_DIMENSION_TYPES.add(dimType);
    }

    public static void removeAllowedDimensionType(DimensionType dimType) {
        ALLOWED_DIMENSION_TYPES.remove(dimType);
    }

    public static boolean isAllowedDimensionType(DimensionType dimType) {
        return ALLOWED_DIMENSION_TYPES.contains(dimType);
    }

    // public static boolean isAllowedDimensionType(int dimId) {
        // DimensionType type = (DimensionManager.getRegistry().(dimId)) ? DimensionManager.getProviderType(dimId) : null;
        // return type != null && ALLOWED_DIMENSION_TYPES.contains(type);
    // }

    @Nullable
    public static IRTGBiome getRTGBiome(@Nonnull Biome biome) {
      return RTGBiomeRegistry.REGISTRY.getRTGBiomeFor(biome);
    }

    @Nullable
    public static IRTGBiome getRTGBiome(int biomeId) {
        return RTGBiomeRegistry.REGISTRY.getById(biomeId);
    }

    public static void setShadowBlocks(IForgeBlockState stone, IForgeBlockState desert) {
        if (shadowStoneBlock  == null) { shadowStoneBlock  = stone  != null ? stone  : BlockUtil.getStateClay(DyeColor.CYAN); }
        if (shadowDesertBlock == null) { shadowDesertBlock = desert != null ? desert : BlockUtil.getStateClay(DyeColor.GRAY); }
    }

    public static IForgeBlockState getShadowStoneBlock() {
        return shadowStoneBlock;
    }

    public static IForgeBlockState getShadowDesertBlock() {
        return shadowDesertBlock;
    }
}
