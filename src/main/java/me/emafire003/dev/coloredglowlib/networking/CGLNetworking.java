package me.emafire003.dev.coloredglowlib.networking;

import me.emafire003.dev.coloredglowlib.networking.packets.EntityMapPacketS2c;
import net.kaupenjoe.tutorialmod.TutorialMod;
import net.kaupenjoe.tutorialmod.networking.packet.*;
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
                .named(new ResourceLocation(TutorialMod.MOD_ID, "packets"))
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

        net.messageBuilder(EnergySyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(EnergySyncS2CPacket::new)
                .encoder(EnergySyncS2CPacket::toBytes)
                .consumerMainThread(EnergySyncS2CPacket::handle)
                .add();

        net.messageBuilder(FluidSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(FluidSyncS2CPacket::new)
                .encoder(FluidSyncS2CPacket::toBytes)
                .consumerMainThread(FluidSyncS2CPacket::handle)
                .add();

        net.messageBuilder(ItemStackSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ItemStackSyncS2CPacket::new)
                .encoder(ItemStackSyncS2CPacket::toBytes)
                .consumerMainThread(ItemStackSyncS2CPacket::handle)
                .add();
    }

    public static <MSG> void send(ServerPlayer player, MSG message) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
