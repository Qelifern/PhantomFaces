package phantomfaces.tileentity;

import net.minecraft.util.math.BlockPos;

public interface IPhantomTile {

    /**
     * @return If the Phantom Tile is currently bound to anything
     */
    boolean hasBoundPosition();

    /**
     * @return If the Phantom Tile's bound position is in range
     */
    boolean isBoundThingInRange();

    /**
     * @return The position this tile is bound to
     */
    BlockPos getBoundPosition();

    /**
     * Sets the bound position
     */
    void setBoundPosition(BlockPos pos);

    /**
     * @return The range the tile currently has
     */
    int getRange();

}
