package me.emafire003.dev.coloredglowlib.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import me.emafire003.dev.coloredglowlib.compat.permissions.PermissionsChecker;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.registries.Registries;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import java.util.Collection;

import static me.emafire003.dev.coloredglowlib.util.ColorUtils.isValidColorOrCustom;

public class SetGlowColorCommand implements CGLCommand {
    

    private int setGlowColor(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<? extends Entity> targets = EntityArgument.getEntities(context, "targets");
        String color = "#"+StringArgumentType.getString(context, "color");
        CommandSourceStack source = context.getSource();

        if(isValidColorOrCustom(color)){
            for (Entity entity : targets) {
                if(color.equalsIgnoreCase("#rainbow")){
                    if (ColoredGlowLibMod.getAPI() != null) {
                        ColoredGlowLibMod.getAPI().setRainbowColor(entity);
                    }else{
                        source.sendFailure(Component.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
                        return 1;
                    }
                }else if(color.equalsIgnoreCase("#random")){

                    if (ColoredGlowLibMod.getAPI() != null) {
                        ColoredGlowLibMod.getAPI().setRandomColor(entity);
                    }else{
                        source.sendFailure(Component.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
                        return 1;
                    }
                }else{
                    if (ColoredGlowLibMod.getAPI() != null) {
                        ColoredGlowLibMod.getAPI().setColor(entity, color);
                    }else{
                        source.sendFailure(Component.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
                        return 1;
                    }
                }
            }

            //source.sendFeedback(new TranslatableText("commands.setglowcolor.success1").append(color).append(new TranslatableText("commands.setglowcolor.success2")), true);
            source.sendSuccess(() -> Component.literal(ColoredGlowLibMod.PREFIX+"§7Setted color '§b" + color + "§7' to the selected entity/entities!"), false);
            return targets.size();
        }else{
            //source.sendError(new TranslatableText("commands.setglowcolor.notcolor"));
            source.sendFailure((Component.literal(ColoredGlowLibMod.PREFIX+"Error! The value you have specified is not §cvalid! It should be RRGGBB (without '#') or 'rainbow' or 'random' or a custom animation name!")));
            return 0;
        }
    }

    private int setGlowColorFor(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        Collection<? extends Entity> targets = EntityArgument.getEntities(context, "targets");
        String color = "#"+StringArgumentType.getString(context, "color");
        Player player = EntityArgument.getPlayer(context, "visibleOnlyToPlayer");


        if(isValidColorOrCustom(color)){
            for (Entity entity : targets) {
                if(color.equalsIgnoreCase("#rainbow")){
                    if (ColoredGlowLibMod.getAPI() != null) {
                        ColoredGlowLibMod.getAPI().setExclusiveColorFor(entity, "rainbow", player);
                    }else{
                        source.sendFailure(Component.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
                        return 1;
                    }
                }else if(color.equalsIgnoreCase("#random")){

                    if (ColoredGlowLibMod.getAPI() != null) {
                        ColoredGlowLibMod.getAPI().setExclusiveColorFor(entity, "random", player);
                    }else{
                        source.sendFailure(Component.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
                        return 1;
                    }
                }else{
                    if (ColoredGlowLibMod.getAPI() != null) {
                        ColoredGlowLibMod.getAPI().setExclusiveColorFor(entity, color, player);
                    }else{
                        source.sendFailure(Component.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
                        return 1;
                    }
                }
            }

            //source.sendFeedback(new TranslatableText("commands.setglowcolor.success1").append(color).append(new TranslatableText("commands.setglowcolor.success2")), true);
            source.sendSuccess(() -> Component.literal(ColoredGlowLibMod.PREFIX+"§7Setted color '§b" + color + "§7' to the selected entity/entities, visible only to §b" + player.getName().getString() + "!"), false);
            return targets.size();
        }else{
            //source.sendError(new TranslatableText("commands.setglowcolor.notcolor"));
            source.sendFailure((Component.literal(ColoredGlowLibMod.PREFIX+"Error! The value you have specified is not §cvalid! It should be RRGGBB (without '#') or 'rainbow' or 'random' or a custom animation name!")));
            return 0;
        }
    }

    private int setTypeGlowColor(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String color = "#"+StringArgumentType.getString(context, "color");
        CommandSourceStack source = context.getSource();

        if(isValidColorOrCustom(color)){
            EntityType<?> type = ResourceArgument.getSummonableEntityType(context, "entity").value();
            if(color.equalsIgnoreCase("#rainbow")){
                if (ColoredGlowLibMod.getAPI() != null) {
                    ColoredGlowLibMod.getAPI().setRainbowColor(type);
                }else{
                    source.sendFailure(Component.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
                    return 1;
                }
            }else if(color.equalsIgnoreCase("#random")){

                if (ColoredGlowLibMod.getAPI() != null) {
                    ColoredGlowLibMod.getAPI().setRandomColor(type);
                }else{
                    source.sendFailure(Component.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
                    return 1;
                }
            }else{
                if (ColoredGlowLibMod.getAPI() != null) {
                    ColoredGlowLibMod.getAPI().setColor(type, color);
                }else{
                    source.sendFailure(Component.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
                    return 1;
                }
            }

            //source.sendFeedback(new TranslatableText("commands.setglowcolor.success1").append(color).append(new TranslatableText("commands.setglowcolor.success2")), true);
            source.sendSuccess(() -> Component.literal(ColoredGlowLibMod.PREFIX+"§7Setted color '" + color + "' to the selected entity/entities!"), false);
            return 1;
        }else{
            //source.sendError(new TranslatableText("commands.setglowcolor.notcolor"));
            source.sendFailure(Component.literal(ColoredGlowLibMod.PREFIX+"Error! The value you have specified is not §cvalid! It should be RRGGBB (without '#') or 'rainbow' or 'random' or a custom animation name!"));
            return 0;
        }
    }


    private int setDefaultGlowColor(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        try {
            String color = "#"+StringArgumentType.getString(context, "color");
            CommandSourceStack source = context.getSource();

            if(isValidColorOrCustom(color)){
                if(color.equalsIgnoreCase("#rainbow")){
                    if (ColoredGlowLibMod.getAPI() != null) {
                        ColoredGlowLibMod.getAPI().setGlobalRainbow();
                    }else{
                        source.sendFailure(Component.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
                        return 1;
                    }
                }else if(color.equalsIgnoreCase("#random")){
                    if (ColoredGlowLibMod.getAPI() != null) {
                        ColoredGlowLibMod.getAPI().setGlobalRandom();
                    }else{
                        source.sendFailure(Component.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
                        return 1;
                    }
                }else{
                    if (ColoredGlowLibMod.getAPI() != null) {
                        ColoredGlowLibMod.getAPI().setGlobalColor(color);
                    }else{
                        source.sendFailure(Component.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
                        return 1;
                    }
                }

                source.sendSuccess(() -> Component.literal(ColoredGlowLibMod.PREFIX+"§7Setted color '§b" + color + "§7' as the default color!"), false);
                return 1;
            }else{
                //source.sendError(new TranslatableText("commands.setglowcolor.notcolor"));
                source.sendFailure(Component.literal(ColoredGlowLibMod.PREFIX+"Error! The value you have specified is not §cvalid! It should be RRGGBB (without '#') or 'rainbow' or 'random' or a custom animation name!"));
                return 0;
            }
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }

    }

    private int clearEntityColor(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<? extends Entity> targets = EntityArgument.getEntities(context, "targets");
        boolean useDefault = BoolArgumentType.getBool(context, "useDefaultColor");
        CommandSourceStack source = context.getSource();

        for (Entity entity : targets) {
            if (ColoredGlowLibMod.getAPI() != null) {
                ColoredGlowLibMod.getAPI().clearColor(entity, useDefault);
            }else{
                source.sendFailure(Component.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
                return 1;
            }
        }

        source.sendSuccess(() -> Component.literal(ColoredGlowLibMod.PREFIX+"Cleared the color from the selected entity/entities!"), true);
        return targets.size();
    }

    private int clearEntityTypeColor(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        boolean useDefault = BoolArgumentType.getBool(context, "useDefaultColor");
        EntityType<?> type = ResourceArgument.getSummonableEntityType(context, "entity").value();

        CommandSourceStack source = context.getSource();

        if (ColoredGlowLibMod.getAPI() != null) {
            ColoredGlowLibMod.getAPI().clearColor(type, useDefault);
        }else{
            source.sendFailure(Component.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
            return 1;
        }

        source.sendSuccess(() -> Component.literal(ColoredGlowLibMod.PREFIX+"Cleared color from the selected entity/entities!"), false);
        return 1;
    }

    public LiteralCommandNode<CommandSourceStack> getNode(CommandBuildContext registryAccess) {
        return Commands
                .literal("setglowcolor")
                .requires(PermissionsChecker.hasPerms("coloredglowlib.commands.setglowcolor", 2))
                .then(
                        Commands.argument("targets", EntityArgument.entities())
                                .then(
                                    Commands.argument("color", StringArgumentType.string())
                                .executes(this::setGlowColor)
                                )
                )
                .then(
                        Commands.argument("targets", EntityArgument.entities())
                                .then(
                                        Commands.argument("color", StringArgumentType.string())
                                                .then(
                                                        Commands.argument("visibleOnlyToPlayer", EntityArgument.players()
                                                        ).executes(this::setGlowColorFor)
                                                )

                                )
                )
                .then(
                        Commands.argument("entity", ResourceArgument.resource(registryAccess, Registries.ENTITY_TYPE)).suggests(SuggestionProviders.cast(SuggestionProviders.SUMMONABLE_ENTITIES))
                                .then(
                                        Commands.argument("color", StringArgumentType.string())
                                                .executes(this::setTypeGlowColor)
                                )
                )
                .then(
                        Commands.literal("default")
                                .then(
                                        Commands.argument("color", StringArgumentType.string())
                                                .executes(this::setDefaultGlowColor)
                                )
                )
                .build();
    }

}
