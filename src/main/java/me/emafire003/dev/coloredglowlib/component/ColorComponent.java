package me.emafire003.dev.coloredglowlib.component;

import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import net.minecraft.entity.Entity;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.ComponentV3;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import me.emafire003.dev.coloredglowlib.util.ColorUtils;
import net.minecraft.nbt.NbtCompound;

public class ColorComponent implements ComponentV3, AutoSyncedComponent{

    public static final ComponentKey<ColorComponent> COLOR_COMPONENT =
            ComponentRegistry.getOrCreate(ColoredGlowLibMod.getIdentifier("color_component"), ColorComponent.class);

    private final Entity self;

    protected String color = ColorUtils.WHITE;

    public ColorComponent(Entity entity) {
        this.self = entity;
    }

    /**
     * @return  A hex color or "rainbow". If no color has been set, returns the default white one*/
    public String getColor(){
        return this.color;
    }

    /**
     * @param color A hex color or "rainbow"*/
    public void setColor(String color) {
        this.color = color;
        COLOR_COMPONENT.sync(self);
    }

    public void clear(){
        this.color = ColorUtils.WHITE;
        COLOR_COMPONENT.sync(self);
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if(tag.contains("color")){
            this.color = tag.getString("color");
        }else{
            this.color = ColorUtils.WHITE;
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putString("color", this.color);
    }
}
