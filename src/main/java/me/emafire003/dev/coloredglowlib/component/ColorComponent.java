package me.emafire003.dev.coloredglowlib.component;

import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import me.emafire003.dev.coloredglowlib.util.ColorUtils;
import net.minecraft.nbt.CompoundTag;

import java.util.*;

public class ColorComponent implements Component, AutoSyncedComponent{

    public static final ComponentKey<ColorComponent> COLOR_COMPONENT =
            ComponentRegistry.getOrCreate(ColoredGlowLibMod.getIdentifier("color_component"), ColorComponent.class);

    private final Entity self;

    protected String color = ColorUtils.WHITE;
    protected CompoundTag exclusiveTargetColorMap = new CompoundTag();

    public ColorComponent(Entity entity) {
        this.self = entity;
    }


    @Override
    public void readData(ValueInput tag) {

        if(tag.contains("color")){
            this.color = tag.getStringOr("color", "#ffffff"); //means something is VERY wrong
        }else{
            this.color = ColorUtils.WHITE;
        }
        if(tag.contains("exclusiveTargetColorMap")){
            //TODO for now i like that it crashes
            this.exclusiveTargetColorMap = tag.read("exclusiveTargetColorMap", CompoundTag.CODEC).get();//.orElse(new NbtCompound());
        }else{
            this.exclusiveTargetColorMap = new CompoundTag();
        }
    }

    @Override
    public void writeData(ValueOutput tag) {
        tag.putString("color", this.color);
        tag.store("exclusiveTargetColorMap", CompoundTag.CODEC, exclusiveTargetColorMap);
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

    public HashMap<UUID, String> getExclusiveTargetColorMap(){
        HashMap<UUID, String> map = new HashMap<>();
        List<String> keys = new ArrayList<>(this.exclusiveTargetColorMap.keySet());
        keys.forEach((key) -> {
            UUID uuid = UUID.fromString(key);
            String color = this.exclusiveTargetColorMap.getStringOr(key, "#ffffff");
            map.put(uuid, color);
        });
        return map;
    }

    /**
     * @param uuid The uuid of the player that will see the specific color
     * @param color A hex color or "rainbow"*/
    public void addExclusiveColorFor(UUID uuid, String color){
        exclusiveTargetColorMap.putString(uuid.toString(), color);
        COLOR_COMPONENT.sync(self);
    }

    /**
     *
     * WARNING! THIS CANNOT BE USED TO CLEAR A TYPE! USE clearExclusiveColor INSTEAD!
     *
     * @param uuid The uuid of the player that will see the specific color
     * @param color A hex color or "rainbow"*/
    public void setExclusiveColorFor(UUID uuid, String color){
        if(exclusiveTargetColorMap.contains(uuid.toString())){
            exclusiveTargetColorMap.remove(uuid.toString());
        }
        //This other method calls the sync
        addExclusiveColorFor(uuid, color);
    }

    public void clearExclusiveColorFor(UUID uuid){
        exclusiveTargetColorMap.remove(uuid.toString());
        COLOR_COMPONENT.sync(self);
    }

    public String getExclusiveColorFor(UUID uuid){
        String color = exclusiveTargetColorMap.getStringOr(uuid.toString(), "#ffffff");
        if(color == null || color.equalsIgnoreCase("")){
            return ColorUtils.WHITE;
        }
        return exclusiveTargetColorMap.getStringOr(uuid.toString(), "#ffffff");
    }

    public void clear(){
        this.color = ColorUtils.WHITE;
        this.exclusiveTargetColorMap = new CompoundTag();
        COLOR_COMPONENT.sync(self);
    }


}
