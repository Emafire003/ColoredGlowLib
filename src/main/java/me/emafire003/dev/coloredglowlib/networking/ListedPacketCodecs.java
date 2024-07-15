package me.emafire003.dev.coloredglowlib.networking;

import io.netty.buffer.ByteBuf;
import me.emafire003.dev.coloredglowlib.custom_data_animations.ColorAnimationItem;
import me.emafire003.dev.coloredglowlib.custom_data_animations.CustomColorAnimation;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.encoding.VarInts;


import java.util.ArrayList;
import java.util.List;

public class ListedPacketCodecs {

    public static PacketCodec<ByteBuf, List<ColorAnimationItem>> ANIMATION_ITEMS = new PacketCodec<>() {
        @Override
        public List<ColorAnimationItem> decode(ByteBuf buf) {

            int i = VarInts.read(buf);
            List<ColorAnimationItem> animationItems = new ArrayList<>(i);

            for(int j = 0; j < i; ++j) {
                animationItems.add(ColorAnimationItem.PACKET_CODEC.decode(buf));
            }

            return animationItems;
        }

        @Override
        public void encode(ByteBuf buf, List<ColorAnimationItem> list) {
            VarInts.write(buf, list.size());
            list.forEach((colorItem) -> {
                PacketCodecs.STRING.encode(buf, colorItem.getColor());
                PacketCodecs.INTEGER.encode(buf, colorItem.getActiveFor());
            });
        }
    };

    public static PacketCodec<ByteBuf, List<CustomColorAnimation>> COLOR_ANIMATIONS = new PacketCodec<>() {
        @Override
        public List<CustomColorAnimation> decode(ByteBuf buf) {

            int i = VarInts.read(buf);
            List<CustomColorAnimation> animations = new ArrayList<>(i);

            for(int j = 0; j < i; ++j) {

                animations.add(CustomColorAnimation.PACKET_CODEC.decode(buf));
            }

            return animations;
        }

        @Override
        public void encode(ByteBuf buf, List<CustomColorAnimation> list) {
            VarInts.write(buf, list.size());
            list.forEach((animation) -> {
                PacketCodecs.STRING.encode(buf, animation.getName());
                ANIMATION_ITEMS.encode(buf, animation.getColorAnimations());
            });
        }
    };
}
