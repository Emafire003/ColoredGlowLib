package me.emafire003.dev.coloredglowlib.networking;

import io.netty.buffer.Unpooled;
import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import me.emafire003.dev.coloredglowlib.custom_data_animations.ColorAnimationItem;
import me.emafire003.dev.coloredglowlib.custom_data_animations.CustomColorAnimation;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static me.emafire003.dev.coloredglowlib.ColoredGlowLibMod.LOGGER;
//Instead of sending the whole animation in one go, it will send only one animation.
public class ColorAnimationPacketS2C extends PacketByteBuf {
    public static final Identifier ID = new Identifier(ColoredGlowLibMod.MOD_ID , "color_animation_packet");

    /**Currently used only for the auto light activation*/
    public ColorAnimationPacketS2C(CustomColorAnimation animation) {
        super(Unpooled.buffer());
        this.writeString(animation.getName());
        this.writeVarInt(animation.getColorAnimations().size());
        for(ColorAnimationItem item : animation.getColorAnimations()){
            this.writeString(item.getColor());
            this.writeInt(item.getActiveFor());
        }
    }

    public static CustomColorAnimation read(PacketByteBuf buf){
        try{
            String id = buf.readString();
            int length = buf.readVarInt();
            List<ColorAnimationItem> animationItems = new ArrayList<>();
            for(int i = 0; i < length; i++){
                animationItems.add(new ColorAnimationItem(buf.readString(), buf.readInt()));
            }
            return new CustomColorAnimation(id, animationItems);
        }catch (NoSuchElementException e){
            LOGGER.warn("No value in the packet while reading, probably not a big problem");
            return null;
        }catch (Exception e){
            LOGGER.error("There was an error while reading the packet!");
            e.printStackTrace();
            return null;
        }
    }

}
