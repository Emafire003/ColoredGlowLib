package me.emafire003.dev.coloredglowlib.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;


public class InfoCommand implements CGLCommand {


    private int info(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        source.sendFeedback(Text.literal(ColoredGlowLibMod.PREFIX+"Running version §l2.0.0§r of ColoredGlowLib"), false);
        source.sendFeedback(Text.literal(ColoredGlowLibMod.PREFIX+"A mod made by §9Emafire003"), false);
        source.sendFeedback(Text.literal(ColoredGlowLibMod.PREFIX+"Source Code: https://github.com/Emafire003/ColoredGlowLib"), false);

        return 1;
    }

    public LiteralCommandNode<CommandSourceStack> getNode() {
        return CommandManager
                .literal("info")
                .executes(this::info)
                .build();
    }

}
