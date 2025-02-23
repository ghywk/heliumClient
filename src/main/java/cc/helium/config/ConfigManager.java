package cc.helium.config;

import cc.helium.config.impl.ClickGuiConfig;
import cc.helium.config.impl.LangConfig;
import cc.helium.config.impl.ModuleConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    protected static final Minecraft mc = Minecraft.getMinecraft();
    public static final File rootDir = new File(mc.mcDataDir, "Helium");
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final List<Config> configList;

    public ConfigManager() {
        configList = new ArrayList<>();
        if (!rootDir.exists()) rootDir.mkdir();
    }

    public Config getConfig(String name) {
        for (Config config : configList) {
            if (config.name.equals(name)) return config;
        }

        throw new RuntimeException("Config not found: " + name);
    }

    public Config getConfig(Class<? extends Config> klass) {
        for (Config config : configList) {
            if (config.getClass() == klass) return config;
        }

        throw new RuntimeException("Config not found: " + klass.getName());
    }

    private void registerConfigs() {
        configList.add(new ModuleConfig());
        configList.add(new ClickGuiConfig());
        configList.add(new LangConfig());
    }

    public void loadConfigs() {
        this.registerConfigs();

        configList.forEach(Config::loadConfig);
    }

    public void saveConfigs() {
        configList.forEach(it -> {
            if (!it.configFile.exists()) {
                try {
                    it.configFile.createNewFile();
                } catch (IOException e) {
                    throw new IllegalStateException("Can't save any config now!");
                }
            }
        });
        configList.forEach(Config::saveConfig);
    }
}
