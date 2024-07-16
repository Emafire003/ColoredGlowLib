package me.emafire003.dev.coloredglowlib;

import me.emafire003.dev.coloredglowlib.custom_data_animations.CustomColorAnimation;
import me.emafire003.dev.coloredglowlib.networking.ColorAnimationPacketS2C;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static com.mojang.text2speech.Narrator.LOGGER;

public class ColoredGlowLibModClient implements ClientModInitializer {

    private static final List<CustomColorAnimation> animations = new ArrayList<>();

    @Override
    public void onInitializeClient() {

        registerColorAnimationPacket();

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            ColoredGlowLibMod.LOGGER.info("Unregistering the color animations received from the server...");
            animations.forEach(animation -> ColoredGlowLibMod.getCustomColorAnimations().remove(animation));
        });

    }

    private void registerColorAnimationPacket(){
        ClientPlayNetworking.registerGlobalReceiver(ColorAnimationPacketS2C.ID, ((client, handler, buf, responseSender) -> {
            CustomColorAnimation animation = ColorAnimationPacketS2C.read(buf);

            client.execute(() -> {
                try{
                    if(!ColoredGlowLibMod.getCustomColorAnimations().contains(animation)){
                        ColoredGlowLibMod.loadCustomColorAnimation(animation);
                        animations.add(animation);
                    }
                }catch (NoSuchElementException e){
                    LOGGER.warn("No value in the packet, probably not a big problem");
                }catch (Exception e){
                    LOGGER.error("There was an error while getting the packet!");
                    e.printStackTrace();
                }
            });
        }));
    }
}
