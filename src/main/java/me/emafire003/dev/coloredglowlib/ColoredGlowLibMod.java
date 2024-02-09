package me.emafire003.dev.coloredglowlib;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import dev.onyxstudios.cca.api.v3.scoreboard.ScoreboardComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.scoreboard.ScoreboardComponentInitializer;
import me.emafire003.dev.coloredglowlib.command.CGLCommands;
import me.emafire003.dev.coloredglowlib.component.ColorComponent;
import me.emafire003.dev.coloredglowlib.component.GlobalColorComponent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Month;

public class ColoredGlowLibMod implements ModInitializer, EntityComponentInitializer, ScoreboardComponentInitializer {

    public static String MOD_ID = "coloredglowlib";
    public static Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static String PREFIX = "§f[§cC§6o§el§ao§3r§9e§5d§cG§6l§eo§aw§3L§9i§5b§f] §r";
        // Old prefix: "§f[§1C§2o§3l§4o§5r§6e§7d§8G§9l§ao§bw§cL§di§eb§f] §r";

    private static ColoredGlowLibAPI coloredGlowLib;

    public static final ComponentKey<ColorComponent> COLOR_COMPONENT =
            ComponentRegistry.getOrCreate(new Identifier(MOD_ID, "color_component"), ColorComponent.class);

    public static final ComponentKey<GlobalColorComponent> GLOBAL_COLOR_COMPONENT =
            ComponentRegistry.getOrCreate(new Identifier(MOD_ID, "global_color_component"), GlobalColorComponent.class);

    public static boolean isAp1 = false;


    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        LOGGER.info("Initializing...");


        ServerLifecycleEvents.SERVER_STARTED.register(server -> coloredGlowLib = new ColoredGlowLibAPI(server.getScoreboard()));

        LocalDate currentDate = LocalDate.now();
        int day = currentDate.getDayOfMonth();
        Month month = currentDate.getMonth();
        if(month.equals(Month.APRIL) && day == 1){
            isAp1 = true;
        }

        CommandRegistrationCallback.EVENT.register(CGLCommands::registerCommands);

        LOGGER.info("Complete!");
    }

    /**
     * Use this to get the ColoredGlowLib API instance and
     * use the methods to set colors and such
     *
     * It will return null if the server hasn't started yet.
     *
     * Aliases: {@link #getColoredGlowLib()} , {@link #getLib()}
     * */
    @Nullable
    public static ColoredGlowLibAPI getAPI(){
        return coloredGlowLib;
    }

    /**
     * Use this to get the ColoredGlowLib API instance and
     * use the methods to set colors and such
     *
     * It will return null if the server hasn't started yet.
     *
     * Aliases: {@link #getAPI()} , {@link #getLib()}
     * */
    @Nullable
    public static ColoredGlowLibAPI getColoredGlowLib(){
        return coloredGlowLib;
    }

    /**
     * Use this to get the ColoredGlowLib API instance and
     * use the methods to set colors and such
     *
     * It will return null if the server hasn't started yet.
     *
     * Aliases: {@link #getAPI()} , {@link #getAPI()}
     * */
    @Nullable
    public static ColoredGlowLibAPI getLib(){
        return coloredGlowLib;
    }


    /**
     * Called to register component factories for statically declared component types.
     *
     * <p><strong>The passed registry must not be held onto!</strong> Static component factories
     * must not be registered outside of this method.
     *
     * @param registry an {@link EntityComponentFactoryRegistry} for <em>statically declared</em> components
     */
    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(COLOR_COMPONENT, ColorComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerFor(LivingEntity.class, COLOR_COMPONENT, ColorComponent::new);
    }

    /**
     * Called to register component factories for statically declared component types.
     *
     * <p><strong>The passed registry must not be held onto!</strong> Static component factories
     * must not be registered outside of this method.
     *
     * @param registry a {@link ScoreboardComponentFactoryRegistry} for <em>statically declared</em> components
     */
    @Override
    public void registerScoreboardComponentFactories(ScoreboardComponentFactoryRegistry registry) {
        registry.registerScoreboardComponent(GLOBAL_COLOR_COMPONENT, GlobalColorComponent::new);
    }
}
