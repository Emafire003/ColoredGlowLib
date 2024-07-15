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

import java.util.*;

public class ColorComponent implements ComponentV3, AutoSyncedComponent{

    public static final ComponentKey<ColorComponent> COLOR_COMPONENT =
            ComponentRegistry.getOrCreate(ColoredGlowLibMod.getIdentifier("color_component"), ColorComponent.class);

    private final Entity self;

    protected String color = ColorUtils.WHITE;
    protected NbtCompound exclusiveTargetColorMap = new NbtCompound();
    protected NbtCompound exclusiveTeamColorMap = new NbtCompound();


    public ColorComponent(Entity entity) {
        this.self = entity;
    }


    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if(tag.contains("color")){
            this.color = tag.getString("color");
        }else{
            this.color = ColorUtils.WHITE;
        }
        if(tag.contains("exclusiveTargetColorMap")){
            this.exclusiveTargetColorMap = tag.getCompound("exclusiveTargetColorMap");
        }else{
            this.exclusiveTargetColorMap = new NbtCompound();
        }

        if(tag.contains("exclusiveTeamColorMap")){
            this.exclusiveTargetColorMap = tag.getCompound("exclusiveTeamColorMap");
        }else{
            this.exclusiveTargetColorMap = new NbtCompound();
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putString("color", this.color);
        tag.put("exclusiveTargetColorMap", exclusiveTargetColorMap);
        tag.put("exclusiveTeamColorMap", exclusiveTeamColorMap);
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
        List<String> keys = new ArrayList<>(this.exclusiveTargetColorMap.getKeys());
        keys.forEach((key) -> {
            UUID uuid = UUID.fromString(key);
            String color = this.exclusiveTargetColorMap.getString(key);
            map.put(uuid, color);
        });
        return map;
    }

    //TODO maybe add dedicated team things

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
        String color = exclusiveTargetColorMap.getString(uuid.toString());
        if(color == null || color.equalsIgnoreCase("")){
            return ColorUtils.WHITE;
        }
        return exclusiveTargetColorMap.getString(uuid.toString());
    }



    public HashMap<String, String> getExclusiveTeamColorMap(){
        HashMap<String, String> map = new HashMap<>();
        List<String> keys = new ArrayList<>(this.exclusiveTeamColorMap.getKeys());
        keys.forEach((key) -> {
            //key == team name
            String color = this.exclusiveTeamColorMap.getString(key);
            map.put(key, color);
        });
        return map;
    }

    /**
     * @param teamName The name of the team that will see the specific color
     * @param color A hex color or "rainbow"*/
    public void addExclusiveColorForTeam(String teamName, String color){
        exclusiveTeamColorMap.putString(teamName, color);
        COLOR_COMPONENT.sync(self);
    }

    /**
     *
     * WARNING! THIS CANNOT BE USED TO CLEAR A TYPE! USE clearExclusiveColor INSTEAD!
     *
     * @param teamName The name of the team that will see the specific color
     * @param color A hex color or "rainbow"*/
    public void setExclusiveColorForTeam(String teamName, String color){
        if(exclusiveTeamColorMap.contains(teamName)){
            exclusiveTeamColorMap.remove(teamName);
        }
        //This other method calls the sync
        addExclusiveColorForTeam(teamName, color);
    }

    public void clearExclusiveColorForTeam(String teamName){
        exclusiveTeamColorMap.remove(teamName);
        COLOR_COMPONENT.sync(self);
    }

    public String getExclusiveColorForTeam(String teamName){
        String color = exclusiveTeamColorMap.getString(teamName);
        if(color == null || color.equalsIgnoreCase("")){
            return ColorUtils.WHITE;
        }
        return exclusiveTeamColorMap.getString(teamName);
    }

    public void clear(){
        this.color = ColorUtils.WHITE;
        this.exclusiveTargetColorMap = new NbtCompound();
        this.exclusiveTeamColorMap = new NbtCompound();
        COLOR_COMPONENT.sync(self);
    }
}
