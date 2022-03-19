package me.emafire003.dev.coloredglowlib.util;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import me.emafire003.dev.coloredglowlib.ColoredGlowLib;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static me.emafire003.dev.coloredglowlib.ColoredGlowLib.LOGGER;

public class DataSaver {

    public static String PATH = String.valueOf(FabricLoader.getInstance().getConfigDir().resolve("coloredglowlibdata.json"));
    public static Type entityColorMapToken = new TypeToken<HashMap<UUID, String>>(){}.getType();
    public static Type entityTypeColorMapToken = new TypeToken<HashMap<String, String>>(){}.getType();
    public static Type entityRainbowListToken = new TypeToken<List<UUID>>(){}.getType();
    public static Type entityTypeRainbowListToken = new TypeToken<List<String>>(){}.getType();
    public static Type generalRainbowEnabledToken = new TypeToken<Boolean>(){}.getType();
    public static Type perEntityTypeToken = new TypeToken<Boolean>(){}.getType();
    public static Type perEntityToken = new TypeToken<Boolean>(){}.getType();
    public static Type overrideTeamsToken = new TypeToken<Boolean>(){}.getType();
    public static Type defaultColorToken = new TypeToken<String>(){}.getType();

    static Gson gson = new Gson();

    /**Use this method to change the path of the
     * data file to your configuration file, otherwise
     * it might get in conflict with other instances
     * such as FabricLoader.getInstance().getConfigDir().resolve("yourmodfolder/yourmodconfig.json")
     *
     * @param path The path to set for the new config.*/
    public static void changePathTo(String path){
        PATH = path;
    }

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

    public static List<String> convertFromEntityTypeList(List<EntityType> typelist){
        List<String> list = new ArrayList<>();
        for(EntityType type : typelist){
            list.add(String.valueOf(type));
        }
        return list;
    }
    public static void write() {
        try {
            FileWriter datafileWriter = new FileWriter(String.valueOf(FabricLoader.getInstance().getConfigDir().resolve(PATH)));
            datafileWriter.write("/*ColoredGlowLib data. DO NOT TOUCH IF YOU DON'T KNOW WHAT YOU ARE DOING*/\n");
            String entityColorMap = gson.toJson(ColoredGlowLib.getEntityColorMap()) + "\n";
            String entityTypeColorMap = gson.toJson(ColoredGlowLib.getEntityTypeColorMap()) + "\n";
            String entityRainbowList = gson.toJson(ColoredGlowLib.getRainbowEntityList()) + "\n";
            String entityTypeRainbowList = gson.toJson(convertFromEntityTypeList(ColoredGlowLib.getRainbowEntityTypeList())) + "\n";
            String perEntityType = gson.toJson(ColoredGlowLib.getPerEntityTypeColor()) + "\n";
            String perEntity = gson.toJson(ColoredGlowLib.getPerEntityColor()) + "\n";
            String generalRainbow = gson.toJson(ColoredGlowLib.getRainbowChangingColor()) + "\n";
            String overrideTeamColors = gson.toJson(ColoredGlowLib.getOverrideTeamColors()) + "\n";
            String defaultColor = gson.toJson(ColoredGlowLib.getColor().toHEX()) + "\n";

            datafileWriter.append(entityColorMap);
            datafileWriter.append(entityTypeColorMap);
            datafileWriter.append(entityRainbowList);
            datafileWriter.append(entityTypeRainbowList);
            datafileWriter.append(perEntity);
            datafileWriter.append(perEntityType);
            datafileWriter.append(generalRainbow);
            datafileWriter.append(overrideTeamColors);
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
            FileReader file = new FileReader(String.valueOf(FabricLoader.getInstance().getConfigDir().resolve(PATH)));
            String line = getFileLine(2, file);
            if(line.equalsIgnoreCase("ERROR001-NOLINEFOUND")){
                return gson.fromJson("{}", entityColorMapToken);
            }
            return gson.fromJson(line, entityColorMapToken);
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

    public static @Nullable HashMap<EntityType, String> getEntityTypeMap(){
        try {
            FileReader file = new FileReader(String.valueOf(FabricLoader.getInstance().getConfigDir().resolve(PATH)));
            String line = getFileLine(3, file);
            if(line.equalsIgnoreCase("ERROR001-NOLINEFOUND")){
                return gson.fromJson("{}", entityTypeColorMapToken);
            }
            HashMap<String, String> map = gson.fromJson(line, entityTypeColorMapToken);
            HashMap<EntityType, String> return_map = new HashMap<>();
            for ( String key : map.keySet() ) {
                return_map.put(EntityType.get(key).get(), map.get(key));
            }
            return return_map;

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

    public static @Nullable List<UUID> getEntityRainbowList(){
        try {
            FileReader file = new FileReader(String.valueOf(FabricLoader.getInstance().getConfigDir().resolve(PATH)));

            String line = getFileLine(4, file);
            if(line.equalsIgnoreCase("ERROR001-NOLINEFOUND")){
                return gson.fromJson("{}", entityRainbowListToken);
            }
            return gson.fromJson(line, entityRainbowListToken);
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

    public static @Nullable List<EntityType> getEntityTypeRainbowList(){
        try {
            FileReader file = new FileReader(String.valueOf(FabricLoader.getInstance().getConfigDir().resolve(PATH)));
            String line = getFileLine(5, file);
            if(line.equalsIgnoreCase("ERROR001-NOLINEFOUND")){
                return gson.fromJson("{}", entityTypeRainbowListToken);
            }
            List<String> list = gson.fromJson(line, entityTypeRainbowListToken);
            List<EntityType> return_list = new ArrayList<>();
            for ( String element : list ) {
                return_list.add(EntityType.get(element).get());
            }
            return return_list;
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
            FileReader file = new FileReader(String.valueOf(FabricLoader.getInstance().getConfigDir().resolve(PATH)));
            String line = getFileLine(6, file);
            if(line.equalsIgnoreCase("ERROR001-NOLINEFOUND")){
                return gson.fromJson("{}", perEntityToken);
            }
            return gson.fromJson(line, perEntityToken);
        } catch (IOException e) {
            LOGGER.error("There was an error trying to read the data on the file!");
            e.printStackTrace();
            return ColoredGlowLib.getPerEntityColor();
        } catch (Exception e){
            LOGGER.error("There was an error while reading on the file");
            e.printStackTrace();
            return ColoredGlowLib.getPerEntityColor();
        }
    }

    public static boolean getPerEntityTypeColor(){
        try {
            FileReader file = new FileReader(String.valueOf(FabricLoader.getInstance().getConfigDir().resolve(PATH)));
            String line = getFileLine(7, file);
            if(line.equalsIgnoreCase("ERROR001-NOLINEFOUND")){
                return gson.fromJson("{}", perEntityTypeToken);
            }
            return gson.fromJson(line, perEntityTypeToken);
        } catch (IOException e) {
            LOGGER.error("There was an error trying to read the data on the file!");
            e.printStackTrace();
            return ColoredGlowLib.getPerEntityTypeColor();
        } catch (Exception e){
            LOGGER.error("There was an error while reading on the file");
            e.printStackTrace();
            return ColoredGlowLib.getPerEntityTypeColor();
        }
    }

    public static boolean getRainbowEnabled(){
        try {
            FileReader file = new FileReader(String.valueOf(FabricLoader.getInstance().getConfigDir().resolve(PATH)));
            String line = getFileLine(8, file);
            if(line.equalsIgnoreCase("ERROR001-NOLINEFOUND")){
                return gson.fromJson("{}", generalRainbowEnabledToken);
            }
            return gson.fromJson(line, generalRainbowEnabledToken);
        } catch (IOException e) {
            LOGGER.error("There was an error trying to read the data on the file!");
            e.printStackTrace();
            return ColoredGlowLib.getRainbowChangingColor();
        } catch (Exception e){
            LOGGER.error("There was an error while reading on the file");
            e.printStackTrace();
            return ColoredGlowLib.getRainbowChangingColor();
        }
    }

    public static boolean getOverrideTeams(){
        try {
            FileReader file = new FileReader(String.valueOf(FabricLoader.getInstance().getConfigDir().resolve(PATH)));
            String line = getFileLine(9, file);
            if(line.equalsIgnoreCase("ERROR001-NOLINEFOUND")){
                return gson.fromJson("{}", overrideTeamsToken);
            }
            return gson.fromJson(line, overrideTeamsToken);
        } catch (IOException e) {
            LOGGER.error("There was an error trying to read the data on the file!");
            e.printStackTrace();
            return ColoredGlowLib.getOverrideTeamColors();
        } catch (Exception e){
            LOGGER.error("There was an error while reading on the file");
            e.printStackTrace();
            return ColoredGlowLib.getOverrideTeamColors();
        }
    }

    public static Color getDefaultColor(){
        try {
            FileReader file = new FileReader(String.valueOf(FabricLoader.getInstance().getConfigDir().resolve(PATH)));
            String line = getFileLine(10, file);
            if(line.equalsIgnoreCase("ERROR001-NOLINEFOUND")){
                return Color.translateFromHEX(gson.fromJson("{}", defaultColorToken));
            }
            return Color.translateFromHEX(gson.fromJson(line, defaultColorToken));
        } catch (IOException e) {
            LOGGER.error("There was an error trying to read the data on the file!");
            e.printStackTrace();
            return ColoredGlowLib.getColor();
        } catch (Exception e){
            LOGGER.error("There was an error while reading on the file");
            e.printStackTrace();
            return ColoredGlowLib.getColor();
        }
    }
}
