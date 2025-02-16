package cc.helium.value.impl;

import cc.helium.value.Value;

/**
 * @author Kev1nLeft
 */

public class BoolValue extends Value<Boolean> {
    public BoolValue(String name, Boolean value) {
        super(name);
        this.setValue(value);
    }

    public BoolValue(String name, Boolean value, ShownDependency dependency) {
        super(name, dependency);
        this.setValue(value);
    }
}
