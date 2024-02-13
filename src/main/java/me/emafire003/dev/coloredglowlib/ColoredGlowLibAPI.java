package me.emafire003.dev.coloredglowlib;

import me.emafire003.dev.coloredglowlib.component.ColorComponent;
import me.emafire003.dev.coloredglowlib.component.GlobalColorComponent;
import me.emafire003.dev.coloredglowlib.custom_data_animations.CustomColorAnimation;
import me.emafire003.dev.coloredglowlib.util.ColorUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.scoreboard.Scoreboard;

import static me.emafire003.dev.coloredglowlib.ColoredGlowLibMod.*;

public class ColoredGlowLibAPI {


	//Needed to access server-wide settings such as default color and entitytype's colors
	private final GlobalColorComponent globalColorComponent;

	/**Create an instance of the ColoredGlowLib API
	 *
	 * @param scoreboard An instance of the server/worlds scoreboard that will be used to get
	 *                      server-wide settings such as defaultColor the EntityType's colors and so on*/
	public ColoredGlowLibAPI(Scoreboard scoreboard){
		this.globalColorComponent = GLOBAL_COLOR_COMPONENT.get(scoreboard);

	}


	/**Set this to true to override the default minecraft team colors
	 * even of the entity is in a team.
	 *
	 * By default this option is set to <i>false</i>
	 *
	 * @param b The value to assing to overrideTeamColors*/
	public void setOverrideTeamColors(boolean b){
		globalColorComponent.setOverrideTeamColors(b);
	}

	/**Gets the overrideTeamColors option.
	 *
	 * @return Returns true if the default minecraft team colors are being overridden by the mod's ones.
	 * */
	public  boolean getOverrideTeamColors(){
		return globalColorComponent.getOverrideTeamColors();
	}

	/** Makes the Default color override a potential Entity-specific or EntityType-specific color.
	 *
	 * By default this option is <i>false</i>
	 *
	 * @param b Set to true to enable overriding, set false to disable it.
	 */
	public void setDefaultOverridesAll(boolean b){
		globalColorComponent.setDefaultOverridesAll(b);
	}

	/**
	 * Gets the current override status of Default color over others
	 *
	 * @return Returns true if the Default color overrides the Entity/EntityType-specific one
	 * */
	public boolean getDefaultOverridesAll(){
		return globalColorComponent.getDefaultOverridesAll();
	}

	/**
	 * Makes the EntityType-specific color override a potential
	 * Entity-specific color.
	 *
	 * By default this option is <i>false</i>
	 *
	 * @param b Set to true to enable overriding, set false to disable it.
	 * */
	public void setEntityTypeColorOverridesEntityColor(boolean b){
		globalColorComponent.setTypeOverridesEntityColor(b);
	}

	/**
	 * Gets the current override status of EntityType<?> color over Entity Color
	 *
	 * @return Returns true if the EntityType-specific color overrides the Entity-specific one
	 * */
	public boolean getEntityTypeColorOverridesEntityColor(){
		return globalColorComponent.getEntityTypeOverridesEntityColor();
	}
	
	// ========= END OF THE SETTINGS PART =========
	
	
	// ========= START OF THE SET COLOR PART =========


	/** Sets a default global color. The one used by default is <b>"#ffffff</b>, same as vanilla Minecraft.
	 *
	 * If an entity has another color assigned to its EntityType, or itself it will glow that color
	 * instead of this one, unless {@link #setDefaultOverridesAll(boolean)} method is used.
	 *
	 * @param color An hexadecimal color value String, like <b>"#RRGGBB"</b>, or <b>"rainbow"</b> to make the rainbow color,
	 *                 or <b>"random"</b> to make a random color each tick.
	 *                 It can also be a custom color animation name added by a datapack
	 */
	public void setGlobalColor(String color){
		globalColorComponent.setDefaultColor(color);
	}

	/** Makes the default color to be "rainbow", so every entity will by default
	 * glow in rainbow colors, kinda like a _jeb sheep.
	 *
	 * If an entity has another color assigned to its EntityType, or itself it will glow that color
	 * instead of this one, unless {@link #setDefaultOverridesAll(boolean)} method is used.
	 */
	public void setGlobalRainbow(){
		setGlobalColor("rainbow");
	}

	/** Makes the default color to be "random", so every entity will by default
	 * glow in a random color each tick.
	 *
	 * If an entity has another color assigned to its EntityType, or itself it will glow that color
	 * instead of this one, unless {@link #setDefaultOverridesAll(boolean)} method is used.
	 */
	public void setGlobalRandom(){
		setGlobalColor("random");
	}

	/**An alias of {@link #setGlobalColor(String)}*/
	public void setDefaultColor(String color){
		setGlobalColor(color);
	}

	/**Resets the global default color to #ffffff, which is the default color
	 * used by vanilla Minecraft.*/
	public void clearGlobalColor(){
		globalColorComponent.setDefaultColor("#ffffff");
	}

	/**
	 * Sets a custom glow color for an Entity.
	 * The entity will now glow the specified color instead of vanilla minecraft's one.
	 *
	 * This glow color can be overridden by other methods, such as:
	 * {@link #setDefaultOverridesAll(boolean)} and {@link #setEntityTypeColorOverridesEntityColor(boolean)}
	 *
	 * If no color is specified, but an EntityType<?> or default color is, the entity will glow that color.
	 *
	 * @param target The Entity that will glow the specified color
	 * @param color An hexadecimal color value String, like <b>"#RRGGBB"</b>, or <b>"rainbow"</b> to make the rainbow color,
	 *                 or <b>"random"</b> to make a random color each tick.
	 *                 It can also be a custom color animation name added by a datapack
	 */
	public void setColor(Entity target, String color){
		ColorComponent component = COLOR_COMPONENT.get(target);
		component.setColor(color);
	}

	/**
	 * Sets a custom glow color for an EntityType.
	 * All entities of the specified EntityType<?> will now glow the specified color instead of vanilla minecraft's one.
	 *
	 * This glow color can be overridden by other methods, such as:
	 * {@link #setDefaultOverridesAll(boolean)}
	 *
	 * If an Entity has a color which is different from its EntityType's one it will glow that color unless
	 * {@link #setEntityTypeColorOverridesEntityColor(boolean)} is enabled
	 *
	 * If no color is specified, but a default color is, the entities will glow that color.
	 *
	 * @param target The EntityType that will glow the specified color
	 * @param color An hexadecimal color value String, like <b>"#RRGGBB"</b>, or <b>"rainbow"</b> to make the rainbow color,
	 *                 or <b>"random"</b> to make a random color each tick.
	 *                 It can also be a custom color animation name added by a datapack
	 */
	public void setColor(EntityType<?> target, String color){
		globalColorComponent.setEntityTypeColor(target, color);
	}

	/**
	 * Sets the custom glow color of an Entity to rainbow.
	 * This will make the entity glow every color periodically like a _jeb sheep
	 *
	 * See {@link #setColor(Entity, String)} for more information
	 *
	 * @param target The Entity that will glow the specified color
	 * */
	public void setRainbowColor(Entity target){
		setColor(target, "rainbow");
	}

	/**
	 * Sets the custom glow color of an Entity to a random, the entity will glow a different color each tick.
	 *
	 * See {@link #setColor(Entity, String)} for more information
	 *
	 * @param target The Entity that will glow the specified color
	 * */
	public void setRandomColor(Entity target){
		setColor(target, "random");
	}

	/**
	 * Sets the custom glow color of an EntityType to rainbow.
	 * This will make the entities of that type glow every color periodically like a _jeb sheep
	 *
	 * See {@link #setColor(EntityType, String)} for more information
	 *
	 * @param target The EntityType that will glow the specified color
	 * */
	public void setRainbowColor(EntityType<?> target){
		setColor(target, "rainbow");
	}

	/**
	 * Sets the custom glow color of an Entity to a random, the entity will glow a different color each tick.
	 *
	 * See {@link #setColor(EntityType, String)} for more information
	 *
	 * @param target The EntityType that will glow the specified color
	 * */
	public void setRandomColor(EntityType<?> target){
		setColor(target, "random");
	}


	/**Removes the custom color from an entity. It will be set back to "#fffff",
	 * unless <b>useDefaultColorInstead</b> is true, in which case the default color
	 * you specified will be used. The default color is the same one that would be applied globally
	 * if {@link #setDefaultOverridesAll(boolean)} is used
	 *
	 * @param entity The entity that will be cleared from the color
	 * @param useDefaultColorInstead Weather or not to use the default color or #ffffff
	 * */
	public void clearColor(Entity entity, boolean useDefaultColorInstead){
		ColorComponent component = COLOR_COMPONENT.get(entity);
		if(useDefaultColorInstead){
			component.setColor(globalColorComponent.getDefaultColor());
			return;
		}
		component.clear();
	}

	/**Removes the custom color from an EntityType. It will be set back to "#fffff",
	 * unless <b>useDefaultColorInstead</b> is true, in which case the default color
	 * you specified will be used. The default color is the same one that would be applied globally
	 * if {@link #setDefaultOverridesAll(boolean)} is used.
	 *
	 * This will also clear the rainbow/random/custom color!
	 *
	 * @param entityType The EntityType that will be cleared from the color
	 * @param useDefaultColorInstead Weather or not to use the default color or #ffffff
	 * */
	public void clearColor(EntityType<?> entityType, boolean useDefaultColorInstead){
		if(useDefaultColorInstead){
			globalColorComponent.setEntityTypeColor(entityType, globalColorComponent.getDefaultColor());
			return;
		}
		globalColorComponent.clearEntityTypeColor(entityType);
	}


	/**
	 * Gets the custom glow color of an Entity.
	 *
	 * The result could be "#ffffff" meaning it does not have a custom color and is using
	 * the vanilla one, or "rainbow" meaning its glowing rainbow,
	 * or "random" meaning its glowing a random color each tick,
	 * or another hexadecimal string color.
	 *
	 * It can also return a custom animation name if they are added by a datapack
	 *
	 * If you need a color value instead you can use {@link me.emafire003.dev.coloredglowlib.util.ColorUtils} to manipulate it
 	 *
	 * @param target The Entity to check the color for
	 *
	 * @return The color string associated to that entity
	 * */
	public String getColor(Entity target){
		return COLOR_COMPONENT.get(target).getColor();
	}

	/**
	 * Gets the custom glow color of an EntityType.
	 *
	 * The result could be "#ffffff" meaning it does not have a custom color and is using
	 * the vanilla one, or "rainbow" meaning its glowing rainbow,
	 * or "random" meaning its glowing a random color each tick,
	 * or another hexadecimal string color.
	 *
	 * It can also return a custom animation name if they are added by a datapack
	 *
	 * If you need a color value instead you can use {@link me.emafire003.dev.coloredglowlib.util.ColorUtils} to manipulate it
	 *
	 * @param target The EntityType to check the color for
	 *
	 * @return The color string associated to that EntityType
	 * */
	public String getColor(EntityType<?> target){
		return globalColorComponent.getEntityTypeColor(target);
	}


	/**
	 * Checks if an EntityType<?> has a custom glow color or not.
	 * This is done by checking if its color is <i>"#ffffff"</i> or not.
	 *
	 * <b>WARNING! This doesn't mean necessarily mean it has a custom color added by a datapack
	 * but that it has a color that is different from the default value of white!</b>
	 *
	 * Warning! If you used {@link #clearColor(EntityType, boolean)} with <i>useDefaultColorInstead</i> to true,
	 * you may want to use: {@link #hasCustomOrDefaultColor(EntityType)}
	 *
	 * @param target The EntityType<?> to check the color for
	 *
	 * @return Returns true if the EntityType<?> has a custom glow color associated to it.
	 */
	public boolean hasCustomColor(EntityType<?> target){
		return !ColorUtils.checkDefault(globalColorComponent.getEntityTypeColor(target));
	}

	/**
	 * Checks if an EntityType has a custom glow color or not.
	 * This is done by checking if its color is <i>"#ffffff"</i> or if it's the defaultColor specified using {@link #setDefaultColor(String)
	 *
	 * <b>WARNING! This doesn't mean necessarily mean it has a custom color added by a datapack
	 * but that it has a color that is different from the default value of white!</b>
	 *
	 * @param target The EntityType to check the color for
	 *
	 * @return Returns true if the EntityType has a custom glow color associated to it that differs from the defaultColor.
	 */
	public boolean hasCustomOrDefaultColor(EntityType<?> target){
		return !(ColorUtils.checkDefault(globalColorComponent.getEntityTypeColor(target))
				|| ColorUtils.checkSameColor(globalColorComponent.getEntityTypeColor(target), globalColorComponent.getDefaultColor())) ;
	}

	/**
	 * Checks if an Entity has a custom glow color or not.
	 * This is done by checking if its color is <i>"#ffffff"</i> or not.
	 *
	 * <b>WARNING! This doesn't mean necessarily mean it has a custom color added by a datapack
	 * but that it has a color that is different from the default value of white!</b>
	 *
	 * Warning! If you used {@link #clearColor(Entity, boolean)} with <i>useDefaultColorInstead</i> to true,
	 * you may want to use: {@link #hasCustomOrDefaultColor(Entity)}
	 *
	 * @param target The Entity to check the color for
	 *
	 * @return Returns true if the Entity has a custom glow color associated to it.
	 */
	public boolean hasCustomColor(Entity target){
		return ColorUtils.checkDefault(COLOR_COMPONENT.get(target).getColor());
	}

	/**
	 * Checks if an Entity has a custom glow color or not.
	 * This is done by checking if its color is <i>"#ffffff"</i> or if it's the defaultColor specified using {@link #setDefaultColor(String)
	 *
	 * <b>WARNING! This doesn't mean necessarily mean it has a custom color added by a datapack
	 * but that it has a color that is different from the default value of white!</b>
	 *
	 * @param target The Entity to check the color for
	 *
	 * @return Returns true if the Entity has a custom glow color associated to it that differs from the defaultColor.
	 */
	public  boolean hasCustomOrDefaultColor(Entity target){
		return ColorUtils.checkDefault(COLOR_COMPONENT.get(target).getColor())
				|| ColorUtils.checkSameColor(COLOR_COMPONENT.get(target).getColor(), globalColorComponent.getDefaultColor()) ;
	}


	/**
	 * Checks is the custom color of EntityType is rainbow
	 *
	 * @param target The EntityType to check the rainbow color for
	 *
	 * @return Returns true if the color associated to that EntityType is rainbow
	 * */
	public boolean hasRainbowColor(EntityType<?> target){
		return globalColorComponent.getEntityTypeColor(target).equalsIgnoreCase("rainbow");
	}

	/**
	 * Checks is the custom color of EntityType is random
	 *
	 * @param target The EntityType to check the random color for
	 *
	 * @return Returns true if the color associated to that EntityType is random
	 * */
	public boolean hasRandomColor(EntityType<?> target){
		return globalColorComponent.getEntityTypeColor(target).equalsIgnoreCase("random");
	}

	/**
	 * Checks is the custom color of Entity is rainbow
	 *
	 * @param target The Entity to check the rainbow color for
	 *
	 * @return Returns true if the color associated to that Entity is rainbow
	 * */
	public boolean hasRainbowColor(Entity target){
		return COLOR_COMPONENT.get(target).getColor().equalsIgnoreCase("rainbow");
	}

	/**
	 * Checks is the custom color of Entity is random
	 *
	 * @param target The Entity to check the random color for
	 *
	 * @return Returns true if the color associated to that Entity is random
	 * */
	public boolean hasRandomColor(Entity target){
		return COLOR_COMPONENT.get(target).getColor().equalsIgnoreCase("random");
	}

	/**
	 * Checks is the custom color of EntityType is a custom animation added through a datapack
	 *
	 * @param target The EntityType to check the rainbow color for
	 *
	 * @return Returns true if the color associated to that EntityType is added by a datapack
	 * */
	public boolean hasCustomColorAnimation(EntityType<?> target){
		String target_color = globalColorComponent.getEntityTypeColor(target);
		for(CustomColorAnimation animation : ColoredGlowLibMod.getCustomColorAnimations()){
			if(target_color.equalsIgnoreCase(animation.getName())){
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks is the custom color of EntityType is a custom animation added through a datapack
	 *
	 * @param target The Entity to check the random color for
	 *
	 * @return Returns true if the color associated to that Entity is added by a datapack
	 * */
	public boolean hasCustomColorAnimation(Entity target){
		String target_color = COLOR_COMPONENT.get(target).getColor();

		return false;
	}


}
