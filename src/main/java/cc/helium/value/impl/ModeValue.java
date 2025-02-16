package cc.helium.value.impl;

import cc.helium.value.Value;

/**
 * @author Kev1nLeft
 */

public class ModeValue extends Value<String> {
    private final String[] modes;

    public ModeValue(String name, String value, String... modes) {
        super(name);
        this.modes = modes;
        this.setValue(value);
    }

    public ModeValue(String name, ShownDependency dependency, String value, String... modes) {
        super(name, dependency);
        this.modes = modes;
        this.setValue(value);
    }

    public boolean is(String string) {
        return this.getValue().equalsIgnoreCase(string);
    }

    @Override
    public void setValue(String mode2) {
        for (String e : this.modes) {
            if (e == null) {
                return;
            }
            if (!e.equalsIgnoreCase(mode2)) continue;
            this.value = e;
        }
    }

    public void next() {
        int index = arrayIndex(modes, getValue()) + 1;
        if (index == modes.length) {
            setValue(modes[0]);
            return;
        }
        setValue(modes[index]);
    }

    public int arrayIndex(String[] array, String target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(target)) return i;
        }

        throw new IllegalStateException("Array: " + target);
    }

    public String[] getModes() {
        return modes;
    }
}
