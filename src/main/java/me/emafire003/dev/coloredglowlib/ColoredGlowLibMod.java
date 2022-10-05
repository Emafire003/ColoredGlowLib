package me.emafire003.dev.coloredglowlib;

import me.emafire003.dev.coloredglowlib.command.CGLCommandRegister;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColoredGlowLibMod implements ModInitializer {

    public static String MOD_ID = "coloredglowlib";
    public static Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static ColoredGlowLib coloredGlowLib = new ColoredGlowLib();

    public static final GameRules.Key<GameRules.BooleanRule> OVERRIDE_TEAM_COLORS =
            GameRuleRegistry.register("overrideTeamColors", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        //Why do I even have to write this :/

        LOGGER.info("Initializing...");
        CGLCommandRegister.registerCommands();

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
