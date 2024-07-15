package me.emafire003.dev.coloredglowlib.networking;

import me.emafire003.dev.coloredglowlib.custom_data_animations.CustomColorAnimation;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.List;


public record ColorAnimationsPayloadS2C(List<CustomColorAnimation> colorAnimations) implements CustomPayload {
    public static final Id<ColorAnimationsPayloadS2C> ID = new Id<>(
            Identifier.of("coloredglowlib", "color_animations_packet")
    );

    public static final PacketCodec<PacketByteBuf, ColorAnimationsPayloadS2C> PACKET_CODEC = PacketCodec.tuple(
            ListedPacketCodecs.COLOR_ANIMATIONS, ColorAnimationsPayloadS2C::colorAnimations,
                ColorAnimationsPayloadS2C::new
    );



    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
