package me.emafire003.dev.coloredglowlib.client;

import me.emafire003.dev.coloredglowlib.ColoredGlowLib;
import me.emafire003.dev.coloredglowlib.networking.*;
import me.emafire003.dev.coloredglowlib.util.Color;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static me.emafire003.dev.coloredglowlib.ColoredGlowLib.LOGGER;

public class ColoredGlowLibClient implements ClientModInitializer {

    public static Color color = new Color(255, 255, 255);

    public static HashMap<UUID, String> per_entity_color_map = new HashMap<>();
    public static List<UUID> entity_rainbow_list = new ArrayList<>();

    public static HashMap<EntityType, String> per_entitytype_color_map = new HashMap<>();
    public static List<EntityType> entitytype_rainbow_list = new ArrayList<>();

    private static boolean per_entity = true;
    private static boolean per_entitytype = true;
    private static boolean generalized_rainbow = false;
    private static boolean overrideTeamColors = false;
    private static boolean debug = false;
    private static boolean ap1 = false;

    @Override
    @Environment(EnvType.CLIENT)
    public void onInitializeClient() {
        LOGGER.info("Registering packets receivers...");
        LocalDate currentDate = LocalDate.now();
        int day = currentDate.getDayOfMonth();
        Month month = currentDate.getMonth();
        if(month.equals(Month.APRIL) && day == 1){
            ap1 = true;
        }
        try{
            registerPackets();
            LOGGER.info("Complete!");
        }catch (Exception e){
            LOGGER.error("FAILED to register packet receivers!");
            e.printStackTrace();
        }
    }

    //Network part

    /**Used to register thes that the client will recive. Normally,
     * you should not use this*/
    public void registerPackets(){
        LOGGER.info("Registering packets");
        registerEntityMapPacket();
        registerEntityListPacket();
        registerEntityTypeMapPacket();
        registerEntityTypeListPacket();
        registerBooleanValuesMapPacket();
        registerColorPacket();
    }

    /**Diz iz a zecret*/
    public static boolean isAp1(){
        return ap1;
    }

    private void registerEntityMapPacket(){
        ClientPlayNetworking.registerGlobalReceiver(EntityMapPacketS2C.ID, ((client, handler, buf, responseSender) -> {
            var results = EntityMapPacketS2C.read(buf);

            client.execute(() -> {
                try{
                    if(debug){
                        LOGGER.info("Getting new data from the server");
                    }
                    if(results != null && !results.isEmpty()){
                        per_entity_color_map = results;
                    }else{
                        if(debug){
                            LOGGER.warn("The packet 'EntityMap' was null or empty, probably not a problem");
                        }
                    }
                    if(debug){
                        LOGGER.info("Got new data! Like: " + results);
                    }
                }catch (NoSuchElementException e){
                    LOGGER.warn("No value in the packet, probably not a big problem");
                }catch (Exception e){
                    LOGGER.error("There was an error while getting the packet!");
                    e.printStackTrace();
                }
            });
        }));
    }

    private void registerEntityTypeMapPacket(){
        ClientPlayNetworking.registerGlobalReceiver(EntityTypeMapPacketS2C.ID, ((client, handler, buf, responseSender) -> {
            var results = EntityTypeMapPacketS2C.read(buf);

            client.execute(() -> {
                try{
                    if(results != null && !results.isEmpty()){
                        if(debug){
                            LOGGER.info("Recived a packet, converting");
                        }
                        per_entitytype_color_map = ColoredGlowLib.convertToEntityTypeMap(results);
                    }else{
                        if(debug){
                            LOGGER.warn("The packet 'EntityTypeMap' was null or empty, probably not a problem");
                        }
                    }
                    
                }catch (NoSuchElementException e){
                    LOGGER.warn("No value in the packet, probably not a big problem");
                }catch (Exception e){
                    LOGGER.error("There was an error while getting the packet!");
                    e.printStackTrace();
                }

            });
        }));
    }

    private void registerEntityListPacket(){
        ClientPlayNetworking.registerGlobalReceiver(EntityListPacketS2C.ID, ((client, handler, buf, responseSender) -> {
            var results = EntityListPacketS2C.read(buf);

            client.execute(() -> {
                try {
                    if(results != null && !results.isEmpty()){
                        entity_rainbow_list = results;
                    }else{
                        if(debug){
                            LOGGER.warn("The packet 'EntityList' was null or empty, probably not a problem");
                        }
                    }
                    
                }catch (NoSuchElementException e){
                    LOGGER.warn("No value in the packet, probably not a big problem");
                }catch (Exception e){
                    LOGGER.error("There was an error while getting the packet!");
                    e.printStackTrace();
                }
            });
        }));
    }

    private void registerEntityTypeListPacket(){
        ClientPlayNetworking.registerGlobalReceiver(EntityTypeListPacketS2C.ID, ((client, handler, buf, responseSender) -> {
            var results = EntityTypeListPacketS2C.read(buf);

            client.execute(() -> {
                try{
                    if(results != null && !results.isEmpty()){
                        entitytype_rainbow_list = ColoredGlowLib.convertToEntityTypeList(results);
                    }else{
                        if(debug){
                            LOGGER.warn("The packet 'EntityTypeList' was null or empty, probably not a problem");
                        }
                    }
                }catch (NoSuchElementException e){
                    LOGGER.warn("No value in the packet, probably not a big problem");
                }catch (Exception e){
                    LOGGER.error("There was an error while getting the packet!");
                    e.printStackTrace();
                }
            });
        }));
    }

    private void registerBooleanValuesMapPacket(){
        ClientPlayNetworking.registerGlobalReceiver(BooleanValuesPacketS2C.ID, ((client, handler, buf, responseSender) -> {
            var results = BooleanValuesPacketS2C.read(buf);

            client.execute(() -> {
                try{
                    if(results != null && !results.isEmpty()){
                        unPackBooleanValuesPackets(results);
                    }else{
                        if(debug){
                            LOGGER.warn("The packet 'Booleans' was null or empty, probably not a problem");
                        }
                    }
                }catch (NoSuchElementException e){
                    LOGGER.warn("No value in the packet, probably not a big problem");
                }catch (Exception e){
                    LOGGER.error("There was an error while getting the packet!");
                    e.printStackTrace();
                }
            });
        }));
    }

    private void registerColorPacket(){
        ClientPlayNetworking.registerGlobalReceiver(ColorPacketS2C.ID, ((client, handler, buf, responseSender) -> {
            var results = ColorPacketS2C.read(buf);

            client.execute(() -> {
                try{
                    color = results;
                }catch (NoSuchElementException e){
                    LOGGER.warn("No value in the packet 'color' , probably not a big problem");
                }catch (Exception e){
                    LOGGER.error("There was an error while getting the packet!");
                    e.printStackTrace();
                }
            });
        }));
    }

    private static void unPackBooleanValuesPackets(List<Boolean> list){
        per_entity = list.get(0);
        per_entitytype = list.get(1);
        generalized_rainbow = list.get(2);
        overrideTeamColors = list.get(3);
    }

    //Get values part

    /**Set this to true to make every entity change color like
     * a jeb sheep aka change color each tick
     *
     * WARNING! This returns the value saved on the server!
     * Use the same method in the ColoredGlowLibClient class to
     * get the value saved on the client!
     * */
    public static boolean getRainbowChangingColor(){
        return generalized_rainbow;
    }

    /**
     * Returns the current Color used for the glowing effect
     * of a specific Entity. If there is no custom color for
     * an Entity it returns the default one.
     * (default is white, (255,255,255))
     *
     * @param entity The Entity to check the color for
     * */
    public static boolean getEntityRainbowColor(Entity entity){
        return entity_rainbow_list.contains(entity.getUuid());
    }

    /**
     * Returns the current Color used for the glowing effect
     * of a specific EntityType. If there is no custom color for
     * an EntityType (such as EntityType.CHICKEN) it returns the default one.
     * (default is white, (255,255,255))
     *
     * @param type The EntityType to check the color for
     * */
    public static boolean getEntityTypeRainbowColor(EntityType type){
        return entitytype_rainbow_list.contains(type);
    }

    /**
     * Returns true if the option for EntityType-specific glow
     * color is enabled, returns false if it's disabled.
     * If enabled it will check for the EntityType of an entity
     * and set (if configured) a custom glow color for the entity.
     * */
    public static boolean getPerEntityTypeColor(){
        return per_entitytype;
    }

    /**
     * Returns the current Color used for the glowing effect
     * of a specific EntityType. If there is no custom color for
     * an EntityType (such as EntityType.CHICKEN) it returns the default one.
     * (default is white, (255,255,255))
     *
     * @param type The EntityType to check the color for
     * */
    public static Color getEntityTypeColor(EntityType type){
        if(!per_entitytype_color_map.containsKey(type)){
            return Color.getWhiteColor();
        }
        return Color.translateFromHEX(per_entitytype_color_map.get(type));
    }

    /**
     * Returns true if the option for Entity-specific glow
     * color is enabled, returns false if it's disabled.
     * If enabled it will check for the UUID of an entity
     * and set (if configured) a custom glow color for the specific entity.
     * */
    public static boolean getPerEntityColor(){
        return per_entity;
    }

    /**
     * Returns the current Color used for the glowing effect
     * of a specific Entity. If there is no custom color for
     * an Entity it returns the default one.
     * (default is white, (255,255,255))
     *
     * @param entity The Entity to check the color for
     * */
    public static Color getEntityColor(Entity entity){
        UUID uuid = entity.getUuid();
        if(!per_entity_color_map.containsKey(uuid)){
            return Color.getWhiteColor();
        }
        return Color.translateFromHEX(per_entity_color_map.get(uuid));
    }

    /**Get the value of the overrideTeamColors variable.
     * (if true overrides the default minecraft team colors
     * even of the entity is in a team)*/
    public static boolean getOverrideTeamColors(){
        return overrideTeamColors;
    }

    /**
     * Returns the current Color used for the glowing effect
     * (default is white, (255,255,255))
     * */
    public static Color getColor(){
        return color;
    }


}
