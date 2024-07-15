package me.emafire003.dev.coloredglowlib.custom_data_animations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public class ColorAnimationItem {

    public static final Codec<ColorAnimationItem> CODEC = RecordCodecBuilder.create(
            instance ->
                    instance.group(Codec.STRING.fieldOf("color")
                                            .forGetter(ColorAnimationItem::getColor),
                                    Codec.INT.fieldOf("active_for")
                                            .forGetter(ColorAnimationItem::getActiveFor)
                            )
                            .apply(instance, ColorAnimationItem::new));

    public static final PacketCodec<ByteBuf, ColorAnimationItem> PACKET_CODEC = new PacketCodec<>() {
        @Override
        public ColorAnimationItem decode(ByteBuf buf) {
            return new ColorAnimationItem(PacketCodecs.STRING.decode(buf), PacketCodecs.INTEGER.decode(buf));
        }

        @Override
        public void encode(ByteBuf buf, ColorAnimationItem value) {
            PacketCodecs.STRING.encode(buf, value.color);
            PacketCodecs.INTEGER.encode(buf, value.active_for);
        }
    };

    private final String color;
    private final int active_for;


    public ColorAnimationItem(String color, int active_for){
        this.color = color;
        this.active_for = active_for;
    }

    public String getColor() {
        return color;
    }

    public int getActiveFor() {
        return active_for;
    }
}
