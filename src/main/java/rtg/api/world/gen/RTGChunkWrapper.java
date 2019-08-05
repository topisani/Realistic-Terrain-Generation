package rtg.api.world.gen;

import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.shorts.ShortList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ITickList;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.*;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.lighting.WorldLightManager;
import net.minecraftforge.common.extensions.IForgeBlockState;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * A wrapper to add overrides whenever mojang changes the setBlockState interface.
 *
 * This object should be passed to all generation code instead of Chunks or ChunkPrimers
 * @author topisani
 */
public class RTGChunkWrapper implements IChunk {

  private final IChunk wrapped;

  public RTGChunkWrapper(IChunk wrapped) {
    this.wrapped = wrapped;
  }

  public BlockState setBlockState(BlockPos bp, IForgeBlockState ibs) {
    return wrapped.setBlockState(bp, ibs.getBlockState(), false);
  }

  public BlockState setBlockState(BlockPos bp, BlockState ibs) {
    return wrapped.setBlockState(bp, ibs, false);
  }

  @Deprecated
  public BlockState setBlockState(int x, int y, int z, IForgeBlockState ibs) {
    return wrapped.setBlockState(new BlockPos(x, y, z), ibs.getBlockState(), false);
  }

  @Deprecated
  public BlockState setBlockState(int x, int y, int z, BlockState ibs) {
    return wrapped.setBlockState(new BlockPos(x, y, z), ibs, false);
  }

  @Deprecated
  public BlockState getBlockState(int x, int y, int z) {
    return wrapped.getBlockState(new BlockPos(x, y, z));
  }

  // wrappers

  @Nullable
  @Override
  public BlockState setBlockState(BlockPos pos, BlockState state, boolean isMoving) {
    return wrapped.setBlockState(pos, state, isMoving);
  }

  @Override
  public void addTileEntity(BlockPos pos, TileEntity tileEntityIn) {
    wrapped.addTileEntity(pos, tileEntityIn);
  }

  @Override
  public void addEntity(Entity entityIn) {
    wrapped.addEntity(entityIn);
  }

  @Override
  public Set<BlockPos> getTileEntitiesPos() {
    return wrapped.getTileEntitiesPos();
  }

  @Override
  public ChunkSection[] getSections() {
    return wrapped.getSections();
  }

  @Nullable
  @Override
  public WorldLightManager getWorldLightManager() {
    return wrapped.getWorldLightManager();
  }

  @Override
  public Collection<Map.Entry<Heightmap.Type, Heightmap>> func_217311_f() {
    return wrapped.func_217311_f();
  }

  @Override
  public void setHeightmap(Heightmap.Type type, long[] data) {
    wrapped.setHeightmap(type, data);
  }

  @Override
  public Heightmap func_217303_b(Heightmap.Type p_217303_1_) {
    return wrapped.func_217303_b(p_217303_1_);
  }

  @Override
  public int getTopBlockY(Heightmap.Type heightmapType, int x, int z) {
    return wrapped.getTopBlockY(heightmapType, x, z);
  }

  @Override
  public ChunkPos getPos() {
    return wrapped.getPos();
  }

  @Override
  public void setLastSaveTime(long saveTime) {
    wrapped.setLastSaveTime(saveTime);
  }

  @Override
  public Map<String, StructureStart> getStructureStarts() {
    return wrapped.getStructureStarts();
  }

  @Override
  public void setStructureStarts(Map<String, StructureStart> structureStartsIn) {
    wrapped.setStructureStarts(structureStartsIn);
  }

  @Override
  public Biome[] getBiomes() {
    return wrapped.getBiomes();
  }

  @Override
  public void setModified(boolean modified) {
    wrapped.setModified(modified);
  }

  @Override
  public boolean isModified() {
    return wrapped.isModified();
  }

  @Override
  public ChunkStatus getStatus() {
    return wrapped.getStatus();
  }

  @Override
  public void removeTileEntity(BlockPos pos) {
    wrapped.removeTileEntity(pos);
  }

  @Override
  public void setLightManager(WorldLightManager p_217306_1_) {
    wrapped.setLightManager(p_217306_1_);
  }

  @Override
  public ShortList[] getPackedPositions() {
    return wrapped.getPackedPositions();
  }

  @Nullable
  @Override
  public CompoundNBT getDeferredTileEntity(BlockPos pos) {
    return wrapped.getDeferredTileEntity(pos);
  }

  @Nullable
  @Override
  public CompoundNBT func_223134_j(BlockPos p_223134_1_) {
    return wrapped.func_223134_j(p_223134_1_);
  }

  @Override
  public Stream<BlockPos> func_217304_m() {
    return wrapped.func_217304_m();
  }

  @Override
  public ITickList<Block> getBlocksToBeTicked() {
    return wrapped.getBlocksToBeTicked();
  }

  @Override
  public ITickList<Fluid> getFluidsToBeTicked() {
    return wrapped.getFluidsToBeTicked();
  }

  @Override
  public UpgradeData getUpgradeData() {
    return wrapped.getUpgradeData();
  }

  @Override
  public void setInhabitedTime(long newInhabitedTime) {
    wrapped.setInhabitedTime(newInhabitedTime);
  }

  @Override
  public long getInhabitedTime() {
    return wrapped.getInhabitedTime();
  }

  @Override
  public boolean hasLight() {
    return wrapped.hasLight();
  }

  @Override
  public void setLight(boolean p_217305_1_) {
    wrapped.setLight(p_217305_1_);
  }

  @Nullable
  @Override
  public StructureStart getStructureStart(String stucture) {
    return wrapped.getStructureStart(stucture);
  }

  @Override
  public void putStructureStart(String structureIn, StructureStart structureStartIn) {
    wrapped.putStructureStart(structureIn, structureStartIn);
  }

  @Override
  public LongSet getStructureReferences(String structureIn) {
    return wrapped.getStructureReferences(structureIn);
  }

  @Override
  public void addStructureReference(String strucutre, long reference) {
    wrapped.addStructureReference(strucutre, reference);
  }

  @Override
  public Map<String, LongSet> getStructureReferences() {
    return wrapped.getStructureReferences();
  }

  @Override
  public void setStructureReferences(Map<String, LongSet> p_201606_1_) {
    wrapped.setStructureReferences(p_201606_1_);
  }

  @Nullable
  @Override
  public TileEntity getTileEntity(BlockPos pos) {
    return wrapped.getTileEntity(pos);
  }

  @Override
  public BlockState getBlockState(BlockPos pos) {
    return wrapped.getBlockState(pos);
  }

  @Override
  public IFluidState getFluidState(BlockPos pos) {
    return wrapped.getFluidState(pos);
  }
}
