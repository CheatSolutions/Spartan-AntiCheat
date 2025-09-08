package kireiko.dev.millennium.types;

import lombok.Data;

@Data
public final class Vec2f {

    private float x, y;

    public Vec2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2f(Number x, Number y) {
        this.x = x.floatValue();
        this.y = y.floatValue();
    }

    public double distance(Vec2f vector2) {
        return Math.sqrt(Math.pow(this.x - vector2.x, 2) + Math.pow(this.y - vector2.y, 2));
    }

    public Vec2f add(Vec2f vector2) {
        return new Vec2f(this.x + vector2.x, this.y + vector2.y);
    }

    public Vec2f subtract(Vec2f vector2) {
        return new Vec2f(this.x - vector2.x, this.y - vector2.y);
    }

    public Vec2f scale(double factor) {
        return new Vec2f(this.x * factor, this.y * factor);
    }

    public boolean compare(Vec2f vector2) {
        return this.x == vector2.x && this.y == vector2.y;
    }
}
