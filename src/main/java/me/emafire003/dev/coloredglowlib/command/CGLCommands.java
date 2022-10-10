package me.emafire003.dev.coloredglowlib.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.RegistryAccess;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;

public class CGLCommands {


    //Based on Factions' code https://github.com/ickerio/factions
    public static void registerCommands(CommandDispatcher<CommandSource> dispatcher, RegistryAccess registryAccess) {
        /*CGLCommand cgl_commands = new CGLCommand() {
            @Override
            public LiteralCommandNode<CommandSourceStack> getNode() {
                return Commands
                        .literal("cgl")
                        .build();
            }
        };*/

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

        dispatcher.getRoot().addChild((CommandNode<CommandSource>) cgl_commands.getRelevantNodes(new StringReader("cgl")));
        dispatcher.getRoot().addChild((CommandNode<CommandSource>) alias.getRelevantNodes(new StringReader("coloredglowlib")));


        CGLCommand[] commands = new CGLCommand[] {
                new SetGlowColorCommand(),
                new ConfigCommand(),
                new InfoCommand()
        };

        for (CGLCommand command : commands) {
            cgl_commands.addChild(command.getNode());
            //alias.addChild(command.getNode());
        }
    }
}
