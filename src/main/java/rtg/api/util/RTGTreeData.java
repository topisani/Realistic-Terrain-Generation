package rtg.api.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeBlockState;

/**
 * Created by WhichOnesPink on 28/07/2019.
 *
 * Please don't refactor or optimize anything in this class. Thanks, Pink.
 */
public class RTGTreeData {

    public boolean firstLog = false;
    public boolean firstLeaves = false;

    public int minXLog = 0;
    public int maxXLog = 0;
    public int minZLog = 0;
    public int maxZLog = 0;
    public int minXLeaves = 0;
    public int maxXLeaves = 0;
    public int minZLeaves = 0;
    public int maxZLeaves = 0;
    public int logRangeX = 0;
    public int logRangeZ = 0;
    public int leavesRangeX = 0;
    public int leavesRangeZ = 0;

    public RTGTreeData() {

    }

    public void placeLogBlock(World world, BlockPos pos, IForgeBlockState logBlock, int generateFlag) {

        if (firstLog) {
            minXLog = Math.min(pos.getX(), minXLog);
            maxXLog = Math.max(pos.getX(), maxXLog);
            minZLog = Math.min(pos.getZ(), minZLog);
            maxZLog = Math.max(pos.getZ(), maxZLog);
        }
        else {
            minXLog = pos.getX();
            maxXLog = pos.getX();
            minZLog = pos.getZ();
            maxZLog = pos.getZ();

            firstLog = true;
        }

        updateRanges();
    }

    public void placeLeavesBlock(World world, BlockPos pos, IForgeBlockState logBlock, int generateFlag) {

        if (firstLeaves) {
            minXLeaves = Math.min(pos.getX(), minXLeaves);
            maxXLeaves = Math.max(pos.getX(), maxXLeaves);
            minZLeaves = Math.min(pos.getZ(), minZLeaves);
            maxZLeaves = Math.max(pos.getZ(), maxZLeaves);
        }
        else {
            minXLeaves = pos.getX();
            maxXLeaves = pos.getX();
            minZLeaves = pos.getZ();
            maxZLeaves = pos.getZ();

            firstLeaves = true;
        }

        updateRanges();
    }

    public void dumpTreeData() {

        if (logRangeX > 16) {
            Logger.error("Log X range = {} to {} ({} blocks)", minXLog, maxXLog, logRangeX);
        }
        else {
            //Logger.info("Log X range = {} to {} ({} blocks)", minXLog, maxXLog, logRangeX);
        }

        if (logRangeZ > 16) {
            Logger.error("Log Z range = {} to {} ({} blocks)", minZLog, maxZLog, logRangeZ);
        }
        else {
            //Logger.info("Log Z range = {} to {} ({} blocks)", minZLog, maxZLog, logRangeZ);
        }

        if (leavesRangeX > 16) {
            Logger.error("Leaves X range = {} to {} ({} blocks)", minXLeaves, maxXLeaves, leavesRangeX);
        }
        else {
            //Logger.info("Leaves X range = {} to {} ({} blocks)", minXLeaves, maxXLeaves, leavesRangeX);
        }

        if (leavesRangeZ > 16) {
            Logger.error("Leaves Z range = {} to {} ({} blocks)", minZLeaves, maxZLeaves, leavesRangeZ);
        }
        else {
            //Logger.info("Leaves Z range = {} to {} ({} blocks)", minZLeaves, maxZLeaves, leavesRangeZ);
        }
    }

    public void updateRanges() {
        logRangeX = Math.abs(maxXLog - minXLog);
        logRangeZ = Math.abs(maxZLog - minZLog);
        leavesRangeX = Math.abs(maxXLeaves - minXLeaves);
        leavesRangeZ = Math.abs(maxZLeaves - minZLeaves);
    }
}
