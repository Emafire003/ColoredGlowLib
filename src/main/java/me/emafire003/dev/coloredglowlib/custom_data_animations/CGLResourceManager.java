package me.emafire003.dev.coloredglowlib.custom_data_animations;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import me.emafire003.dev.coloredglowlib.networking.ColorAnimationPacketS2C;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.io.Reader;
import java.util.Map;

import static me.emafire003.dev.coloredglowlib.ColoredGlowLibMod.getCustomColorAnimations;


public class CGLResourceManager {

    public static String RESOURCE_PATH = "color_animations";

    public static void register(){
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {

            @Override
            public Identifier getFabricId() {
                return ColoredGlowLibMod.getIdentifier(RESOURCE_PATH);
            }

            @Override
            public void reload(ResourceManager manager) {
                // Clear Caches Here
                getCustomColorAnimations().clear();

                Map<Identifier, Resource> resources = manager.findResources(RESOURCE_PATH, identifier -> identifier.getPath().endsWith(".json"));

                for(Identifier id : resources.keySet()) {
                    try {

                        Reader reader = resources.get(id).getReader();//manager.getResource(id).get().getReader();
                        JsonElement jsonElement = JsonParser.parseReader(reader);

                        DataResult<CustomColorAnimation> result = CustomColorAnimation.CODEC.parse(new Dynamic<>(JsonOps.INSTANCE, jsonElement));

                        ColoredGlowLibMod.LOGGER.debug("Processing file: " + id);

                        CustomColorAnimation groupOutput = result.getOrThrow(false, ColoredGlowLibMod.LOGGER::warn);

                        ColoredGlowLibMod.loadCustomColorAnimation(groupOutput);

                    } catch(Exception e) {
                        ColoredGlowLibMod.LOGGER.error("Error occurred while loading resource json" + id.toString(), e);
                    }
                }

                //Checks if animation weren't correct, like using "red" instead of "#ff0000" which isn't defined or another wrong color string of sorts
                ColoredGlowLibMod.checkMaybeWrongAnimations();

                if(FabricLoader.getInstance().getEnvironmentType().equals(EnvType.SERVER)){
                    @Nullable MinecraftServer server = ColoredGlowLibMod.getServerInstance();
                    if(server == null){
                        return;
                    }
                    for(CustomColorAnimation animation : getCustomColorAnimations()){
                        server.getPlayerManager().getPlayerList()
                                .forEach(player -> ServerPlayNetworking.send(player, ColorAnimationPacketS2C.ID, new ColorAnimationPacketS2C(animation)));
                    }
                }
            }
        });
    }
}
