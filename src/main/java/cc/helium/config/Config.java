package cc.helium.config;

import java.io.File;

public abstract class Config {
    public String name;
    protected File configFile;

    public Config(String configName, String fileName) {
        this.name = configName;
        this.configFile = new File(ConfigManager.rootDir, fileName);
    }

    public abstract void loadConfig();

    public abstract void saveConfig();
}
