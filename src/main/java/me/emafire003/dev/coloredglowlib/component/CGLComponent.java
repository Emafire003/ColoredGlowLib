package me.emafire003.dev.coloredglowlib.component;

import net.minecraft.nbt.NbtCompound;

public interface CGLComponent {
    void readFromNbt(NbtCompound tag);
    void writeToNbt(NbtCompound tag);
}
