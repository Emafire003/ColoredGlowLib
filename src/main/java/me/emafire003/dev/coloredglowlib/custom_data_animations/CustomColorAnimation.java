package me.emafire003.dev.coloredglowlib.custom_data_animations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import me.emafire003.dev.coloredglowlib.networking.ListedPacketCodecs;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

import java.util.ArrayList;
import java.util.List;

public class CustomColorAnimation {

    public static final Codec<CustomColorAnimation> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.STRING.fieldOf("name").forGetter(CustomColorAnimation::getName),
                            ColorAnimationItem.CODEC.listOf().fieldOf("colors").forGetter(CustomColorAnimation::getColorAnimations))
                    .apply(instance, CustomColorAnimation::new)
    );

    public static final PacketCodec<ByteBuf, CustomColorAnimation> PACKET_CODEC = new PacketCodec<>() {
        @Override
        public CustomColorAnimation decode(ByteBuf buf) {
            return new CustomColorAnimation(PacketCodecs.STRING.decode(buf), ListedPacketCodecs.ANIMATION_ITEMS.decode(buf));
        }

        @Override
        public void encode(ByteBuf buf, CustomColorAnimation value) {
            PacketCodecs.STRING.encode(buf, value.name);
            ListedPacketCodecs.ANIMATION_ITEMS.encode(buf, value.colors);
        }
    };


    private final String name;
    private final List<ColorAnimationItem> colors;

    private int current_ticks = 0;
    private int color_index = 0;

    public int getCurrentTicks(){
        return this.current_ticks;
    }

    public void setCurrentTicks(int ticks){
        this.current_ticks = ticks;
    }

    public int getCurrentColorIndex(){
        return color_index;
    }

    public void setCurrentColorIndex(int index){
        this.color_index = index;
    }


    public CustomColorAnimation(String id, List<ColorAnimationItem> entries) {
        //super(CreativeModeTabs.TABS[getIndexFromString(id)], null);
        this.name = id;
        this.colors = new ArrayList<>(entries);
    }

    /**
     * Warnign! Ignores case!*/
    public String getName() {
        return name;
    }

    public List<ColorAnimationItem> getColorAnimations() {
        return colors;
    }

}
