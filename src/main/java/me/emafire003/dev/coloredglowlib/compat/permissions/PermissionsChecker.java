package me.emafire003.dev.coloredglowlib.compat.permissions;

import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.Entity;
import net.minecraft.commands.CommandSourceStack;

import java.util.function.Predicate;

//Based on Factions' code https://github.com/ickerio/factions (MIT license)
public class PermissionsChecker {

    public static final boolean permissions = FabricLoader.getInstance().isModLoaded("fabric-permissions-api-v0");

    @SafeVarargs
    public static Predicate<CommandSourceStack> multiple(
            Predicate<CommandSourceStack>... args) {
        return source -> {
            for (Predicate<CommandSourceStack> predicate : args) {
                if (!predicate.test(source))
                    return false;
            }
            return true;
        };
    }

    public static Predicate<CommandSourceStack> hasPerms(String permission, int defaultValue) {
        return (source) -> {
            if(!permissions){
                return source.permissions().hasPermission(net.minecraft.server.permissions.Permissions.COMMANDS_ADMIN);
            }else {
                return Permissions.check(source, permission, defaultValue);
            }
        };
    }

    public static boolean hasPerms(Entity entity, String permission, boolean defValue){
        if(!permissions){
            return defValue;
        }else {
            return Permissions.check(entity, permission, defValue);
        }
    }
}
