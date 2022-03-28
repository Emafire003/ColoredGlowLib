package me.emafire003.dev.coloredglowlib.networking;

import io.netty.buffer.Unpooled;
import me.emafire003.dev.coloredglowlib.ColoredGlowLib;
import net.minecraft.entity.EntityType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.UUID;

import static me.emafire003.dev.coloredglowlib.ColoredGlowLib.LOGGER;

public class EntityMapPacketS2C extends PacketByteBuf {
    public static final Identifier ID = new Identifier(ColoredGlowLib.MOD_ID , "entity_map_packet");

    public EntityMapPacketS2C(HashMap<UUID, String> results) {
        super(Unpooled.buffer());
        this.writeInt(results.size());
        for (HashMap.Entry<UUID, String> entry : results.entrySet()) {
            this.writeUuid(entry.getKey());
            this.writeString(entry.getValue());
        }
    }

    public static @Nullable HashMap<UUID, String> read(PacketByteBuf buf) {
        try {
            HashMap<UUID, String> results = new LinkedHashMap<>();
            int max = buf.readInt();
            for (int i = 0; i < max; i++) {
                results.put(buf.readUuid(), buf.readString());
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
