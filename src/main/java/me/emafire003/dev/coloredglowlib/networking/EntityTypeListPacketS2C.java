package me.emafire003.dev.coloredglowlib.networking;

import io.netty.buffer.Unpooled;
import me.emafire003.dev.coloredglowlib.ColoredGlowLib;
import net.minecraft.entity.EntityType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class EntityTypeListPacketS2C extends PacketByteBuf {
    public static final Identifier ID = new Identifier(ColoredGlowLib.MOD_ID , "rainbow_entitytype_list_packet");

    public EntityTypeListPacketS2C(List<String> results) {
        super(Unpooled.buffer());
        this.writeInt(results.size());
        for (String type : results) {
            this.writeString(type);
        }
    }

    public static List<EntityType> read(PacketByteBuf buf) {
        List<EntityType> results = new LinkedList<>();
        int max = buf.readInt();
        for (int i = 0; i < max; i++) {
            results.add(EntityType.get(buf.readString()).get());
        }
        return results;
    }
}
