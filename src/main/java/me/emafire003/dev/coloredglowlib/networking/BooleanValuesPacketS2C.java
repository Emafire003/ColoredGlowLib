package me.emafire003.dev.coloredglowlib.networking;

import io.netty.buffer.Unpooled;
import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import static me.emafire003.dev.coloredglowlib.ColoredGlowLibMod.LOGGER;

public class BooleanValuesPacketS2C extends PacketByteBuf {
    public static final Identifier ID = new Identifier(ColoredGlowLibMod.MOD_ID , "boolean_values_packet");

    public BooleanValuesPacketS2C(List<Boolean> results) {
        super(Unpooled.buffer());
        this.writeInt(results.size());
        for (Boolean value : results) {
            this.writeBoolean(value);
        }
    }

    public static @Nullable List<Boolean> read(PacketByteBuf buf) {
        try{
            List<Boolean> results = new LinkedList<>();
            int max = buf.readInt();
            for (int i = 0; i < max; i++) {
                results.add(buf.readBoolean());
            }
            return results;
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
