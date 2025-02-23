package cc.helium.util.lang;

import cc.helium.Client;
import cc.helium.util.Util;
import cc.helium.util.logging.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Kev1nLeft
 */

public class LangUtil implements Util {
    public static String getTranslation(String key) {
        Properties properties = new Properties();
        String filePath = "/assets/minecraft/helium/lang/lang_" + Client.getInstance().lang.name().toLowerCase() + ".properties";

        try (InputStream inputStream = LangUtil.class.getResourceAsStream(filePath)) {
            if (inputStream == null) {
                LogUtil.log_error("Language file not found: " + filePath);
                return key;
            }
            properties.load(inputStream);
        } catch (IOException e) {
            LogUtil.log_error("Failed to load language file: " + filePath);
            return key;
        }

        return properties.getProperty(key, key);
    }
}
