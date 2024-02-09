package me.emafire003.dev.coloredglowlib.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.emafire003.dev.coloredglowlib.compat.permissions.PermissionsChecker;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class CGLCommands {


    //Based on Factions' code https://github.com/ickerio/factions
    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        LiteralCommandNode<ServerCommandSource> cgl_commands = CommandManager
                .literal("cgl")
                .build();

        LiteralCommandNode<ServerCommandSource> alias = CommandManager
                .literal("coloredglowlib")
                //TODO make sure it doesn't crash withouth the dependency
                .requires(PermissionsChecker.hasPerms("coloredglowlib.commands", 2))
                .build();

        dispatcher.getRoot().addChild(cgl_commands);
        dispatcher.getRoot().addChild(alias);

        CGLCommand[] commands = new CGLCommand[] {
                new SetGlowColorCommand(),
                new SettingsCommand(),
                new ClearGlowColorCommand()
        };

        for (CGLCommand command : commands) {
            cgl_commands.addChild(command.getNode(registryAccess));
            alias.addChild(command.getNode(registryAccess));
        }
    }
}
