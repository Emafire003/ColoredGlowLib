package me.emafire003.dev.coloredglowlib.client;

import me.emafire003.dev.coloredglowlib.networking.EntityListPacketS2C;
import me.emafire003.dev.coloredglowlib.networking.EntityMapPacketS2C;
import me.emafire003.dev.coloredglowlib.networking.EntityTypeListPacketS2C;
import me.emafire003.dev.coloredglowlib.networking.EntityTypeMapPacketS2C;
import me.emafire003.dev.coloredglowlib.util.Color;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static me.emafire003.dev.coloredglowlib.ColoredGlowLib.LOGGER;

public class ColoredGlowLibClient implements ClientModInitializer {

    public static Color color = new Color(255, 255, 255);
    public static HashMap<EntityType, String> per_entitytype_color_map = new HashMap<>();
    private static boolean per_entitytype = true;
    public static List<EntityType> entitytype_rainbow_list = new ArrayList<>();
    private static boolean per_entity = true;
    public static HashMap<UUID, String> per_entity_color_map = new HashMap<>();
    public static List<UUID> entity_rainbow_list = new ArrayList<>();
    private static boolean overrideTeamColors = false;
    private static boolean generalized_rainbow = false;
    private static boolean debug = true;

    @Override
    @Environment(EnvType.CLIENT)
    public void onInitializeClient() {
        entitytype_rainbow_list.add(EntityType.VILLAGER);
        LOGGER.info("Registering packets receivers...");
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
    }

    private void registerEntityMapPacket(){
        ClientPlayNetworking.registerGlobalReceiver(EntityMapPacketS2C.ID, ((client, handler, buf, responseSender) -> {
            var results = EntityMapPacketS2C.read(buf);

            client.execute(() -> {
                if(debug){
                    LOGGER.info("Getting new data from the server");
                }
                per_entity_color_map = results;

                if(debug){
                    LOGGER.info("Got new data! Like: " + results);
                }

            });
        }));
    }

    private void registerEntityTypeMapPacket(){
        ClientPlayNetworking.registerGlobalReceiver(EntityTypeMapPacketS2C.ID, ((client, handler, buf, responseSender) -> {
            var results = EntityTypeMapPacketS2C.read(buf);

            client.execute(() -> {
                per_entitytype_color_map = results;
            });
        }));
    }

    private void registerEntityListPacket(){
        ClientPlayNetworking.registerGlobalReceiver(EntityListPacketS2C.ID, ((client, handler, buf, responseSender) -> {
            var results = EntityListPacketS2C.read(buf);

            client.execute(() -> {
                entity_rainbow_list = results;
            });
        }));
    }

    private void registerEntityTypeListPacket(){
        ClientPlayNetworking.registerGlobalReceiver(EntityTypeListPacketS2C.ID, ((client, handler, buf, responseSender) -> {
            var results = EntityTypeListPacketS2C.read(buf);

            client.execute(() -> {
                entitytype_rainbow_list = results;
            });
        }));
    }

    //Get values part

    /**Set this to true to make every entity change color like
     * a jeb sheep aka change color each tick
     *
     * WARNING! This returns the value saved on the server!
     * Use the same method in the ColoredGlowLibClient class to
     * get the value saved on the client!*/
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

    /*WARNING! This returns the value saved on the server
    Use the same method in the ColoredGlowLibClient class to
	get the value saved on the client*/

}
