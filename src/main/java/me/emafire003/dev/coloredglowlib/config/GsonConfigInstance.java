package me.emafire003.dev.coloredglowlib.config;

import com.google.gson.*;
import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.function.UnaryOperator;

//Code credits: isXander from YetAnotherConfigLib https://github.com/isXander/YetAnotherConfigLib

/**
 * Uses GSON to serialize and deserialize config data from JSON to a file.
 *
 * Only fields annotated with {@link me.emafire003.dev.coloredglowlib.config.ConfigEntry} are included in the JSON.
 * {@link Text}, {@link Style} and {@link Color} have default type adapters, so there is no need to provide them in your GSON instance.
 * GSON is automatically configured to format fields as {@code lower_camel_case}.
 *
 * @param <T> config data type
 */
public class GsonConfigInstance<T> extends ConfigInstance<T> {
    private final Gson gson;
    private final Path path;

    public GsonConfigInstance(Class<T> configClass, Path path) {
        this(configClass, path, new GsonBuilder());
    }

    public GsonConfigInstance(Class<T> configClass, Path path, Gson gson) {
        this(configClass, path, gson.newBuilder());
    }

    public GsonConfigInstance(Class<T> configClass, Path path, UnaryOperator<GsonBuilder> builder) {
        this(configClass, path, builder.apply(new GsonBuilder()));
    }

    public GsonConfigInstance(Class<T> configClass, Path path, GsonBuilder builder) {
        super(configClass);
        this.path = path;
        this.gson = builder
                .setExclusionStrategies(new ConfigExclusionStrategy())
                .registerTypeHierarchyAdapter(Text.class, new Text.Serializer())
                //.registerTypeHierarchyAdapter(Style.class, new Style.Serializer())
                .registerTypeHierarchyAdapter(Color.class, new ColorTypeAdapter())
                .serializeNulls()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    @Override
    public void save() {
        try {
            ColoredGlowLibMod.LOGGER.info("Saving {}...", getConfigClass().getSimpleName());
            Files.writeString(path, gson.toJson(getConfig()), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load() {
        try {
            if (Files.notExists(path)) {
                Files.createDirectories(ColoredGlowLibMod.PATH);
                save();
                ColoredGlowLibMod.LOGGER.info("while loading");
                return;
            }

            ColoredGlowLibMod.LOGGER.info("Loading {}...", getConfigClass().getSimpleName());
            setConfig(gson.fromJson(Files.readString(path), getConfigClass()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Path getPath() {
        return this.path;
    }

    private static class ConfigExclusionStrategy implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes fieldAttributes) {
            return fieldAttributes.getAnnotation(ConfigEntry.class) == null;
        }

        @Override
        public boolean shouldSkipClass(Class<?> aClass) {
            return false;
        }
    }

    public static class ColorTypeAdapter implements JsonSerializer<Color>, JsonDeserializer<Color> {
        @Override
        public Color deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return new Color(jsonElement.getAsInt(), true);
        }

        @Override
        public JsonElement serialize(Color color, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(color.getRGB());
        }
    }
}
