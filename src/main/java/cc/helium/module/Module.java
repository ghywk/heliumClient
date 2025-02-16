package cc.helium.module;

import cc.helium.Client;
import cc.helium.util.Util;
import cc.helium.value.Value;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kev1nLeft
 */

public class Module implements Util {
    private boolean enable;

    private final String name;
    private final Category category;
    private int key;
    private String suffix = null;
    private boolean invisible = false;

    public Module(String name, int key, Category category) {
        this.name = name;
        this.key = key;
        this.category = category;
    }

    /**
     * 第二个允许的、管理该模块开关状态的方法。
     * <p>该方法为调转开关至其相反状态。
     */
    public void toggle() {
        this.setEnable(!enable);
    }

    public boolean isEnable() {
        return enable;
    }

    /**
     * 第一个允许的、管理该模块开关状态的方法。
     * @param enable 开启 / 关闭
     */
    public void setEnable(boolean enable) {
        this.enable = enable;

        if (this.enable) {
            Client.getInstance().eventManager.register(this);
            this.onEnable();
        } else {
            Client.getInstance().eventManager.unregister(this);
            this.onDisable();
        }
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public Value getValue(final String valueName) {
        for (final Field field : getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                final Object o = field.get(this);
                if(o instanceof Value) {
                    final Value value = (Value) o;
                    if (value.getName().equalsIgnoreCase(valueName)) return value;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public List<Value> getValues() {
        final List<Value> values = new ArrayList<>();
        for (final Field field : getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                final Object o = field.get(this);
                if (o instanceof Value) values.add((Value) o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return values;
    }
}
