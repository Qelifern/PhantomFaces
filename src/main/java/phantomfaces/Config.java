package phantomfaces;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;

import java.nio.file.Path;

import static phantomfaces.Main.LOGGER;
import static phantomfaces.Main.MODID;

@Mod.EventBusSubscriber
public class Config {

    public static final String CATEGORY_GENERAL = "general";
    public static ForgeConfigSpec.ConfigValue<String> itemRedstoneConfigurator;
    public static ForgeConfigSpec.ConfigValue<String> itemRedstoneConfiguratorMod;
    public static Item itemRedstoneTorchConfigurator;

    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

        COMMON_BUILDER.comment("Settings").push(CATEGORY_GENERAL);

        setupConfig(COMMON_BUILDER);

        COMMON_BUILDER.pop();


        COMMON_CONFIG = COMMON_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    private static void setupConfig(ForgeConfigSpec.Builder COMMON_BUILDER) {

        itemRedstoneConfigurator = COMMON_BUILDER.comment(
                " Redstone Torch, used to configure the Redstone Mode. If another mod overrides usage of this item, you can change the registry name of the used item (using blocks is not possible) here." +
                        "\n Default: minecraft:redstone_torch")
                .define("general.redstone_mode_item", "redstone_torch");
        itemRedstoneConfiguratorMod = COMMON_BUILDER.comment(
                " The modid of the item" +
                        "\n Default: minecraft:redstone_torch")
                .define("general.redstone_mode_mod", "minecraft");
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        LOGGER.debug("Loading config file {}", path);

        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        LOGGER.debug("Built TOML config for {}", path.toString());
        configData.load();
        LOGGER.debug("Loaded TOML config file {}", path.toString());
        spec.setConfig(configData);
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {

    }

    @SubscribeEvent
    public static void onReload(final ModConfig.Reloading configEvent) {

    }

    @SubscribeEvent
    public static void onWorldLoad(final WorldEvent.Load event) {
        Config.loadConfig(Config.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MODID + "-client.toml"));
        Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MODID + ".toml"));

    }
}
