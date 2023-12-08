package me.emafire003.dev.coloredglowlib.config;


import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import net.minecraft.entity.EntityType;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static me.emafire003.dev.coloredglowlib.ColoredGlowLibMod.MOD_ID;


public class ConfigDataSaver {

    public static GsonConfigInstance<ConfigDataSaver> CONFIG_INSTANCE = new GsonConfigInstance<>(ConfigDataSaver.class, ColoredGlowLibMod.PATH.resolve(MOD_ID+"_config.json"));
    @ConfigEntry
    public boolean per_entity = true;

    @ConfigEntry
    public boolean per_entitytype = true;

    @ConfigEntry
    public boolean generalized_rainbow = false;

    @ConfigEntry
    public boolean override_team_colors = false;

    @ConfigEntry
    public HashMap<UUID, String> entityColorMap = ColoredGlowLibMod.getLib().getEntityColorMap();

    @ConfigEntry
    public HashMap<String, String> entityTypeColorMap = ColoredGlowLibMod.getLib().convertFromEntityTypeMap(ColoredGlowLibMod.getLib().getEntityTypeColorMap());

    @ConfigEntry
    public List<UUID> entityRainbowList = ColoredGlowLibMod.getLib().getRainbowEntityList();

    @ConfigEntry
    public List<String> entityTypeRainbowList = ColoredGlowLibMod.getLib().convertFromEntityTypeList(ColoredGlowLibMod.getLib().getRainbowEntityTypeList());

    @ConfigEntry
    public String defaultColor = ColoredGlowLibMod.getLib().getColor().toHEX();

    public HashMap<EntityType, String> getEntityTypeColorMap(){
        return ColoredGlowLibMod.getLib().convertToEntityTypeMap(entityTypeColorMap);
    }

    public List<EntityType> getEntityTypeRainbowList(){
        return ColoredGlowLibMod.getLib().convertToEntityTypeList(entityTypeRainbowList);
    }

    /*public Screen createGui(Screen parent) {
        // time to use YOCL!
        return new TitleScreen();
    }*/

}
