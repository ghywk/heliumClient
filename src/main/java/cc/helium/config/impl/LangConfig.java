package cc.helium.config.impl;

import cc.helium.Client;
import cc.helium.config.Config;
import cc.helium.config.ConfigManager;
import cc.helium.util.lang.Languages;
import com.google.gson.JsonObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Kev1nLeft
 */

public class LangConfig extends Config {
    public LangConfig() {
        super("language", "language.json");
    }

    @Override
    public void loadConfig() {
        try {
            JsonObject jsonObject = ConfigManager.gson.fromJson(new FileReader(configFile), JsonObject.class);
            if(!jsonObject.has("languages")) {
                return;
            }
            JsonObject jsonLang = jsonObject.getAsJsonObject("languages");
            Client.getInstance().lang = Languages.valueOf(jsonLang.get("language").getAsString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveConfig() {
        try {
            final JsonObject jsonObject = new JsonObject();
            JsonObject jsonMod = new JsonObject();
            jsonMod.addProperty("language", Client.getInstance().lang.name());
            jsonObject.add("languages", jsonMod);

            try {
                PrintWriter pw = new PrintWriter(configFile);
                pw.write(ConfigManager.gson.toJson(jsonObject));
                pw.flush();
                pw.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
