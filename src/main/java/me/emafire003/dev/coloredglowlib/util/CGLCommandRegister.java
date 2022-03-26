package me.emafire003.dev.coloredglowlib.util;

import me.emafire003.dev.coloredglowlib.command.SetGlowingColor;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

import static me.emafire003.dev.coloredglowlib.ColoredGlowLib.LOGGER;

public class CGLCommandRegister {
    public static void registerCommands() {
        LOGGER.info("Registering commands...");
        CommandRegistrationCallback.EVENT.register(SetGlowingColor::register);
        LOGGER.info("Done!");
    }
}
