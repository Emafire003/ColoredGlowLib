package me.emafire003.dev.coloredglowlib.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import me.emafire003.dev.coloredglowlib.compat.permissions.PermissionsChecker;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;


public class SettingsCommand implements CGLCommand {


    // I can't use something like that because the server hasn't started yet when the command is registered, so i need to get the api inside the method call
    // private final ColoredGlowLibAPI cgl = ColoredGlowLibMod.getColoredGlowLib();

    private int setOverrideTeamColors(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        boolean b = BoolArgumentType.getBool(context, "value");
        CommandSourceStack source = context.getSource();

        if (ColoredGlowLibMod.getAPI() != null) {
            ColoredGlowLibMod.getAPI().setOverrideTeamColors(b);
        }else{
            source.sendFailure(Component.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
            return 0;
        }

        if(b){
            source.sendSuccess(() -> Component.literal(ColoredGlowLibMod.PREFIX+"§aThe color set by the mod will a have priority over the vanilla team color! (Override Team Colors : Enabled) "), true);
        }else{
            source.sendSuccess(() -> Component.literal(ColoredGlowLibMod.PREFIX+"§cThe color of the vanilla team will have priority over the one set by the mod! (Override Team Colors : Disabled)"), true);
        }
        return 1;
    }

    private int getOverrideTeamColors(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        boolean b = false;
        if (ColoredGlowLibMod.getAPI() != null) {
            b = ColoredGlowLibMod.getAPI().getOverrideTeamColors();
        }else{
            source.sendFailure(Component.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
            return 0;
        }

        if(b){
            source.sendSuccess(() -> Component.literal(ColoredGlowLibMod.PREFIX+"§aThe color set by the mod has priority over the vanilla team color! (Override Team Colors : Enabled) "), false);
        }else{
            source.sendSuccess(() -> Component.literal(ColoredGlowLibMod.PREFIX+"§cThe color of the vanilla team has priority over the one set by the mod! (Override Team Colors : Disabled)"), false);
        }
        return 1;
    }

    private int setEntityTypeColorOverridesEntityColor(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        boolean b = BoolArgumentType.getBool(context, "value");
        CommandSourceStack source = context.getSource();

        if (ColoredGlowLibMod.getAPI() != null) {
           ColoredGlowLibMod.getAPI().setEntityTypeColorOverridesEntityColor(b);
        }else{
            source.sendFailure(Component.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
            return 0;
        }

        if(b){
            source.sendSuccess(() -> Component.literal(ColoredGlowLibMod.PREFIX+"§aEntityType glow color is now predominant over Entity-specific color! (EntityType >> Entity) "), true);
        }else{
            source.sendSuccess(() -> Component.literal(ColoredGlowLibMod.PREFIX+"§cEntityType glow color will now be overridden by Entity-specific color! (Entity >> EntityType) "), true);
        }
        return 1;
    }

    private int getEntityTypeColorOverridesEntityColor(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        boolean b = false;

        if (ColoredGlowLibMod.getAPI() != null) {
            b = ColoredGlowLibMod.getAPI().getEntityTypeColorOverridesEntityColor();
        }else{
            source.sendFailure(Component.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
            return 0;
        }

        if(b){
            source.sendSuccess(() -> Component.literal(ColoredGlowLibMod.PREFIX+"§aEntityTypes glow color is currently predominant over Entity-specific color. (EntityType >> Entity) "), false);
        }else{
            source.sendSuccess(() -> Component.literal(ColoredGlowLibMod.PREFIX+"§cEntity glow color is currently predominant over EntityType color. (Entity >> EntityType)"), false);
        }
        return 1;
    }

    private int setGlobalOverridesAll(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        boolean b = BoolArgumentType.getBool(context, "value");
        CommandSourceStack source = context.getSource();

        if (ColoredGlowLibMod.getAPI() != null) {
            ColoredGlowLibMod.getAPI().setDefaultOverridesAll(b);
        }else{
            source.sendFailure(Component.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
            return 0;
        }

        if(b){
            source.sendSuccess(() -> Component.literal(ColoredGlowLibMod.PREFIX+"§aGlobal/Default glow color is now predominant over Entity or EntityType colors! (Global >> everything else) "), true);
        }else{
            source.sendSuccess(() -> Component.literal(ColoredGlowLibMod.PREFIX+"§cEntityType or Entity color is now predominant over Global/Default color! (everything else >> Global) "), true);
        }
        return 1;
    }

    private int getGlobalOverridesAll(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        boolean b = false;

        if (ColoredGlowLibMod.getAPI() != null) {
            b = ColoredGlowLibMod.getAPI().getDefaultOverridesAll();
        }else{
            source.sendFailure(Component.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
            return 0;
        }

        if(b){
            source.sendSuccess(() -> Component.literal(ColoredGlowLibMod.PREFIX+"§aGlobal/Default glow color is currently predominant over Entity or EntityType colors! (Global >> everything else) "), false);
        }else{
            source.sendSuccess(() -> Component.literal(ColoredGlowLibMod.PREFIX+"§cEntityType or Entity color is currently predominant over Global/Default color! (everything else >> Global)"), false);
        }
        return 1;
    }


//CommandManager.argument("entity", EntitySummonArgumentType.entitySummon()).suggests(SuggestionProviders.SUMMONABLE_ENTITIES).then(((RequiredArgumentBuilder)CommandManager.argument("color", StringArgumentType.string())
     public LiteralCommandNode<CommandSourceStack> getNode(CommandBuildContext registryAccess) {
        return Commands
                .literal("settings")
                .requires(PermissionsChecker.hasPerms("coloredglowlib.commands.settings", 2))
                .then(
                        Commands.literal("set")
                                .then(
                                        Commands.literal("globalOverEverything")
                                                .then(
                                                        Commands.argument("value", BoolArgumentType.bool())
                                                                .executes(this::setGlobalOverridesAll)
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
                                        Commands.literal("typeOverEntity")
                                                .then(
                                                        Commands.argument("value", BoolArgumentType.bool())
                                                                .executes(this::setEntityTypeColorOverridesEntityColor)
                                                )
                                )
                )
                .then(
                        Commands.literal("get")
                                .then(
                                        Commands.literal("globalOverEverything").executes(this::getGlobalOverridesAll)

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
                                        Commands.literal("typeOverEntity").executes(this::getEntityTypeColorOverridesEntityColor)

                                )
                )
                .build();
    }

}
