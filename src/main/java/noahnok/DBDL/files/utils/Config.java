package noahnok.dbdl.files.utils;

import noahnok.dbdl.files.DeadByDaylight;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Config {

    private final DeadByDaylight plugin;

    private final File config;
    private final FileConfiguration configConf;
    private final String name;

    public Config(String name, DeadByDaylight main) {
        this.plugin = main;

        config = new File(plugin.getDataFolder(), name + ".yml");

        if (!config.exists()) {
            config.getParentFile().mkdir();
            plugin.saveResource(name + ".yml", false);
        }

        configConf = new YamlConfiguration();
        try {
            configConf.load(config);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        this.name = name;
    }

    public FileConfiguration getConfig() {
        return configConf;
    }

    public void saveConfig() {
        try {
            configConf.save(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        if (!config.exists()) {
            config.getParentFile().mkdir();
            plugin.saveResource(name + ".yml", false);
        }
        try {
            configConf.load(config);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
