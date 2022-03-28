package me.emafire003.dev.coloredglowlib.networking;

import io.netty.buffer.Unpooled;
import me.emafire003.dev.coloredglowlib.ColoredGlowLib;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static me.emafire003.dev.coloredglowlib.ColoredGlowLib.LOGGER;

public class EntityListPacketS2C extends PacketByteBuf {
    public static final Identifier ID = new Identifier(ColoredGlowLib.MOD_ID , "rainbow_entity_list_packet");

    public EntityListPacketS2C(List<UUID> results) {
        super(Unpooled.buffer());
        this.writeInt(results.size());
        for (UUID uuid : results) {
            this.writeUuid(uuid);
        }
    }

    public static @Nullable List<UUID> read(PacketByteBuf buf) {
        try{
            List<UUID> results = new LinkedList<>();
            int max = buf.readInt();
            for (int i = 0; i < max; i++) {
                results.add(buf.readUuid());
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
