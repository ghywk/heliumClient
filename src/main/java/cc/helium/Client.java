package cc.helium;

import cc.helium.config.ConfigManager;
import cc.helium.event.EventManager;
import cc.helium.module.ModuleManager;
import cc.helium.util.lang.Languages;
import cc.helium.util.viafix.Fixer;
import cc.helium.visual.clickgui.ClickGui;
import de.florianmichael.viamcp.ViaMCP;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Kev1nLeft
 */

public class Client {
    public String name = "Helium";
    public String version = "1.0.1";
    public String edit = "pre4";
    private static Client instance;
    public static Logger logger = LogManager.getLogger("Helium Client");

    public EventManager eventManager;
    public ModuleManager moduleManager;
    public ConfigManager configManager;

    public Languages lang;

    public Client() {
        instance = this;
    }

    public void onStart() {
        logger.info("Starting.");
        eventManager = new EventManager();
        moduleManager = new ModuleManager();
        configManager = new ConfigManager();

        new ClickGui();
        new Fixer();
        configManager.loadConfigs();

        eventManager.register(moduleManager);

        this.startViaMCP();
    }

    public void onShut() {
        logger.info("Shutting.");
        configManager.saveConfigs();
    }

    private void startViaMCP() {
        try {
            ViaMCP.create();
            ViaMCP.INSTANCE.initAsyncSlider();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Client getInstance() {
        return instance;
    }
}