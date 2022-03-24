package me.emafire003.dev.coloredglowlib.networking;

import io.netty.buffer.Unpooled;
import me.emafire003.dev.coloredglowlib.ColoredGlowLib;
import net.minecraft.entity.EntityType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

public class EntityTypeMapPacketS2C extends PacketByteBuf {
    public static final Identifier ID = new Identifier(ColoredGlowLib.MOD_ID , "entitytype_map_packet");

    public EntityTypeMapPacketS2C(HashMap<String, String> results) {
        super(Unpooled.buffer());
        this.writeInt(results.size());
        for (HashMap.Entry<String, String> entry : results.entrySet()) {
            this.writeString(entry.getKey());
            this.writeString(entry.getValue());
        }
    }

    public static HashMap<EntityType, String> read(PacketByteBuf buf) {
        HashMap<EntityType, String> results = new LinkedHashMap<>();
        int max = buf.readInt();
        for (int i = 0; i < max; i++) {
            results.put(EntityType.get(buf.readString()).get(), buf.readString());
        }
        return results;
    }
}
