package me.emafire003.dev.coloredglowlib.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.emafire003.dev.coloredglowlib.compat.permissions.PermissionsChecker;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;

public class CGLCommands {


    //Based on Factions' code https://github.com/ickerio/factions
    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment) {
        LiteralCommandNode<CommandSourceStack> cgl_commands = Commands
                .literal("cgl")
                .build();

        LiteralCommandNode<CommandSourceStack> alias = Commands
                .literal("coloredglowlib")
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
