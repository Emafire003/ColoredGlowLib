package me.emafire003.dev.coloredglowlib.networking;

import io.netty.buffer.Unpooled;
import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;

import static me.emafire003.dev.coloredglowlib.ColoredGlowLibMod.LOGGER;

public class EntityTypeMapPacketS2C extends PacketByteBuf {
    public static final Identifier ID = new Identifier(ColoredGlowLibMod.MOD_ID , "entitytype_map_packet");

    public EntityTypeMapPacketS2C(HashMap<String, String> results) {
        super(Unpooled.buffer());
        this.writeInt(results.size());
        for (HashMap.Entry<String, String> entry : results.entrySet()) {
            this.writeString(entry.getKey());
            this.writeString(entry.getValue());
        }
    }

    public static @Nullable HashMap<String, String> read(PacketByteBuf buf) {
        try{
            HashMap<String, String> results = new LinkedHashMap<>();
            int max = buf.readInt();
            for (int i = 0; i < max; i++) {
                results.put(buf.readString(), buf.readString());
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
