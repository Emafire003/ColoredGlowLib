package me.emafire003.dev.coloredglowlib.mixin;

import me.emafire003.dev.coloredglowlib.ColoredGlowLib;
import net.minecraft.entity.Entity;
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

    @Inject(method = "getTeamColorValue", at = @At("RETURN"), cancellable = true)
    public void injectChangeColorValue(CallbackInfoReturnable<Integer> cir){
        //Gamerule to ovveride the team color
        //System.out.println("Color " + this.getColor().getColorValue());
        if(this.getScoreboardTeam() == null || this.getEntityWorld().getGameRules().getBoolean(OVERRIDE_TEAM_COLORS)) {
            cir.setReturnValue(ColoredGlowLib.getColor().getColorValue());
        }
    }


}

