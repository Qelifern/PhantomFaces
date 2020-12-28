package phantomfaces.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import phantomfaces.tileentity.IPhantomTile;
import phantomfaces.tileentity.TileEntityPhantomFaceBase;

import javax.annotation.Nullable;
import java.util.List;

import static phantomfaces.Main.MODID;

public class ItemPhantomConnector extends Item {

    public ItemPhantomConnector(Properties properties) {
        super(properties);
    }


    public static BlockPos getStoredPosition(ItemStack stack) {
        CompoundNBT tag = stack.getTag();
        if (tag != null) {
            int x = tag.getInt("XCoordOfTileStored");
            int y = tag.getInt("YCoordOfTileStored");
            int z = tag.getInt("ZCoordOfTileStored");
            if (!(x == 0 && y == 0 && z == 0)) { return new BlockPos(x, y, z); }
        }
        return null;
    }

    public static void clearStorage(ItemStack stack, String... keys) {
        if (stack.hasTag()) {
            CompoundNBT compound = stack.getTag();
            for (String key : keys) {
                compound.remove(key);
            }
        }
    }

    public static void storeConnection(ItemStack stack, int x, int y, int z, World world) {
        CompoundNBT tag = stack.getTag();
        if (tag == null) {
            tag = new CompoundNBT();
        }

        tag.putInt("XCoordOfTileStored", x);
        tag.putInt("YCoordOfTileStored", y);
        tag.putInt("ZCoordOfTileStored", z);

        stack.setTag(tag);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        Hand hand = context.getHand();
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote) {
            //Passing Data to Phantoms
            TileEntity tile = world.getTileEntity(pos);
            if (tile != null) {
                //Passing to Phantom
                if (tile instanceof IPhantomTile) {
                    BlockPos stored = getStoredPosition(stack);
                    if (stored != null) {
                        ((IPhantomTile) tile).setBoundPosition(stored);
                        if (tile instanceof TileEntityPhantomFaceBase) {
                            ((TileEntityPhantomFaceBase) tile).sendUpdate();
                        }
                        clearStorage(stack, "XCoordOfTileStored", "YCoordOfTileStored", "ZCoordOfTileStored");
                        player.sendStatusMessage(new TranslationTextComponent("tooltip." + MODID + ".phantom.connected.desc"), true);
                        return ActionResultType.SUCCESS;
                    }
                    return ActionResultType.FAIL;
                }
            }
            //Storing Connections
            storeConnection(stack, pos.getX(), pos.getY(), pos.getZ(), world);
            player.sendStatusMessage(new TranslationTextComponent("tooltip." + MODID + ".phantom.stored.desc"), true);
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        BlockPos coords = getStoredPosition(stack);
        if (coords != null) {
            tooltip.add(new TranslationTextComponent("tooltip." + MODID + ".boundTo.desc").append(new StringTextComponent(":")));
            tooltip.add(new StringTextComponent("X: " + coords.getX()));
            tooltip.add(new StringTextComponent("Y: " + coords.getY()));
            tooltip.add(new StringTextComponent("Z: " + coords.getZ()));
            tooltip.add(new TranslationTextComponent("tooltip." + MODID + ".clearStorage.desc").setStyle(Style.EMPTY.setFormatting(TextFormatting.ITALIC)));
        }
    }
}
