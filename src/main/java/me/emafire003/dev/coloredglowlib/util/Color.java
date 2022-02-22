package me.emafire003.dev.coloredglowlib.util;

public class Color {
    public int r;
    public int g;
    public int b;

    public Color(int red, int green, int blue){
        this.r = red;
        this.g = green;
        this.b = blue;
    }

    public Color(int colorvalue){
        this.r = colorvalue >> 16 & 255;
        this.g = colorvalue >> 8 & 255;
        this.b = colorvalue & 255;
    }

    public void setColor(int red, int green, int blue){
        this.r = red;
        this.g = green;
        this.b = blue;
    }

    public static Color translateFromColorValue(int colorvalue){
        int k = colorvalue >> 16 & 255;
        int l = colorvalue >> 8 & 255;
        int m = colorvalue & 255;
        return new Color(k,l,m);
    }

    public static int translateToColorValue(int r, int g, int b){
        return (r << 16) | (g << 8) | (b);
    }

    public int getColorValue(){
        return (r << 16) | (g << 8) | (b);
    }

    public void setColorValue(int value){
        Color color = translateFromColorValue(value);
        r = color.r;
        g = color.g;
        b = color.b;
    }

    public Color getColor(){
        return this;
    }

    public int getRed(){
        return r;
    }

    public int getGreen(){
        return g;
    }

    public int getBlue(){
        return b;
    }
}
