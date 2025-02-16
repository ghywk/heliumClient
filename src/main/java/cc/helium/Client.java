package cc.helium;

import cc.helium.config.ConfigManager;
import cc.helium.event.EventManager;
import cc.helium.module.ModuleManager;
import cc.helium.visual.clickgui.ClickGui;
import cc.helium.visual.font.FontHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Kev1nLeft
 */

public class Client {
    public String name = "Helium";
    public String version = "1.0.0";
    private static Client instance;
    public static Logger logger = LogManager.getLogger("Helium Client");

    public EventManager eventManager;
    public ModuleManager moduleManager;
    public ConfigManager configManager;

    public Client() {
        instance = this;
    }

    public void onStart() {
        logger.info("Starting.");
        eventManager = new EventManager();
        moduleManager = new ModuleManager();
        configManager = new ConfigManager();

        new ClickGui();
        FontHelper.loadFonts();
        configManager.loadConfigs();

        eventManager.register(moduleManager);
    }

    public void onShut() {
        logger.info("Shutting.");
        configManager.saveConfigs();
    }

    public static Client getInstance() {
        return instance;
    }
}