package me.emafire003.dev.coloredglowlib.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import me.emafire003.dev.coloredglowlib.compat.permissions.PermissionsChecker;
import me.emafire003.dev.coloredglowlib.component.GlobalColorComponent;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.RegistryEntryReferenceArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Collection;

public class ClearGlowColorCommand implements CGLCommand {

    private int resetDefaultColor(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        ServerCommandSource source = context.getSource();

        if (ColoredGlowLibMod.getAPI() != null) {
            ColoredGlowLibMod.getAPI().clearGlobalColor();
        }else{
            source.sendError(Text.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
            return 1;
        }
        source.sendFeedback(() -> Text.literal(ColoredGlowLibMod.PREFIX+"§7Resetted the default color to white!"), false);
        return 1;

    }

    private int clearEntityColor(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Collection<? extends Entity> targets = EntityArgumentType.getEntities(context, "targets");
        ServerCommandSource source = context.getSource();

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
                source.sendError(Text.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
                return 1;
            }
        }

        source.sendFeedback(() -> Text.literal(ColoredGlowLibMod.PREFIX+"§7Cleared the color from the selected entity/entities!"), true);
        return targets.size();
    }


    private int clearEntityColorFor(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Collection<? extends Entity> targets = EntityArgumentType.getEntities(context, "targets");
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "visibleToPlayer");

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
                source.sendError(Text.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
                return 1;
            }
        }

        source.sendFeedback(() -> Text.literal(ColoredGlowLibMod.PREFIX+"§7Cleared the color visible only to " + player.getName().getString() + "from the selected entity/entities!"), true);
        return targets.size();
    }

    //TODO actually implement
    //TODO maybe move to dedicated team things
    private int clearEntityColorForTeam(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Collection<? extends Entity> targets = EntityArgumentType.getEntities(context, "targets");
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "visibleToPlayer");

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
                source.sendError(Text.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
                return 1;
            }
        }

        source.sendFeedback(() -> Text.literal(ColoredGlowLibMod.PREFIX+"§7Cleared the color visible only to " + player.getName().getString() + "from the selected entity/entities!"), true);
        return targets.size();
    }


    private int clearEntityTypeColor(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        EntityType<?> type = RegistryEntryReferenceArgumentType.getSummonableEntityType(context, "entity").value();
        ServerCommandSource source = context.getSource();

        boolean useDefault;
        try{
            useDefault = BoolArgumentType.getBool(context, "useDefaultColor");
        }catch (Exception e){
            useDefault = false;
        }

        if (ColoredGlowLibMod.getAPI() != null) {
            ColoredGlowLibMod.getAPI().clearColor(type, useDefault);
        }else{
            source.sendError(Text.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
            return 1;
        }

        source.sendFeedback(() -> Text.literal(ColoredGlowLibMod.PREFIX+"§7Cleared color from the selected entity/entities!"), false);
        return 1;
    }

    private int resetSettings(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();

        if (ColoredGlowLibMod.getAPI() != null) {
            ColoredGlowLibMod.getAPI().setEntityTypeColorOverridesEntityColor(false);
            ColoredGlowLibMod.getAPI().setDefaultOverridesAll(false);
            ColoredGlowLibMod.getAPI().setOverrideTeamColors(false);
        }else{
            source.sendError(Text.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
            return 1;
        }

        source.sendFeedback(() -> Text.literal(ColoredGlowLibMod.PREFIX+"§7All settings have been reset to default values!"), false);
        return 1;
    }

    private int resetAll(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        GlobalColorComponent globalColorComponent = GlobalColorComponent.GLOBAL_COLOR_COMPONENT.get(source.getServer().getScoreboard());
        globalColorComponent.clear();

        source.sendFeedback(() -> Text.literal(ColoredGlowLibMod.PREFIX+"§7All settings and entitytype/default/global colors have been reset to default values!"), false);
        source.sendFeedback(() -> Text.literal(ColoredGlowLibMod.PREFIX+"§7If you want to clear entity-specifc colors as well use /cgl clear @e!"), false);

        return 1;
    }

    public LiteralCommandNode<ServerCommandSource> getNode(CommandRegistryAccess registryAccess) {
        return CommandManager
                .literal("clear")
                .requires(PermissionsChecker.hasPerms("coloredglowlib.commands.clearcolor", 2))
                .then(
                        CommandManager.argument("targets", EntityArgumentType.entities())
                                .executes(this::clearEntityColor)
                                .then(
                                    CommandManager.argument("useDefaultColor", BoolArgumentType.bool())
                                .executes(this::clearEntityColor)
                                )
                )
                //TODO i'm working on this
                .then(
                        CommandManager.argument("targets", EntityArgumentType.entities())
                                .executes(this::clearEntityColor).then(
                                       CommandManager.argument("visibleToPlayer", EntityArgumentType.players())
                                               .then(
                                                       CommandManager.argument("useDefaultColor", BoolArgumentType.bool())
                                                               .executes(this::clearEntityColorFor)
                                               )
                                )

                )

                .then(
                        CommandManager.argument("entity", RegistryEntryReferenceArgumentType.registryEntry(registryAccess, RegistryKeys.ENTITY_TYPE)).suggests(SuggestionProviders.SUMMONABLE_ENTITIES)
                                .executes(this::clearEntityTypeColor)
                                .then(
                                        CommandManager.argument("useDefaultColor", BoolArgumentType.bool())
                                                .executes(this::clearEntityTypeColor)
                                )
                )
                .then(
                        CommandManager.literal("default")
                                .executes(this::resetDefaultColor)
                ).then(
                        CommandManager.literal("settings")
                                .executes(this::resetSettings)
                )
                .then(
                        CommandManager.literal("all")
                                .requires(PermissionsChecker.hasPerms("coloredglowlib.commands.clearcolor.all", 2))
                                .then(
                                CommandManager.literal("confirm")
                                        .executes(this::resetAll)
                        )

                )
                .build();
    }

}
