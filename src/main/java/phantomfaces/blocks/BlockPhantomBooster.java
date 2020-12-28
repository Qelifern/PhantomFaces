package phantomfaces.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import phantomfaces.tileentity.TileEntityPhantomBooster;

import javax.annotation.Nullable;

public class BlockPhantomBooster extends Block {

    private static final AxisAlignedBB AABB = new AxisAlignedBB(2 * 0.0625, 0, 2 * 0.0625, 1 - 2 * 0.0625, 1, 1 - 2 * 0.0625);

    public BlockPhantomBooster(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return VoxelShapes.create(AABB);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityPhantomBooster();
    }
}
