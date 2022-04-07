package me.emafire003.dev.coloredglowlib.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.emafire003.dev.coloredglowlib.ColoredGlowLib;
import me.emafire003.dev.coloredglowlib.util.Color;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.EntitySummonArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.SummonCommand;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import java.util.Collection;

import static me.emafire003.dev.coloredglowlib.ColoredGlowLib.updateData;

public class SetTypeGlowingColor {

    @SuppressWarnings("all")
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean b) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("setglowcolor").requires((source) -> {
            return source.hasPermissionLevel(2);
        })).then(CommandManager.argument("entity", EntitySummonArgumentType.entitySummon()).suggests(SuggestionProviders.SUMMONABLE_ENTITIES).then(((RequiredArgumentBuilder)CommandManager.argument("color", StringArgumentType.string()).executes((context) -> {
            return execute((ServerCommandSource)context.getSource(), StringArgumentType.getString(context, "color"), EntitySummonArgumentType.getEntitySummon(context, "entity"));
        })))));
    }


    private static int execute(ServerCommandSource source, String color, Identifier id) throws CommandSyntaxException {
        color = "#"+color;

        if(Color.isHexColor(color) || color.equalsIgnoreCase("#rainbow")){
            EntityType type = EntityType.get(id.toString()).get();
            ColoredGlowLib.removeColor(type);
            if(color.equalsIgnoreCase("#rainbow")){
                ColoredGlowLib.setRainbowColorToEntityType(type, true);
            }else{
                ColoredGlowLib.setColorToEntityType(type, Color.translateFromHEX(color));
            }

            if(!source.getWorld().isClient){
                updateData(source.getServer());
            }

            //source.sendFeedback(new TranslatableText("commands.setglowcolor.success1").append(color).append(new TranslatableText("commands.setglowcolor.success2")), true);
            source.sendFeedback(new LiteralText("Setted color '" + color + "' to the selected entity/entities!"), false);
            return 1;
        }else{
            //source.sendError(new TranslatableText("commands.setglowcolor.notcolor"));
            source.sendError(new LiteralText("Error! The value you have specified is not valid! It should be RRGGBB (without '#') or 'rainbow'"));
            return 0;
        }
    }



}
