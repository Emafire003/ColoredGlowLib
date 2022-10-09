package me.emafire003.dev.coloredglowlib.command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;


public class PrefixCommand implements CGLCommand {

    public LiteralCommandNode<CommandSourceStack> getNode() {
        return Commands
                .literal("cgl")

                .build();
    }

}
