package xiaochangsama.xiaochangmodcore;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xiaochangsama.xiaochangmodcore.networks.ModMessages;


import static net.minecraftforge.registries.ForgeRegistries.Keys.ITEMS;
import static xiaochangsama.xiaochangmodcore.ModConfig.CONFIG;

@Mod("xiaochang_mod_core")
public class XiaoChangModCore {



    public static final org.slf4j.Logger XC_LOGGER = LogUtils.getLogger();
    public static final String MODID = "xiaochang_mod_core";


    public XiaoChangModCore() {
        MinecraftForge.EVENT_BUS.register(this);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(this::commonSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CONFIG);
        XC_LOGGER.info("XiaoChang:The loading is complete!!!");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ModMessages.register();
    }


}