package me.emafire003.dev.coloredglowlib.mixin;

import me.emafire003.dev.coloredglowlib.client.ColoredGlowLibClient;
import me.emafire003.dev.coloredglowlib.component.ColorComponent;
import me.emafire003.dev.coloredglowlib.component.GlobalColorComponent;
import me.emafire003.dev.coloredglowlib.util.Color;
import me.emafire003.dev.coloredglowlib.util.ColorUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.scoreboard.Team;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.emafire003.dev.coloredglowlib.ColoredGlowLibMod.COLOR_COMPONENT;
import static me.emafire003.dev.coloredglowlib.ColoredGlowLibMod.GLOBAL_COLOR_COMPONENT;

@Environment(EnvType.CLIENT)
@Mixin(Entity.class)
public abstract class EntityColorMixin {

    @Shadow @Nullable public abstract Team getScoreboardTeam();

    @Shadow public abstract EntityType<?> getType();

    @Shadow public abstract boolean equals(Object o);

    private Entity entity = ((Entity)(Object)this);

    private Color jebcolor = new Color(255, 0, 0);

    /*public int nameSpecificColor(Entity entity){
        if(ColoredGlowLibClient.getPerNameColor()){
            int cvalue = ColoredGlowLibClient.getEntityColor(entity).getColorValue();
            if(cvalue == Color.getWhiteColor().getColorValue()){
                return -1;
            }else{
                return cvalue;
            }
        }else{
            return -1;
        }
    }*/


    //TODO test it
    private ColorUtils.RainbowChanger rainbowColor = new ColorUtils.RainbowChanger(255, 0, 0);

    /*public int rainbowColor(Entity entity){
        ColorComponent component = COLOR_COMPONENT.get(entity);
        //TODO see if it changes from entity ot entity/world/level/server whatever
        GlobalColorComponent globalColorComponent = GLOBAL_COLOR_COMPONENT.get(entity.getEntityWorld().getScoreboard());

        if(entity.getName().getString().equalsIgnoreCase("jeb_") || globalColorComponent.getGlobalRainbow()){
            rainbowColor.setRainbowColor(10);
            return rainbowColor.getColorValue();
        }else if(component.getIsRainbow()){
            rainbowColor.setRainbowColor(10);
            return(rainbowColor.getColorValue());
        }else if(globalColorComponent.getEntityTypeColor(entity.getType()).equalsIgnoreCase("rainbow")){
            rainbowColor.setRainbowColor(10);
            return(jebcolor.getColorValue());
        }else{
            return -1;
        }
    }*/

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
            if(ColoredGlowLibClient.isAp1()){
                cir.setReturnValue(new Color(255, 0, 174).getColorValue());
                return;
            }

            /**Checks if the entity is named _jeb*/
            if(entity.getName().getString().equalsIgnoreCase("jeb_")){
                cir.setReturnValue(getRainbowColor());
            }

            /**Checks if the GLOBAL color overrides everything*/
            if(globalComponent.getDefaultOverridesAll()){
                String color = globalComponent.getDefaultColor();

                //Checks if the global color is rainbow.
                if(color.equalsIgnoreCase("rainbow")){
                    cir.setReturnValue(getRainbowColor());
                }else{
                    cir.setReturnValue(ColorUtils.toColorValue(color));
                }
                return;
            }


            /**Checks if there is the EntityType color verrides the Entity's one
             *
             * If ( EntityType >> Entity-specific ) do stuff
             * */
            if(globalComponent.getEntityTypeOverridesEntityColor()){
                //Checks if the entitytype should glow rainbow
                if(globalComponent.getEntityTypeColor(entity.getType()).equalsIgnoreCase("rainbow")){
                    cir.setReturnValue(getRainbowColor());
                }
                cir.setReturnValue(ColorUtils.toColorValue(globalComponent.getEntityTypeColor(entity.getType())));
                return;
            }

            /**If nothing overrides the entity specific color, checks for entity specific color.
             * If it's the default one, it will check its entitytype*/
            String entity_col = component.getColor();
            //TODO make configurable? Like using the default color instead
            if(ColorUtils.checkDefault(entity_col)){
                //If the entity type's color is the default one as well, returns the default color
                String type_col = globalComponent.getEntityTypeColor(entity.getType());
                if(ColorUtils.checkDefault(type_col)){
                    //If nothing has been found, returns the default/global color.
                    cir.setReturnValue(ColorUtils.toColorValue(globalComponent.getDefaultColor()));
                }else{
                    cir.setReturnValue(ColorUtils.toColorValue(type_col));
                }
                return;
            }

            /**If it hasn't returned yet, it means that the entity has a specific color, so it returns it*/
            cir.setReturnValue(ColorUtils.toColorValue(entity_col));
        }
    }
}

