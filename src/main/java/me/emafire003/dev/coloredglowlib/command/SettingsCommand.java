package me.emafire003.dev.coloredglowlib.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import me.emafire003.dev.coloredglowlib.compat.permissions.PermissionsChecker;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;


public class SettingsCommand implements CGLCommand {


    // I can't use something like that because the server hasn't started yet when the command is registered, so i need to get the api inside the method call
    // private final ColoredGlowLibAPI cgl = ColoredGlowLibMod.getColoredGlowLib();

    private int setOverrideTeamColors(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        boolean b = BoolArgumentType.getBool(context, "value");
        ServerCommandSource source = context.getSource();

        if (ColoredGlowLibMod.getAPI() != null) {
            ColoredGlowLibMod.getAPI().setOverrideTeamColors(b);
        }else{
            source.sendError(Text.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
            return 1;
        }

        if(b){
            source.sendFeedback(Text.literal(ColoredGlowLibMod.PREFIX+"§aThe color set by the mod will a have priority over the vanilla team color! (Override Team Colors : Enabled) "), true);
        }else{
            source.sendFeedback(Text.literal(ColoredGlowLibMod.PREFIX+"§cThe color of the vanilla team will have priority over the one set by the mod! (Override Team Colors : Disabled)"), true);
        }
        return 1;
    }

    private int getOverrideTeamColors(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        boolean b = false;
        if (ColoredGlowLibMod.getAPI() != null) {
            b = ColoredGlowLibMod.getAPI().getOverrideTeamColors();
        }else{
            source.sendError(Text.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
            return 1;
        }

        if(b){
            source.sendFeedback(Text.literal(ColoredGlowLibMod.PREFIX+"§aThe color set by the mod has priority over the vanilla team color! (Override Team Colors : Enabled) "), false);
        }else{
            source.sendFeedback(Text.literal(ColoredGlowLibMod.PREFIX+"§cThe color of the vanilla team has priority over the one set by the mod! (Override Team Colors : Disabled)"), false);
        }
        return 1;
    }

    private int setEntityTypeColorOverridesEntityColor(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        boolean b = BoolArgumentType.getBool(context, "value");
        ServerCommandSource source = context.getSource();

        if (ColoredGlowLibMod.getAPI() != null) {
           ColoredGlowLibMod.getAPI().setEntityTypeColorOverridesEntityColor(b);
        }else{
            source.sendError(Text.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
            return 1;
        }

        if(b){
            source.sendFeedback(Text.literal(ColoredGlowLibMod.PREFIX+"§aEntityType glow color is now predominant over Entity-specific color! (EntityType >> Entity) "), true);
        }else{
            source.sendFeedback(Text.literal(ColoredGlowLibMod.PREFIX+"§cEntityType glow color will now be overridden by Entity-specific color! (Entity >> EntityType) "), true);
        }
        return 1;
    }

    private int getEntityTypeColorOverridesEntityColor(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        boolean b = false;

        if (ColoredGlowLibMod.getAPI() != null) {
            b = ColoredGlowLibMod.getAPI().getEntityTypeColorOverridesEntityColor();
        }else{
            source.sendError(Text.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
            return 1;
        }

        if(b){
            source.sendFeedback(Text.literal(ColoredGlowLibMod.PREFIX+"§aEntityTypes glow color is currently predominant over Entity-specific color. (EntityType >> Entity) "), false);
        }else{
            source.sendFeedback(Text.literal(ColoredGlowLibMod.PREFIX+"§cEntity glow color is currently predominant over EntityType color. (Entity >> EntityType)"), false);
        }
        return 1;
    }

    private int setGlobalOverridesAll(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        boolean b = BoolArgumentType.getBool(context, "value");
        ServerCommandSource source = context.getSource();

        if (ColoredGlowLibMod.getAPI() != null) {
            ColoredGlowLibMod.getAPI().setDefaultOverridesAll(b);
        }else{
            source.sendError(Text.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
            return 1;
        }

        if(b){
            source.sendFeedback(Text.literal(ColoredGlowLibMod.PREFIX+"§aGlobal/Default glow color is now predominant over Entity or EntityType colors! (Global >> everything else) "), true);
        }else{
            source.sendFeedback(Text.literal(ColoredGlowLibMod.PREFIX+"§cEntityType or Entity color is now predominant over Global/Default color! (everything else >> Global) "), true);
        }
        return 1;
    }

    private int getGlobalOverridesAll(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        boolean b = false;

        if (ColoredGlowLibMod.getAPI() != null) {
            b = ColoredGlowLibMod.getAPI().getDefaultOverridesAll();
        }else{
            source.sendError(Text.literal(ColoredGlowLibMod.PREFIX+"§cAn error has occurred. The API hasn't yet been initialised!"));
            return 1;
        }

        if(b){
            source.sendFeedback(Text.literal(ColoredGlowLibMod.PREFIX+"§aGlobal/Default glow color is currently predominant over Entity or EntityType colors! (Global >> everything else) "), false);
        }else{
            source.sendFeedback(Text.literal(ColoredGlowLibMod.PREFIX+"§cEntityType or Entity color is currently predominant over Global/Default color! (everything else >> Global)"), false);
        }
        return 1;
    }


//CommandManager.argument("entity", EntitySummonArgumentType.entitySummon()).suggests(SuggestionProviders.SUMMONABLE_ENTITIES).then(((RequiredArgumentBuilder)CommandManager.argument("color", StringArgumentType.string())
     public LiteralCommandNode<ServerCommandSource> getNode(CommandRegistryAccess registryAccess) {
        return CommandManager
                .literal("settings")
                .requires(PermissionsChecker.hasPerms("coloredglowlib.commands.settings", 2))
                .then(
                        CommandManager.literal("set")
                                .then(
                                        CommandManager.literal("globalOverEverything")
                                                .then(
                                                        CommandManager.argument("value", BoolArgumentType.bool())
                                                                .executes(this::setGlobalOverridesAll)
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
                                        CommandManager.literal("typeOverEntity")
                                                .then(
                                                        CommandManager.argument("value", BoolArgumentType.bool())
                                                                .executes(this::setEntityTypeColorOverridesEntityColor)
                                                )
                                )
                )
                .then(
                        CommandManager.literal("get")
                                .then(
                                        CommandManager.literal("globalOverEverything").executes(this::getGlobalOverridesAll)

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
                                        CommandManager.literal("typeOverEntity").executes(this::getEntityTypeColorOverridesEntityColor)

                                )
                )
                .build();
    }

}
