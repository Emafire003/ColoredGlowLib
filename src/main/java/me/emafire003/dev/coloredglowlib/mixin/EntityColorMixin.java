package me.emafire003.dev.coloredglowlib.mixin;

import me.emafire003.dev.coloredglowlib.ColoredGlowLib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.emafire003.dev.coloredglowlib.ColoredGlowLib.OVERRIDE_TEAM_COLORS;


@Mixin(Entity.class)
public abstract class EntityColorMixin {

    @Shadow @Nullable public abstract AbstractTeam getScoreboardTeam();


    @Shadow public abstract World getEntityWorld();

    @Shadow public abstract EntityType<?> getType();

    @Inject(method = "getTeamColorValue", at = @At("RETURN"), cancellable = true)
    public void injectChangeColorValue(CallbackInfoReturnable<Integer> cir){
        if(this.getScoreboardTeam() == null || this.getEntityWorld().getGameRules().getBoolean(OVERRIDE_TEAM_COLORS)) {
            if(ColoredGlowLib.getPerEntityTypeColor()){
                cir.setReturnValue(ColoredGlowLib.getEntityTypeColor(this.getType()).getColorValue());
            }else{
                cir.setReturnValue(ColoredGlowLib.getColor().getColorValue());
            }
        }
    }


}

