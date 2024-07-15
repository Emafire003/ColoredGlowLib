package me.emafire003.dev.coloredglowlib.networking;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public interface PlayerJoinEvent {
    Event<PlayerJoinEvent> EVENT = EventFactory.createArrayBacked(PlayerJoinEvent.class, (listeners) -> (player, server) -> {
        for (PlayerJoinEvent listener : listeners) {
           listener.joinServer(player, server);
        }
        return ActionResult.PASS;
    });

    ActionResult joinServer(ServerPlayerEntity player, MinecraftServer server);
}
