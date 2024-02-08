package me.emafire003.dev.coloredglowlib.component;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class GlobalColorComponent implements ComponentV3, AutoSyncedComponent, CGLComponent {


    private final Scoreboard scoreboard;
    private final MinecraftServer server;

    protected String default_color = "#ffffff";
    protected boolean typeOverridesEntityColor = false;
    protected boolean defaultOverridesAll = false;
    protected boolean overrideTeamColors = false;

    protected NbtCompound entityTypeColorMap = new NbtCompound();

    public GlobalColorComponent(Scoreboard scoreboard, @Nullable MinecraftServer server) {
        this.scoreboard = scoreboard;
        this.server = server;
    }

    //TODO remove (after testing everything else)
    //represent a list of strings https://minecraft.wiki/w/NBT_format
    private final byte listType = 8;

    @Override
    public void readFromNbt(NbtCompound tag) {

        if(tag.contains("defaultColor")){
            this.default_color = tag.getString("defaultColor");
        }else{
            this.default_color = "#ffffff";
        }

        if(tag.contains("typeOverridesEntityColor")){
            this.typeOverridesEntityColor = tag.getBoolean("typeOverridesEntityColor");
        }else{
            this.typeOverridesEntityColor = false;
        }

        if(tag.contains("defaultOverridesAll")){
            this.defaultOverridesAll= tag.getBoolean("defaultOverridesAll");
        }else{
            this.defaultOverridesAll = false;
        }

        if(tag.contains("overrideTeamColors")){
            this.overrideTeamColors = tag.getBoolean("overrideTeamColors");
        }else{
            this.overrideTeamColors = false;
        }

        if(tag.contains("entityTypeColorMap")){
            this.entityTypeColorMap = tag.getCompound("entityTypeColorMap");
        }else{
            this.entityTypeColorMap = new NbtCompound();
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putString("color", this.default_color);
        tag.putBoolean("typeOverridesEntityColor", this.typeOverridesEntityColor);
        tag.putBoolean("defaultOverridesAll", this.defaultOverridesAll);
        tag.putBoolean("overrideTeamColors", this.overrideTeamColors);
        //TODO need to test these
        tag.put("entityTypeColorMap", this.entityTypeColorMap);
    }


    //TODO TEST TEST TEST very well
    public HashMap<EntityType, String> getEntityTypeColorMap(){
        HashMap<EntityType, String> map = new HashMap<>();
        List<String> keys = new ArrayList<>(this.entityTypeColorMap.getKeys());
        keys.forEach((key) -> {
            Optional<EntityType<?>> type = EntityType.get(key);
            String color = this.entityTypeColorMap.getString(key);
            type.ifPresent(entityType -> map.put(entityType, color));
        });
        return map;
    }

    //TODO TEST TEST TEST very well
    /**
     * @param type An entity type
     * @param color A hex color or "rainbow"*/
    public void addEntityTypeColor(EntityType type, String color){
        //TODO may need to substitute .toString with getUntranslatedName
        entityTypeColorMap.putString(type.toString(), color);
        ColoredGlowLibMod.GLOBAL_COLOR_COMPONENT.sync(scoreboard);
    }

    /**
     *
     * WARNING! THIS CANNOT BE USED TO CLEAR A TYPE! USE clearEntityTypeColor INSTEAD!
     *
     * @param type An entity type
     * @param color A hex color or "rainbow"*/
    public void setEntityTypeColor(EntityType type, String color){
        if(entityTypeColorMap.contains(type.toString())){
            entityTypeColorMap.remove(type.toString());
        }
        entityTypeColorMap.putString(type.toString(), color);
        ColoredGlowLibMod.GLOBAL_COLOR_COMPONENT.sync(scoreboard);
    }

    //TODO TEST TEST TEST very well
    public void clearEntityTypeColor(EntityType type){
        entityTypeColorMap.remove(type.toString());
        ColoredGlowLibMod.GLOBAL_COLOR_COMPONENT.sync(scoreboard);
    }

    //TODO TEST TEST TEST very well
    public String getEntityTypeColor(EntityType type){
        String color = entityTypeColorMap.getString(type.toString());
        if(color == null){
            //TODO make configurable? Like using the default color instead
            return "#ffffff";
        }
        return entityTypeColorMap.getString(type.toString());
    }

    //TODO TEST TEST TEST very well
    public String getDefaultColor(){
        return this.default_color;
    }

    public void setDefaultColor(String default_color) {
        this.default_color = default_color;
        ColoredGlowLibMod.GLOBAL_COLOR_COMPONENT.sync(scoreboard);
    }

    public boolean getEntityTypeOverridesEntityColor() {
        return this.typeOverridesEntityColor;
    }

    public void setTypeOverridesEntityColor(boolean b) {
        this.typeOverridesEntityColor = b;
        ColoredGlowLibMod.GLOBAL_COLOR_COMPONENT.sync(scoreboard);
    }

    public boolean getDefaultOverridesAll() {
        return this.defaultOverridesAll;
    }

    public void setDefaultOverridesAll(boolean b) {
        this.defaultOverridesAll = b;
        ColoredGlowLibMod.GLOBAL_COLOR_COMPONENT.sync(scoreboard);
    }

    public boolean getOverrideTeamColors() {
        return this.overrideTeamColors;
    }

    public void setOverrideTeamColors(boolean b) {
        this.overrideTeamColors = b;
        ColoredGlowLibMod.GLOBAL_COLOR_COMPONENT.sync(scoreboard);
    }

    public void clear(){
        this.default_color = "#ffffff";
        this.typeOverridesEntityColor = false;
        this.defaultOverridesAll = false;
        this.entityTypeColorMap = new NbtCompound();
        ColoredGlowLibMod.GLOBAL_COLOR_COMPONENT.sync(scoreboard);
    }

}
