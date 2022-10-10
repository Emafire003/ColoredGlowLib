package me.emafire003.dev.coloredglowlib;

import me.emafire003.dev.coloredglowlib.config.Config;
import me.emafire003.dev.coloredglowlib.networking.CGLNetworking;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

@Mod(ColoredGlowLibMod.MOD_ID)
public class ColoredGlowLibMod {

    public static final String MOD_ID = "coloredglowlib";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final String PREFIX = "§f[§1C§2o§3l§4o§5r§6e§7d§8G§9l§ao§bw§cL§di§eb§f] §r";

    private static ColoredGlowLib coloredGlowLib = new ColoredGlowLib();
    //TODO figure out where the config folder is
    public static Path PATH = Path.of( "/" + MOD_ID + "/");

    public ColoredGlowLibMod(){
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        LOGGER.info("Initializing...");
        //CGLCommandRegister.registerCommands();
        //CommandRegistrationCallback.EVENT.register(CGLCommands::registerCommands);
        Config.registerConfigs();
        Config.reloadConfig();
        coloredGlowLib.setPerEntityColor(Config.PER_ENTITY);
        coloredGlowLib.setPerEntityTypeColor(Config.PER_ENTITYTYPE);
        coloredGlowLib.setRainbowChangingColor(Config.GENERALIZED_RAINBOW);
        coloredGlowLib.setOverrideTeamColors(Config.OVERRIDE_TEAM_COLORS);
        LOGGER.info("Complete!");

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            CGLNetworking.register();
        });
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }

    public static ColoredGlowLib getAPI(){
        return coloredGlowLib;
    }

    public static ColoredGlowLib getColoredGlowLib(){
        return coloredGlowLib;
    }

    public static ColoredGlowLib getLib(){
        return coloredGlowLib;
    }

    public static ColoredGlowLib getInstance(){
        return coloredGlowLib;
    }

    public static ColoredGlowLib getCGLInstance(){
        return coloredGlowLib;
    }
}
