package me.emafire003.dev.coloredglowlib.command;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import static me.emafire003.dev.coloredglowlib.ColoredGlowLibMod.LOGGER;

public class CGLCommandRegister {
    public static void registerCommands() {
        LOGGER.info("Registering commands...");
        CommandRegistrationCallback.EVENT.register(SetGlowingColor::register);
        CommandRegistrationCallback.EVENT.register(SetTypeGlowingColor::register);
        LOGGER.info("Done!");
    }
}
