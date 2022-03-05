package me.emafire003.dev.coloredglowlib.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.emafire003.dev.coloredglowlib.ColoredGlowLib;
import me.emafire003.dev.coloredglowlib.util.Color;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;

import java.util.Collection;

import static me.emafire003.dev.coloredglowlib.ColoredGlowLib.per_entity_color_map;

public class SetGlowingColor {


    @SuppressWarnings("all")
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean b) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("setglowcolor").requires((source) -> {
            return source.hasPermissionLevel(2);
        })).then(CommandManager.argument("targets", EntityArgumentType.entities()).then(((RequiredArgumentBuilder)CommandManager.argument("color", StringArgumentType.string()).executes((context) -> {
            return execute((ServerCommandSource)context.getSource(), StringArgumentType.getString(context, "color"), EntityArgumentType.getEntities(context, "targets"));
        }))/*.then(CommandManager.argument("count", IntegerArgumentType.integer(1)).executes((context) -> {
            return execute((ServerCommandSource)context.getSource(), ItemStackArgumentType.getItemStackArgument(context, "item"), EntityArgumentType.getPlayers(context, "targets"), IntegerArgumentType.getInteger(context, "count"));
        }))*/)));
    }


    private static int execute(ServerCommandSource source, String color, Collection<? extends Entity> targets) throws CommandSyntaxException {
        //TODO add a regex check to see if the color already has the #
        color = "#"+color;
        if(Color.isHexColor(color) || color.equalsIgnoreCase("#rainbow")){
            for (Entity entity : targets) {
                if(color.equalsIgnoreCase("#rainbow")){
                    ColoredGlowLib.setRainbowColorToEntity(entity, true);
                }else{
                    //per_entity_color_map.put(entity.getUuid(), Color.translateFromHEX(color));
                    ColoredGlowLib.setColorToEntity(entity, Color.translateFromHEX(color));
                }
            }
            source.sendFeedback(new TranslatableText("commands.setglowcolor.success1").append(color).append(new TranslatableText("commands.setglowcolor.success2")), true);
            return targets.size();
        }else{
            source.sendError(new TranslatableText("commands.setglowcolor.notcolor"));
            return 0;
        }
    }

}
