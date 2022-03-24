package me.emafire003.dev.coloredglowlib.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.emafire003.dev.coloredglowlib.ColoredGlowLib;
import me.emafire003.dev.coloredglowlib.util.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

import java.util.Collection;
import java.util.List;

import static me.emafire003.dev.coloredglowlib.ColoredGlowLib.LOGGER;

//@Environment(EnvType.SERVER)
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
                    ColoredGlowLib.setColorToEntity(entity, Color.translateFromHEX(color));
                }
            }

            if(!source.getWorld().isClient){
                List<ServerPlayerEntity> players = source.getServer().getPlayerManager().getPlayerList();
                for (ServerPlayerEntity player : players) {
                    ColoredGlowLib.sendDataPackets(player);
                }
            }

            //source.sendFeedback(new TranslatableText("commands.setglowcolor.success1").append(color).append(new TranslatableText("commands.setglowcolor.success2")), true);
            source.sendFeedback(new LiteralText("Setted color '" + color + "' to the selected entity/entities!"), false);
            return targets.size();
        }else{
            //source.sendError(new TranslatableText("commands.setglowcolor.notcolor"));
            source.sendError(new LiteralText("Error! The value you have specified is not valid! It should be RRGGBB (without '#') or 'rainbow'"));
            return 0;
        }
    }

}
