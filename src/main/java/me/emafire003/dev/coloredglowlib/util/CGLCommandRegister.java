package me.emafire003.dev.coloredglowlib.util;

import me.emafire003.dev.coloredglowlib.command.SetGlowingColor;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

public class CGLCommandRegister {
    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(SetGlowingColor::register);
    }
}
