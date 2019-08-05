package rtg.api.world.gen.feature.tree.rtg;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LogBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.common.extensions.IForgeBlockState;
import rtg.RTGConfig;
import rtg.api.util.RTGTreeData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * The base class for all RTG trees.
 *
 * @author WhichOnesPink
 * @see <a href="http://imgur.com/a/uoJsU">RTG Tree Gallery</a>
 */
public abstract class TreeRTG extends AbstractTreeFeature<NoFeatureConfig> {

    protected IForgeBlockState logBlock;
    protected IForgeBlockState leavesBlock;
    protected int trunkSize;
    protected int crownSize;
    protected boolean noLeaves;

    protected IForgeBlockState saplingBlock;

    protected int generateFlag;

    protected int minTrunkSize;
    protected int maxTrunkSize;
    protected int minCrownSize;
    protected int maxCrownSize;

    protected ArrayList<IForgeBlockState> validGroundBlocks;
    protected ArrayList<Material> canGrowIntoMaterials;

    private boolean allowBarkCoveredLogs;

    public TreeRTG(boolean notify) {
        // TODO: What does this mean?
        super((arg) -> NoFeatureConfig.NO_FEATURE_CONFIG, notify);
    }

    public TreeRTG() {

        this(false);

        this.setLogBlock(Blocks.OAK_LOG.getDefaultState());
        this.setLeavesBlock(Blocks.OAK_LEAVES.getDefaultState());
        this.trunkSize = 2;
        this.crownSize = 4;
        this.setNoLeaves(false);

        this.saplingBlock = Blocks.OAK_SAPLING.getDefaultState();

        this.generateFlag = 2;

        // These need to default to zero as they're only used when generating trees from saplings.
        this.setMinTrunkSize(0);
        this.setMaxTrunkSize(0);
        this.setMinCrownSize(0);
        this.setMaxCrownSize(0);

        // Each tree sub-class is responsible for using (or not using) this list as part of its generation logic.
        this.validGroundBlocks = new ArrayList<>(Arrays.asList(
                Blocks.GRASS_BLOCK.getDefaultState(),
                Blocks.DIRT.getDefaultState(),
                Blocks.PODZOL.getDefaultState(),
                Blocks.RED_SAND.getDefaultState()
        ));

        this.canGrowIntoMaterials = new ArrayList<>(Arrays.asList(
            Material.AIR,
            Material.WOOD,
            Material.LEAVES,
            Material.ORGANIC,
            Material.EARTH,
            Material.PLANTS,
            Material.TALL_PLANTS,
            Material.WATER,
            Material.SNOW
        ));

        this.allowBarkCoveredLogs = true; // TODO: RTGConfig.barkCoveredLogs();
    }

    public void buildTrunk(World world, Random rand, int x, int y, int z) {

    }

    public void buildBranch(World world, Random rand, int x, int y, int z, int dX, int dZ, int logLength, int leaveSize) {

    }

    public void buildLeaves(World world, int x, int y, int z) {

    }

    public void buildLeaves(World world, Random rand, int x, int y, int z, int size) {

    }

    protected boolean isGroundValid(World world, BlockPos trunkPos) {

        return this.isGroundValid(world, trunkPos, RTGConfig.treesCanGenerateOnSand());
    }

    protected boolean isGroundValid(World world, BlockPos trunkPos, boolean sandAllowed) {

        IForgeBlockState g = world.getBlockState(new BlockPos(trunkPos.getX(), trunkPos.getY() - 1, trunkPos.getZ()));

        if (g.getBlockState().getBlock() == Blocks.SAND && !sandAllowed) {
            return false;
        }

        for (int i = 0; i < this.validGroundBlocks.size(); i++) {
            if (g == this.validGroundBlocks.get(i)) {
                return true;
            }
        }

        return false;
    }

    protected boolean isGroundValid(World world, ArrayList<BlockPos> trunkPos) {

        if (trunkPos.isEmpty()) {
            throw new RuntimeException("Unable to determine if ground is valid. No trunks.");
        }

        for (int i = 0; i < trunkPos.size(); i++) {
            if (!this.isGroundValid(world, trunkPos.get(i))) {
                return false;
            }
        }

        return true;
    }

    protected void placeLogBlock(World world, BlockPos pos, IForgeBlockState logBlock, int generateFlag) {

        if (this.isReplaceable(world, pos)) {
            world.setBlockState(pos, logBlock.getBlockState(), generateFlag);
        }
    }

    protected void placeLeavesBlock(World world, BlockPos pos, IForgeBlockState leavesBlock, int generateFlag) {

        if (world.isAirBlock(pos)) {
            world.setBlockState(pos, leavesBlock.getBlockState(), generateFlag);
        }
    }

    protected void placeLogBlock(World world, BlockPos pos, IForgeBlockState logBlock, int generateFlag, RTGTreeData treeData) {

        this.placeLogBlock(world, pos, logBlock, generateFlag);
        //treeData.placeLogBlock(world, pos, logBlock, generateFlag);
    }

    protected void placeLeavesBlock(World world, BlockPos pos, IForgeBlockState leavesBlock, int generateFlag, RTGTreeData treeData) {

        this.placeLeavesBlock(world, pos, leavesBlock, generateFlag);
        //treeData.placeLeavesBlock(world, pos, leavesBlock, generateFlag);
    }

    // TODO: @Override
    public boolean isReplaceable(World world, BlockPos pos) {

        BlockState state = world.getBlockState(pos).getBlockState();

        return state.getBlock().isAir(state, world, pos)
                // TODO: || state.getBlock().isLeaves(state, world, pos)
                // TODO: || state.getBlock().isWood(world, pos)
                || canGrowInto(state.getBlock());
    }

    // TODO: @Override
    protected boolean canGrowInto(Block blockType) {

        Material material = blockType.getDefaultState().getMaterial();

        for (int i = 0; i < this.canGrowIntoMaterials.size(); i++) {
            if (material == this.canGrowIntoMaterials.get(i)) {
                //Logger.debug("Log has grown into %s (%s)", this.canGrowIntoMaterials.get(i).toString(), blockType.getLocalizedName());
                return true;
            }
        }

        return false;
    }

    public IForgeBlockState getTrunkLog(IForgeBlockState defaultLog) {

        if (!this.allowBarkCoveredLogs) {
            return defaultLog;
        }

        IForgeBlockState trunkLog;

        try {
            trunkLog = defaultLog.getBlockState().with(LogBlock.AXIS, Direction.Axis.X);
        }
        catch (Exception e) {
            trunkLog = defaultLog;
        }

        return trunkLog;
    }

    public IForgeBlockState getLogBlock() {

        return logBlock;
    }

    public TreeRTG setLogBlock(IForgeBlockState logBlock) {

        this.logBlock = logBlock;
        return this;
    }

    public IForgeBlockState getLeavesBlock() {

        return leavesBlock;
    }

    public TreeRTG setLeavesBlock(IForgeBlockState leavesBlock) {

        this.leavesBlock = leavesBlock;
        return this;
    }

    public int getTrunkSize() {

        return trunkSize;
    }

    public TreeRTG setTrunkSize(int trunkSize) {

        this.trunkSize = trunkSize;
        return this;
    }

    public int getCrownSize() {

        return crownSize;
    }

    public TreeRTG setCrownSize(int crownSize) {

        this.crownSize = crownSize;
        return this;
    }

    public boolean getNoLeaves() {

        return noLeaves;
    }

    public TreeRTG setNoLeaves(boolean noLeaves) {

        this.noLeaves = noLeaves;
        return this;
    }

    public IForgeBlockState getSaplingBlock() {

        return saplingBlock;
    }

    public TreeRTG setSaplingBlock(IForgeBlockState saplingBlock) {

        this.saplingBlock = saplingBlock;
        return this;
    }

    public int getGenerateFlag() {

        return generateFlag;
    }

    public TreeRTG setGenerateFlag(int generateFlag) {

        this.generateFlag = generateFlag;
        return this;
    }

    public int getMinTrunkSize() {

        return minTrunkSize;
    }

    public TreeRTG setMinTrunkSize(int minTrunkSize) {

        this.minTrunkSize = minTrunkSize;
        return this;
    }

    public int getMaxTrunkSize() {

        return maxTrunkSize;
    }

    public TreeRTG setMaxTrunkSize(int maxTrunkSize) {

        this.maxTrunkSize = maxTrunkSize;
        return this;
    }

    public int getMinCrownSize() {

        return minCrownSize;
    }

    public TreeRTG setMinCrownSize(int minCrownSize) {

        this.minCrownSize = minCrownSize;
        return this;
    }

    public int getMaxCrownSize() {

        return maxCrownSize;
    }

    public TreeRTG setMaxCrownSize(int maxCrownSize) {

        this.maxCrownSize = maxCrownSize;
        return this;
    }

    public ArrayList<IForgeBlockState> getValidGroundBlocks() {

        return validGroundBlocks;
    }

    public TreeRTG setValidGroundBlocks(ArrayList<IForgeBlockState> validGroundBlocks) {

        this.validGroundBlocks = validGroundBlocks;
        return this;
    }

}