package me.emafire003.dev.coloredglowlib.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;


//Initially based on Factions' code https://github.com/ickerio/factions
//then modified by me (Emafire003) and ported to forge, again, by me
public class CGLCommands {
    public CGLCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode<CommandSourceStack> cgl_commands = Commands
                .literal("cgl")
                .requires(serverCommandSource -> {
                    return serverCommandSource.hasPermission(2);
                })
                .build();

        LiteralCommandNode<CommandSourceStack> alias = Commands
                .literal("coloredglowlib")
                .requires(serverCommandSource -> {
                    return serverCommandSource.hasPermission(2);
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
