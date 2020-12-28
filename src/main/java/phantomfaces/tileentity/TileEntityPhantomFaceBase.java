package phantomfaces.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import phantomfaces.blocks.BlockPhantomFaceBase;
import phantomfaces.init.Registration;

import javax.annotation.Nullable;

public abstract class TileEntityPhantomFaceBase extends TileEntity implements ITickableTileEntity, ITickable, IPhantomTile {

    public boolean isRedstonePowered;
    public boolean isPulseMode;
    public static final int RANGE = 16;
    public BlockPos boundPosition;
    public BlockPhantomFaceBase.Type type;
    public int range;
    private int rangeBefore;
    private BlockPos boundPosBefore;
    private Block boundBlockBefore;
    private int lastStrength;

    public TileEntityPhantomFaceBase(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public static int upgradeRange(int defaultRange, World world, BlockPos pos) {
        int newRange = defaultRange;
        for (int i = 0; i < 3; i++) {
            Block block = world.getBlockState(pos.up(1 + i)).getBlock();
            if (block == Registration.PHANTOM_BOOSTER.get()) {
                newRange = newRange * 2;
            } else {
                break;
            }
        }
        return newRange;
    }


    @Override
    public CompoundNBT write(CompoundNBT compound) {
        if (this.isRedstoneToggle() && (this.isPulseMode)) {
            compound.putBoolean("IsPulseMode", this.isPulseMode);
        }
        compound.putBoolean("Redstone", this.isRedstonePowered);
        compound.putInt("Range", this.range);
        if (this.boundPosition != null) {
            compound.putInt("xOfTileStored", this.boundPosition.getX());
            compound.putInt("yOfTileStored", this.boundPosition.getY());
            compound.putInt("zOfTileStored", this.boundPosition.getZ());
        }
        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        if (this.isRedstoneToggle()) {
            this.isPulseMode = compound.getBoolean("IsPulseMode");
        }
        this.isRedstonePowered = compound.getBoolean("Redstone");
        int x = compound.getInt("xOfTileStored");
        int y = compound.getInt("yOfTileStored");
        int z = compound.getInt("zOfTileStored");
        this.range = compound.getInt("Range");
        if (!(x == 0 && y == 0 && z == 0)) {
            this.boundPosition = new BlockPos(x, y, z);
            this.markDirty();
        }
    }

    @Override
    public void tick() {
        if (!this.world.isRemote) {
            this.range = upgradeRange(RANGE, this.world, this.getPos());

            if (!this.hasBoundPosition()) {
                this.boundPosition = null;
            }

            if (this.doesNeedUpdateSend()) {
                this.onUpdateSent();
            }

            int strength = this.getComparatorStrength();
            if (this.lastStrength != strength) {
                this.lastStrength = strength;

                this.markDirty();
            }
        } else {
            if (this.boundPosition != null) {
                this.renderParticles();
            }
        }
    }

    protected boolean doesNeedUpdateSend() {
        return this.boundPosition != this.boundPosBefore || this.boundPosition != null && this.world.getBlockState(this.boundPosition).getBlock() != this.boundBlockBefore || this.rangeBefore != this.range;
    }

    protected void onUpdateSent() {
        this.rangeBefore = this.range;
        this.boundPosBefore = this.boundPosition;
        this.boundBlockBefore = this.boundPosition == null ? null : this.world.getBlockState(this.boundPosition).getBlock();

        if (this.boundPosition != null) {
            this.world.notifyNeighborsOfStateChange(this.pos, this.world.getBlockState(this.boundPosition).getBlock());
        }

        this.sendUpdate();
        this.markDirty();
    }

    @Override
    public boolean hasBoundPosition() {
        if (this.boundPosition != null) {
            if (this.world.getTileEntity(this.boundPosition) instanceof IPhantomTile || this.getPos().getX() == this.boundPosition.getX() && this.getPos().getY() == this.boundPosition.getY() && this.getPos().getZ() == this.boundPosition.getZ()) {
                this.boundPosition = null;
                return false;
            }
            return true;
        }
        return false;
    }

    public void renderParticles() {
        if (this.world.rand.nextInt(2) == 0) {
            double d1 = this.boundPosition.getY() + this.world.rand.nextFloat();
            int i1 = this.world.rand.nextInt(2) * 2 - 1;
            int j1 = this.world.rand.nextInt(2) * 2 - 1;
            double d4 = (this.world.rand.nextFloat() - 0.5D) * 0.125D;
            double d2 = this.boundPosition.getZ() + 0.5D + 0.25D * j1;
            double d5 = this.world.rand.nextFloat() * 1.0F * j1;
            double d0 = this.boundPosition.getX() + 0.5D + 0.25D * i1;
            double d3 = this.world.rand.nextFloat() * 1.0F * i1;
            this.world.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
        }
    }

    @Override
    public boolean isBoundThingInRange() {
        return this.hasBoundPosition() && this.boundPosition.distanceSq(this.getPos()) <= this.range * this.range;
    }

    @Override
    public BlockPos getBoundPosition() {
        return this.boundPosition;
    }

    @Override
    public void setBoundPosition(BlockPos pos) {
        this.boundPosition = pos;
    }

    @Override
    public int getRange() {
        return this.range;
    }

    public int getComparatorStrength() {
        if (this.isBoundThingInRange()) {
            BlockPos pos = this.getBoundPosition();
            BlockState state = this.world.getBlockState(pos);

            if (state.hasComparatorInputOverride()) { return state.getComparatorInputOverride(this.world, pos); }
        }
        return 0;
    }

    public boolean isRedstoneToggle() {
        return false;
    }

    public boolean respondsToPulses() {
        return this.isRedstoneToggle() && this.isPulseMode;
    }

    protected abstract boolean isCapabilitySupported(Capability<?> capability);

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(@Nullable Capability<T> cap, Direction facing) {
        if (this.isBoundThingInRange() && this.isCapabilitySupported(cap)) {
            TileEntity tile = this.world.getTileEntity(this.getBoundPosition());
            if (tile != null) {
                return tile.getCapability(cap, facing);
            }
        }
        return super.getCapability(cap, facing);
    }

    public final void sendUpdate() {
        if (this.world != null && !this.world.isRemote) world.notifyNeighborsOfStateChange(pos, getBlockState().getBlock());
    }

    public void setRedstonePowered(boolean powered) {
        this.isRedstonePowered = powered;
        this.markDirty();
    }
}
