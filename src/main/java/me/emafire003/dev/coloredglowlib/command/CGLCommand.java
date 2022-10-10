package me.emafire003.dev.coloredglowlib.command;

import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.commands.CommandSourceStack;


//Based on Factions' code https://github.com/ickerio/factions
public interface CGLCommand {
    CommandNode<CommandSourceStack> getNode();

}
