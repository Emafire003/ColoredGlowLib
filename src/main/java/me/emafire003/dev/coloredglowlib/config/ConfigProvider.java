package me.emafire003.dev.coloredglowlib.config;

import com.mojang.datafixers.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class ConfigProvider implements SimpleConfig.DefaultConfig {

    private String configContents = "";

    @SuppressWarnings("all")
    public List<Pair> getConfigsList() {
        return configsList;
    }

    @SuppressWarnings("all")
    private final List<Pair> configsList = new ArrayList<>();

    public void addKeyValuePair(Pair<String, ?> keyValuePair, String comment) {
        configsList.add(keyValuePair);
        configContents += keyValuePair.getFirst() + ":" + keyValuePair.getSecond() + " #"
                + comment + " | default= " + keyValuePair.getSecond() +  " | type= " + keyValuePair.getSecond().getClass().getSimpleName() +"\n";
    }

    @Override
    public String get(String namespace) {
        return configContents;
    }
}
