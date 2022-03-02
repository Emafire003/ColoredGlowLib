package me.emafire003.dev.coloredglowlib.util;

import net.minecraft.client.render.entity.feature.SheepWoolFeatureRenderer;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.*;

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

    /**
     * It will need to be called periodically to function properly*/
    public void setRainbowColor(int factor){
        int re = this.r;
        int gr = this.g;
        int bl = this.b;

        if(re == 255 && gr == 0 && (bl != 255)){
            this.b = bl+factor;
            if(this.b > 255){
                this.b = 255;
            }
            return;
        }
        if(bl == 255 && gr == 0 && (re != 0)){
            this.r = this.r - factor;
            if(this.r < 0){
                this.r = 0;
            }
            return;
        }


        if(bl == 255 && re == 0 && (gr != 255)){
            this.g = gr+factor;
            if(this.g > 255){
                this.g = 255;
            }
            return;
        }
        if(re == 0 && gr == 255 && (bl != 0)){
            this.b = this.b - factor;
            if(this.b < 0){
                this.b = 0;
            }
            return;
        }

        if(gr == 255 && bl == 0 && (re != 255)){
            this.r = re+factor;
            if(this.r > 255){
                this.r = 255;
            }
            return;
        }

        if(re == 255 && bl == 0 && (gr != 0)){
            this.g = this.g - factor;
            if(this.g < 0){
                this.g = 0;
            }
            return;
        }
    }

    public void addColor(int red, int green, int blue){
        this.r = this.r + red;
        this.g = this.g + green;
        this.b = this.b + blue;
        if(this.r > 255){
            this.r = 0+red;
        }if(this.g > 255){
            this.g = 0+green;
        }if(this.b > 255){
            this.b = 0+blue;
        }
        if(this.r < 0){
            this.r = 0;
        }if(this.g < 0){
            this.g = 0;
        }if(this.b < 0){
            this.b = 0;
        }
    }

    public void removeColor(int red, int green, int blue){
        this.r = this.r - red;
        this.g = this.g - green;
        this.b = this.b - blue;
        if(this.r > 255){
            this.r = 0+red;
        }if(this.g > 255){
            this.g = 0+green;
        }if(this.b > 255){
            this.b = 0+blue;
        }
        if(this.r < 0){
            this.r = 0;
        }if(this.g < 0){
            this.g = 0;
        }if(this.b < 0){
            this.b = 0;
        }
    }

    public static Color getWhiteColor(){
        return new Color(255, 255, 255);
    }

    public static Color getRedColor(){
        return new Color(255, 0, 0);
    }

    public static Color getBlueColor(){
        return new Color(0, 0, 255);
    }

    public static Color getGreenColor(){
        return new Color(0, 255, 0);
    }

    public static Color getSapphireColor(){
        return new Color(0, 87, 183);
    }

    public static Color getRedItalyColor(){
        return new Color(205, 33, 42);
    }

    public static Color getGreenItalyColor(){
        return new Color(0, 140, 69);
    }

    public static Color getYellowColor(){
        return new Color(255, 221, 0);
    }

    public static Color getBlackColor(){
        return new Color(0, 0, 0);
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

    public static String translateToHEX(int red, int green, int blue){
        return "#"+Integer.toHexString(red)+Integer.toHexString(green)+Integer.toHexString(blue);
    }

    public static Pattern hexColorPattern = Pattern.compile("#([0-9a-f]{3}|[0-9a-f]{6}|[0-9a-f]{8})");

    private static boolean isHexColor(String color){
        return hexColorPattern.matcher(color).matches();
    }

    public static Color translateFromHEX(String hex_color){
        if(isHexColor(hex_color)){
            hex_color = hex_color.replaceAll("#", "");
            String red = String.valueOf(hex_color.charAt(0) + +hex_color.charAt(1));
            String green = String.valueOf(hex_color.charAt(2) + +hex_color.charAt(3));
            String blue = String.valueOf(hex_color.charAt(4) + +hex_color.charAt(5));
            return new Color(Integer.parseInt(red, 16), Integer.parseInt(green, 16), Integer.parseInt(blue, 16));
        }
        return Color.getWhiteColor();

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
