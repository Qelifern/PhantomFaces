package phantomfaces;

import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import phantomfaces.init.ClientSetup;
import phantomfaces.init.ModSetup;
import phantomfaces.init.Registration;

import static phantomfaces.Main.MODID;

@Mod(MODID)
public class Main
{
    public static final String MODID = "phantomfaces";
    public static final Logger LOGGER = LogManager.getLogger();
    public static IEventBus MOD_EVENT_BUS;

    public Main() {
        MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.COMMON_CONFIG);

        MOD_EVENT_BUS.register(Registration.class);

        Registration.init();
        MOD_EVENT_BUS.addListener(ModSetup::init);
        MOD_EVENT_BUS.addListener(ClientSetup::init);

        Config.loadConfig(Config.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MODID + "-client.toml"));
        Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MODID + ".toml"));

        MOD_EVENT_BUS.register(this);

        Config.itemRedstoneTorchConfigurator = null;
        Config.itemRedstoneTorchConfigurator = ForgeRegistries.ITEMS.getValue(new ResourceLocation(Config.itemRedstoneConfiguratorMod.get(), Config.itemRedstoneConfigurator.get()));
        if (Config.itemRedstoneTorchConfigurator == null) {
            Config.itemRedstoneTorchConfigurator = Items.REDSTONE_TORCH;
        }
    }
}
