package me.emafire003.dev.coloredglowlib.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.emafire003.dev.coloredglowlib.ColoredGlowLib;
import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import me.emafire003.dev.coloredglowlib.util.Color;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Collection;

public class SetGlowingColor {


    @SuppressWarnings("all")
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher
                .register(
                        (LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("setglowcolor")
                                .requires((source) -> {
            return source.hasPermissionLevel(2);
        }
        )).then(CommandManager.argument("targets", EntityArgumentType.entities()).then(((RequiredArgumentBuilder)CommandManager.argument("color", StringArgumentType.string()).executes((context) -> {
            return execute((ServerCommandSource)context.getSource(), StringArgumentType.getString(context, "color"), EntityArgumentType.getEntities(context, "targets"));
        }))/*.then(CommandManager.argument("count", IntegerArgumentType.integer(1)).executes((context) -> {
            return execute((ServerCommandSource)context.getSource(), ItemStackArgumentType.getItemStackArgument(context, "item"), EntityArgumentType.getPlayers(context, "targets"), IntegerArgumentType.getInteger(context, "count"));
        }))*/)));
    }


    private static int execute(ServerCommandSource source, String color, Collection<? extends Entity> targets) throws CommandSyntaxException {
        color = "#"+color;

        if(Color.isHexColor(color) || color.equalsIgnoreCase("#rainbow")){
            for (Entity entity : targets) {
                ColoredGlowLibMod.getLib().removeColor(entity);
                if(color.equalsIgnoreCase("#rainbow")){
                    ColoredGlowLibMod.getLib().setRainbowColorToEntity(entity, true);
                }else{
                    ColoredGlowLibMod.getLib().setColorToEntity(entity, Color.translateFromHEX(color));
                }
            }

            if(!source.getWorld().isClient){
                ColoredGlowLibMod.getLib().updateData(source.getServer());
            }

            //source.sendFeedback(new TranslatableText("commands.setglowcolor.success1").append(color).append(new TranslatableText("commands.setglowcolor.success2")), true);
            source.sendFeedback(Text.literal("Setted color '" + color + "' to the selected entity/entities!"), false);
            return targets.size();
        }else{
            //source.sendError(new TranslatableText("commands.setglowcolor.notcolor"));
            source.sendError(Text.literal("Error! The value you have specified is not valid! It should be RRGGBB (without '#') or 'rainbow'"));
            return 0;
        }
    }
}
