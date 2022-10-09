package me.emafire003.dev.coloredglowlib.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import me.emafire003.dev.coloredglowlib.config.Config;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;


public class ConfigCommand implements CGLCommand {


    private int setGeneralizedRainbow(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        boolean b = BoolArgumentType.getBool(context, "value");
        CommandSource source = (CommandSource) context.getSource();
        ColoredGlowLibMod.getLib().setRainbowChangingColor(b);
        if(b){
            source.sendSystemMessage(Component.literal(ColoredGlowLibMod.PREFIX+"§aNow all entities will glow in rainbow! (Generalized Rainbow : Enabled) "));
        }else{
            source.sendSystemMessage(Component.literal(ColoredGlowLibMod.PREFIX+"§cEntities will no longer glow in rainbow unless specified! (Generalized Rainbow: Disabled)"));
        }
        return 1;
    }

    private int getGeneralizedRainbow(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSource source = (CommandSource) context.getSource();
        boolean b = ColoredGlowLibMod.getLib().getRainbowChangingColor();
        if(b){
            source.sendSystemMessage(Component.literal(ColoredGlowLibMod.PREFIX+"§aAll entities glow in rainbow! (Generalized Rainbow : Enabled) "));
        }else{
            source.sendSystemMessage(Component.literal(ColoredGlowLibMod.PREFIX+"§cEntities will no longer glow in rainbow unless specified! (Generalized Rainbow: Disabled)"));
        }
        return 1;
    }

    private int setOverrideTeamColors(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        boolean b = BoolArgumentType.getBool(context, "value");
        CommandSource source = (CommandSource) context.getSource();
        ColoredGlowLibMod.getLib().setOverrideTeamColors(b);
        if(b){
            source.sendSystemMessage(Component.literal(ColoredGlowLibMod.PREFIX+"§aThe color set by the mod will a have priority over the vanilla team color! (Override Team Colors : Enabled) "));
        }else{
            source.sendSystemMessage(Component.literal(ColoredGlowLibMod.PREFIX+"§cThe color of the vanilla team will have priority over the one set by the mod! (Override Team Colors : Disabled)"));
        }
        return 1;
    }

    private int getOverrideTeamColors(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSource source = (CommandSource) context.getSource();
        boolean b = ColoredGlowLibMod.getLib().getOverrideTeamColors();
        if(b){
            source.sendSystemMessage(Component.literal(ColoredGlowLibMod.PREFIX+"§aThe color set by the mod has priority over the vanilla team color! (Override Team Colors : Enabled) "));
        }else{
            source.sendSystemMessage(Component.literal(ColoredGlowLibMod.PREFIX+"§cThe color of the vanilla team has priority over the one set by the mod! (Override Team Colors : Disabled)"));
        }
        return 1;
    }

    private int setPerEntityColor(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        boolean b = BoolArgumentType.getBool(context, "value");
        CommandSource source = (CommandSource) context.getSource();
        ColoredGlowLibMod.getLib().setPerEntityColor(b);
        if(b){
            source.sendSystemMessage(Component.literal(ColoredGlowLibMod.PREFIX+"§aThe glow color is now specific for each entity! (Per Entity Color : Enabled) "));
        }else{
            source.sendSystemMessage(Component.literal(ColoredGlowLibMod.PREFIX+"§cThe glow color is no longer specific for each entity! (Per Entity Color : Disabled)"));
        }
        return 1;
    }

    private int getPerEntityColor(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSource source = (CommandSource) context.getSource();
        boolean b = ColoredGlowLibMod.getLib().getPerEntityColor();
        if(b){
            source.sendSystemMessage(Component.literal(ColoredGlowLibMod.PREFIX+"§aThe glow color is specific for each entity! (Per Entity Color : Enabled) "));
        }else{
            source.sendSystemMessage(Component.literal(ColoredGlowLibMod.PREFIX+"§cThe glow color is NOT specific for each entity! (Per Entity Color : Disabled)"));
        }
        return 1;
    }

    private int setPerEntityTypeColor(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        boolean b = BoolArgumentType.getBool(context, "value");
        CommandSource source = (CommandSource) context.getSource();
        ColoredGlowLibMod.getLib().setPerEntityTypeColor(b);
        if(b){
            source.sendSystemMessage(Component.literal(ColoredGlowLibMod.PREFIX+"§aThe glow color is now specific for each entity type! (Per EntityType Color : Enabled) "));
        }else{
            source.sendSystemMessage(Component.literal(ColoredGlowLibMod.PREFIX+"§cThe glow color is no longer specific for each entity type! (Per EntityType Color : Disabled)"));
        }
        return 1;
    }

    private int getPerEntityTypeColor(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSource source = (CommandSource) context.getSource();
        boolean b = ColoredGlowLibMod.getLib().getPerEntityTypeColor();
        if(b){
            source.sendSystemMessage(Component.literal(ColoredGlowLibMod.PREFIX+"§aThe glow color is specific for each entity type! (Per EntityType Color : Enabled) "));
        }else{
            source.sendSystemMessage(Component.literal(ColoredGlowLibMod.PREFIX+"§cThe glow color is NOT specific for each entity type! (Per EntityType Color : Disabled)"));
        }
        return 1;
    }

    private int reloadConfig(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSource source = (CommandSource) context.getSource();
        Config.reloadConfig();
        source.sendSystemMessage(Component.literal(ColoredGlowLibMod.PREFIX+"The config has been reloaded!"));
        return 1;
    }

//Commands.argument("entity", EntitySummonArgumentType.entitySummon()).suggests(SuggestionProviders.SUMMONABLE_ENTITIES).then(((RequiredArgumentBuilder)Commands.argument("color", StringArgumentType.string())
     public LiteralCommandNode<CommandSourceStack> getNode() {
        return Commands
                .literal("config")
                .then(
                        Commands.literal("set")
                                .then(
                                        Commands.literal("generalizedRainbow")
                                                .then(
                                                        Commands.argument("value", BoolArgumentType.bool())
                                                                .executes(this::setGeneralizedRainbow)
                                                )
                                )
                )
                .then(
                        Commands.literal("set")
                                .then(
                                        Commands.literal("overrideTeamColors")
                                                .then(
                                                        Commands.argument("value", BoolArgumentType.bool())
                                                                .executes(this::setOverrideTeamColors)
                                                )
                                )
                )
                .then(
                        Commands.literal("set")
                                .then(
                                        Commands.literal("perEntityColor")
                                                .then(
                                                        Commands.argument("value", BoolArgumentType.bool())
                                                                .executes(this::setPerEntityColor)
                                                )
                                )
                )
                .then(
                        Commands.literal("set")
                                .then(
                                        Commands.literal("perEntityTypeColor")
                                                .then(
                                                        Commands.argument("value", BoolArgumentType.bool())
                                                                .executes(this::setPerEntityTypeColor)
                                                )
                                )
                )
                .then(
                        Commands.literal("get")
                                .then(
                                        Commands.literal("generalizedRainbow").executes(this::getGeneralizedRainbow)

                                )
                )
                .then(
                        Commands.literal("get")
                                .then(
                                        Commands.literal("overrideTeamColors").executes(this::getOverrideTeamColors)

                                )
                )
                .then(
                        Commands.literal("get")
                                .then(
                                        Commands.literal("perEntityColor").executes(this::getPerEntityColor)

                                )
                )
                .then(
                        Commands.literal("get")
                                .then(
                                        Commands.literal("perEntityTypeColor").executes(this::getPerEntityTypeColor)

                                )
                )
                .then(
                        Commands.literal("reload")
                                .executes(this::reloadConfig)
                )
                .build();
    }


}
