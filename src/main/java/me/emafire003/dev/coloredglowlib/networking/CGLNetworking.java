package me.emafire003.dev.coloredglowlib.networking;

import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import me.emafire003.dev.coloredglowlib.networking.packets.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class CGLNetworking {
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(ColoredGlowLibMod.MOD_ID, "packets"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(EntityMapPacketS2c.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(EntityMapPacketS2c::new)
                .encoder(EntityMapPacketS2c::toBytes)
                .consumerMainThread(EntityMapPacketS2c::handle)
                .add();

        net.messageBuilder(EntityTypeMapPacketS2c.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(EntityTypeMapPacketS2c::new)
                .encoder(EntityTypeMapPacketS2c::toBytes)
                .consumerMainThread(EntityTypeMapPacketS2c::handle)
                .add();

        net.messageBuilder(EntityListPacketS2c.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(EntityListPacketS2c::new)
                .encoder(EntityListPacketS2c::toBytes)
                .consumerMainThread(EntityListPacketS2c::handle)
                .add();

        net.messageBuilder(EntityTypeListPacketS2c.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(EntityTypeListPacketS2c::new)
                .encoder(EntityTypeListPacketS2c::toBytes)
                .consumerMainThread(EntityTypeListPacketS2c::handle)
                .add();

        net.messageBuilder(BooleanValuesPacketS2c.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(BooleanValuesPacketS2c::new)
                .encoder(BooleanValuesPacketS2c::toBytes)
                .consumerMainThread(BooleanValuesPacketS2c::handle)
                .add();

        net.messageBuilder(ColorPacketS2c.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ColorPacketS2c::new)
                .encoder(ColorPacketS2c::toBytes)
                .consumerMainThread(ColorPacketS2c::handle)
                .add();
    }

    public static <MSG> void send(ServerPlayer player, MSG message) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
