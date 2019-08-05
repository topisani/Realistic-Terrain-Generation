package rtg.world.gen;

import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.*;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.lighting.WorldLightManager;

import javax.annotation.Nullable;
import java.util.BitSet;
import java.util.Map;
import java.util.stream.Stream;

/**
 * A Dummy chunkPrimer implementation which
 * @author topisani
 */
public class HeightChunkPrimer extends ChunkPrimer {

  public HeightChunkPrimer(ChunkPos pos) {
    super(pos, UpgradeData.EMPTY);
  }

  @Nullable
  public TileEntity getTileEntity(BlockPos pos) {
    return this.getTileEntity(pos);
  }

  @Nullable
  public BlockState getBlockState(BlockPos pos) {
    return this.getBlockState(pos);
  }

  public IFluidState getFluidState(BlockPos pos) {
    return this.getFluidState(pos);
  }

  public int getMaxLightLevel() {
    return this.getMaxLightLevel();
  }

  @Nullable
  public BlockState setBlockState(BlockPos pos, BlockState state, boolean isMoving) {
    return null;
  }

  public void addTileEntity(BlockPos pos, TileEntity tileEntityIn) {
  }

  /**
   * Adds an entity to the chunk.
   */
  public void addEntity(Entity entityIn) {
  }

  public void setStatus(ChunkStatus status) {
  }

  /**
   * Returns the ExtendedBlockStorage array for this Chunk.
   */
  public ChunkSection[] getSections() {
    return this.getSections();
  }

  @Nullable
  public WorldLightManager getWorldLightManager() {
    return this.getWorldLightManager();
  }

  public void setHeightmap(Heightmap.Type type, long[] data) {
  }

  private Heightmap.Type func_209532_c(Heightmap.Type p_209532_1_) {
    if (p_209532_1_ == Heightmap.Type.WORLD_SURFACE_WG) {
      return Heightmap.Type.WORLD_SURFACE;
    } else {
      return p_209532_1_ == Heightmap.Type.OCEAN_FLOOR_WG ? Heightmap.Type.OCEAN_FLOOR : p_209532_1_;
    }
  }

  public int getTopBlockY(Heightmap.Type heightmapType, int x, int z) {
    return this.getTopBlockY(this.func_209532_c(heightmapType), x, z);
  }

  /**
   * Gets a {@link ChunkPos} representing the x and z coordinates of this.
   */
  public ChunkPos getPos() {
    return this.getPos();
  }

  public void setLastSaveTime(long saveTime) {
  }

  @Nullable
  public StructureStart getStructureStart(String stucture) {
    return this.getStructureStart(stucture);
  }

  public void putStructureStart(String structureIn, StructureStart structureStartIn) {
  }

  public Map<String, StructureStart> getStructureStarts() {
    return this.getStructureStarts();
  }

  public void setStructureStarts(Map<String, StructureStart> structureStartsIn) {
  }

  public LongSet getStructureReferences(String structureIn) {
    return this.getStructureReferences(structureIn);
  }

  public void addStructureReference(String strucutre, long reference) {
  }

  public Map<String, LongSet> getStructureReferences() {
    return this.getStructureReferences();
  }

  public void setStructureReferences(Map<String, LongSet> p_201606_1_) {
  }

  public Biome[] getBiomes() {
    return this.getBiomes();
  }

  public void setModified(boolean modified) {
  }

  public boolean isModified() {
    return false;
  }

  public ChunkStatus getStatus() {
    return this.getStatus();
  }

  public void removeTileEntity(BlockPos pos) {
  }

  public void markBlockForPostprocessing(BlockPos pos) {
  }

  public void addTileEntity(CompoundNBT nbt) {
  }

  @Nullable
  public CompoundNBT getDeferredTileEntity(BlockPos pos) {
    return this.getDeferredTileEntity(pos);
  }

  @Nullable
  public CompoundNBT func_223134_j(BlockPos p_223134_1_) {
    return this.func_223134_j(p_223134_1_);
  }

  public void setBiomes(Biome[] biomesIn) {
  }

  public Stream<BlockPos> func_217304_m() {
    return this.func_217304_m();
  }

  public ChunkPrimerTickList<Block> getBlocksToBeTicked() {
    return new ChunkPrimerTickList<>((p_209219_0_) -> {
      return p_209219_0_.getDefaultState().isAir();
    }, this.getPos());
  }

  public ChunkPrimerTickList<Fluid> getFluidsToBeTicked() {
    return new ChunkPrimerTickList<>((p_209218_0_) -> {
      return p_209218_0_ == Fluids.EMPTY;
    }, this.getPos());
  }

  public BitSet getCarvingMask(GenerationStage.Carving type) {
    return this.getCarvingMask(type);
  }

  public boolean hasLight() {
    return this.hasLight();
  }

  public void setLight(boolean p_217305_1_) {
    this.setLight(p_217305_1_);
  }
}
