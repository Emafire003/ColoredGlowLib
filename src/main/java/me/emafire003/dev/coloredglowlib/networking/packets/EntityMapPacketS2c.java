package me.emafire003.dev.coloredglowlib.networking.packets;

import me.emafire003.dev.coloredglowlib.client.ColoredGlowLibClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Supplier;

public class EntityMapPacketS2c {
    private final HashMap<UUID, String> map;

    public EntityMapPacketS2c(HashMap<UUID, String> map) {
        this.map = map;
    }

    public EntityMapPacketS2c(FriendlyByteBuf buf) {
        this.map = (HashMap<UUID, String>) buf.readMap(FriendlyByteBuf::readUUID, FriendlyByteBuf::readUtf);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeMap(map, FriendlyByteBuf::writeUUID, FriendlyByteBuf::writeUtf);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ColoredGlowLibClient.handleEntityMapPacket(map);
        });
        return true;
    }
}
