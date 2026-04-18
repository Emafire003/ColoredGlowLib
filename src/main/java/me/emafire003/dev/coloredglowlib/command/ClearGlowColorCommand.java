package me.emafire003.dev.coloredglowlib.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import me.emafire003.dev.coloredglowlib.compat.permissions.PermissionsChecker;
import me.emafire003.dev.coloredglowlib.component.GlobalColorComponent;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.registries.Registries;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;

import java.util.Collection;

public class ClearGlowColorCommand implements CGLCommand {

    private int resetDefaultColor(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

        CommandSourceStack source = context.getSource();

        if (ColoredGlowLibMod.getAPI() != null) {
            ColoredGlowLibMod.getAPI().clearGlobalColor();
        }else{
            source.sendFailure(Component.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
            return 0;
        }
        source.sendSuccess(() -> Component.literal(ColoredGlowLibMod.PREFIX+"§7Resetted the default color to white!"), false);
        return 1;

    }

    private int clearEntityColor(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<? extends Entity> targets = EntityArgument.getEntities(context, "targets");
        CommandSourceStack source = context.getSource();

        boolean useDefault;
        try{
            useDefault = BoolArgumentType.getBool(context, "useDefaultColor");
        }catch (Exception e){
            useDefault = false;
        }

        for (Entity entity : targets) {
            if (ColoredGlowLibMod.getAPI() != null) {
                ColoredGlowLibMod.getAPI().clearColor(entity, useDefault);
            }else{
                source.sendFailure(Component.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
                return 1;
            }
        }

        source.sendSuccess(() -> Component.literal(ColoredGlowLibMod.PREFIX+"§7Cleared the color from the selected entity/entities!"), true);
        return targets.size();
    }


    private int clearEntityColorFor(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<? extends Entity> targets = EntityArgument.getEntities(context, "targets");
        CommandSourceStack source = context.getSource();
        ServerPlayer player = EntityArgument.getPlayer(context, "visibleToPlayer");

        boolean useDefault;
        try{
            useDefault = BoolArgumentType.getBool(context, "useDefaultColor");
        }catch (Exception e){
            useDefault = false;
        }

        for (Entity entity : targets) {
            if (ColoredGlowLibMod.getAPI() != null) {
                ColoredGlowLibMod.getAPI().clearExclusiveColorFor(entity, player, useDefault);
            }else{
                source.sendFailure(Component.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
                return 1;
            }
        }

        source.sendSuccess(() -> Component.literal(ColoredGlowLibMod.PREFIX+"§7Cleared the color visible only to " + player.getName().getString() + "from the selected entity/entities!"), true);
        return targets.size();
    }

    private int clearEntityTypeColor(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        EntityType<?> type = ResourceArgument.getSummonableEntityType(context, "entity").value();
        CommandSourceStack source = context.getSource();

        boolean useDefault;
        try{
            useDefault = BoolArgumentType.getBool(context, "useDefaultColor");
        }catch (Exception e){
            useDefault = false;
        }

        if (ColoredGlowLibMod.getAPI() != null) {
            ColoredGlowLibMod.getAPI().clearColor(type, useDefault);
        }else{
            source.sendFailure(Component.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
            return 0;
        }

        source.sendSuccess(() -> Component.literal(ColoredGlowLibMod.PREFIX+"§7Cleared color from the selected entity/entities!"), false);
        return 1;
    }

    private int resetSettings(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();

        if (ColoredGlowLibMod.getAPI() != null) {
            ColoredGlowLibMod.getAPI().setEntityTypeColorOverridesEntityColor(false);
            ColoredGlowLibMod.getAPI().setDefaultOverridesAll(false);
            ColoredGlowLibMod.getAPI().setOverrideTeamColors(false);
        }else{
            source.sendFailure(Component.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
            return 0;
        }

        source.sendSuccess(() -> Component.literal(ColoredGlowLibMod.PREFIX+"§7All settings have been reset to default values!"), false);
        return 1;
    }

    private int resetAll(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        GlobalColorComponent globalColorComponent = GlobalColorComponent.GLOBAL_COLOR_COMPONENT.get(source.getServer().getScoreboard());
        globalColorComponent.clear();

        source.sendSuccess(() -> Component.literal(ColoredGlowLibMod.PREFIX+"§7All settings and entitytype/default/global colors have been reset to default values!"), false);
        source.sendSuccess(() -> Component.literal(ColoredGlowLibMod.PREFIX+"§7If you want to clear entity-specifc colors as well use /cgl clear @e!"), false);

        return 1;
    }

    public LiteralCommandNode<CommandSourceStack> getNode(CommandBuildContext registryAccess) {
        return Commands
                .literal("clear")
                .requires(PermissionsChecker.hasPerms("coloredglowlib.commands.clearcolor", 2))
                .then(
                        Commands.argument("targets", EntityArgument.entities())
                                .executes(this::clearEntityColor)
                                .then(
                                    Commands.argument("useDefaultColor", BoolArgumentType.bool())
                                .executes(this::clearEntityColor)
                                )
                )
                .then(
                        Commands.argument("targets", EntityArgument.entities())
                                .executes(this::clearEntityColor).then(
                                       Commands.argument("visibleToPlayer", EntityArgument.players())
                                               .then(
                                                       Commands.argument("useDefaultColor", BoolArgumentType.bool())
                                                               .executes(this::clearEntityColorFor)
                                               )
                                )

                )

                .then(
                        Commands.argument("entity", ResourceArgument.resource(registryAccess, Registries.ENTITY_TYPE)).suggests(SuggestionProviders.cast(SuggestionProviders.SUMMONABLE_ENTITIES))
                                .executes(this::clearEntityTypeColor)
                                .then(
                                        Commands.argument("useDefaultColor", BoolArgumentType.bool())
                                                .executes(this::clearEntityTypeColor)
                                )
                )
                .then(
                        Commands.literal("default")
                                .executes(this::resetDefaultColor)
                ).then(
                        Commands.literal("settings")
                                .executes(this::resetSettings)
                )
                .then(
                        Commands.literal("all")
                                .requires(PermissionsChecker.hasPerms("coloredglowlib.commands.clearcolor.all", 2))
                                .then(
                                Commands.literal("confirm")
                                        .executes(this::resetAll)
                        )

                )
                .build();
    }

}
