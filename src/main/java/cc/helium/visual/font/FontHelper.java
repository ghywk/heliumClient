package cc.helium.visual.font;

import cc.helium.util.Util;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FontHelper implements Util {

    public static CFontRenderer verdana32;

    public static CFontRenderer sf_regular36;
    public static CFontRenderer sf_light36;
    public static CFontRenderer sf_light72;
    public static CFontRenderer sf_light200;

    public static void loadFonts() {
        try {
            verdana32 = new CFontRenderer(new Font("Verdana", Font.PLAIN, 32), true, 8);

            // InputStream stream = mc.getResourceManager().getResource(new ResourceLocation("helium/font/SF-UI-Display-Light.ttf")).getInputStream();
            InputStream stream = FontHelper.class.getResourceAsStream("/assets/minecraft/helium/font/SF-UI-Display-Light.ttf");
            if (stream == null) {
                throw new FileNotFoundException("Not found : SF-UI-Display-Light.ttf");
            }
            stream = new BufferedInputStream(stream);
            Font font = Font.createFont(Font.PLAIN, stream).deriveFont(36f);
            sf_light36 = new CFontRenderer(font, true, 8);

            Font font33 = Font.createFont(Font.PLAIN, stream).deriveFont(72f);
            sf_light72 = new CFontRenderer(font33, true, 8);

            Font font34 = Font.createFont(Font.PLAIN, stream).deriveFont(200f);
            sf_light200 = new CFontRenderer(font34, true, 8);

            InputStream stream2 = FontHelper.class.getResourceAsStream("/assets/minecraft/helium/font/SF-UI-Display-Regular.otf");
            Font font2 = Font.createFont(Font.PLAIN, stream2).deriveFont(36f);
            sf_regular36 = new CFontRenderer(font2, true, 8);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
