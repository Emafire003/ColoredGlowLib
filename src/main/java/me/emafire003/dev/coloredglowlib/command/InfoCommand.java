package me.emafire003.dev.coloredglowlib.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;


public class InfoCommand implements CGLCommand {


    private int info(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        source.sendSystemMessage(Component.literal(ColoredGlowLibMod.PREFIX+"Running version §l2.0.0§r of ColoredGlowLib - Forge"));
        source.sendSystemMessage(Component.literal(ColoredGlowLibMod.PREFIX+"A mod made by §9Emafire003"));
        source.sendSystemMessage(Component.literal(ColoredGlowLibMod.PREFIX+"Source Code: https://github.com/Emafire003/ColoredGlowLib"));

        return 1;
    }

    public LiteralCommandNode<CommandSourceStack> getNode() {
        return Commands
                .literal("info")
                .executes(this::info)
                .build();
    }

}
