package me.emafire003.dev.coloredglowlib.mixin;

import me.emafire003.dev.coloredglowlib.ColoredGlowLib;
import me.emafire003.dev.coloredglowlib.client.ColoredGlowLibClient;
import me.emafire003.dev.coloredglowlib.util.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.emafire003.dev.coloredglowlib.ColoredGlowLib.*;

@Environment(EnvType.CLIENT)
@Mixin(Entity.class)
public abstract class EntityColorMixin {

    @Shadow @Nullable public abstract AbstractTeam getScoreboardTeam();

    @Shadow public abstract World getEntityWorld();

    @Shadow public abstract EntityType<?> getType();

    @Shadow public abstract Text getName();

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
        if(entity.getName().asString().equalsIgnoreCase("jeb_") || ColoredGlowLibClient.getRainbowChangingColor()){
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

    @Inject(method = "getTeamColorValue", at = @At("RETURN"), cancellable = true)
    public void injectChangeColorValue(CallbackInfoReturnable<Integer> cir){
        if(this.getScoreboardTeam() == null || ColoredGlowLib.getOverrideTeamColors() || this.getEntityWorld().getGameRules().getBoolean(OVERRIDE_TEAM_COLORS)) {
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
            cir.setReturnValue(ColoredGlowLib.getColor().getColorValue());

        }
    }
}

