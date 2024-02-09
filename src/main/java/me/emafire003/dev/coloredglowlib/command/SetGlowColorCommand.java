package me.emafire003.dev.coloredglowlib.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.emafire003.dev.coloredglowlib.ColoredGlowLibAPI;
import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import me.emafire003.dev.coloredglowlib.compat.permissions.PermissionsChecker;
import me.emafire003.dev.coloredglowlib.util.ColorUtils;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.RegistryEntryArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Collection;

public class SetGlowColorCommand implements CGLCommand {

    private int setGlowColor(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Collection<? extends Entity> targets = EntityArgumentType.getEntities(context, "targets");
        String color = "#"+StringArgumentType.getString(context, "color");
        ServerCommandSource source = context.getSource();

        if(ColorUtils.isHexColor(color) || color.equalsIgnoreCase("#rainbow")){
            for (Entity entity : targets) {
                if(color.equalsIgnoreCase("#rainbow")){

                    if (ColoredGlowLibMod.getAPI() != null) {
                        ColoredGlowLibMod.getAPI().setRainbowColor(entity);
                    }else{
                        source.sendError(Text.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
                        return 1;
                    }
                }else{
                    if (ColoredGlowLibMod.getAPI() != null) {
                        ColoredGlowLibMod.getAPI().setColor(entity, color);
                    }else{
                        source.sendError(Text.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
                        return 1;
                    }
                }
            }

            //source.sendFeedback(new TranslatableText("commands.setglowcolor.success1").append(color).append(new TranslatableText("commands.setglowcolor.success2")), true);
            source.sendFeedback(() -> Text.literal(ColoredGlowLibMod.PREFIX+"Setted color '" + color + "' to the selected entity/entities!"), false);
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

        if(ColorUtils.isHexColor(color) || color.equalsIgnoreCase("#rainbow")){
            EntityType<?> type = RegistryEntryArgumentType.getSummonableEntityType(context, "entity").value();
            if(color.equalsIgnoreCase("#rainbow")){
                if (ColoredGlowLibMod.getAPI() != null) {
                    ColoredGlowLibMod.getAPI().setRainbowColor(type);
                }else{
                    source.sendError(Text.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
                    return 1;
                }
            }else{
                if (ColoredGlowLibMod.getAPI() != null) {
                    ColoredGlowLibMod.getAPI().setColor(type, color);
                }else{
                    source.sendError(Text.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
                    return 1;
                }
            }

            //source.sendFeedback(new TranslatableText("commands.setglowcolor.success1").append(color).append(new TranslatableText("commands.setglowcolor.success2")), true);
            source.sendFeedback(() -> Text.literal(ColoredGlowLibMod.PREFIX+"Setted color '" + color + "' to the selected entity/entities!"), false);
            return 1;
        }else{
            //source.sendError(new TranslatableText("commands.setglowcolor.notcolor"));
            source.sendError(Text.literal(ColoredGlowLibMod.PREFIX+"Error! The value you have specified is not valid! It should be RRGGBB (without '#') or 'rainbow'"));
            return 0;
        }
    }

    private int setDefaultGlowColor(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        try {
            String color = "#"+StringArgumentType.getString(context, "color");
            ServerCommandSource source = context.getSource();

            if(ColorUtils.isHexColor(color) || color.equalsIgnoreCase("#rainbow")){
                if(color.equalsIgnoreCase("#rainbow")){
                    if (ColoredGlowLibMod.getAPI() != null) {
                        ColoredGlowLibMod.getAPI().setGlobalRainbow();
                    }else{
                        source.sendError(Text.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
                        return 1;
                    }
                }else{
                    if (ColoredGlowLibMod.getAPI() != null) {
                        ColoredGlowLibMod.getAPI().setGlobalColor(color);
                    }else{
                        source.sendError(Text.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
                        return 1;
                    }
                }

                source.sendFeedback(() -> Text.literal(ColoredGlowLibMod.PREFIX+"Setted color '" + color + "' as the default color!"), false);
                return 1;
            }else{
                //source.sendError(new TranslatableText("commands.setglowcolor.notcolor"));
                source.sendError(Text.literal(ColoredGlowLibMod.PREFIX+"Error! The value you have specified is not valid! It should be RRGGBB (without '#') or 'rainbow'"));
                return 0;
            }
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }

    }

    private int clearEntityColor(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Collection<? extends Entity> targets = EntityArgumentType.getEntities(context, "targets");
        boolean useDefault = BoolArgumentType.getBool(context, "useDefaultColor");
        ServerCommandSource source = context.getSource();

        for (Entity entity : targets) {
            if (ColoredGlowLibMod.getAPI() != null) {
                ColoredGlowLibMod.getAPI().clearColor(entity, useDefault);
            }else{
                source.sendError(Text.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
                return 1;
            }
        }

        source.sendFeedback(() -> Text.literal(ColoredGlowLibMod.PREFIX+"Cleared the color from the selected entity/entities!"), true);
        return targets.size();
    }

    private int clearEntityTypeColor(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        boolean useDefault = BoolArgumentType.getBool(context, "useDefaultColor");
        EntityType<?> type = RegistryEntryArgumentType.getSummonableEntityType(context, "entity").value();

        ServerCommandSource source = context.getSource();

        if (ColoredGlowLibMod.getAPI() != null) {
            ColoredGlowLibMod.getAPI().clearColor(type, useDefault);
        }else{
            source.sendError(Text.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
            return 1;
        }

        source.sendFeedback(() -> Text.literal(ColoredGlowLibMod.PREFIX+"Cleared color from the selected entity/entities!"), false);
        return 1;
    }

    public LiteralCommandNode<ServerCommandSource> getNode(CommandRegistryAccess registryAccess) {
        return CommandManager
                .literal("setglowcolor")
                .requires(PermissionsChecker.hasPerms("coloredglowlib.commands.setglowcolor", 2))
                .then(
                        CommandManager.argument("targets", EntityArgumentType.entities())
                                .then(
                                    CommandManager.argument("color", StringArgumentType.string())
                                .executes(this::setGlowColor)
                                )
                )
                .then(
                        CommandManager.argument("entity", RegistryEntryArgumentType.registryEntry(registryAccess, RegistryKeys.ENTITY_TYPE)).suggests(SuggestionProviders.SUMMONABLE_ENTITIES)
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
