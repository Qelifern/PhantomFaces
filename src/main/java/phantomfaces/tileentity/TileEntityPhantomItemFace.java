package phantomfaces.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import phantomfaces.blocks.BlockPhantomFaceBase;
import phantomfaces.init.Registration;

public class TileEntityPhantomItemFace extends TileEntityPhantomFaceBase{
    public TileEntityPhantomItemFace() {
        super(Registration.PHANTOM_ITEMFACE_TILE.get());
        this.type = BlockPhantomFaceBase.Type.ITEMFACE;
    }

    @Override
    public boolean isBoundThingInRange() {
        if (super.isBoundThingInRange()) {
            TileEntity tile = this.world.getTileEntity(this.getBoundPosition());
            if (tile != null) {
                for (Direction facing : Direction.values()) {
                    if (tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing).isPresent()) { return true; }
                }
            }
        }
        return false;
    }

    @Override
    protected boolean isCapabilitySupported(Capability<?> capability) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

}
