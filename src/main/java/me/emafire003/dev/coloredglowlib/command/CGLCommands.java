package me.emafire003.dev.coloredglowlib.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.RegistryAccess;

public class CGLCommands {


    //Based on Factions' code https://github.com/ickerio/factions
    public static void registerCommands(CommandDispatcher<CommandSource> dispatcher, RegistryAccess registryAccess) {
        CGLCommand cgl_commands = new CGLCommand() {
            @Override
            public LiteralCommandNode<CommandSourceStack> getNode() {
                return Commands
                        .literal("cgl")
                        .build();
            }
        };

        CGLCommand alias = new CGLCommand() {
            @Override
            public LiteralCommandNode<CommandSourceStack> getNode() {
                return Commands
                        .literal("coloredglowlib")
                        .build();
            }
        };


        dispatcher.getRoot().addChild((CommandNode<CommandSource>) cgl_commands);
        dispatcher.getRoot().addChild((CommandNode<CommandSource>) alias);


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
