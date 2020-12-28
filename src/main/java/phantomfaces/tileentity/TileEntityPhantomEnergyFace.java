package phantomfaces.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import phantomfaces.blocks.BlockPhantomFaceBase;
import phantomfaces.init.Registration;

public class TileEntityPhantomEnergyFace extends TileEntityPhantomFaceBase {

    public TileEntityPhantomEnergyFace() {
        super(Registration.PHANTOM_ENERGYFACE_TILE.get());
        this.type = BlockPhantomFaceBase.Type.ENERGYFACE;
    }

    @Override
    public boolean isBoundThingInRange() {
        if (super.isBoundThingInRange()) {
            TileEntity tile = this.world.getTileEntity(this.boundPosition);
            if (tile != null) {
                for (Direction facing : Direction.values()) {
                    if (tile.getCapability(CapabilityEnergy.ENERGY, facing).isPresent()) { return true; }
                }
            }
        }
        return false;
    }

    @Override
    protected boolean isCapabilitySupported(Capability<?> capability) {
        return capability == CapabilityEnergy.ENERGY;
    }
}
