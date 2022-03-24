package me.emafire003.dev.coloredglowlib.networking;

import io.netty.buffer.Unpooled;
import me.emafire003.dev.coloredglowlib.ColoredGlowLib;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.*;

public class EntityListPacketS2C extends PacketByteBuf {
    public static final Identifier ID = new Identifier(ColoredGlowLib.MOD_ID , "rainbow_entity_list_packet");

    public EntityListPacketS2C(List<UUID> results) {
        super(Unpooled.buffer());
        this.writeInt(results.size());
        for (UUID uuid : results) {
            this.writeUuid(uuid);
        }
    }

    public static List<UUID> read(PacketByteBuf buf) {
        List<UUID> results = new LinkedList<>();
        int max = buf.readInt();
        for (int i = 0; i < max; i++) {
            results.add(buf.readUuid());
        }
        return results;
    }
}
