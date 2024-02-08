package me.emafire003.dev.coloredglowlib.component;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;

public class ColorComponent implements ComponentV3, AutoSyncedComponent, CGLComponent {


    private final LivingEntity self;

    //TODO the rainbow thing doesn't work, it's black
    protected String color = "#ffffff";

    public ColorComponent(LivingEntity livingEntity) {
        this.self = livingEntity;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {

        if(tag.contains("color")){
            this.color = tag.getString("color");
        }else{
            this.color = "#ffffff";
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
        this.color = color;
        ColoredGlowLibMod.COLOR_COMPONENT.sync(self);
    }

    public void clear(){
        this.color = "#ffffff";
        ColoredGlowLibMod.COLOR_COMPONENT.sync(self);
    }

}
