package me.emafire003.dev.coloredglowlib.mixin;

import me.emafire003.dev.coloredglowlib.ColoredGlowLibAPI;
import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import me.emafire003.dev.coloredglowlib.custom_data_animations.CustomColorAnimation;
import me.emafire003.dev.coloredglowlib.util.ColorUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.emafire003.dev.coloredglowlib.ColoredGlowLibMod.*;

@Environment(EnvType.CLIENT)
@Mixin(Entity.class)
public abstract class EntityColorMixin {

    /*public int nameSpecificColor(Entity entity){
        Maybe i'll add it maybe not
    }*/

    @Unique
    public int handleCustomColor(String color){
        if(color.startsWith("#")){
            color = color.replaceAll("#", "");
        }
        for(CustomColorAnimation customColorAnimation : ColoredGlowLibMod.getCustomColorAnimations()){
            if(color.equalsIgnoreCase(customColorAnimation.getName())){
                int color_index = customColorAnimation.getCurrentColorIndex();
                int current_ticks = customColorAnimation.getCurrentTicks();

                if(current_ticks > customColorAnimation.getColorAnimations().get(color_index).getActiveFor()){
                    color_index++;
                    current_ticks = 0;
                }
                if(color_index > customColorAnimation.getColorAnimations().size()-1){
                    color_index = 0;
                }

                //Sets the new values for the animation to proceed
                current_ticks++;
                customColorAnimation.setCurrentColorIndex(color_index);
                customColorAnimation.setCurrentTicks(current_ticks);
                //Returns the color from the color at index number "color_index"

                String custom_color = customColorAnimation.getColorAnimations().get(color_index).getColor();

                //Checks if it's a random/rainbow or custom color animation.
                if(custom_color.equalsIgnoreCase("rainbow")){
                    return getRainbowColor();
                }else if(custom_color.equalsIgnoreCase("random")){
                    return randomColor();
                }else if(!custom_color.equalsIgnoreCase(customColorAnimation.getName()) && ColorUtils.isCustomAnimation(custom_color)){
                    return handleCustomColor(custom_color);
                }

                //If nothing else has been found, it means it's an RGB color so returning it.
                return ColorUtils.toColorValue(custom_color);
            }
        }
        return -1;
    }

    @Unique
    private final ColorUtils.RainbowChanger rainbowColor = new ColorUtils.RainbowChanger(255, 0, 0);

    /**Returns the rainbow color*/
    @Unique
    private int getRainbowColor(){
        rainbowColor.setRainbowColor(10);
        return rainbowColor.getColorValue();
    }

    @Unique
    int random_delay_counter = 0;
    @Unique
    int prev_random_color = ColorUtils.toColorValue(ColorUtils.WHITE);

    @Unique
    private int randomColor(){
        Entity entity = ((Entity)(Object)this);
        Random r = entity.getWorld().getRandom();
        if(random_delay_counter == 10){
            random_delay_counter = 0;
            prev_random_color = ColorUtils.toColorValue(r.nextBetween(0, 255), r.nextBetween(0, 255), r.nextBetween(0, 255));
        }else{
            random_delay_counter++;
        }
        return prev_random_color;
    }

    @Inject(method = "getTeamColorValue", at = @At("RETURN"), cancellable = true)
    public void injectChangeColorValue(CallbackInfoReturnable<Integer> cir){
        Entity entity = ((Entity)(Object)this);
        ColoredGlowLibAPI cgl = ColoredGlowLibMod.getAPI();

        if(cgl == null){
            LOGGER.warn("The ColoredGlowLib API instance is null!");
            return;
        }

        if(entity == null){
            LOGGER.warn("The entity is null! Can't display the custom color!");
            return;
        }

        if(entity.getScoreboardTeam() == null || cgl.getOverrideTeamColors()) {

            /**Checks if it's april 1st for jokes*/
            if(ColoredGlowLibMod.isAp1){
                cir.setReturnValue(ColorUtils.toColorValue(255, 0, 174));
                return;
            }

            /**Checks if the entity is named jeb_. If it is, it will glow rainbow*/
            if(entity.getName().getString().equalsIgnoreCase("jeb_")){
                cir.setReturnValue(getRainbowColor());
                return;
            }

            /**Checks if the GLOBAL color overrides everything*/
            if(cgl.getDefaultOverridesAll()){
                String color = cgl.getGlobalColor();

                //Checks if the global color is rainbow.
                if(color.equalsIgnoreCase("rainbow")){
                    cir.setReturnValue(getRainbowColor());
                    return;
                }else if(color.equalsIgnoreCase("random")){
                    cir.setReturnValue(randomColor());
                    return;
                }else{
                    int custom = handleCustomColor(color);
                    if(custom != -1){
                        cir.setReturnValue(custom);
                        return;
                    }
                    cir.setReturnValue(ColorUtils.toColorValue(color));
                    return;
                }
            }

            /**Checks if there is the EntityType color overrides the Entity's one
             *<p>
             * If ( EntityType >> Entity-specific ) do stuff
             * */
            if(cgl.getEntityTypeColorOverridesEntityColor()){
                //Checks if the entitytype should glow rainbow
                String color = cgl.getColor(entity.getType());
                if(color.equalsIgnoreCase("rainbow")){
                    cir.setReturnValue(getRainbowColor());
                    return;
                } else if(color.equalsIgnoreCase("random")){
                    cir.setReturnValue(randomColor());
                    return;
                }
                int custom = handleCustomColor(color);
                if(custom != -1){
                    cir.setReturnValue(custom);
                    return;
                }
                cir.setReturnValue(ColorUtils.toColorValue(cgl.getColor(entity.getType())));
                return;
            }

            /**If nothing overrides the entity specific color, checks for entity specific color.
             * If it's the default one, it will check its entitytype, then the default color*/
            String entity_col = cgl.getColor(entity);

            //If the entity does not have a custom color, it will check for other things, like default color and entitytype color
            if(ColoredGlowLibMod.getAPI() != null && !ColoredGlowLibMod.getAPI().hasCustomColor(entity)){
                //If the entity type's color is the default one as well, returns the default color
                String type_col = cgl.getColor(entity.getType());
                //Checking if the enttiy has a custom color
                if(ColoredGlowLibMod.getAPI().hasCustomColor(entity.getType())){
                    if(type_col.equalsIgnoreCase("rainbow")){
                        //Checks if the entitytype is rainbow or random colored
                        cir.setReturnValue(getRainbowColor());
                        return;
                    }else if(type_col.equalsIgnoreCase("random")){
                        cir.setReturnValue(randomColor());
                        return;
                    }else{
                        int custom = handleCustomColor(type_col);
                        if(custom != -1){
                            cir.setReturnValue(custom);
                            return;
                        }
                        cir.setReturnValue(ColorUtils.toColorValue(type_col));
                        return;
                    }
                }

                //If nothing has been found, returns the default/global color.
                if(cgl.getGlobalColor().equalsIgnoreCase("rainbow")){
                    cir.setReturnValue(getRainbowColor());
                    return;
                }else if(cgl.getDefaultColor().equalsIgnoreCase("random")){
                    cir.setReturnValue(randomColor());
                    return;
                }else{
                    int custom = handleCustomColor(cgl.getDefaultColor());
                    if(custom != -1){
                        cir.setReturnValue(custom);
                        return;
                    }
                    cir.setReturnValue(ColorUtils.toColorValue(cgl.getDefaultColor()));
                    return;
                }
            }

            //It should be ending up here if the entity actually has a custom color, which entity_col
            /**Checks if the entity color is rainbow*/
            if(entity_col.equalsIgnoreCase("rainbow")){
                cir.setReturnValue(getRainbowColor());
                return;
            }else if(entity_col.equalsIgnoreCase("random")){
                cir.setReturnValue(randomColor());
                return;
            }else{
                int custom = handleCustomColor(entity_col);
                if(custom != -1){
                    cir.setReturnValue(custom);
                    return;
                }
            }


            //TODO maybe set back to white if the color animation gets removed?

            /**If it hasn't returned yet, it means that the entity has a specific color, so it returns it*/
            cir.setReturnValue(ColorUtils.toColorValue(entity_col));
        }
    }
}

