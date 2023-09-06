package me.emafire003.dev.coloredglowlib.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import me.emafire003.dev.coloredglowlib.config.ConfigDataSaver;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;


public class ConfigCommand implements CGLCommand {


    private int setGeneralizedRainbow(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        boolean b = BoolArgumentType.getBool(context, "value");
        ServerCommandSource source = context.getSource();
        ColoredGlowLibMod.getLib().setGeneralizedRainbow(b);
        if(b){
            source.sendFeedback(() ->Text.literal(ColoredGlowLibMod.PREFIX+"§aNow all entities will glow in rainbow! (Generalized Rainbow : Enabled) "), true);
        }else{
            source.sendFeedback(() ->Text.literal(ColoredGlowLibMod.PREFIX+"§cEntities will no longer glow in rainbow unless specified! (Generalized Rainbow: Disabled)"), true);
        }
        return 1;
    }

    private int getGeneralizedRainbow(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        boolean b = ColoredGlowLibMod.getLib().getGeneralizedRainbow();
        if(b){
            source.sendFeedback(() ->Text.literal(ColoredGlowLibMod.PREFIX+"§aAll entities glow in rainbow! (Generalized Rainbow : Enabled) "), false);
        }else{
            source.sendFeedback(() ->Text.literal(ColoredGlowLibMod.PREFIX+"§cEntities will no longer glow in rainbow unless specified! (Generalized Rainbow: Disabled)"), false);
        }
        return 1;
    }

    private int setOverrideTeamColors(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        boolean b = BoolArgumentType.getBool(context, "value");
        ServerCommandSource source = context.getSource();
        ColoredGlowLibMod.getLib().setOverrideTeamColors(b);
        if(b){
            source.sendFeedback(() ->Text.literal(ColoredGlowLibMod.PREFIX+"§aThe color set by the mod will a have priority over the vanilla team color! (Override Team Colors : Enabled) "), true);
        }else{
            source.sendFeedback(() ->Text.literal(ColoredGlowLibMod.PREFIX+"§cThe color of the vanilla team will have priority over the one set by the mod! (Override Team Colors : Disabled)"), true);
        }
        return 1;
    }

    private int getOverrideTeamColors(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        boolean b = ColoredGlowLibMod.getLib().getOverrideTeamColors();
        if(b){
            source.sendFeedback(() ->Text.literal(ColoredGlowLibMod.PREFIX+"§aThe color set by the mod has priority over the vanilla team color! (Override Team Colors : Enabled) "), false);
        }else{
            source.sendFeedback(() ->Text.literal(ColoredGlowLibMod.PREFIX+"§cThe color of the vanilla team has priority over the one set by the mod! (Override Team Colors : Disabled)"), false);
        }
        return 1;
    }

    private int setPerEntityColor(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        boolean b = BoolArgumentType.getBool(context, "value");
        ServerCommandSource source = context.getSource();
        ColoredGlowLibMod.getLib().setPerEntityColor(b);
        if(b){
            source.sendFeedback(() ->Text.literal(ColoredGlowLibMod.PREFIX+"§aThe glow color is now specific for each entity! (Per Entity Color : Enabled) "), false);
        }else{
            source.sendFeedback(() ->Text.literal(ColoredGlowLibMod.PREFIX+"§cThe glow color is no longer specific for each entity! (Per Entity Color : Disabled)"), false);
        }
        return 1;
    }

    private int getPerEntityColor(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        boolean b = ColoredGlowLibMod.getLib().getPerEntityColor();
        if(b){
            source.sendFeedback(() ->Text.literal(ColoredGlowLibMod.PREFIX+"§aThe glow color is specific for each entity! (Per Entity Color : Enabled) "), false);
        }else{
            source.sendFeedback(() ->Text.literal(ColoredGlowLibMod.PREFIX+"§cThe glow color is NOT specific for each entity! (Per Entity Color : Disabled)"), false);
        }
        return 1;
    }

    private int setPerEntityTypeColor(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        boolean b = BoolArgumentType.getBool(context, "value");
        ServerCommandSource source = context.getSource();
        ColoredGlowLibMod.getLib().setPerEntityTypeColor(b);
        if(b){
            source.sendFeedback(() ->Text.literal(ColoredGlowLibMod.PREFIX+"§aThe glow color is now specific for each entity type! (Per EntityType Color : Enabled) "), false);
        }else{
            source.sendFeedback(() ->Text.literal(ColoredGlowLibMod.PREFIX+"§cThe glow color is no longer specific for each entity type! (Per EntityType Color : Disabled)"), false);
        }
        return 1;
    }

    private int getPerEntityTypeColor(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        boolean b = ColoredGlowLibMod.getLib().getPerEntityTypeColor();
        if(b){
            source.sendFeedback(() ->Text.literal(ColoredGlowLibMod.PREFIX+"§aThe glow color is specific for each entity type! (Per EntityType Color : Enabled) "), false);
        }else{
            source.sendFeedback(() ->Text.literal(ColoredGlowLibMod.PREFIX+"§cThe glow color is NOT specific for each entity type! (Per EntityType Color : Disabled)"), false);
        }
        return 1;
    }

    private int reloadConfig(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ColoredGlowLibMod.getLib().getValuesFromFile();

        source.sendFeedback(() -> Text.literal(ColoredGlowLibMod.PREFIX+"The config has been reloaded!"), true);
        return 1;
    }

//CommandManager.argument("entity", EntitySummonArgumentType.entitySummon()).suggests(SuggestionProviders.SUMMONABLE_ENTITIES).then(((RequiredArgumentBuilder)CommandManager.argument("color", StringArgumentType.string())
     public LiteralCommandNode<ServerCommandSource> getNode(CommandRegistryAccess registryAccess) {
        return CommandManager
                .literal("config")
                .then(
                        CommandManager.literal("set")
                                .then(
                                        CommandManager.literal("generalizedRainbow")
                                                .then(
                                                        CommandManager.argument("value", BoolArgumentType.bool())
                                                                .executes(this::setGeneralizedRainbow)
                                                )
                                )
                )
                .then(
                        CommandManager.literal("set")
                                .then(
                                        CommandManager.literal("overrideTeamColors")
                                                .then(
                                                        CommandManager.argument("value", BoolArgumentType.bool())
                                                                .executes(this::setOverrideTeamColors)
                                                )
                                )
                )
                .then(
                        CommandManager.literal("set")
                                .then(
                                        CommandManager.literal("perEntityColor")
                                                .then(
                                                        CommandManager.argument("value", BoolArgumentType.bool())
                                                                .executes(this::setPerEntityColor)
                                                )
                                )
                )
                .then(
                        CommandManager.literal("set")
                                .then(
                                        CommandManager.literal("perEntityTypeColor")
                                                .then(
                                                        CommandManager.argument("value", BoolArgumentType.bool())
                                                                .executes(this::setPerEntityTypeColor)
                                                )
                                )
                )
                .then(
                        CommandManager.literal("get")
                                .then(
                                        CommandManager.literal("generalizedRainbow").executes(this::getGeneralizedRainbow)

                                )
                )
                .then(
                        CommandManager.literal("get")
                                .then(
                                        CommandManager.literal("overrideTeamColors").executes(this::getOverrideTeamColors)

                                )
                )
                .then(
                        CommandManager.literal("get")
                                .then(
                                        CommandManager.literal("perEntityColor").executes(this::getPerEntityColor)

                                )
                )
                .then(
                        CommandManager.literal("get")
                                .then(
                                        CommandManager.literal("perEntityTypeColor").executes(this::getPerEntityTypeColor)

                                )
                )
                .then(
                        CommandManager.literal("reload")
                                .executes(this::reloadConfig)
                )
                .build();
    }

}
