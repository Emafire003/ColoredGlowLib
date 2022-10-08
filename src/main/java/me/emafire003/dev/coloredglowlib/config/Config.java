package me.emafire003.dev.coloredglowlib.config;

import com.mojang.datafixers.util.Pair;
import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;

import static me.emafire003.dev.coloredglowlib.ColoredGlowLibMod.LOGGER;

public class Config {
    public static SimpleConfig CONFIG;
    private static ConfigProvider configs;

    public static boolean PER_ENTITY = true;
    public static boolean PER_ENTITYTYPE = true;
    public static boolean GENERALIZED_RAINBOW = false;
    public static boolean OVERRIDE_TEAM_COLORS = false;

    public static void registerConfigs() {
        configs = new ConfigProvider();
        createConfigs();

        CONFIG = SimpleConfig.of(ColoredGlowLibMod.MOD_ID + "_config").provider(configs).request();

        assignConfigs();
        LOGGER.info("All " + configs.getConfigsList().size() + " have been set properly");
    }

    private static void createConfigs() {
        configs.addKeyValuePair(new Pair<>("per_entity", true), "Sets the Entity-specific option for the custom colored glow effect. If enabled it will check for the UUID of an entity and set (if configured) a custom glow color for the specific entity.");
        configs.addKeyValuePair(new Pair<>("per_entitytype", true), "Sets the EntityType-specific option for the custom colored. If enabled it will check for the EntityType of an entity and set (if configured) a custom glow color for the entity");
        configs.addKeyValuePair(new Pair<>("generalized_rainbow", false), "Set this to true to make every entity change the glowing color like a jeb sheep aka change color each tick");
        configs.addKeyValuePair(new Pair<>("override_team_colors", false), "Set this to true to override the default minecraft team colors even of the entity is in a team");

    }

    public static void reloadConfig(){
        registerConfigs();
        LOGGER.info("All " + configs.getConfigsList().size() + " have been reloaded properly");
    }

    private static void assignConfigs() {

        PER_ENTITY = CONFIG.getOrDefault("per_entity", true);
        PER_ENTITYTYPE = CONFIG.getOrDefault("per_entitytype", true);
        GENERALIZED_RAINBOW = CONFIG.getOrDefault("generalized_rainbow", false);
        OVERRIDE_TEAM_COLORS = CONFIG.getOrDefault("override_team_colors", false);

    }
}

