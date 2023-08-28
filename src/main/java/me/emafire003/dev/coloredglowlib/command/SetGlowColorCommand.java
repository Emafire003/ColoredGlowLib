package me.emafire003.dev.coloredglowlib.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import me.emafire003.dev.coloredglowlib.util.Color;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Collection;

public class SetGlowColorCommand implements CGLCommand {

    private int setGlowColor(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Collection<? extends Entity> targets = EntityArgumentType.getEntities(context, "targets");
        String color = "#"+StringArgumentType.getString(context, "color");
        ServerCommandSource source = context.getSource();

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
            source.sendFeedback(() -> Text.literal(ColoredGlowLibMod.PREFIX+"Setted color '" + color + "' to the selected entity/entities!"), false);
            ColoredGlowLibMod.getLib().optimizeData();
            return targets.size();
        }else{
            //source.sendError(new TranslatableText("commands.setglowcolor.notcolor"));
            source.sendError((Text.literal(ColoredGlowLibMod.PREFIX+"Error! The value you have specified is not valid! It should be RRGGBB (without '#') or 'rainbow'")));
            return 0;
        }
    }

    private int setTypeGlowColor(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String color = "#"+StringArgumentType.getString(context, "color");
        ServerCommandSource source = context.getSource();

        if(Color.isHexColor(color) || color.equalsIgnoreCase("#rainbow")){
            EntityType type = EntityType.get(EntityArgumentType.getEntity(context, "entity").toString()).get();
            ColoredGlowLibMod.getLib().removeColor(type);
            if(color.equalsIgnoreCase("#rainbow")){
                ColoredGlowLibMod.getLib().setRainbowColorToEntityType(type, true);
            }else{
                ColoredGlowLibMod.getLib().setColorToEntityType(type, Color.translateFromHEX(color));
            }

            if(!source.getWorld().isClient){
                ColoredGlowLibMod.getLib().updateData(source.getServer());
            }

            //source.sendFeedback(new TranslatableText("commands.setglowcolor.success1").append(color).append(new TranslatableText("commands.setglowcolor.success2")), true);
            source.sendFeedback(() -> Text.literal(ColoredGlowLibMod.PREFIX+"Setted color '" + color + "' to the selected entity/entities!"), false);
            ColoredGlowLibMod.getLib().optimizeData();
            return 1;
        }else{
            //source.sendError(new TranslatableText("commands.setglowcolor.notcolor"));
            source.sendError(Text.literal(ColoredGlowLibMod.PREFIX+"Error! The value you have specified is not valid! It should be RRGGBB (without '#') or 'rainbow'"));
            return 0;
        }
    }

    private int setDefaultGlowColor(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String color = "#"+StringArgumentType.getString(context, "color");
        ServerCommandSource source = context.getSource();

        if(Color.isHexColor(color) || color.equalsIgnoreCase("#rainbow")){
            EntityType type = EntityType.get(EntityArgumentType.getEntity(context, "entity").toString()).get();
            ColoredGlowLibMod.getLib().removeColor(type);
            if(color.equalsIgnoreCase("#rainbow")){
                ColoredGlowLibMod.getLib().setGeneralizedRainbow(true);
            }else{
                ColoredGlowLibMod.getLib().setColor(Color.translateFromHEX(color));
            }

            if(!source.getWorld().isClient){
                ColoredGlowLibMod.getLib().updateData(source.getServer());
            }

            source.sendFeedback(() -> Text.literal(ColoredGlowLibMod.PREFIX+"Setted color '" + color + "' as the default color!"), false);
            ColoredGlowLibMod.getLib().optimizeData();
            return 1;
        }else{
            //source.sendError(new TranslatableText("commands.setglowcolor.notcolor"));
            source.sendError(Text.literal(ColoredGlowLibMod.PREFIX+"Error! The value you have specified is not valid! It should be RRGGBB (without '#') or 'rainbow'"));
            return 0;
        }
    }

    public LiteralCommandNode<ServerCommandSource> getNode() {
        return CommandManager
                .literal("setglowcolor")
                .then(
                        CommandManager.argument("targets", EntityArgumentType.entities())
                                .then(
                                    CommandManager.argument("color", StringArgumentType.string())
                                .executes(this::setGlowColor)
                                )
                )
                .then(
                        CommandManager.argument("entity", EntityArgumentType.entity()).suggests(SuggestionProviders.SUMMONABLE_ENTITIES)
                                .then(
                                        CommandManager.argument("color", StringArgumentType.string())
                                                .executes(this::setTypeGlowColor)
                                )
                )
                .then(
                        CommandManager.literal("default")
                                .then(
                                        CommandManager.argument("color", StringArgumentType.string())
                                                .executes(this::setDefaultGlowColor)
                                )
                )
                .build();
    }

}
