package me.emafire003.dev.coloredglowlib.mixin;

import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import me.emafire003.dev.coloredglowlib.util.DataSaver;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class ClearGlowOnDeath {

    @Inject(method = "Lnet/minecraft/world/entity/Entity;remove(Lnet/minecraft/world/entity/Entity$RemovalReason;)V", at = @At("HEAD"))
    public void injectRemoveGlow(Entity.RemovalReason pReason, CallbackInfo ci) {
        if(ColoredGlowLibMod.getLib().getEntityRainbowColor(((Entity)(Object)this))){
            ColoredGlowLibMod.getLib().setRainbowColorToEntity(((Entity)(Object)this), false);
        }
        if(ColoredGlowLibMod.getLib().hasEntityColor(((Entity)(Object)this))){
            ColoredGlowLibMod.getLib().removeColorFromEntity(((Entity)(Object)this));

        }
        DataSaver.write();
    }
}
