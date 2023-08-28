package me.emafire003.dev.coloredglowlib.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//Code credits: isXander from YetAnotherConfigLib https://github.com/isXander/YetAnotherConfigLib

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigEntry {
}
