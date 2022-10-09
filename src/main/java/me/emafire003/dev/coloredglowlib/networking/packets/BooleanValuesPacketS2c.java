package me.emafire003.dev.coloredglowlib.networking.packets;

import me.emafire003.dev.coloredglowlib.client.ColoredGlowLibClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class BooleanValuesPacketS2c {
    private final List<Boolean> list;

    public BooleanValuesPacketS2c(List<Boolean> list) {
        this.list = list;
    }

    public BooleanValuesPacketS2c(FriendlyByteBuf buf) {
        this.list = buf.readList(FriendlyByteBuf::readBoolean);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeCollection(list, FriendlyByteBuf::writeBoolean);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ColoredGlowLibClient.handleBooleanValuesMapPacket(list);
        });
        return true;
    }
}
