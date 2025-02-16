package cc.helium.config.impl;

import cc.helium.config.Config;
import cc.helium.config.ConfigManager;
import cc.helium.util.logging.LogUtil;
import cc.helium.visual.clickgui.ClickGui;
import cc.helium.visual.clickgui.component.Frame;
import com.google.gson.*;

import java.io.*;

public class ClickGuiConfig extends Config {
    public ClickGuiConfig() {
        super("clickGui", "clickGui.json");
    }

    public void saveConfig() {
        try {
            final JsonObject jsonObject = new JsonObject();
            for (Frame frame : ClickGui.frames) {
                JsonObject jsonMod = new JsonObject();
                jsonMod.addProperty("posX", frame.getX());
                jsonMod.addProperty("posY", frame.getY());
                jsonMod.addProperty("open", frame.isOpen());
                jsonObject.add(frame.category.name(), jsonMod);
            }

            try {
                PrintWriter pw = new PrintWriter(configFile);
                pw.write(ConfigManager.gson.toJson(jsonObject));
                pw.flush();
                pw.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                LogUtil.log_error("Failed to write " + name + " config.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadConfig() {
        try {
            JsonObject jsonObject = ConfigManager.gson.fromJson(new FileReader(configFile), JsonObject.class);
            for (Frame frame : ClickGui.frames) {
                if(!jsonObject.has(frame.category.name())) {
                    continue;
                }
                JsonObject jsonPanel = jsonObject.getAsJsonObject(frame.category.name());
                frame.setX(jsonPanel.get("posX").getAsInt());
                frame.setY(jsonPanel.get("posY").getAsInt());
                frame.setOpen(jsonPanel.get("open").getAsBoolean());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
