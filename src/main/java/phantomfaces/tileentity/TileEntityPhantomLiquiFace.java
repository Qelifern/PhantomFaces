package phantomfaces.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import phantomfaces.blocks.BlockPhantomFaceBase;
import phantomfaces.init.Registration;

public class TileEntityPhantomLiquiFace extends TileEntityPhantomFaceBase {

    public TileEntityPhantomLiquiFace() {
        super(Registration.PHANTOM_LIQUIFACE_TILE.get());
        this.type = BlockPhantomFaceBase.Type.LIQUIFACE;
    }

    @Override
    public boolean isBoundThingInRange() {
        if (super.isBoundThingInRange()) {
            TileEntity tile = this.world.getTileEntity(this.boundPosition);
            if (tile != null) {
                for (Direction facing : Direction.values()) {
                    if (tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing).isPresent()) { return true; }
                }
            }
        }
        return false;
    }

    @Override
    protected boolean isCapabilitySupported(Capability<?> capability) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
    }
}
