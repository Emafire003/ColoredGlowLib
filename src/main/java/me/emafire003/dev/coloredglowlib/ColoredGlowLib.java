package me.emafire003.dev.coloredglowlib;

import me.emafire003.dev.coloredglowlib.networking.EntityListPacketS2C;
import me.emafire003.dev.coloredglowlib.networking.EntityMapPacketS2C;
import me.emafire003.dev.coloredglowlib.networking.EntityTypeListPacketS2C;
import me.emafire003.dev.coloredglowlib.networking.EntityTypeMapPacketS2C;
import me.emafire003.dev.coloredglowlib.util.CGLCommandRegister;
import me.emafire003.dev.coloredglowlib.util.Color;
import me.emafire003.dev.coloredglowlib.util.DataSaver;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ColoredGlowLib implements ModInitializer {

	public static String MOD_ID = "coloredglowlib";

	public static Color color = new Color(255, 255, 255);
	public static HashMap<EntityType, String> per_entitytype_color_map = new HashMap<>();
	private static boolean per_entitytype = true;
	public static List<EntityType> entitytype_rainbow_list = new ArrayList<>();
	private static boolean per_entity = true;
	public static HashMap<UUID, String> per_entity_color_map = new HashMap<>();
	public static List<UUID> entity_rainbow_list = new ArrayList<>();
	private static boolean overrideTeamColors = false;
	private static boolean generalized_rainbow = false;
	public static Logger LOGGER = LoggerFactory.getLogger("ColoredGlowLib");
	private static boolean debug = true;
	private static int ticks = 0;

	public static final GameRules.Key<GameRules.BooleanRule> OVERRIDE_TEAM_COLORS =
			GameRuleRegistry.register("overrideTeamColors", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));


	/*public static void createServerTick(FunctionInterface function){
		ServerTickEvents.END_SERVER_TICK.register(minecraftServer -> function.run());
	}*/

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		//Why do i even have to write this :/
		LOGGER.info("Hey, if you have downloaded this mod from a site different from CurseForge," +
				" Modrinth or Github, please remove this mod and download it again from an official source, " +
				"such as the ones cited before. Mods on other sites are NOT checked or secure since i did NOT " +
				"upload them there (those site also violate the license of the mod, and thus they are NOT LEGAL)");

		CGLCommandRegister.registerCommands();
		ServerTickEvents.END_SERVER_TICK.register(minecraftServer -> {
			if((ticks*60)%20==0){
				List<ServerPlayerEntity> players = minecraftServer.getPlayerManager().getPlayerList();
				for (ServerPlayerEntity player : players) {
					ColoredGlowLib.sendDataPackets(player);
				}
				ticks = 0;
			}
			ticks++;
		});

		LOGGER.info("Initializing...");
		DataSaver.createFile();
		LOGGER.info("Getting variables values from the data file...");
		if(DataSaver.getEntityMap() != null){
			per_entity_color_map = DataSaver.getEntityMap();
		}
		if(DataSaver.getEntityTypeMap() != null){
			per_entitytype_color_map = DataSaver.getEntityTypeMap();
		}
		if(DataSaver.getEntityRainbowList() != null){
			entity_rainbow_list = DataSaver.getEntityRainbowList();
		}
		if(DataSaver.getEntityTypeRainbowList() != null){
			entitytype_rainbow_list = DataSaver.getEntityTypeRainbowList();
		}
		per_entitytype = DataSaver.getPerEntityTypeColor();
		per_entity = DataSaver.getPerEntityColor();
		overrideTeamColors = DataSaver.getOverrideTeams();
		generalized_rainbow = DataSaver.getRainbowEnabled();
		color = DataSaver.getDefaultColor();
		LOGGER.info("Complete!");
	}

	@Environment(EnvType.SERVER)
	public static void sendDataPackets(ServerPlayerEntity player){
		debug = false;
		if(debug){
			LOGGER.info("Sending packets to the client...");
		}
		try{
			ServerPlayNetworking.send(player, EntityMapPacketS2C.ID, new EntityMapPacketS2C(per_entity_color_map));
			ServerPlayNetworking.send(player, EntityTypeMapPacketS2C.ID, new EntityTypeMapPacketS2C(convertFromEntityTypeMap(per_entitytype_color_map)));
			ServerPlayNetworking.send(player, EntityListPacketS2C.ID, new EntityListPacketS2C(entity_rainbow_list));
			ServerPlayNetworking.send(player, EntityTypeListPacketS2C.ID, new EntityTypeListPacketS2C(convertFromEntityTypeList(entitytype_rainbow_list)));

		}catch(Exception e){
			LOGGER.error("FAILED to send data packets to the client!");
			e.printStackTrace();
			return;
		}
		if(debug){
			LOGGER.info("Packets sent!");
		}
	}

	/**Enable or disable debug output (at the moment only packet stuff use this)
	 *
	 * @param b Set to true to enable, false to disable
	 * */
	public static void enableOrDisableDebugOutput(boolean b){
		debug = b;
	}

	/**WARNING! You should use the proper setEntityTypeColor() method instead of the
	 * .put() method of this map!*/
	public static HashMap<EntityType, String> getEntityTypeColorMap(){
		return per_entitytype_color_map;
	}

	/**WARNING! You should use the proper setEntityColor() method instead of the
	 * .put() method of this map!*/
	public static HashMap<UUID, String> getEntityColorMap(){
		return per_entity_color_map;
	}

	/**WARNING! You should use the proper setRainbowColorToEntityType() method instead of the
	 * .add() method of this list!*/
	public static List<EntityType> getRainbowEntityTypeList(){
		return entitytype_rainbow_list;
	}

	/**WARNING! You should use the proper setRainbowColorToEntity() method instead of the
	 * .add() method of this list!*/
	public static List<UUID> getRainbowEntityList(){
		return entity_rainbow_list;
	}

	/**Set this to true to override the default minecraft team colors
	 * even of the entity is in a team
	 *
	 * @param b The value to assing to overrideTeamColors*/
	public static void setOverrideTeamColors(boolean b){
		overrideTeamColors = b;
		DataSaver.write();
	}

	/**Get the value of the overrideTeamColors variable.
	 * (if true overrides the default minecraft team colors
	 * even of the entity is in a team)*/
	public static boolean getOverrideTeamColors(){
		return overrideTeamColors;
	}

	/**Set this to true to make every entity change color like
	 * a jeb sheep aka change color each tick*/
	public static void setRainbowChangingColor(boolean b){
		generalized_rainbow = true;
		DataSaver.write();
	}

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
	 * Sets the EntityType-specifc option for the custom colored
	 * glow effect.
	 * If enabled it will check for the EntityType of an entity
	 * and set (if configured) a custom glow color for the entity.
	 * */
	public static void setPerEntityTypeColor(boolean b){
		per_entitytype = b;
		DataSaver.write();
	}

	/**
	 * Returns true if the option for EntityType-specific glow
	 * color is enabled, returns false if it's disabled.
	 * If enabled it will check for the EntityType of an entity
	 * and set (if configured) a custom glow color for the entity.
	 *
	 * WARNING! This returns the value saved on the server
	 * Use the same method in the ColoredGlowLibClient class to
	 * get the value saved on the client
	 * */
	public static boolean getPerEntityTypeColor(){
		return per_entitytype;
	}

	/**
	 * Sets the Entity-specifc option for the custom colored
	 * glow effect.
	 * If enabled it will check for the UUID of an entity
	 * and set (if configured) a custom glow color for the specific entity.
	 * */
	public static void setPerEntityColor(boolean b){
		per_entity = b;
		DataSaver.write();
	}

	/**
	 * Returns true if the option for Entity-specific glow
	 * color is enabled, returns false if it's disabled.
	 * If enabled it will check for the UUID of an entity
	 * and set (if configured) a custom glow color for the specific entity.
	 *
	 * WARNING! This returns the value saved on the server
	 * Use the same method in the ColoredGlowLibClient class to
	 * get the value saved on the client
	 *
	 * */
	public static boolean getPerEntityColor(){
		return per_entity;
	}

	/**
	 * Sets a new custom Color value for the glowing effect
	 * of a specific Entity
	 *
	 * @param entity The Entity to set the color for
	 * @param color The color to set for the EntityType
	 * */
	public static void setColorToEntity(Entity entity, Color color){
		per_entity_color_map.put(entity.getUuid(), color.toHEX());
		DataSaver.write();
	}

	/**
	 * Removes the color set to an Entity
	 * for the glowing effect.
	 *
	 * @param entity The Entity that will no longer have a custom color
	 * */
	public static void removeColorFromEntity(Entity entity){
		UUID uuid = entity.getUuid();
		if(per_entity_color_map.containsKey(uuid)){
			per_entity_color_map.remove(uuid);
		}
	}

	/**
	 * Returns the current Color used for the glowing effect
	 * of a specific Entity. If there is no custom color for
	 * an Entity it returns the default one.
	 * (default is white, (255,255,255))
	 *
	 * WARNING! This returns the value saved on the server
	 * Use the same method in the ColoredGlowLibClient class to
	 * get the value saved on the client
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

	/**
	 * Returns true if the entity has a custom color
	 * for the glowing effect
	 *
	 * @param entity The Entity to check the color for
	 *
	 */
	public static boolean hasEntityColor(Entity entity){
		return per_entity_color_map.containsKey(entity.getUuid());
	}

	/**
	 * Returns true if the EntityType has a custom color
	 * for the glowing effect
	 *
	 * @param type The EntityType to check the color for
	 * */
	public static boolean hasEntityTypeColor(EntityType type){
		return per_entity_color_map.containsKey(type);
	}

	/**
	 * Returns true if the entity has a custom rainbow
	 * color for the glowing effect
	 * (works exactly like @<code>getEntityRainbowColor()</code>)
	 *
	 * @param entity The Entity to check the rainbow color for
	 * */
	public static boolean hasEntityRainbowColor(Entity entity){
		return per_entity_color_map.containsKey(entity.getUuid());
	}

	/**
	 * Returns true if the EntityType has a custom rainbow
	 * color for the glowing effect
	 * (works exactly like @<code>getEntityTypeRainbowColor()</code>)
	 *
	 * @param type The EntityType to check the rainbow color for
	 * */
	public static boolean hasEntityTypeRainbowColor(EntityType type){
		return entitytype_rainbow_list.contains(type);
	}

	/**
	 * Sets a new rainbow Color that changes every tick for the glowing effect
	 * of a specific Entity
	 *
	 * @param entity The Entity to set the rainbow for
	 * @param enabled Weather or not to enable or disable the rainbow color
	 * */
	public static void setRainbowColorToEntity(Entity entity, boolean enabled){
		if(enabled){
			entity_rainbow_list.add(entity.getUuid());
		}else if(entity_rainbow_list.contains(entity.getUuid())){
			entity_rainbow_list.remove(entity.getUuid());
		}
		DataSaver.write();
	}

	/**
	 * Removes the rainbow coloring from an Entity
	 * (you can also use @<code>setEntityRainbowColor(entity, false)</code>)
	 *
	 * @param entity The Entity to remove the rainbow color from
	 * */
	public static void removeRainbowColorFromEntity(Entity entity){
		if(getEntityRainbowColor(entity)){
			entity_rainbow_list.remove(entity.getUuid());
			DataSaver.write();
		}
	}

	/**
	 * Removes the rainbow coloring from an EntityType
	 * (you can also use @<code>setEntityTypeRainbowColor(type, false)</code>)
	 *
	 * @param type The EntityType to remove the rainbow color from
	 * */
	public static void removeRainbowColorFromEntityType(EntityType type){
		if(getEntityTypeRainbowColor(type)){
			entitytype_rainbow_list.remove(type);
			DataSaver.write();
		}
	}

	/**
	 * Returns the current Color used for the glowing effect
	 * of a specific Entity. If there is no custom color for
	 * an Entity it returns the default one.
	 * (default is white, (255,255,255))
	 *
	 * WARNING! This returns the value saved on the server!
	 * Use the same method in the ColoredGlowLibClient class to
	 * get the value saved on the client
	 *
	 * @param entity The Entity to check the color for
	 * */
	public static boolean getEntityRainbowColor(Entity entity){
		return entity_rainbow_list.contains(entity.getUuid());
	}

	/**
	 * Sets a new custom Color value for the glowing effect
	 * of a specific EntityType, such as EntityType.CHICKEN
	 *
	 * @param type The EntityType to set the color for
	 * @param color The color to set for the EntityType
	 * */
	public static void setColorToEntityType(EntityType type, Color color){
		per_entitytype_color_map.put(type, color.toHEX());
		DataSaver.write();
	}

	/**
	 * Removes the EntityType from having a custom color
	 * for the glowing effect.
	 *
	 * @param type The EntityType that will no longer have a custom color
	 * */
	public static void removeColorFromEntityType(EntityType type){
		if(per_entitytype_color_map.containsKey(type)){
			per_entitytype_color_map.remove(type);
		}
	}

	/**
	 * Returns the current Color used for the glowing effect
	 * of a specific EntityType. If there is no custom color for
	 * an EntityType (such as EntityType.CHICKEN) it returns the default one.
	 * (default is white, (255,255,255))
	 *
	 * WARNING! This returns the value saved on the server
	 * Use the same method in the ColoredGlowLibClient class to
	 * get the value saved on the client
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
	 * Sets a new rainbow Color that changes every tick for the glowing effect
	 * of a specific EntityType, such as EntityType.CHICKEN
	 *
	 * @param type The EntityType to set the rainbow for
	 * @param enabled Weather or not to enable or disable the rainbow color
	 * */
	public static void setRainbowColorToEntityType(EntityType type, boolean enabled){
		if(enabled){
			entitytype_rainbow_list.add(type);
		}else if(entitytype_rainbow_list.contains(type)){
			entitytype_rainbow_list.remove(type);
		}
		DataSaver.write();
	}

	/**
	 * Returns the current Color used for the glowing effect
	 * of a specific EntityType. If there is no custom color for
	 * an EntityType (such as EntityType.CHICKEN) it returns the default one.
	 * (default is white, (255,255,255))
	 *
	 * WARNING! This returns the value saved on the server
	 * Use the same method in the ColoredGlowLibClient class to
	 * get the value saved on the client
	 *
	 * @param type The EntityType to check the color for
	 * */
	public static boolean getEntityTypeRainbowColor(EntityType type){
		return entitytype_rainbow_list.contains(type);
	}


	/**
	 * Returns the current Color used for the glowing effect
	 * (default is white, (255,255,255))
	 * */
	public static Color getColor(){
		return color;
	}

	/**
	 * Set the color used for the glowing effect
	 *
	 * @param colour A Color object that will determine the glowing color
	 * */
	public static void setColor(Color colour){
		color = colour;
	}

	/**
	 * Set the color used for the glowing effect
	 *
	 * @param red Red color value (0-255)
	 * @param green Green value (0-255)
	 * @param blue Blue value (0-255)
	 * */
	public static void setColor(int red, int green, int blue){
		color = new Color(red, green, blue);
	}

	/**
	 * Set the color used for the glowing effect
	 *
	 * @param value The colorvalue (RRRRRRGGGGGGBBBBBB) You can get this via Color.translateToColorValue()
	 */
	public static void setColorValue(int value){
		color = Color.translateFromColorValue(value);
	}

	/**
	 * Used to convert EntityType values to string (those in a list)
	 * */
	public static List<String> convertFromEntityTypeList(List<EntityType> typelist){
		List<String> list = new ArrayList<>();
		for(EntityType type : typelist){
			list.add(String.valueOf(type));
		}
		return list;
	}

	/**
	 * Used to convert EntityType values to string (those in a hashmap)
	 * */
	public static HashMap<String, String> convertFromEntityTypeMap(HashMap<EntityType, String> typemap){
		List<String> list = new ArrayList<>();
		HashMap<String, String> stringmap = new HashMap<>();
		for(EntityType type : typemap.keySet()){
			stringmap.put(type.toString(), typemap.get(type));
		}
		return stringmap;
	}

}
