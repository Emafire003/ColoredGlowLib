package me.emafire003.dev.coloredglowlib.component;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import me.emafire003.dev.coloredglowlib.util.ColorUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import static me.emafire003.dev.coloredglowlib.ColoredGlowLibMod.MOD_ID;

public class ColorComponent implements ComponentV3, AutoSyncedComponent, CGLComponent {

    public static final ComponentKey<ColorComponent> COLOR_COMPONENT =
            ComponentRegistry.getOrCreate(new Identifier(MOD_ID, "color_component"), ColorComponent.class);

    private final LivingEntity self;

    //TODO the rainbow thing doesn't work, it's black
    protected String color = ColorUtils.WHITE;

    public ColorComponent(LivingEntity livingEntity) {
        this.self = livingEntity;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {

        if(tag.contains("color")){
            this.color = tag.getString("color");
        }else{
            this.color = ColorUtils.WHITE;
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putString("color", this.color);
    }

    /**
     * @return  A hex color or "rainbow". If no color has been set, returns the default white one*/
    public String getColor(){
        return this.color;
    }

    /**
     * @param color A hex color or "rainbow"*/
    public void setColor(String color) {
        ColoredGlowLibMod.LOGGER.info("Setting color to entity ( "+ this.self + ") : " + color);
        this.color = color;
        COLOR_COMPONENT.sync(self);
    }

    public void clear(){
        this.color = ColorUtils.WHITE;
        ColoredGlowLibMod.LOGGER.info("Clearing the color of ( "+ this.self + ") : " + color);
        ColoredGlowLibMod.LOGGER.info("The entity color: " + getColor());
        COLOR_COMPONENT.sync(self);
    }

}
