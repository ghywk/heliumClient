package cc.helium.module;

import cc.helium.util.lang.LangUtil;

/**
 * @author Kev1nLeft
 */

public enum Category {
    Combat,
    Movement,
    Player,
    Render,
    World,
    Misc;

    public String getTranslatedName() {
        return LangUtil.getTranslation("module.category." + this.name().toLowerCase());
    }
}
