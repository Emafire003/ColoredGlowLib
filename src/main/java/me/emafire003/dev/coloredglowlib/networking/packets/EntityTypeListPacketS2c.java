package me.emafire003.dev.coloredglowlib.networking.packets;

import me.emafire003.dev.coloredglowlib.client.ColoredGlowLibClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class EntityTypeListPacketS2c {
    private final List<String> list;

    public EntityTypeListPacketS2c(List<String> list) {
        this.list = list;
    }

    public EntityTypeListPacketS2c(FriendlyByteBuf buf) {
        this.list = buf.readList(FriendlyByteBuf::readUtf);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeCollection(list, FriendlyByteBuf::writeUtf);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ColoredGlowLibClient.handleEntityTypeListPacket(list);
        });
        return true;
    }
}
