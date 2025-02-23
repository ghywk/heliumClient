package cc.helium.visual.font;

import cc.helium.visual.font.impl.UFontRenderer;

import java.util.HashMap;

public class FontManager {
    private static final HashMap<Integer, UFontRenderer> sf_lightMap = new HashMap<>();
    private static final HashMap<Integer, UFontRenderer> verdanaMap = new HashMap<>();
    private static final HashMap<Integer, UFontRenderer> arialMap = new HashMap<>();

    public static UFontRenderer sf_light20 = SF_UI_Display_Light(20);
    public static UFontRenderer sf_light18 = SF_UI_Display_Light(18);

    public static UFontRenderer verdana18 = Verdana(18);

    public static UFontRenderer arial18 = Arial(18);

    private static UFontRenderer getRenderer(String name, int size, HashMap<Integer, UFontRenderer> map) {
        if (map.containsKey(size)) {
            return map.get(size);
        }
        UFontRenderer newRenderer = new UFontRenderer(name, size);
        map.put(size, newRenderer);
        return newRenderer;
    }

    public static UFontRenderer SF_UI_Display_Light(int size) {
        return getRenderer("SF-UI-Display-Light", size, sf_lightMap);
    }

    public static UFontRenderer Verdana(int size) {
        return getRenderer("Verdana", size, verdanaMap);
    }

    public static UFontRenderer Arial(int size) {
        return getRenderer("Arial", size, arialMap);
    }
}
