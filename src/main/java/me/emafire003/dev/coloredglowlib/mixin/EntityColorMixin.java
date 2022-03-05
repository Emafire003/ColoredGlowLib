package me.emafire003.dev.coloredglowlib.mixin;

import me.emafire003.dev.coloredglowlib.ColoredGlowLib;
import me.emafire003.dev.coloredglowlib.util.Color;
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

import java.util.UUID;

import static me.emafire003.dev.coloredglowlib.ColoredGlowLib.OVERRIDE_TEAM_COLORS;
import static me.emafire003.dev.coloredglowlib.ColoredGlowLib.per_entity_color_map;


@Mixin(Entity.class)
public abstract class EntityColorMixin {

    @Shadow @Nullable public abstract AbstractTeam getScoreboardTeam();

    @Shadow public abstract World getEntityWorld();

    @Shadow public abstract EntityType<?> getType();

    @Shadow public abstract Text getName();

    @Shadow public abstract UUID getUuid();

    private Color jebcolor = new Color(255, 0, 0);

    @Inject(method = "getTeamColorValue", at = @At("RETURN"), cancellable = true)
    public void injectChangeColorValue(CallbackInfoReturnable<Integer> cir){
        if(this.getScoreboardTeam() == null || ColoredGlowLib.getOverrideTeamColors() || this.getEntityWorld().getGameRules().getBoolean(OVERRIDE_TEAM_COLORS)) {

            if(this.getName().asString().equalsIgnoreCase("jeb_") || ColoredGlowLib.getRainbowChangingColor()){
                jebcolor.setRainbowColor(10);
                cir.setReturnValue(jebcolor.getColorValue());
                return;
            }
            else{
                //Checks if the entity itself has a glow color if the per-entitycolor is enabled
                if(ColoredGlowLib.getPerEntityColor()){
                    if(ColoredGlowLib.getEntityRainbowColor(((Entity)(Object)this))){
                        jebcolor.setRainbowColor(10);
                        cir.setReturnValue(jebcolor.getColorValue());
                    }else{
                        cir.setReturnValue(ColoredGlowLib.getEntityColor(((Entity)(Object)this)).getColorValue());
                    }
                    return;
                }
                //Checks for the entitytype (if it's enabled)
                if(ColoredGlowLib.getPerEntityTypeColor()){
                    if(ColoredGlowLib.getEntityTypeRainbowColor(this.getType())){
                        jebcolor.setRainbowColor(10);
                        cir.setReturnValue(jebcolor.getColorValue());
                    }else{
                        cir.setReturnValue(ColoredGlowLib.getEntityTypeColor(this.getType()).getColorValue());
                    }
                }else{
                    cir.setReturnValue(ColoredGlowLib.getColor().getColorValue());
                    return;
                }
            }

        }
    }
}

