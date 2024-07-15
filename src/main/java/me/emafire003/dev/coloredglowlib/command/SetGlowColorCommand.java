package me.emafire003.dev.coloredglowlib.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import me.emafire003.dev.coloredglowlib.compat.permissions.PermissionsChecker;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.RegistryEntryReferenceArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Collection;

import static me.emafire003.dev.coloredglowlib.util.ColorUtils.isValidColorOrCustom;

public class SetGlowColorCommand implements CGLCommand {
    

    private int setGlowColor(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Collection<? extends Entity> targets = EntityArgumentType.getEntities(context, "targets");
        String color = "#"+StringArgumentType.getString(context, "color");
        ServerCommandSource source = context.getSource();

        if(isValidColorOrCustom(color)){
            for (Entity entity : targets) {
                if(color.equalsIgnoreCase("#rainbow")){
                    if (ColoredGlowLibMod.getAPI() != null) {
                        ColoredGlowLibMod.getAPI().setRainbowColor(entity);
                    }else{
                        source.sendError(Text.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
                        return 1;
                    }
                }else if(color.equalsIgnoreCase("#random")){

                    if (ColoredGlowLibMod.getAPI() != null) {
                        ColoredGlowLibMod.getAPI().setRandomColor(entity);
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
            source.sendFeedback(() -> Text.literal(ColoredGlowLibMod.PREFIX+"§7Setted color '§b" + color + "§7' to the selected entity/entities!"), false);
            return targets.size();
        }else{
            //source.sendError(new TranslatableText("commands.setglowcolor.notcolor"));
            source.sendError((Text.literal(ColoredGlowLibMod.PREFIX+"Error! The value you have specified is not §cvalid! It should be RRGGBB (without '#') or 'rainbow' or 'random' or a custom animation name!")));
            return 0;
        }
    }

    private int setGlowColorFor(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        Collection<? extends Entity> targets = EntityArgumentType.getEntities(context, "targets");
        String color = "#"+StringArgumentType.getString(context, "color");
        PlayerEntity player = EntityArgumentType.getPlayer(context, "visibleOnlyToPlayer");


        if(isValidColorOrCustom(color)){
            for (Entity entity : targets) {
                if(color.equalsIgnoreCase("#rainbow")){
                    if (ColoredGlowLibMod.getAPI() != null) {
                        ColoredGlowLibMod.getAPI().setExclusiveColorFor(entity, "rainbow", player);
                    }else{
                        source.sendError(Text.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
                        return 1;
                    }
                }else if(color.equalsIgnoreCase("#random")){

                    if (ColoredGlowLibMod.getAPI() != null) {
                        ColoredGlowLibMod.getAPI().setExclusiveColorFor(entity, "random", player);
                    }else{
                        source.sendError(Text.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
                        return 1;
                    }
                }else{
                    if (ColoredGlowLibMod.getAPI() != null) {
                        ColoredGlowLibMod.getAPI().setExclusiveColorFor(entity, color, player);
                    }else{
                        source.sendError(Text.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
                        return 1;
                    }
                }
            }

            //source.sendFeedback(new TranslatableText("commands.setglowcolor.success1").append(color).append(new TranslatableText("commands.setglowcolor.success2")), true);
            source.sendFeedback(() -> Text.literal(ColoredGlowLibMod.PREFIX+"§7Setted color '§b" + color + "§7' to the selected entity/entities, visible only to §b" + player.getName().getString() + "!"), false);
            return targets.size();
        }else{
            //source.sendError(new TranslatableText("commands.setglowcolor.notcolor"));
            source.sendError((Text.literal(ColoredGlowLibMod.PREFIX+"Error! The value you have specified is not §cvalid! It should be RRGGBB (without '#') or 'rainbow' or 'random' or a custom animation name!")));
            return 0;
        }
    }

    private int setTypeGlowColor(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String color = "#"+StringArgumentType.getString(context, "color");
        ServerCommandSource source = context.getSource();

        if(isValidColorOrCustom(color)){
            EntityType<?> type = RegistryEntryReferenceArgumentType.getSummonableEntityType(context, "entity").value();
            if(color.equalsIgnoreCase("#rainbow")){
                if (ColoredGlowLibMod.getAPI() != null) {
                    ColoredGlowLibMod.getAPI().setRainbowColor(type);
                }else{
                    source.sendError(Text.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
                    return 1;
                }
            }else if(color.equalsIgnoreCase("#random")){

                if (ColoredGlowLibMod.getAPI() != null) {
                    ColoredGlowLibMod.getAPI().setRandomColor(type);
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
            source.sendFeedback(() -> Text.literal(ColoredGlowLibMod.PREFIX+"§7Setted color '" + color + "' to the selected entity/entities!"), false);
            return 1;
        }else{
            //source.sendError(new TranslatableText("commands.setglowcolor.notcolor"));
            source.sendError(Text.literal(ColoredGlowLibMod.PREFIX+"Error! The value you have specified is not §cvalid! It should be RRGGBB (without '#') or 'rainbow' or 'random' or a custom animation name!"));
            return 0;
        }
    }


    private int setDefaultGlowColor(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        try {
            String color = "#"+StringArgumentType.getString(context, "color");
            ServerCommandSource source = context.getSource();

            if(isValidColorOrCustom(color)){
                if(color.equalsIgnoreCase("#rainbow")){
                    if (ColoredGlowLibMod.getAPI() != null) {
                        ColoredGlowLibMod.getAPI().setGlobalRainbow();
                    }else{
                        source.sendError(Text.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
                        return 1;
                    }
                }else if(color.equalsIgnoreCase("#random")){
                    if (ColoredGlowLibMod.getAPI() != null) {
                        ColoredGlowLibMod.getAPI().setGlobalRandom();
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

                source.sendFeedback(() -> Text.literal(ColoredGlowLibMod.PREFIX+"§7Setted color '§b" + color + "§7' as the default color!"), false);
                return 1;
            }else{
                //source.sendError(new TranslatableText("commands.setglowcolor.notcolor"));
                source.sendError(Text.literal(ColoredGlowLibMod.PREFIX+"Error! The value you have specified is not §cvalid! It should be RRGGBB (without '#') or 'rainbow' or 'random' or a custom animation name!"));
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
        EntityType<?> type = RegistryEntryReferenceArgumentType.getSummonableEntityType(context, "entity").value();

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
                        CommandManager.argument("targets", EntityArgumentType.entities())
                                .then(
                                        CommandManager.argument("color", StringArgumentType.string())
                                                .then(
                                                        CommandManager.argument("visibleOnlyToPlayer", EntityArgumentType.players()
                                                        ).executes(this::setGlowColorFor)
                                                )

                                )
                )
                .then(
                        CommandManager.argument("entity", RegistryEntryReferenceArgumentType.registryEntry(registryAccess, RegistryKeys.ENTITY_TYPE)).suggests(SuggestionProviders.SUMMONABLE_ENTITIES)
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
