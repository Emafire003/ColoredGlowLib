package me.emafire003.dev.coloredglowlib.networking;

import io.netty.buffer.Unpooled;
import me.emafire003.dev.coloredglowlib.ColoredGlowLib;
import net.minecraft.entity.EntityType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static me.emafire003.dev.coloredglowlib.ColoredGlowLib.LOGGER;

public class EntityTypeListPacketS2C extends PacketByteBuf {
    public static final Identifier ID = new Identifier(ColoredGlowLib.MOD_ID , "rainbow_entitytype_list_packet");

    public EntityTypeListPacketS2C(List<String> results) {
        super(Unpooled.buffer());
        this.writeInt(results.size());
        for (String type : results) {
            this.writeString(type);
        }
    }

    public static @Nullable List<String> read(PacketByteBuf buf) {
        try{
            List<String> results = new LinkedList<>();
            int max = buf.readInt();
            for (int i = 0; i < max; i++) {
                results.add(buf.readString());
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
