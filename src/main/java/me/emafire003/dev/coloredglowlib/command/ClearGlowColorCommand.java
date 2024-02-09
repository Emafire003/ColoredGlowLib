package me.emafire003.dev.coloredglowlib.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import me.emafire003.dev.coloredglowlib.compat.permissions.PermissionsChecker;
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

public class ClearGlowColorCommand implements CGLCommand {

    private int resetDefaultColor(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        ServerCommandSource source = context.getSource();

        if (ColoredGlowLibMod.getAPI() != null) {
            ColoredGlowLibMod.getAPI().clearGlobalColor();
        }else{
            source.sendError(Text.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
            return 1;
        }
        source.sendFeedback(() -> Text.literal(ColoredGlowLibMod.PREFIX+"Resetted the default color to white!"), false);
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

        source.sendFeedback(() -> Text.literal(ColoredGlowLibMod.PREFIX+"Cleared the color from the selected entity/entities!"), true);
        return targets.size();
    }

    private int clearEntityTypeColor(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        EntityType<?> type = RegistryEntryArgumentType.getSummonableEntityType(context, "entity").value();
        ServerCommandSource source = context.getSource();

        boolean useDefault;
        try{
            useDefault = BoolArgumentType.getBool(context, "useDefaultColor");
        }catch (Exception e){
            context.getSource().sendError(Text.literal("Ok we are in the catch"));
            useDefault = false;
        }

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
                .then(
                        CommandManager.argument("entity", RegistryEntryArgumentType.registryEntry(registryAccess, RegistryKeys.ENTITY_TYPE)).suggests(SuggestionProviders.SUMMONABLE_ENTITIES)
                                .executes(this::clearEntityTypeColor)
                                .then(
                                        CommandManager.argument("useDefaultColor", BoolArgumentType.bool())
                                                .executes(this::clearEntityTypeColor)
                                )
                )
                .then(
                        CommandManager.literal("default")
                                .executes(this::resetDefaultColor)
                )
                .build();
    }

}
