package me.emafire003.dev.coloredglowlib.networking;

import io.netty.buffer.Unpooled;
import me.emafire003.dev.coloredglowlib.ColoredGlowLib;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.LinkedList;
import java.util.List;

public class BooleanValuesPacketS2C extends PacketByteBuf {
    public static final Identifier ID = new Identifier(ColoredGlowLib.MOD_ID , "boolean_values_packet");

    public BooleanValuesPacketS2C(List<Boolean> results) {
        super(Unpooled.buffer());
        this.writeInt(results.size());
        for (Boolean value : results) {
            this.writeBoolean(value);
        }
    }

    public static List<Boolean> read(PacketByteBuf buf) {
        List<Boolean> results = new LinkedList<>();
        int max = buf.readInt();
        for (int i = 0; i < max; i++) {
            results.add(buf.readBoolean());
        }
        return results;
    }
}
