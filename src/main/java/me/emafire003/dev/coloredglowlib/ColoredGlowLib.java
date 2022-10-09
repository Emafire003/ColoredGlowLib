package me.emafire003.dev.coloredglowlib;

import me.emafire003.dev.coloredglowlib.config.Config;
import me.emafire003.dev.coloredglowlib.networking.*;
import me.emafire003.dev.coloredglowlib.networking.packets.*;
import me.emafire003.dev.coloredglowlib.util.*;
import net.minecraft.server.level.ServerPlayer;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.server.ServerStoppedEvent;
import org.jetbrains.annotations.Nullable;

import static me.emafire003.dev.coloredglowlib.ColoredGlowLibMod.LOGGER;

import java.util.*;

public class ColoredGlowLib{
	
	public  Color color = new Color(255, 255, 255);

	public  HashMap<UUID, String> per_entity_color_map = new HashMap<>();
	public  List<UUID> entity_rainbow_list = new ArrayList<>();

	public  HashMap<EntityType, String> per_entitytype_color_map = new HashMap<>();
	public  List<EntityType> entitytype_rainbow_list = new ArrayList<>();

	private  boolean per_entity = true;
	private  boolean per_entitytype = true;
	private  boolean generalized_rainbow = false;
	private  boolean overrideTeamColors = false;

	private  boolean debug = false;

	//How many seconds should pass between updating the data and sending packets to the client?
	private  double seconds = 0.5;
	private  MinecraftServer server = null;
	public  boolean server_registered = false;

	public ColoredGlowLib(){
	}

	/**
	 * This method removes entities/entitytypes that have their color
	 * set to #FFFFFF aka white aka default. This is not needed since the
	 * default color rendered is white.
	 * So to save resources and space this method gets called with updateData().
	 * You can call it yourself too, every once in a while, if you feel like it.*/
	public void optimizeData(){
		HashMap<UUID, String> temp_entity_map = this.per_entity_color_map;
		for(Map.Entry<UUID, String> c : temp_entity_map.entrySet()){
			if(c.getValue().equalsIgnoreCase("ffffff")){
				this.per_entity_color_map.remove(c.getKey());
			}
		}
		HashMap<EntityType, String> temp_type_map = this.per_entitytype_color_map;
		for(Map.Entry<EntityType, String> c : temp_type_map.entrySet()) {
			if (c.getValue().equalsIgnoreCase("ffffff")) {
				this.per_entitytype_color_map.remove(c.getKey());
			}
		}
	}

	/**
	 * This method saves the data the server has to the file, and
	 * then it sends the same updated that to the client with packets.
	 *
	 * Normally, you should use sendDataPacketsToPlayers() if the action will
	 * be repeated several times. Instead if it is a one-time change, you might
	 * as well use this method in order to secure the data to the file.
	 *
	 * @param server The server object needed to send packets to players
	 * */
	public void updateData(MinecraftServer server){
		this.optimizeData();
		if(server == null){
			LOGGER.warn("The provided server variable in ColoredGlowLib.updateData(server); is null, skipping update");
		}
		if(isOnServer()){
			saveDataToFile();
			sendDataPacketsToPlayers(server);	
		}
		else{
			saveDataToFile();
			LOGGER.warn("Server not ready yet, only saving the data to file without sending packets");
		}
		if(debug){
			LOGGER.info("Updating data...");
		}
	}

	/**This method gets usually called when the server is stopping or when
	 * calling updateData().
	 * It saves the data of the maps, lists, and variables that were changed
	 * during playtime.
	 * It can be called after modifying a setting or a color of
	 * something to ensure it is saved permanently. */
	public void saveDataToFile(){
		LOGGER.info("Saving the colored glow data to the file...");
		DataSaver.write();
		LOGGER.info("Saved!");
	}

	/**This method removes every color from an entity, restoring it
	 * to the default one. (both rainbow & non rainbow)*/
	public  void removeColor(Entity entity){
		this.setRainbowColorToEntity(entity, false);
		this.removeColorFromEntity(entity);
	}

	/**This method removes every color from an EntityType, restoring it
	 * to the default one. (both rainbow & non rainbow)*/
	public  void removeColor(EntityType type){
		this.setRainbowColorToEntityType(type, false);
		this.removeColorFromEntityType(type);
	}

	/**
	 * WARNING! Not intended to be used aside from the start event. I needed
	 * to set this to public because I needed to use this method in the event
	 * class.
	 *
	 * This method gets the data from the file and puts into the variables.*/
	public  void getValuesFromFile(){
		try{
			DataSaver.createFile();
			LOGGER.info("Getting variables values from the data file...");

			if(DataSaver.getEntityMap() != null && !DataSaver.getEntityMap().isEmpty()){
				if(!per_entity_color_map.isEmpty()){
					per_entity_color_map.putAll(DataSaver.getEntityMap());
				}else{
					per_entity_color_map = DataSaver.getEntityMap();
				}
			}

			if(DataSaver.getEntityTypeMap() != null && !DataSaver.getEntityTypeMap().isEmpty()){
				if(!per_entitytype_color_map.isEmpty()){
					per_entitytype_color_map.putAll(DataSaver.getEntityTypeMap());
				}else{
					per_entitytype_color_map = DataSaver.getEntityTypeMap();
				}
			}

			if(DataSaver.getEntityRainbowList() != null && !DataSaver.getEntityRainbowList().isEmpty()){
				entity_rainbow_list = DataSaver.getEntityRainbowList();
			}

			if(DataSaver.getEntityTypeRainbowList() != null && !DataSaver.getEntityTypeRainbowList().isEmpty()){
				entitytype_rainbow_list = DataSaver.getEntityTypeRainbowList();
			}

			/*per_entitytype = DataSaver.getPerEntityTypeColor();
			per_entity = DataSaver.getPerEntityColor();
			overrideTeamColors = DataSaver.getOverrideTeams();
			generalized_rainbow = DataSaver.getRainbowEnabled();*/
			color = DataSaver.getDefaultColor();
			LOGGER.info("Done!");
		}catch (Exception e){
			LOGGER.error("There was an error while getting the values from the file onto the mod");
			e.printStackTrace();
		}
	}

	/**
	 * This method changes the time between packets sent
	 * by the server to client to update the values on the client.
	 * The delay is in SECONDS not ticks.
	 *
	 * The higher the value, the less frequently the client will get
	 * updated information on the colors of the entities, but better performance.
	 * the lower the value, the more frequently the client will get updated data,
	 * but worse performance.
	 *
	 * Default value: 0.5 seconds
	 *
	 * @param delay The delay in SECONDS between one packet being sent after another one
	 * */
	public  void setDelayBetweenSendingPackets(double delay){
		seconds = delay;
	}

	/**
	 * This method returns the time between packets sent
	 * by the server to client to update the values on the client.
	 * The delay is in SECONDS not ticks.
	 *
	 * The higher the value, the less frequently the client will get
	 * updated information on the colors of the entities, but better performance.
	 * the lower the value, the more frequently the client will get updated data,
	 * but worse performance.
	 *
	 * Default value: 0.5 seconds
	 * */
	public double getDelayBetweenSendingPackets(){
		return this.seconds;
	}
	
	/**This will return true if the mod is run on a dedicated
	 * or integrated server, false if it's just the client
	 * 
	 * It gets this after the first tick of the server. So use it wisely*/
	public  boolean isOnServer(){
		return server_registered;
	}

	/**Use this to check if you can submit new stuff to the mod or not.
	 *
	 * This will return true if the mod has loaded the config
	 *
	 * It gets this after the first tick of the server*/
	public  boolean isReady(){
		return server_registered;
	}

	/**
	 * This method saves the data the server has to the file
	 *
	 * */
	public  void saveDataOnFile(){
		if(isOnServer()){
			DataSaver.write();
		}
	}

	/**This method returns the server on which ColoredGlowLib is running,
	 * and it gets it in a bit of a "hacky" way, so if you can use another
	 * method instead of this, use that other method.
	 * It gets the server after its first tick. Probably not going to work in the ModInitializer
	 * */
	@Nullable
	public  MinecraftServer getServer(){
		return server;
	}

	/**This method sends updated data to all players on the server
	 *
	 * @param server The server the players are on. And also where the mod runs most likely.*/
	public  void sendDataPacketsToPlayers(MinecraftServer server){
		List<ServerPlayer> players = server.getPlayerList().getPlayers();
		
		for (ServerPlayer player : players) {
			//Maybe this should go upper with maybe FabricLoader.getEnv is server ecc
			sendDataPackets(player);
		}
	}

	public void sendDataPackets(ServerPlayer player){
		if(debug){
			LOGGER.info("Sending packets to the client...");
		}
		try{
			CGLNetworking.send(player, new EntityMapPacketS2c(per_entity_color_map));
			CGLNetworking.send(player, new EntityTypeMapPacketS2c(convertFromEntityTypeMap(per_entitytype_color_map)));
			CGLNetworking.send(player, new EntityListPacketS2c(entity_rainbow_list));
			CGLNetworking.send(player, new EntityTypeListPacketS2c(convertFromEntityTypeList(entitytype_rainbow_list)));
			CGLNetworking.send(player, new BooleanValuesPacketS2c(packBooleanValuesForPackets()));
			CGLNetworking.send(player, new ColorPacketS2c(Color.translateToHEX(color)));
		}catch(Exception e){
			LOGGER.error("FAILED to send data packets to the client!");
			e.printStackTrace();
			return;
		}
		if(debug){
			LOGGER.info("Packets sent!");
		}
	}

	private  List<Boolean> packBooleanValuesForPackets(){
		List<Boolean> list = new ArrayList<>();
		list.add(per_entity);
		list.add(per_entitytype);
		list.add(generalized_rainbow);
		list.add(overrideTeamColors);
		return list;
	}

	/**Enable or disable debug output (at the moment only packet stuff use this)
	 *
	 * @param b Set to true to enable, false to disable
	 * */
	public  void enableOrDisableDebugOutput(boolean b){
		debug = b;
	}

	/**WARNING! You should use the proper setEntityTypeColor() method instead of the
	 * .put() method of this map!*/
	public  HashMap<EntityType, String> getEntityTypeColorMap(){
		return per_entitytype_color_map;
	}

	/**WARNING! You should use the proper setEntityColor() method instead of the
	 * .put() method of this map!*/
	public  HashMap<UUID, String> getEntityColorMap(){
		return per_entity_color_map;
	}

	/**WARNING! You should use the proper setRainbowColorToEntityType() method instead of the
	 * .add() method of this list!*/
	public  List<EntityType> getRainbowEntityTypeList(){
		return entitytype_rainbow_list;
	}

	/**WARNING! You should use the proper setRainbowColorToEntity() method instead of the
	 * .add() method of this list!*/
	public  List<UUID> getRainbowEntityList(){
		return entity_rainbow_list;
	}

	/**Set this to true to override the default minecraft team colors
	 * even of the entity is in a team.
	 * To update the data for the client && to save it on file
	 * use ColoredGlowLib.updateData(MinecraftServer server);
	 *
	 * WARNING! You will be able to set this only AFTER the server has started & loaded the world.
	 * Aka, you will be able to set this after its first tick
	 *
	 * @param b The value to assing to overrideTeamColors*/
	public  void setOverrideTeamColors(boolean b){
		overrideTeamColors = b;
		Config.OVERRIDE_TEAM_COLORS = overrideTeamColors;
		Config.reloadConfig();

	}

	/**Get the value of the overrideTeamColors variable.
	 * (if true overrides the default minecraft team colors
	 * even of the entity is in a team)
	 *
	 * WARNING! This returns the value saved on the server
	 * Use the same method in the ColoredGlowLibClient class to
	 * get the value saved on the client
	 * */
	public  boolean getOverrideTeamColors(){
		return overrideTeamColors;
	}

	/**Set this to true to make every entity change color like
	 * a jeb sheep aka change color each tick
	 *
	 * To update the data for the client && to save it on file
	 * use ColoredGlowLib.updateData(MinecraftServer server);
	 *
	 * WARNING! You will be able to set this only AFTER the server has started & loaded the world.
	 * Aka, you will be able to set this after its first tick
	 *
	 * */
	public  void setRainbowChangingColor(boolean b){
		generalized_rainbow = b;
		Config.GENERALIZED_RAINBOW = generalized_rainbow;
		Config.reloadConfig();

	}

	/**Set this to true to make every entity change color like
	 * a jeb sheep aka change color each tick
	 *
	 * WARNING! This returns the value saved on the server!
	 * Use the same method in the ColoredGlowLibClient class to
	 * get the value saved on the client!*/
	public  boolean getRainbowChangingColor(){
		return generalized_rainbow;
	}

	/**
	 * Sets the EntityType-specific option for the custom colored
	 * glow effect.
	 * If enabled it will check for the EntityType of an entity
	 * and set (if configured) a custom glow color for the entity.
	 *
	 * WARNING! You will be able to set this only AFTER the server has started & loaded the world.
	 * Aka, you will be able to set this after its first tick
	 *
	 * To update the data for the client && to save it on file
	 * use ColoredGlowLib.updateData(MinecraftServer server);
	 *
	 * */
	public  void setPerEntityTypeColor(boolean b){
		per_entitytype = b;
		Config.PER_ENTITYTYPE = per_entitytype;
		Config.reloadConfig();

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
	public  boolean getPerEntityTypeColor(){
		return per_entitytype;
	}

	/**
	 * Sets the Entity-specifc option for the custom colored
	 * glow effect.
	 * If enabled it will check for the UUID of an entity
	 * and set (if configured) a custom glow color for the specific entity.
	 *
	 * WARNING! You will be able to set this only AFTER the server has started & loaded the world.
	 * Aka, you will be able to set this after its first tick
	 *
	 * To update the data for the client && to save it on file
	 * use ColoredGlowLib.updateData(MinecraftServer server);
	 *
	 * */
	public  void setPerEntityColor(boolean b){
		per_entity = b;
		Config.PER_ENTITY = per_entity;
		Config.reloadConfig();
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
	public  boolean getPerEntityColor(){
		return per_entity;
	}

	/**
	 * Sets a new custom Color value for the glowing effect
	 * of a specific Entity
	 *
	 * WARNING! You will be able to set this only AFTER the server has started & loaded the world.
	 * Aka, you will be able to set this after its first tick
	 *
	 * To update the data for the client && to save it on file
	 * use ColoredGlowLib.updateData(MinecraftServer server);
	 *
	 * @param entity The Entity to set the color for
	 * @param color The color to set for the EntityType
	 * */
	public  void setColorToEntity(Entity entity, Color color){
		if(color.equals(Color.getWhiteColor())){
			return;
		}
		per_entity_color_map.put(entity.getUUID(), color.toHEX());

	}

	/**
	 * Removes the color set to an Entity
	 * for the glowing effect.
	 *
	 * @param entity The Entity that will no longer have a custom color
	 * */
	public  void removeColorFromEntity(Entity entity){
		UUID uuid = entity.getUUID();
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
	public  Color getEntityColor(Entity entity){
		UUID uuid = entity.getUUID();
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
	public  boolean hasEntityColor(Entity entity){
		return per_entity_color_map.containsKey(entity.getUUID());
	}

	/**
	 * Returns true if the EntityType has a custom color
	 * for the glowing effect
	 *
	 * @param type The EntityType to check the color for
	 * */
	public  boolean hasEntityTypeColor(EntityType type){
		return per_entity_color_map.containsKey(type);
	}

	/**
	 * Returns true if the entity has a custom rainbow
	 * color for the glowing effect
	 * (works exactly like @<code>getEntityRainbowColor()</code>)
	 *
	 * @param entity The Entity to check the rainbow color for
	 * */
	public  boolean hasEntityRainbowColor(Entity entity){
		return per_entity_color_map.containsKey(entity.getUUID());
	}

	/**
	 * Returns true if the EntityType has a custom rainbow
	 * color for the glowing effect
	 * (works exactly like @<code>getEntityTypeRainbowColor()</code>)
	 *
	 * @param type The EntityType to check the rainbow color for
	 * */
	public  boolean hasEntityTypeRainbowColor(EntityType type){
		return entitytype_rainbow_list.contains(type);
	}

	/**
	 * Sets a new rainbow Color that changes every tick for the glowing effect
	 * of a specific Entity
	 *
	 * To update the data for the client && to save it on file
	 * use ColoredGlowLib.updateData(MinecraftServer server);
	 *
	 * @param entity The Entity to set the rainbow for
	 * @param enabled Weather or not to enable or disable the rainbow color
	 * */
	public  void setRainbowColorToEntity(Entity entity, boolean enabled){
		if(enabled){
			entity_rainbow_list.add(entity.getUUID());
		}else if(entity_rainbow_list.contains(entity.getUUID())){
			entity_rainbow_list.remove(entity.getUUID());
		}

	}

	/**
	 * Removes the rainbow coloring from an Entity
	 * (you can also use @<code>setEntityRainbowColor(entity, false)</code>)
	 *
	 * To update the data for the client && to save it on file
	 * use ColoredGlowLib.updateData(MinecraftServer server);
	 *
	 * @param entity The Entity to remove the rainbow color from
	 * */
	public  void removeRainbowColorFromEntity(Entity entity){
		if(getEntityRainbowColor(entity)){
			entity_rainbow_list.remove(entity.getUUID());

		}
	}

	/**
	 * Removes the rainbow coloring from an EntityType
	 * (you can also use @<code>setEntityTypeRainbowColor(type, false)</code>)
	 *
	 * To update the data for the client && to save it on file
	 * use ColoredGlowLib.updateData(MinecraftServer server);
	 *
	 * @param type The EntityType to remove the rainbow color from
	 * */
	public  void removeRainbowColorFromEntityType(EntityType type){
		if(getEntityTypeRainbowColor(type)){
			entitytype_rainbow_list.remove(type);

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
	public  boolean getEntityRainbowColor(Entity entity){
		return entity_rainbow_list.contains(entity.getUUID());
	}

	/**
	 * Sets a new custom Color value for the glowing effect
	 * of a specific EntityType, such as EntityType.CHICKEN
	 *
	 * To update the data for the client && to save it on file
	 * use ColoredGlowLib.updateData(MinecraftServer server);
	 *
	 * @param type The EntityType to set the color for
	 * @param color The color to set for the EntityType
	 * */
	public  void setColorToEntityType(EntityType type, Color color){
		if(color.equals(Color.getWhiteColor())){
			return;
		}
		per_entitytype_color_map.put(type, color.toHEX());
	}

	/**
	 * Removes the EntityType from having a custom color
	 * for the glowing effect.
	 *
	 * @param type The EntityType that will no longer have a custom color
	 * */
	public  void removeColorFromEntityType(EntityType type){
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
	public  Color getEntityTypeColor(EntityType type){
		if(!per_entitytype_color_map.containsKey(type)){
			return Color.getWhiteColor();
		}
		return Color.translateFromHEX(per_entitytype_color_map.get(type));
	}

	/**
	 * Sets a new rainbow Color that changes every tick for the glowing effect
	 * of a specific EntityType, such as EntityType.CHICKEN
	 *
	 * To update the data for the client && to save it on file
	 * use ColoredGlowLib.updateData(MinecraftServer server);
	 *
	 * @param type The EntityType to set the rainbow for
	 * @param enabled Weather or not to enable or disable the rainbow color
	 * */
	public  void setRainbowColorToEntityType(EntityType type, boolean enabled){
		if(enabled){
			entitytype_rainbow_list.add(type);
		}else if(entitytype_rainbow_list.contains(type)){
			entitytype_rainbow_list.remove(type);
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
	public  boolean getEntityTypeRainbowColor(EntityType type){
		return entitytype_rainbow_list.contains(type);
	}


	/**
	 * Returns the current Color used for the glowing effect
	 * (default is white, (255,255,255))
	 *
	 * WARNING! This returns the value saved on the server
	 * Use the same method in the ColoredGlowLibClient class to
	 * get the value saved on the client
	 * */
	public  Color getColor(){
		return color;
	}

	/**
	 * Set the color used for the glowing effect
	 *
	 * @param colour A Color object that will determine the glowing color
	 * */
	public  void setColor(Color colour){
		if(colour.equals(Color.getWhiteColor())){
			return;
		}
		color = colour;
	}

	/**
	 * Set the color used for the glowing effect
	 *
	 * @param red Red color value (0-255)
	 * @param green Green value (0-255)
	 * @param blue Blue value (0-255)
	 * */
	public  void setColor(int red, int green, int blue){
		if(red == 255 && green == 255 && blue == 255){
			return;
		}
		color = new Color(red, green, blue);
	}

	/**
	 * Set the color used for the glowing effect
	 *
	 * @param value The colorvalue (RRRRRRGGGGGGBBBBBB) You can get this via Color.translateToColorValue()
	 */
	public  void setColorValue(int value){
		if(value == Color.translateToColorValue(255, 255, 255)){
			return;
		}
		color = Color.translateFromColorValue(value);
	}

	/**
	 * Used to convert EntityType values to string (those in a list)
	 * */
	public  List<String> convertFromEntityTypeList(List<EntityType> typelist){
		List<String> list = new ArrayList<>();
		for(EntityType type : typelist){
			list.add(EntityType.getKey(type).toString());
		}
		return list;
	}

	/**
	 * Used to convert String values to EntityType (those in a list)
	 * */
	public  List<EntityType> convertToEntityTypeList(List<String> typelist){
		List<EntityType> list = new ArrayList<>();
		for(String type : typelist){
			list.add(EntityType.byString(type).get());
		}
		return list;
	}

	/**
	 * Used to convert EntityType values to string (those in a hashmap)
	 * */
	public  HashMap<String, String> convertFromEntityTypeMap(HashMap<EntityType, String> typemap){
		HashMap<String, String> stringmap = new HashMap<>();
		for(EntityType type : typemap.keySet()){
			stringmap.put(EntityType.getKey(type).toString(), typemap.get(type));
		}
		return stringmap;
	}

	/**
	 * Used to convert String values to EntityType (those in a hashmap)
	 * */
	public  HashMap<EntityType, String> convertToEntityTypeMap(HashMap<String, String> typemap){
		HashMap<EntityType, String> enmap = new HashMap<>();
		for(String type : typemap.keySet()){
			enmap.put(EntityType.byString(type).get(), typemap.get(type));
		}
		return enmap;
	}

}
