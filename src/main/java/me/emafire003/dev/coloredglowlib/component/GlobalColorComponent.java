package me.emafire003.dev.coloredglowlib.component;

import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import me.emafire003.dev.coloredglowlib.util.ColorUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class GlobalColorComponent implements Component, AutoSyncedComponent{

    public static final ComponentKey<GlobalColorComponent> GLOBAL_COLOR_COMPONENT =
            ComponentRegistry.getOrCreate(ColoredGlowLibMod.getIdentifier("global_color_component"), GlobalColorComponent.class);

    private final Scoreboard scoreboard;

    protected String default_color = ColorUtils.WHITE;
    protected boolean typeOverridesEntityColor = false;
    protected boolean defaultOverridesAll = false;
    protected boolean overrideTeamColors = false;

    protected CompoundTag entityTypeColorMap = new CompoundTag();

    public GlobalColorComponent(Scoreboard scoreboard, @Nullable MinecraftServer server) {
        this.scoreboard = scoreboard;
        //this.server = server;
    }

    @Override
    public void readData(ValueInput tag) {
        if(tag.contains("defaultColor")){
            this.default_color = tag.getStringOr("defaultColor", "#ffffff");
        }else{
            this.default_color = ColorUtils.WHITE;
        }

        if(tag.contains("typeOverridesEntityColor")){
            this.typeOverridesEntityColor = tag.getBooleanOr("typeOverridesEntityColor", false);
        }else{
            this.typeOverridesEntityColor = false;
        }

        if(tag.contains("defaultOverridesAll")){
            this.defaultOverridesAll= tag.getBooleanOr("defaultOverridesAll", false);
        }else{
            this.defaultOverridesAll = false;
        }

        if(tag.contains("overrideTeamColors")){
            this.overrideTeamColors = tag.getBooleanOr("overrideTeamColors", false);
        }else{
            this.overrideTeamColors = false;
        }

        if(tag.contains("entityTypeColorMap")){
            this.entityTypeColorMap = tag.read("entityTypeColorMap", CompoundTag.CODEC).get(); //.orElse(new NbtCompound());
        }else{
            this.entityTypeColorMap = new CompoundTag();
        }
    }

    @Override
    public void writeData(ValueOutput tag) {
        tag.putString("defaultColor", this.default_color);
        tag.putBoolean("typeOverridesEntityColor", this.typeOverridesEntityColor);
        tag.putBoolean("defaultOverridesAll", this.defaultOverridesAll);
        tag.putBoolean("overrideTeamColors", this.overrideTeamColors);
        tag.store("entityTypeColorMap", CompoundTag.CODEC, this.entityTypeColorMap);
    }

    public HashMap<EntityType<?>, String> getEntityTypeColorMap(){
        HashMap<EntityType<?>, String> map = new HashMap<>();
        List<String> keys = new ArrayList<>(this.entityTypeColorMap.keySet());
        keys.forEach((key) -> {
            Optional<EntityType<?>> type = EntityType.byString(key);
            String color = this.entityTypeColorMap.getStringOr(key, "#ffffff");
            type.ifPresent(entityType -> map.put(entityType, color));
        });
        return map;
    }

    /**
     * @param type An entity type
     * @param color A hex color or "rainbow"*/
    public void addEntityTypeColor(EntityType<?> type, String color){
        entityTypeColorMap.putString(type.toString(), color);
        GLOBAL_COLOR_COMPONENT.sync(scoreboard);
    }

    /**
     *
     * WARNING! THIS CANNOT BE USED TO CLEAR A TYPE! USE clearEntityTypeColor INSTEAD!
     *
     * @param type An entity type
     * @param color A hex color or "rainbow"*/
    public void setEntityTypeColor(EntityType<?> type, String color){
        if(entityTypeColorMap.contains(type.toString())){
            entityTypeColorMap.remove(type.toString());
        }
        //This other method calls the sync
        addEntityTypeColor(type, color);
    }

    public void clearEntityTypeColor(EntityType<?> type){
        entityTypeColorMap.remove(type.toString());
        GLOBAL_COLOR_COMPONENT.sync(scoreboard);
    }

    public String getEntityTypeColor(EntityType<?> type){
        String color = entityTypeColorMap.getStringOr(type.toString(), "#ffffff");
        if(color == null || color.equalsIgnoreCase("")){
            return ColorUtils.WHITE;
        }
        return entityTypeColorMap.getStringOr(type.toString(), "#ffffff");
    }

    public String getDefaultColor(){
        return this.default_color;
    }

    public void setDefaultColor(String default_color) {
        this.default_color = default_color;
        GLOBAL_COLOR_COMPONENT.sync(scoreboard);
    }

    public boolean getEntityTypeOverridesEntityColor() {
        return this.typeOverridesEntityColor;
    }

    public void setTypeOverridesEntityColor(boolean b) {
        this.typeOverridesEntityColor = b;
        GLOBAL_COLOR_COMPONENT.sync(scoreboard);
    }

    public boolean getDefaultOverridesAll() {
        return this.defaultOverridesAll;
    }

    public void setDefaultOverridesAll(boolean b) {
        this.defaultOverridesAll = b;
        GLOBAL_COLOR_COMPONENT.sync(scoreboard);
    }

    public boolean getOverrideTeamColors() {
        return this.overrideTeamColors;
    }

    public void setOverrideTeamColors(boolean b) {
        this.overrideTeamColors = b;
        GLOBAL_COLOR_COMPONENT.sync(scoreboard);
    }

    public void clear(){
        this.default_color = ColorUtils.WHITE;
        this.typeOverridesEntityColor = false;
        this.defaultOverridesAll = false;
        this.overrideTeamColors = false;
        this.entityTypeColorMap = new CompoundTag();
        GLOBAL_COLOR_COMPONENT.sync(scoreboard);
    }
}
