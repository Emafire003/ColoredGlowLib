package me.emafire003.dev.coloredglowlib.networking.packets;

import me.emafire003.dev.coloredglowlib.client.ColoredGlowLibClient;
import me.emafire003.dev.coloredglowlib.util.Color;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class ColorPacketS2c {
    private String color;

    public ColorPacketS2c(String color) {
        this.color = color;
    }

    public ColorPacketS2c(FriendlyByteBuf buf) {
        this.color = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(color);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ColoredGlowLibClient.handleColorPacket(Color.translateFromHEX(color));
        });
        return true;
    }
}
