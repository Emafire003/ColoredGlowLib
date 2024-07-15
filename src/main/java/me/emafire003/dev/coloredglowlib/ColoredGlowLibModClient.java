package me.emafire003.dev.coloredglowlib;

import me.emafire003.dev.coloredglowlib.custom_data_animations.CustomColorAnimation;
import me.emafire003.dev.coloredglowlib.networking.ColorAnimationsPayloadS2C;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.ArrayList;
import java.util.List;

public class ColoredGlowLibModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        List<CustomColorAnimation> animations = new ArrayList<>();
        ClientPlayNetworking.registerGlobalReceiver(ColorAnimationsPayloadS2C.ID, (payload, context) -> context.client().execute(() -> {
            ColoredGlowLibMod.LOGGER.info("Getting color animations from the server...");
            animations.addAll(payload.colorAnimations());
            for(CustomColorAnimation animation : payload.colorAnimations()){
                if(!ColoredGlowLibMod.getCustomColorAnimations().contains(animation)){
                    ColoredGlowLibMod.loadCustomColorAnimation(animation);
                }
            }
        }));

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            ColoredGlowLibMod.LOGGER.info("Unregistering the color animations received from the server...");
            animations.forEach(animation -> ColoredGlowLibMod.getCustomColorAnimations().remove(animation));
        });

    }
}
