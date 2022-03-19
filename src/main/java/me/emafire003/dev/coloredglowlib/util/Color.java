package me.emafire003.dev.coloredglowlib.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static me.emafire003.dev.coloredglowlib.ColoredGlowLib.LOGGER;

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

    public int toColorValue(){
        return translateToColorValue(this.r, this.g, this.b);
    }

    //TODO for some reason a color like af0fab does not work
    //TODO also for the per world file save problem i can use a dir and then thorw in there a file for every wiworld
    public static String translateToHEX(int red, int green, int blue){
        String hexcolor;
        if(red <= 9){
            hexcolor = "#"+0+Integer.toHexString(red)+Integer.toHexString(green)+Integer.toHexString(blue);
        }else if(green <= 9){
            hexcolor = "#"+Integer.toHexString(red)+0+Integer.toHexString(green)+Integer.toHexString(blue);
        }else if(blue <= 9){
            hexcolor = "#"+Integer.toHexString(red)+Integer.toHexString(green)+0+Integer.toHexString(blue);
        }else {
            hexcolor = "#"+Integer.toHexString(red)+Integer.toHexString(green)+Integer.toHexString(blue);
        }
        //LOGGER.debug(hexcolor);
        return hexcolor;
    }

    public String toHEX(){
        return Color.translateToHEX(this.r, this.g, this.b);
    }

    public static boolean isHexColor(String str)
    {
        // Regex to check valid hexadecimal color code.
        String regex = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the string is empty
        // return false
        if (str == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given string
        // and regular expression.
        Matcher m = p.matcher(str);

        // Return if the string
        // matched the ReGex
        return m.matches();
    }

    public static Color translateFromHEX(String hex_color){
        if(isHexColor(hex_color)){
            return Color.translateFromColorValue(Integer.decode(hex_color).intValue());
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
