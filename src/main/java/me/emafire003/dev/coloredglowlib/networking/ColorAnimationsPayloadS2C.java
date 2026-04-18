package me.emafire003.dev.coloredglowlib.networking;

import me.emafire003.dev.coloredglowlib.custom_data_animations.CustomColorAnimation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.List;


public record ColorAnimationsPayloadS2C(List<CustomColorAnimation> colorAnimations) implements CustomPacketPayload {
    public static final Type<ColorAnimationsPayloadS2C> ID = new Type<>(
            Identifier.fromNamespaceAndPath("coloredglowlib", "color_animations_packet")
    );

    public static final StreamCodec<FriendlyByteBuf, ColorAnimationsPayloadS2C> PACKET_CODEC = StreamCodec.composite(
            ListedPacketCodecs.COLOR_ANIMATIONS, ColorAnimationsPayloadS2C::colorAnimations,
                ColorAnimationsPayloadS2C::new
    );



    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
