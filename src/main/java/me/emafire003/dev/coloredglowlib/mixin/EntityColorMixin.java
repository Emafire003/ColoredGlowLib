package me.emafire003.dev.coloredglowlib.mixin;

import me.emafire003.dev.coloredglowlib.client.ColoredGlowLibClient;
import me.emafire003.dev.coloredglowlib.util.Color;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.scores.Team;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(Entity.class)
public abstract class EntityColorMixin {

    @Shadow @Nullable
    public abstract Team getTeam();

    @Shadow public abstract EntityType<?> getType();


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

    public int entitySpecificColor(Entity entity){
        if(ColoredGlowLibClient.getPerEntityColor()){
            int cvalue = ColoredGlowLibClient.getEntityColor(entity).getColorValue();
            if(cvalue == Color.getWhiteColor().getColorValue()){
                return -1;
            }else{
                return cvalue;
            }
        }else{
            return -1;
        }
    }

    public int entityTypeSpecificColor(EntityType type){
        if(ColoredGlowLibClient.getPerEntityTypeColor()){
            int cvalue = ColoredGlowLibClient.getEntityTypeColor(type).getColorValue();
            if(cvalue == Color.getWhiteColor().getColorValue()){
                return -1;
            }else{
                return cvalue;
            }
        }else{
            return -1;
        }
    }

    public int rainbowColor(Entity entity){
        if(entity.getName().getString().equalsIgnoreCase("jeb_") || ColoredGlowLibClient.getRainbowChangingColor()){
            jebcolor.setRainbowColor(10);
            return jebcolor.getColorValue();
        }else if(ColoredGlowLibClient.getEntityRainbowColor(entity)){
            jebcolor.setRainbowColor(10);
            return(jebcolor.getColorValue());
        }else if(ColoredGlowLibClient.getEntityTypeRainbowColor(this.getType())){
            jebcolor.setRainbowColor(10);
            return(jebcolor.getColorValue());
        }else{
            return -1;
        }
    }

    @Inject(method = "getTeamColor", at = @At("RETURN"), cancellable = true)
    public void injectChangeColorValue(CallbackInfoReturnable<Integer> cir){

        if(this.getTeam() == null || ColoredGlowLibClient.getOverrideTeamColors() /*|| this.getEntityWorld().getGameRules().getBoolean(OVERRIDE_TEAM_COLORS)*/) {

            if(ColoredGlowLibClient.isAp1()){
                cir.setReturnValue(new Color(255, 0, 174).getColorValue());
                return;
            }
            //Checks if it should glow rainbow
            int rainbow_col = rainbowColor(entity);
            if(rainbow_col != -1){
                cir.setReturnValue(rainbow_col);
                return;
            }
            //If not, checks if there is a specific color for an entity
            int entity_col = entitySpecificColor(entity);
            if(entity_col != -1){
                cir.setReturnValue(entity_col);
                return;
            }

            //If not, checks if there is a specific color for an entitytype
            int type_col = entityTypeSpecificColor(entity.getType());
            if(type_col != -1){
                cir.setReturnValue(type_col);
                return;
            }

            //If nothing else has been found, set the generalized one.
            if(!ColoredGlowLibClient.getColor().equals(Color.getWhiteColor())){
                cir.setReturnValue(ColoredGlowLibClient.getColor().getColorValue());
            }

        }
    }
}

