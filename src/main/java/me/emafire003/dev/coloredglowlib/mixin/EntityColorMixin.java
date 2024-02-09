package me.emafire003.dev.coloredglowlib.mixin;

import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import me.emafire003.dev.coloredglowlib.component.ColorComponent;
import me.emafire003.dev.coloredglowlib.component.GlobalColorComponent;
import me.emafire003.dev.coloredglowlib.util.ColorUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.scoreboard.Team;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.emafire003.dev.coloredglowlib.ColoredGlowLibMod.*;

@Environment(EnvType.CLIENT)
@Mixin(Entity.class)
public abstract class EntityColorMixin {

    @Shadow @Nullable public abstract Team getScoreboardTeam();

    @Shadow public abstract boolean equals(Object o);

    private final Entity entity = ((Entity)(Object)this);

    /*public int nameSpecificColor(Entity entity){
        Maybe i'll add it maybe not
    }*/


    private final ColorUtils.RainbowChanger rainbowColor = new ColorUtils.RainbowChanger(255, 0, 0);

    /**Returns the rainbow color*/
    private int getRainbowColor(){
        rainbowColor.setRainbowColor(10);
        return rainbowColor.getColorValue();
    }

    @Inject(method = "getTeamColorValue", at = @At("RETURN"), cancellable = true)
    public void injectChangeColorValue(CallbackInfoReturnable<Integer> cir){

        ColorComponent component = COLOR_COMPONENT.get(entity);
        //TODO VERYFY IT DOESNT MESS UP BETWEEN WORLDS
        GlobalColorComponent globalComponent = GLOBAL_COLOR_COMPONENT.get(entity.getEntityWorld().getScoreboard());

        if(this.getScoreboardTeam() == null || globalComponent.getOverrideTeamColors()) {

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
            if(globalComponent.getDefaultOverridesAll()){
                String color = globalComponent.getDefaultColor();

                //Checks if the global color is rainbow.
                if(color.equalsIgnoreCase("rainbow")){

                    cir.setReturnValue(getRainbowColor());
                    return;
                }else{
                    cir.setReturnValue(ColorUtils.toColorValue(color));
                    return;
                }
            }


            /**Checks if there is the EntityType color overrides the Entity's one
             *
             * If ( EntityType >> Entity-specific ) do stuff
             * */
            if(globalComponent.getEntityTypeOverridesEntityColor()){
                //Checks if the entitytype should glow rainbow
                if(globalComponent.getEntityTypeColor(entity.getType()).equalsIgnoreCase("rainbow")){
                    cir.setReturnValue(getRainbowColor());
                    return;
                }
                cir.setReturnValue(ColorUtils.toColorValue(globalComponent.getEntityTypeColor(entity.getType())));
                return;
            }

            /**If nothing overrides the entity specific color, checks for entity specific color.
             * If it's the default one, it will check its entitytype, then the default color*/
            String entity_col = component.getColor();

            //TODO make configurable? Like using the default color instead. The has custom color i mean
            if(ColoredGlowLibMod.getAPI() != null && ColoredGlowLibMod.getAPI().hasCustomColor(entity)){
                //If the entity type's color is the default one as well, returns the default color
                String type_col = globalComponent.getEntityTypeColor(entity.getType());

                if(ColoredGlowLibMod.getAPI().hasCustomColor(entity.getType())){
                    if(type_col.equalsIgnoreCase("rainbow")){
                        //Checks if the entitytype is rainbow colored
                        cir.setReturnValue(getRainbowColor());
                        return;
                    }else{
                        cir.setReturnValue(ColorUtils.toColorValue(type_col));
                        return;
                    }
                }

                //If nothing has been found, returns the default/global color.
                if(globalComponent.getDefaultColor().equalsIgnoreCase("rainbow")){
                    cir.setReturnValue(getRainbowColor());
                    return;
                }else{
                    cir.setReturnValue(ColorUtils.toColorValue(globalComponent.getDefaultColor()));
                    return;
                }
            }

            /**Checks if the entity color is rainbow*/
            if(entity_col.equalsIgnoreCase("rainbow")){
                cir.setReturnValue(getRainbowColor());
                return;
            }
            /**If it hasn't returned yet, it means that the entity has a specific color, so it returns it*/
            cir.setReturnValue(ColorUtils.toColorValue(entity_col));
        }
    }
}

