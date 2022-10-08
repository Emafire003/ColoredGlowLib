package me.emafire003.dev.coloredglowlib.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

public class CGLCommands {


    //Based on Factions' code https://github.com/ickerio/factions
    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        LiteralCommandNode<ServerCommandSource> cgl_commands = CommandManager
                .literal("cgl")
                .requires(serverCommandSource -> {
                    return serverCommandSource.hasPermissionLevel(2);
                })
                .build();

        LiteralCommandNode<ServerCommandSource> alias = CommandManager
                .literal("coloredglowlib")
                .requires(serverCommandSource -> {
                    return serverCommandSource.hasPermissionLevel(2);
                })
                .build();

        dispatcher.getRoot().addChild(cgl_commands);
        dispatcher.getRoot().addChild(alias);

        CGLCommand[] commands = new CGLCommand[] {
                new SetGlowColorCommand(),
                new ConfigCommand(),
                new InfoCommand()
        };

        for (CGLCommand command : commands) {
            cgl_commands.addChild(command.getNode());
            alias.addChild(command.getNode());
        }
    }
}
