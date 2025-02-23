package cc.helium.config.impl;

import cc.helium.Client;
import cc.helium.config.Config;
import cc.helium.config.ConfigManager;
import cc.helium.module.Module;
import cc.helium.util.logging.LogUtil;
import cc.helium.value.Value;
import cc.helium.value.impl.BoolValue;
import cc.helium.value.impl.ModeValue;
import cc.helium.value.impl.NumberValue;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

public class ModuleConfig extends Config {
    public ModuleConfig() {
        super("module", "module.json");
    }

    @Override
    public void loadConfig() {
        if (!configFile.exists()) return;
        try {
            JsonObject config = ConfigManager.gson.fromJson(new FileReader(configFile), JsonObject.class);
            for (Module module : Client.getInstance().moduleManager.getModules()) {
                if (!config.has(module.getName())) continue;
                JsonObject moduleObject = config.get(module.getName()).getAsJsonObject();

                if (moduleObject.has("enabled") && module.isEnable() != moduleObject.get("enabled").getAsBoolean()) module.toggle();
                if (moduleObject.has("key")) module.setKey(moduleObject.get("key").getAsInt());

                if (!moduleObject.has("value")) continue;
                JsonObject valueObject = moduleObject.get("value").getAsJsonObject();

                for (Value<?> value : module.getValues()) {
                    if (!valueObject.has(value.getName())) continue;
                    JsonElement valueElement = valueObject.get(value.getName());

                    if (value instanceof BoolValue) {
                        BoolValue boolValue = (BoolValue) value;
                        boolValue.setValue(valueElement.getAsBoolean());
                    } else if (value instanceof NumberValue) {
                        NumberValue numberValue = (NumberValue) value;
                        numberValue.setValue(valueElement.getAsDouble());
                    } else if (value instanceof ModeValue) {
                        ModeValue modeValue = (ModeValue) value;
                        modeValue.setValue(valueElement.getAsString());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveConfig() {
        JsonObject config = new JsonObject();

        for (Module module : Client.getInstance().moduleManager.getModules()) {
            JsonObject moduleObject = new JsonObject();
            moduleObject.addProperty("enabled", module.isEnable());
            moduleObject.addProperty("key", module.getKey());

            JsonObject valueObject = getJsonObject(module);
            moduleObject.add("value", valueObject);
            config.add(module.getName(), moduleObject);
        }

        try {
            PrintWriter pw = new PrintWriter(configFile);
            pw.write(ConfigManager.gson.toJson(config));
            pw.flush();
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static JsonObject getJsonObject(Module module) {
        JsonObject valueObject = new JsonObject();

        for (Value<?> value : module.getValues()) {
            if (value instanceof BoolValue) {
                BoolValue boolValue = (BoolValue) value;
                valueObject.addProperty(boolValue.getName(), boolValue.getValue());
            } else if (value instanceof NumberValue) {
                NumberValue numberValue = (NumberValue) value;
                valueObject.addProperty(numberValue.getName(), numberValue.getValue());
            } else if (value instanceof ModeValue) {
                ModeValue modeValue = (ModeValue) value;
                valueObject.addProperty(modeValue.getName(), modeValue.getValue());
            }
        }
        return valueObject;
    }
}
