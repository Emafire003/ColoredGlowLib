package me.emafire003.dev.coloredglowlib.custom_data_animations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class ColorAnimationItem {

    public static final Codec<ColorAnimationItem> CODEC = RecordCodecBuilder.create(
            instance ->
                    instance.group(Codec.STRING.fieldOf("color")
                                            .forGetter(ColorAnimationItem::getColor),
                                    Codec.INT.fieldOf("active_for")
                                            .forGetter(ColorAnimationItem::getActiveFor)
                            )
                            .apply(instance, ColorAnimationItem::new));

    private String color;
    private int active_for;


    public ColorAnimationItem(String color, int active_for){
        this.color = color;
        this.active_for = active_for;
    }

    public String getColor() {
        return color;
    }

    public int getActiveFor() {
        return active_for;
    }
}
