package cc.helium.value;

/**
 * @author Kev1nLeft
 */

public abstract class Value<T> {
    protected T value;
    protected final String name;
    protected final ShownDependency dependency;

    public Value(String name, ShownDependency dependency) {
        this.name = name;
        this.dependency = dependency;
    }

    public Value(String name) {
        this.name = name;
        this.dependency = () -> Boolean.TRUE;
    }

    public boolean isDisplayable() {
        return this.dependency != null && this.dependency.shouldShown();
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    @FunctionalInterface
    public interface ShownDependency {
        boolean shouldShown();
    }
}
