package me.emafire003.dev.coloredglowlib;

import me.emafire003.dev.coloredglowlib.command.CGLCommands;
import me.emafire003.dev.coloredglowlib.config.ConfigDataSaver;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class ColoredGlowLibMod implements ModInitializer {

    public static String MOD_ID = "coloredglowlib";
    public static Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static String PREFIX = "§f[§1C§2o§3l§4o§5r§6e§7d§8G§9l§ao§bw§cL§di§eb§f] §r";

    private static ColoredGlowLib coloredGlowLib = new ColoredGlowLib();
    public static Path PATH = Path.of(String.valueOf(FabricLoader.getInstance().getConfigDir())+ "/" + MOD_ID + "/");

    /*public static final GameRules.Key<GameRules.BooleanRule> OVERRIDE_TEAM_COLORS =
            GameRuleRegistry.register("overrideTeamColors", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));
*/
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        //Why do I even have to write this :/

        LOGGER.info("Initializing...");
        //CGLCommandRegister.registerCommands();
        CommandRegistrationCallback.EVENT.register(CGLCommands::registerCommands);
        ConfigDataSaver.CONFIG_INSTANCE.load();
        LOGGER.info("Complete!");
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
