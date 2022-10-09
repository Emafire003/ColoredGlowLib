package me.emafire003.dev.coloredglowlib.networking.packets;

import me.emafire003.dev.coloredglowlib.client.ColoredGlowLibClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class EntityListPacketS2c {
    private final List<UUID> list;

    public EntityListPacketS2c(List<UUID> list) {
        this.list = list;
    }

    public EntityListPacketS2c(FriendlyByteBuf buf) {
        this.list = buf.readList(FriendlyByteBuf::readUUID);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeCollection(list, FriendlyByteBuf::writeUUID);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ColoredGlowLibClient.handleEntityListPacket(list);
        });
        return true;
    }
}
