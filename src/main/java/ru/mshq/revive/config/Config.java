package ru.mshq.revive.config;


import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
    private Integer removeHealth = 2;
    private Integer addHealth = 2;
    private Integer maxHealth = 20;
    private Integer minHealth = 2;

    public static Config load(Path configPath) {
        Config config;
        if (Files.exists(configPath)) {
            config = new Toml().read(configPath.toFile()).to(Config.class);
        } else {
            config = new Config();
        }

        File configFile = configPath.toFile();
        try {
            Files.createDirectories(configPath.getParent());
            new TomlWriter().write(config, configFile);
        } catch (IOException e) {
            LogManager.getLogger().error("Init config failed.", e);
        }

        return config;
    }

    public Integer getAddHealth() {
        return addHealth;
    }

    public Integer getRemoveHealth() {
        return removeHealth;
    }

    public Integer getMaxHealth() {
        return maxHealth;
    }

    public Integer getMinHealth() {
        return minHealth;
    }
}
