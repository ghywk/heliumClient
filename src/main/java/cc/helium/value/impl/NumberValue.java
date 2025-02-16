package cc.helium.value.impl;

import cc.helium.value.Value;

/**
 * @author Kev1nLeft
 */

public class NumberValue extends Value<Double> {
    private final double max;
    private final double min;
    private final double inc;

    public NumberValue(String name, double val, double min, double max, double inc) {
        super(name);
        this.setValue(val);
        this.max = max;
        this.min = min;
        this.inc = inc;
    }

    public NumberValue(String name, double val, double min, double max, double inc, ShownDependency dependency) {
        super(name, dependency);
        this.setValue(val);
        this.max = max;
        this.min = min;
        this.inc = inc;
    }

    public Double getMax() {
        return this.max;
    }

    public Double getMin() {
        return this.min;
    }

    public Double getInc() {
        return this.inc;
    }
}
