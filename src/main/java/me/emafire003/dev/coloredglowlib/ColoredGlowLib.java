package me.emafire003.dev.coloredglowlib;

import me.emafire003.dev.coloredglowlib.util.Color;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class ColoredGlowLib implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static String MOD_ID = "coloredglowlib";
	public static Color color = new Color(255, 255, 255);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		//Why do i even have to write this :/
		System.out.println("[ColoredGlowLib] Hey, if you have downloaded this mod from a site different from CurseForge," +
				" Modrinth or Github, please remove this mod and download it again from an official source, " +
				"such as the ones cited before. Mods on other sites are NOT checked or secure since i did NOT " +
				"upload them there (those site also violate the license of the mod, and thus they are NOT LEGAL)");

	}
	public static final GameRules.Key<GameRules.BooleanRule> OVERRIDE_TEAM_COLORS =
			GameRuleRegistry.register("overrideTeamColors", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));

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

}
