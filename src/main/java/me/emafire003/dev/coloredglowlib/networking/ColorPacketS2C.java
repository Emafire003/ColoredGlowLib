package me.emafire003.dev.coloredglowlib.networking;

import io.netty.buffer.Unpooled;
import me.emafire003.dev.coloredglowlib.ColoredGlowLib;
import me.emafire003.dev.coloredglowlib.util.Color;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import static me.emafire003.dev.coloredglowlib.ColoredGlowLib.LOGGER;

public class ColorPacketS2C extends PacketByteBuf {
    public static final Identifier ID = new Identifier(ColoredGlowLib.MOD_ID , "color_packet");

    public ColorPacketS2C(Color results) {
        super(Unpooled.buffer());
        String color = results.toHEX();
        this.writeString(color);
    }

    public static Color read(PacketByteBuf buf) {
        return Color.translateFromHEX(buf.readString());
    }
}
