package me.emafire003.dev.coloredglowlib.mixin;

import me.emafire003.dev.coloredglowlib.ColoredGlowLib;
import me.emafire003.dev.coloredglowlib.util.DataSaver;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

import static me.emafire003.dev.coloredglowlib.ColoredGlowLib.LOGGER;

@Mixin(Entity.class)
public abstract class ClearGlowOnDeath {

    @Inject(method = "remove", at = @At("HEAD"))
    public void injectRemoveGlow(CallbackInfo ci) {
        if(ColoredGlowLib.getEntityRainbowColor(((Entity)(Object)this))){
            ColoredGlowLib.setRainbowColorToEntity(((Entity)(Object)this), false);
            DataSaver.write();
        }
        if(ColoredGlowLib.hasEntityColor(((Entity)(Object)this))){
            ColoredGlowLib.removeColorFromEntity(((Entity)(Object)this));
            DataSaver.write();
        }
    }
}
