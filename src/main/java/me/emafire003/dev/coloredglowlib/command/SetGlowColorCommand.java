package me.emafire003.dev.coloredglowlib.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import me.emafire003.dev.coloredglowlib.util.Color;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.EntitySummonArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.Collection;

public class SetGlowColorCommand implements CGLCommand {

    private int setGlowColor(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<? extends Entity> targets = EntityArgument.getEntities(context, "targets");
        String color = "#"+StringArgumentType.getString(context, "color");
        CommandSourceStack source = context.getSource();

        if(Color.isHexColor(color) || color.equalsIgnoreCase("#rainbow")){
            for (Entity entity : targets) {
                ColoredGlowLibMod.getLib().removeColor(entity);
                if(color.equalsIgnoreCase("#rainbow")){
                    ColoredGlowLibMod.getLib().setRainbowColorToEntity(entity, true);
                }else{
                    ColoredGlowLibMod.getLib().setColorToEntity(entity, Color.translateFromHEX(color));
                }
            }
            
            if(!source.getLevel().isClientSide) {
                ColoredGlowLibMod.getLib().updateData(source.getServer());
            }
            

            //source.sendFeedback(new TranslatableText("commands.setglowcolor.success1").append(color).append(new TranslatableText("commands.setglowcolor.success2")), true);
            source.sendSystemMessage(Component.literal(ColoredGlowLibMod.PREFIX+"Setted color '" + color + "' to the selected entity/entities!"));
            ColoredGlowLibMod.getLib().optimizeData();
            return targets.size();
        }else{
            //source.sendError(new TranslatableText("commands.setglowcolor.notcolor"));
            source.sendSystemMessage(Component.literal(ColoredGlowLibMod.PREFIX+"Error! The value you have specified is not valid! It should be RRGGBB (without '#') or 'rainbow'"));
            return 0;
        }
    }

    private int setTypeGlowColor(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String color = "#"+StringArgumentType.getString(context, "color");
        CommandSourceStack source = context.getSource();
        
        
        if(Color.isHexColor(color) || color.equalsIgnoreCase("#rainbow")){
            EntityType type = EntityType.byString(EntitySummonArgument.getSummonableEntity(context, "entity").toString()).get();
            ColoredGlowLibMod.getLib().removeColor(type);
            if(color.equalsIgnoreCase("#rainbow")){
                ColoredGlowLibMod.getLib().setRainbowColorToEntityType(type, true);
            }else{
                ColoredGlowLibMod.getLib().setColorToEntityType(type, Color.translateFromHEX(color));
            }

            if(!source.getLevel().isClientSide) {
                ColoredGlowLibMod.getLib().updateData(source.getServer());
            }

            //source.sendFeedback(new TranslatableText("commands.setglowcolor.success1").append(color).append(new TranslatableText("commands.setglowcolor.success2")), true);
            source.sendSystemMessage(Component.literal(ColoredGlowLibMod.PREFIX+"Setted color '" + color + "' to the selected entity/entities!"));
            ColoredGlowLibMod.getLib().optimizeData();
            return 1;
        }else{
            //source.sendError(new TranslatableText("commands.setglowcolor.notcolor"));
            source.sendSystemMessage(Component.literal(ColoredGlowLibMod.PREFIX+"Error! The value you have specified is not valid! It should be RRGGBB (without '#') or 'rainbow'"));
            return 0;
        }
    }

    public LiteralCommandNode<CommandSourceStack> getNode() {
        return Commands
                .literal("setglowcolor")
                .then(
                        Commands.argument("targets", EntityArgument.entities())
                                .then(
                                    Commands.argument("color", StringArgumentType.string())
                                .executes(this::setGlowColor)
                                )
                )
                .then(
                        Commands.argument("entity", EntitySummonArgument.id()).suggests(SuggestionProviders.SUMMONABLE_ENTITIES)
                                .then(
                                        Commands.argument("color", StringArgumentType.string())
                                                .executes(this::setTypeGlowColor)
                                )
                )
                .build();
    }

}
