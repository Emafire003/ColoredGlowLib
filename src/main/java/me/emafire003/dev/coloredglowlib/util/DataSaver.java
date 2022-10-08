package me.emafire003.dev.coloredglowlib.util;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import net.minecraft.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

import static me.emafire003.dev.coloredglowlib.ColoredGlowLibMod.LOGGER;

public class DataSaver {

    public static String PATH = String.valueOf(ColoredGlowLibMod.PATH+ "/" +ColoredGlowLibMod.MOD_ID + "_data.json");
    @SuppressWarnings("all")
    public static Type entityColorMapToken = new TypeToken<HashMap<UUID, String>>(){}.getType();
    @SuppressWarnings("all")
    public static Type entityTypeColorMapToken = new TypeToken<HashMap<String, String>>(){}.getType();
    @SuppressWarnings("all")
    public static Type entityRainbowListToken = new TypeToken<List<UUID>>(){}.getType();
    @SuppressWarnings("all")
    public static Type entityTypeRainbowListToken = new TypeToken<List<String>>(){}.getType();
    @SuppressWarnings("all")
    public static Type generalRainbowEnabledToken = new TypeToken<Boolean>(){}.getType();
    @SuppressWarnings("all")
    public static Type perEntityTypeToken = new TypeToken<Boolean>(){}.getType();
    @SuppressWarnings("all")
    public static Type perEntityToken = new TypeToken<Boolean>(){}.getType();
    @SuppressWarnings("all")
    public static Type overrideTeamsToken = new TypeToken<Boolean>(){}.getType();
    @SuppressWarnings("all")
    public static Type defaultColorToken = new TypeToken<String>(){}.getType();

    static Gson gson = new Gson();

    public static void createFile() {
        try {
            File datafile = new File(PATH);
            if (datafile.createNewFile()) {
                write();
            }
        } catch (IOException e) {
            LOGGER.error("There was an error trying to save the data on the file! (First time saving it)");
            e.printStackTrace();
        }
    }



    public static void write() {
        try {
            FileWriter datafileWriter = new FileWriter(PATH);
            String head = gson.toJson("ColoredGlowLib data. DO NOT TOUCH IF YOU DO NOT KNOW WHAT YOU ARE DOING") +"\n";
            String entityColorMap = gson.toJson(ColoredGlowLibMod.getLib().getEntityColorMap()) + "\n";
            String entityTypeColorMap = gson.toJson(ColoredGlowLibMod.getLib().getEntityTypeColorMap()) + "\n";
            String entityRainbowList = gson.toJson(ColoredGlowLibMod.getLib().getRainbowEntityList()) + "\n";
            String entityTypeRainbowList = gson.toJson(ColoredGlowLibMod.getLib().convertFromEntityTypeList(ColoredGlowLibMod.getLib().getRainbowEntityTypeList())) + "\n";
            /*String perEntityType = gson.toJson(ColoredGlowLibMod.getLib().getPerEntityTypeColor()) + "\n";
            String perEntity = gson.toJson(ColoredGlowLibMod.getLib().getPerEntityColor()) + "\n";
            String generalRainbow = gson.toJson(ColoredGlowLibMod.getLib().getRainbowChangingColor()) + "\n";
            String overrideTeamColors = gson.toJson(ColoredGlowLibMod.getLib().getOverrideTeamColors()) + "\n";
            */
            String defaultColor = "{" + gson.toJson(ColoredGlowLibMod.getLib().getColor().toHEX()) + "}" + "\n";

            datafileWriter.write(head);
            datafileWriter.append(entityColorMap);
            datafileWriter.append(entityTypeColorMap);
            datafileWriter.append(entityRainbowList);
            datafileWriter.append(entityTypeRainbowList);
            /*datafileWriter.append(perEntity);
            datafileWriter.append(perEntityType);
            datafileWriter.append(generalRainbow);
            datafileWriter.append(overrideTeamColors);*/
            datafileWriter.append(defaultColor);

            datafileWriter.close();
        } catch (IOException e) {
            LOGGER.error("There was an error trying to save the data on the file!");
            e.printStackTrace();
        } catch (Exception e){
            LOGGER.error("There was an error while writing on the file");
            e.printStackTrace();
        }
    }


    public static String getFileLine(int line, FileReader file) throws IOException {
        int lineNumber;
        BufferedReader readbuffer = new BufferedReader(file);
        for (lineNumber = 1; lineNumber < 10; lineNumber++) {
            if (lineNumber == line) {
                String the_line = readbuffer.readLine();
                return the_line;
            } else{
                readbuffer.readLine();
            }
        }
        return "ERROR001-NOLINEFOUND";
    }

    public static @Nullable HashMap<UUID, String> getEntityMap(){
        try {
            FileReader file = new FileReader(PATH);
            String line = getFileLine(2, file);
            if(line.equalsIgnoreCase("ERROR001-NOLINEFOUND")){
                return null;
            }
            return gson.fromJson(line, entityColorMapToken);
        } catch (IOException e) {
            LOGGER.error("There was an error trying to read the data on the file!");
            e.printStackTrace();
            return null;
        } catch (NoSuchElementException e){
            return null;
        } catch (Exception e){
            LOGGER.error("There was an error while reading on the file");
            e.printStackTrace();
            return null;
        }
    }

    public static @Nullable HashMap<EntityType, String> getEntityTypeMap(){
        try {
            FileReader file = new FileReader(PATH);
            String line = getFileLine(3, file);
            if(line.equalsIgnoreCase("ERROR001-NOLINEFOUND")){
                return null;
            }
            HashMap<String, String> map = gson.fromJson(line, entityTypeColorMapToken);
            return ColoredGlowLibMod.getLib().convertToEntityTypeMap(map);

        } catch (IOException e) {
            LOGGER.error("There was an error trying to read the data on the file!");
            e.printStackTrace();
            return null;
        } catch (NoSuchElementException e){
            return null;
        } catch (Exception e){
            LOGGER.error("There was an error while reading on the file");
            e.printStackTrace();
            return null;
        }
    }

    public static @Nullable List<UUID> getEntityRainbowList(){
        try {
            FileReader file = new FileReader(PATH);

            String line = getFileLine(4, file);
            if(line.equalsIgnoreCase("ERROR001-NOLINEFOUND")){
                return null;
            }
            return gson.fromJson(line, entityRainbowListToken);
        } catch (IOException e) {
            LOGGER.error("There was an error trying to read the data on the file!");
            e.printStackTrace();
            return null;
        } catch (NoSuchElementException e){
            return null;
        } catch (Exception e){
            LOGGER.error("There was an error while reading on the file");
            e.printStackTrace();
            return null;
        }
    }

    public static @Nullable List<EntityType> getEntityTypeRainbowList(){
        try {
            FileReader file = new FileReader(PATH);
            String line = getFileLine(5, file);
            if(line.equalsIgnoreCase("ERROR001-NOLINEFOUND")){
                return null;
            }
            List<String> list = gson.fromJson(line, entityTypeRainbowListToken);
            return ColoredGlowLibMod.getLib().convertToEntityTypeList(list);
        } catch (NoSuchElementException e){
            return null;
        } catch (IOException e) {
            LOGGER.error("There was an error trying to read the data on the file!");
            e.printStackTrace();
            return null;
        } catch (Exception e){
            LOGGER.error("There was an error while reading on the file");
            e.printStackTrace();
            return null;
        }
    }

    public static boolean getPerEntityColor(){
        try {
            FileReader file = new FileReader(PATH);
            String line = getFileLine(6, file);
            if(line.equalsIgnoreCase("ERROR001-NOLINEFOUND")){
                return ColoredGlowLibMod.getLib().getPerEntityColor();
            }
            return gson.fromJson(line, perEntityToken);
        } catch (IOException e) {
            LOGGER.error("There was an error trying to read the data on the file!");
            e.printStackTrace();
            return ColoredGlowLibMod.getLib().getPerEntityColor();
        } catch (Exception e){
            LOGGER.error("There was an error while reading on the file");
            e.printStackTrace();
            return ColoredGlowLibMod.getLib().getPerEntityColor();
        }
    }

    public static boolean getPerEntityTypeColor(){
        try {
            FileReader file = new FileReader(PATH);
            String line = getFileLine(7, file);
            if(line.equalsIgnoreCase("ERROR001-NOLINEFOUND")){
                return ColoredGlowLibMod.getLib().getPerEntityTypeColor();
            }
            return gson.fromJson(line, perEntityTypeToken);
        } catch (IOException e) {
            LOGGER.error("There was an error trying to read the data on the file!");
            e.printStackTrace();
            return ColoredGlowLibMod.getLib().getPerEntityTypeColor();
        } catch (Exception e){
            LOGGER.error("There was an error while reading on the file");
            e.printStackTrace();
            return ColoredGlowLibMod.getLib().getPerEntityTypeColor();
        }
    }

    public static boolean getRainbowEnabled(){
        try {
            FileReader file = new FileReader(PATH);
            String line = getFileLine(8, file);
            if(line.equalsIgnoreCase("ERROR001-NOLINEFOUND")){
                return ColoredGlowLibMod.getLib().getRainbowChangingColor();
            }
            return gson.fromJson(line, generalRainbowEnabledToken);
        } catch (IOException e) {
            LOGGER.error("There was an error trying to read the data on the file!");
            e.printStackTrace();
            return ColoredGlowLibMod.getLib().getRainbowChangingColor();
        } catch (Exception e){
            LOGGER.error("There was an error while reading on the file");
            e.printStackTrace();
            return ColoredGlowLibMod.getLib().getRainbowChangingColor();
        }
    }

    public static boolean getOverrideTeams(){
        try {
            FileReader file = new FileReader(PATH);
            String line = getFileLine(9, file);
            if(line.equalsIgnoreCase("ERROR001-NOLINEFOUND")){
                return ColoredGlowLibMod.getLib().getOverrideTeamColors();
            }
            return gson.fromJson(line, overrideTeamsToken);
        } catch (IOException e) {
            LOGGER.error("There was an error trying to read the data on the file!");
            e.printStackTrace();
            return ColoredGlowLibMod.getLib().getOverrideTeamColors();
        } catch (Exception e){
            LOGGER.error("There was an error while reading on the file");
            e.printStackTrace();
            return ColoredGlowLibMod.getLib().getOverrideTeamColors();
        }
    }

    public static Color getDefaultColor(){
        try {
            FileReader file = new FileReader(PATH);
            String line = getFileLine(10, file);
            if(line.equalsIgnoreCase("ERROR001-NOLINEFOUND")){
                return Color.getWhiteColor();
            }
            return Color.translateFromHEX(gson.fromJson(line, defaultColorToken));
        } catch (IOException e) {
            LOGGER.error("There was an error trying to read the data on the file!");
            e.printStackTrace();
            return ColoredGlowLibMod.getLib().getColor();
        } catch (Exception e){
            LOGGER.error("There was an error while reading on the file");
            e.printStackTrace();
            return ColoredGlowLibMod.getLib().getColor();
        }
    }
}
