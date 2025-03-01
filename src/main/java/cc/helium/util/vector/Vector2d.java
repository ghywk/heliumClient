package cc.helium.util.vector;

public final class Vector2d {
    public double x, y;

    public Vector2d() {
    }

    public Vector2d(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2d offset(final double x, final double y) {
        return new Vector2d(this.x + x, this.y + y);
    }

    public Vector2d offset(Vector2d xy) {
        return offset(xy.x, xy.y);
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }
}
