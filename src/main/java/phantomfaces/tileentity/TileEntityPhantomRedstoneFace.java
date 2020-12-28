package phantomfaces.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import phantomfaces.blocks.BlockPhantomFaceBase;
import phantomfaces.init.Registration;

import java.util.Arrays;

public class TileEntityPhantomRedstoneFace extends TileEntityPhantomFaceBase {

    public final int[] providesStrong = new int[Direction.values().length];
    public final int[] providesWeak = new int[Direction.values().length];

    private final int[] lastProvidesStrong = new int[this.providesStrong.length];
    private final int[] lastProvidesWeak = new int[this.providesWeak.length];

    public TileEntityPhantomRedstoneFace() {
        super(Registration.PHANTOM_REDSTONEFACE_TILE.get());
        this.type = BlockPhantomFaceBase.Type.REDSTONEFACE;
    }

    @Override
    public void tick() {
        if (!this.world.isRemote) {
            if (this.isBoundThingInRange()) {
                BlockState boundState = this.world.getBlockState(this.boundPosition);
                if (boundState != null) {
                    Block boundBlock = boundState.getBlock();
                    if (boundBlock != null) {
                        for (int i = 0; i < Direction.values().length; i++) {
                            Direction facing = Direction.values()[i];
                            this.providesWeak[i] = boundState.getWeakPower(this.world, this.boundPosition, facing);
                            this.providesStrong[i] = boundState.getStrongPower(this.world, this.boundPosition, facing);
                        }
                    }
                }
            }
        }

        super.tick();
    }

    @Override
    protected boolean doesNeedUpdateSend() {
        return super.doesNeedUpdateSend() || !Arrays.equals(this.providesStrong, this.lastProvidesStrong) || !Arrays.equals(this.providesWeak, this.lastProvidesWeak);
    }

    @Override
    protected void onUpdateSent() {
        System.arraycopy(this.providesWeak, 0, this.lastProvidesWeak, 0, this.providesWeak.length);
        System.arraycopy(this.providesStrong, 0, this.lastProvidesStrong, 0, this.providesStrong.length);

        super.onUpdateSent();
    }

    @Override
    protected boolean isCapabilitySupported(Capability<?> capability) {
        return false;
    }

}
