package me.emafire003.dev.coloredglowlib.command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;


//Based on Factions' code https://github.com/ickerio/factions
public interface CGLCommand {
    LiteralCommandNode<ServerCommandSource> getNode(CommandRegistryAccess registryAccess);
}
