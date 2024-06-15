package me.emafire003.dev.coloredglowlib;

import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import org.ladysnake.cca.api.v3.scoreboard.ScoreboardComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.scoreboard.ScoreboardComponentInitializer;
import me.emafire003.dev.coloredglowlib.command.CGLCommands;
import me.emafire003.dev.coloredglowlib.component.ColorComponent;
import me.emafire003.dev.coloredglowlib.component.GlobalColorComponent;
import me.emafire003.dev.coloredglowlib.custom_data_animations.CGLResourceManager;
import me.emafire003.dev.coloredglowlib.custom_data_animations.ColorAnimationItem;
import me.emafire003.dev.coloredglowlib.custom_data_animations.CustomColorAnimation;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static me.emafire003.dev.coloredglowlib.component.ColorComponent.COLOR_COMPONENT;
import static me.emafire003.dev.coloredglowlib.component.GlobalColorComponent.GLOBAL_COLOR_COMPONENT;
import static me.emafire003.dev.coloredglowlib.util.ColorUtils.isValidColorOrCustom;

public class ColoredGlowLibMod implements ModInitializer, EntityComponentInitializer, ScoreboardComponentInitializer {

    public static String MOD_ID = "coloredglowlib";
    public static Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static String PREFIX = "§f[§cC§6o§el§ao§3r§9e§5d§cG§6l§eo§aw§3L§9i§5b§f] §7";
        // Old prefix: "§f[§1C§2o§3l§4o§5r§6e§7d§8G§9l§ao§bw§cL§di§eb§f] §r";

    private static ColoredGlowLibAPI coloredGlowLib;
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

        CGLResourceManager.register();

        CommandRegistrationCallback.EVENT.register(CGLCommands::registerCommands);

        LOGGER.info("Complete!");
    }

    /**Used (internally) to get an identifier with this mod's namespace*/
    public static Identifier getIdentifier(String path){
        return Identifier.of(MOD_ID, path);
    }

    /**
     * Use this to get the ColoredGlowLib API instance and
     * use the methods to set colors and such
     * <p>
     * It will return null if the server hasn't started yet.
     * <p>
     * Aliases: {@link #getColoredGlowLib()} , {@link #getLib()}
     * */
    @Nullable
    public static ColoredGlowLibAPI getAPI(){
        return coloredGlowLib;
    }

    /**
     * Use this to get the ColoredGlowLib API instance and
     * use the methods to set colors and such
     *<p>
     * It will return null if the server hasn't started yet.
     *<p>
     * Aliases: {@link #getAPI()} , {@link #getLib()}
     * */
    @Nullable
    public static ColoredGlowLibAPI getColoredGlowLib(){
        return coloredGlowLib;
    }

    /**
     * Use this to get the ColoredGlowLib API instance and
     * use the methods to set colors and such
     * <p>
     * It will return null if the server hasn't started yet.
     *<p>
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
        registry.registerFor(Entity.class, COLOR_COMPONENT, ColorComponent::new);
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

    private static final List<CustomColorAnimation> custom_color_animations = new ArrayList<>();

    private static final List<CustomColorAnimation> maybe_wrong_color_animations = new ArrayList<>();

    /**
     * Returns false if there were issues reading colors
     *<p>
     * */
    public static boolean loadCustomColorAnimation(CustomColorAnimation animation){
        for(CustomColorAnimation customColorAnimation : custom_color_animations){
            if(animation.getName().equalsIgnoreCase(customColorAnimation.getName())){
                LOGGER.error("The color animation '" + animation.getName() + "' wasn't loaded because there already is an animation with that name!!");
                return false;
            }
        }
        for(ColorAnimationItem color_item : animation.getColorAnimations()){
            if(color_item.getColor().equalsIgnoreCase(animation.getName())){
                LOGGER.error("The color animation '" + animation.getName() + "' wasn't loaded because you can't set a color to be the same as the name of the animation");
                return false;
            }
            if(!isValidColorOrCustom(color_item.getColor())){
                maybe_wrong_color_animations.add(animation);
                LOGGER.warn("The color animation '" + animation.getName() + "' wasn't loaded yet because '" + color_item.getColor() + "' isn't a valid color! Maybe it's a custom animation processed later, flagging this for a later check!");
                return false;
            }
        }
        custom_color_animations.add(animation);
        //TODO move to .debug?
        LOGGER.info("The custom color animation '" + animation.getName() + "' was loaded successfully!");
        return true;
    }

    /**Gets called after every datapack finished loading to see if an animation name
     * that is used inside a datapack is valid or if it's wrong
     * */
    public static void checkMaybeWrongAnimations(){
        for(CustomColorAnimation animation : maybe_wrong_color_animations){
            boolean should_add = true;
            for(ColorAnimationItem color_item : animation.getColorAnimations()){
                if(!isValidColorOrCustom(color_item.getColor())){
                    LOGGER.error("The color animation '" + animation.getName() + "' wasn't loaded because '" + color_item.getColor() + "' isn't a valid color! Use #RRGGBB or 'rainbow' or 'random' or another color animation name!");
                    should_add = false;
                    break;
                }
            }
            if(should_add){
                custom_color_animations.add(animation);
                //TODO move to .debug?
                LOGGER.info("The custom color animation '" + animation.getName() + "' was loaded successfully!");
            }
        }
        maybe_wrong_color_animations.clear();

    }

    public static List<CustomColorAnimation> getCustomColorAnimations(){
        return custom_color_animations;
    }
}
