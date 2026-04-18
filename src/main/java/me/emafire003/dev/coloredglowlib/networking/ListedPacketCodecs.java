package me.emafire003.dev.coloredglowlib.networking;

import io.netty.buffer.ByteBuf;
import me.emafire003.dev.coloredglowlib.custom_data_animations.ColorAnimationItem;
import me.emafire003.dev.coloredglowlib.custom_data_animations.CustomColorAnimation;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.VarInt;


import java.util.ArrayList;
import java.util.List;

public class ListedPacketCodecs {

    public static StreamCodec<ByteBuf, List<ColorAnimationItem>> ANIMATION_ITEMS = new StreamCodec<>() {
        @Override
        public List<ColorAnimationItem> decode(ByteBuf buf) {

            int i = VarInt.read(buf);
            List<ColorAnimationItem> animationItems = new ArrayList<>(i);

            for(int j = 0; j < i; ++j) {
                animationItems.add(ColorAnimationItem.PACKET_CODEC.decode(buf));
            }

            return animationItems;
        }

        @Override
        public void encode(ByteBuf buf, List<ColorAnimationItem> list) {
            VarInt.write(buf, list.size());
            list.forEach((colorItem) -> {
                ByteBufCodecs.STRING_UTF8.encode(buf, colorItem.getColor());
                ByteBufCodecs.INT.encode(buf, colorItem.getActiveFor());
            });
        }
    };

    public static StreamCodec<ByteBuf, List<CustomColorAnimation>> COLOR_ANIMATIONS = new StreamCodec<>() {
        @Override
        public List<CustomColorAnimation> decode(ByteBuf buf) {

            int i = VarInt.read(buf);
            List<CustomColorAnimation> animations = new ArrayList<>(i);

            for(int j = 0; j < i; ++j) {

                animations.add(CustomColorAnimation.PACKET_CODEC.decode(buf));
            }

            return animations;
        }

        @Override
        public void encode(ByteBuf buf, List<CustomColorAnimation> list) {
            VarInt.write(buf, list.size());
            list.forEach((animation) -> {
                ByteBufCodecs.STRING_UTF8.encode(buf, animation.getName());
                ANIMATION_ITEMS.encode(buf, animation.getColorAnimations());
            });
        }
    };
}
