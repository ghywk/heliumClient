package cc.helium.event.impl.world;

import cc.helium.event.api.cancellable.CancellableEvent;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * @author Kev1nLeft
 */

public class BlockAABBEvent extends CancellableEvent {
    private final World world;
    private final Block block;
    private final BlockPos blockPos;
    private AxisAlignedBB boundingBox;
    private final AxisAlignedBB maskBoundingBox;

    public BlockAABBEvent(Block block, World world, BlockPos blockPos, AxisAlignedBB maskBoundingBox, AxisAlignedBB boundingBox) {
        this.block = block;
        this.world = world;
        this.blockPos = blockPos;
        this.maskBoundingBox = maskBoundingBox;
        this.boundingBox = boundingBox;
    }

    public Block getBlock() {
        return block;
    }

    public World getWorld() {
        return world;
    }

    public AxisAlignedBB getMaskBoundingBox() {
        return maskBoundingBox;
    }

    public AxisAlignedBB getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(AxisAlignedBB boundingBox) {
        this.boundingBox = boundingBox;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }
}
