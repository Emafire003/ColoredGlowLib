package me.emafire003.dev.coloredglowlib.networking.packets;

import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import me.emafire003.dev.coloredglowlib.client.ColoredGlowLibClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Supplier;

public class EntityTypeMapPacketS2c {
    private final HashMap<String, String> map;

    public EntityTypeMapPacketS2c(HashMap<String, String> map) {
        this.map = map;
    }

    public EntityTypeMapPacketS2c(FriendlyByteBuf buf) {
        map = (HashMap<String, String>) buf.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readUtf);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeMap(map, FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeUtf);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ColoredGlowLibClient.handleEntityTypeMapPacket(map);
        });
        return true;
    }
}
