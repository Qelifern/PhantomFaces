package phantomfaces.init;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import phantomfaces.blocks.*;
import phantomfaces.items.BlockItemPhantomBooster;
import phantomfaces.items.ItemPhantomConnector;
import phantomfaces.tileentity.*;

import static phantomfaces.Main.MODID;

public class Registration {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MODID);
    //private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MOD_ID);
    //private static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, MOD_ID);
    //private static final DeferredRegister<ModDimension> DIMENSIONS = new DeferredRegister<>(ForgeRegistries.MOD_DIMENSIONS, MOD_ID);

    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        //CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        //ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        //DIMENSIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }


    public static final RegistryObject<BlockPhantomEnergyFace> PHANTOM_ENERGYFACE = BLOCKS.register(BlockPhantomEnergyFace.FACE, () -> new BlockPhantomEnergyFace(
            Block.Properties.create(Material.ROCK)
            .hardnessAndResistance(4.5f, 10.0f)
            .harvestTool(ToolType.PICKAXE)
            .sound(SoundType.STONE)));
    public static final RegistryObject<Item> PHANTOM_ENERGYFACE_ITEM = ITEMS.register(BlockPhantomEnergyFace.FACE, () -> new BlockItem(PHANTOM_ENERGYFACE.get(), new Item.Properties().group(ModSetup.ITEM_GROUP).rarity(Rarity.EPIC)));
    public static final RegistryObject<TileEntityType<TileEntityPhantomEnergyFace>> PHANTOM_ENERGYFACE_TILE = TILES.register(BlockPhantomEnergyFace.FACE, () -> TileEntityType.Builder.create(TileEntityPhantomEnergyFace::new, PHANTOM_ENERGYFACE.get()).build(null));

    public static final RegistryObject<BlockPhantomItemFace> PHANTOM_ITEMFACE = BLOCKS.register(BlockPhantomItemFace.FACE, () -> new BlockPhantomItemFace(
            Block.Properties.create(Material.ROCK)
                    .hardnessAndResistance(4.5f, 10.0f)
                    .harvestTool(ToolType.PICKAXE)
                    .sound(SoundType.STONE)));
    public static final RegistryObject<Item> PHANTOM_ITEMFACE_ITEM = ITEMS.register(BlockPhantomItemFace.FACE, () -> new BlockItem(PHANTOM_ITEMFACE.get(), new Item.Properties().group(ModSetup.ITEM_GROUP).rarity(Rarity.EPIC)));
    public static final RegistryObject<TileEntityType<TileEntityPhantomItemFace>> PHANTOM_ITEMFACE_TILE = TILES.register(BlockPhantomItemFace.FACE, () -> TileEntityType.Builder.create(TileEntityPhantomItemFace::new, PHANTOM_ITEMFACE.get()).build(null));


    public static final RegistryObject<BlockPhantomLiquiFace> PHANTOM_LIQUIFACE = BLOCKS.register(BlockPhantomLiquiFace.FACE, () -> new BlockPhantomLiquiFace(
            Block.Properties.create(Material.ROCK)
                    .hardnessAndResistance(4.5f, 10.0f)
                    .harvestTool(ToolType.PICKAXE)
                    .sound(SoundType.STONE)));
    public static final RegistryObject<Item> PHANTOM_LIQUIFACE_ITEM = ITEMS.register(BlockPhantomLiquiFace.FACE, () -> new BlockItem(PHANTOM_LIQUIFACE.get(), new Item.Properties().group(ModSetup.ITEM_GROUP).rarity(Rarity.EPIC)));
    public static final RegistryObject<TileEntityType<TileEntityPhantomLiquiFace>> PHANTOM_LIQUIFACE_TILE = TILES.register(BlockPhantomLiquiFace.FACE, () -> TileEntityType.Builder.create(TileEntityPhantomLiquiFace::new, PHANTOM_LIQUIFACE.get()).build(null));


    public static final RegistryObject<BlockPhantomRedstoneFace> PHANTOM_REDSTONEFACE = BLOCKS.register(BlockPhantomRedstoneFace.FACE, () -> new BlockPhantomRedstoneFace(
            Block.Properties.create(Material.ROCK)
                    .hardnessAndResistance(4.5f, 10.0f)
                    .harvestTool(ToolType.PICKAXE)
                    .sound(SoundType.STONE)
                    .setOpaque(Registration::isntSolid)));
    public static final RegistryObject<Item> PHANTOM_REDSTONEFACE_ITEM = ITEMS.register(BlockPhantomRedstoneFace.FACE, () -> new BlockItem(PHANTOM_REDSTONEFACE.get(), new Item.Properties().group(ModSetup.ITEM_GROUP).rarity(Rarity.EPIC)));
    public static final RegistryObject<TileEntityType<TileEntityPhantomRedstoneFace>> PHANTOM_REDSTONEFACE_TILE = TILES.register(BlockPhantomRedstoneFace.FACE, () -> TileEntityType.Builder.create(TileEntityPhantomRedstoneFace::new, PHANTOM_REDSTONEFACE.get()).build(null));

    public static final RegistryObject<BlockPhantomBooster> PHANTOM_BOOSTER = BLOCKS.register("block_phantom_booster", () -> new BlockPhantomBooster(
            Block.Properties.create(Material.ROCK)
                    .hardnessAndResistance(4.5f, 10.0f)
                    .harvestTool(ToolType.PICKAXE)
                    .sound(SoundType.STONE)
                    .notSolid()));
    public static final RegistryObject<BlockItemPhantomBooster> PHANTOM_BOOSTER_ITEM = ITEMS.register("block_phantom_booster", () -> new BlockItemPhantomBooster(PHANTOM_BOOSTER.get(), new Item.Properties().group(ModSetup.ITEM_GROUP).rarity(Rarity.EPIC)));
    public static final RegistryObject<TileEntityType<TileEntityPhantomBooster>> PHANTOM_BOOSTER_TILE = TILES.register("block_phantom_booster", () -> TileEntityType.Builder.create(TileEntityPhantomBooster::new, PHANTOM_BOOSTER.get()).build(null));


    public static final RegistryObject<ItemPhantomConnector> ITEM_PHANTOM_CONNECTOR = ITEMS.register("item_phantom_connector", () -> new ItemPhantomConnector(new Item.Properties().group(ModSetup.ITEM_GROUP).maxStackSize(1)));


    private static boolean isntSolid(BlockState state, IBlockReader reader, BlockPos pos) {
        return false;
    }

}
