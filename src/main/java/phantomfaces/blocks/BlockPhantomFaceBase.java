package phantomfaces.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import phantomfaces.Config;
import phantomfaces.tileentity.*;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockPhantomFaceBase extends Block {

    public final Type type;


    public BlockPhantomFaceBase(Type type, Properties properties) {
        super(properties);
        this.type = type;
    }

    @Override
    public boolean canProvidePower(BlockState state) {
        return this.type == Type.REDSTONEFACE;
    }

    @Override
    public int getWeakPower(BlockState blockState, IBlockReader world, BlockPos pos, Direction side) {
        if (this.type == Type.REDSTONEFACE) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileEntityPhantomRedstoneFace) { return ((TileEntityPhantomRedstoneFace) tile).providesWeak[side.ordinal()]; }
        }
        return 0;
    }

    @Override
    public int getStrongPower(BlockState blockState, IBlockReader world, BlockPos pos, Direction side) {
        if (this.type == Type.REDSTONEFACE) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileEntityPhantomRedstoneFace) { return ((TileEntityPhantomRedstoneFace) tile).providesStrong[side.ordinal()]; }
        }
        return 0;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        switch (this.type) {
            case LIQUIFACE:
                return new TileEntityPhantomLiquiFace();
            case ENERGYFACE:
                return new TileEntityPhantomEnergyFace();
            case REDSTONEFACE:
                return new TileEntityPhantomRedstoneFace();
            default:
                return new TileEntityPhantomItemFace();
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (this.tryToggleRedstone(worldIn, pos, player)) { return ActionResultType.SUCCESS; }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState oldState, boolean p_196243_5_) {
        if (state.getBlock() != oldState.getBlock()) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileEntityPhantomFaceBase) {
                world.updateComparatorOutputLevel(pos, this);
            }

            super.onReplaced(state, world, pos, oldState, p_196243_5_);
        }
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
        this.updateRedstoneState(world, pos);
    }

    public void updateRedstoneState(World world, BlockPos pos) {
        if (!world.isRemote) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileEntityPhantomFaceBase) {
                TileEntityPhantomFaceBase base = (TileEntityPhantomFaceBase) tile;
                boolean powered = world.getRedstonePowerFromNeighbors(pos) > 0;
                boolean wasPowered = base.isRedstonePowered;
                if (powered && !wasPowered) {
                    if (base.respondsToPulses()) {
                        world.getPendingBlockTicks().scheduleTick(pos, this, 5);
                    }
                    base.setRedstonePowered(true);
                } else if (!powered && wasPowered) {
                    base.setRedstonePowered(false);
                }
            }
        }
    }

    public boolean tryToggleRedstone(World world, BlockPos pos, PlayerEntity player) {
        ItemStack stack = player.getHeldItemMainhand();
        if (!stack.isEmpty() && stack.getItem() == Config.itemRedstoneTorchConfigurator) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileEntityPhantomFaceBase) {
                TileEntityPhantomFaceBase base = (TileEntityPhantomFaceBase) tile;
                if (!world.isRemote && base.isRedstoneToggle()) {
                    base.isPulseMode = !base.isPulseMode;
                    base.markDirty();
                    base.sendUpdate();
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    public enum Type {
        ITEMFACE,
        LIQUIFACE,
        ENERGYFACE,
        REDSTONEFACE
    }
}
