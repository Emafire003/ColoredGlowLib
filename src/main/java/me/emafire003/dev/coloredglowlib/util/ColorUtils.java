package me.emafire003.dev.coloredglowlib.util;

import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import me.emafire003.dev.coloredglowlib.custom_data_animations.CustomColorAnimation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class ColorUtils {

    public static final String WHITE = "#ffffff";
    public static final String RED = "#ff0000";
    public static final String GREEN = "#00ff00";
    public static final String BLUE = "#0000ff";
    public static final String BLACK = "#000000";

    /**Checks if a given String represents a Color in Hexadecimal format.
     * The string can start with or without "#"
     *
     * @param str The supposed string color to check
     * @return Returns weather teh givin string is a color or not
     * */
    public static boolean isHexColor(String str) {
        if(!str.startsWith("#")){
            str = "#" + str;
        }
        // Define the regular expression pattern for a valid hexadecimal color code
        // It matches either a 6-character or 3-character code, preceded by a #
        Pattern hexaPattern = Pattern.compile("^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$");
        // Create a Matcher object to match the input against the pattern
        Matcher matcher = hexaPattern.matcher(str);

        // Return true if the input matches the pattern, otherwise false
        return matcher.matches();
    }

    //TODO should work but needs testing
    /** Converts RGB decimal numbers into a single Color value
     *
     * @param r The red RGB value (0-255)
     * @param g The green RGB value (0-255)
     * @param b The blue RGB value (0-255)
     * */
    public static int toColorValue(int r, int g, int b){
        return (r << 16) | (g << 8) | (b);
    }

    //TODO should work but needs testing
    /** Converts a given string to an integer Color value
     * The string is checked with isHexColor to confirm
     * it's indeed a color in hex string format.
     * The string can start with or without "#"
     *
     * @param hex The string representing the color in hexadecimal format
     * @return Returns the color value corresponding to the string, or black if the string was not a color
     * */
    public static int toColorValue(String hex){
        if(!isHexColor(hex)){
            return toColorValue(0, 0, 0);
        }
        if(!hex.startsWith("#")){
            hex = "#"+ hex;
        }
        return Integer.decode(hex);
    }

    /** Converts a given integer Color value to an */
    public static String toHex(int colorValue){
        return "#"+Integer.toHexString(colorValue).substring(2);
    }

    public static String toHex(int r, int g, int b){
        return "#"+Integer.toHexString(toColorValue(r,g,b)).substring(2);
    }

    public static boolean checkDefault(String color){
        return (color.equalsIgnoreCase("#ffffff") || color.equalsIgnoreCase("ffffff"));
    }

    public static boolean checkSameColor(String color, String color1){
        if(!color.startsWith("#")){
            color = "#"+color;
        }
        if(!color1.startsWith("#")){
            color1 = "#"+color1;
        }
        return color.equalsIgnoreCase(color1);
    }

    //TODO untested
    public static int[] toRGB(String color){
        if(isHexColor(color)){
            if(color.startsWith("#")){
                color = color.replaceAll("#", "");
            }
            int r = Integer.valueOf(color.substring(0, 2), 16);
            int g = Integer.valueOf(color.substring(2, 4), 16);
            int b = Integer.valueOf(color.substring(4, 6), 16);
            return new int[]{r, g, b};
        }
        return new int[]{-1, -1, -1};
    }


    public static class RainbowChanger{
        int r = 0;
        int g = 0;
        int b = 0;

        public RainbowChanger(int r, int g, int b){
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public int getColorValue(){
            return ColorUtils.toColorValue(r,g,b);
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
    }

    /**Checks is a given string is actually a valid color
     *<p>
     * It's here instead of in the API because it is sometimes needed before the server loads
     * */
    public static boolean isValidColor(String color){
        if(color.startsWith("#")){
            color = color.replaceAll("#", "");
        }
        return (ColorUtils.isHexColor(color) || color.equalsIgnoreCase("rainbow") || color.equalsIgnoreCase("random"));
    }

    /**Checks is a given string is actually a valid color
     *<p>
     * It's here instead of in the API because it is sometimes needed before the server loads
     * */
    public static boolean isValidColorOrCustom(String color){
        if(color.startsWith("#")){
            color = color.replaceAll("#", "");
        }
        return isValidColor(color) || isCustomAnimation(color);
    }

    /** Checks if a given color is a custom animation added by a datapack */
    public static boolean isCustomAnimation(String color){
        for(CustomColorAnimation animation : ColoredGlowLibMod.getCustomColorAnimations()){
            if(color.equalsIgnoreCase(animation.getName())){
                return true;
            }
        }
        return false;
    }

}
