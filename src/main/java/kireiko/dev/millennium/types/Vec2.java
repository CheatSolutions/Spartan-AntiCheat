package kireiko.dev.millennium.types;

import lombok.Data;

@Data
public final class Vec2 {

    private double x, y;

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2(Number x, Number y) {
        this.x = x.doubleValue();
        this.y = y.doubleValue();
    }

    public double distance(Vec2 vector2) {
        return Math.sqrt(Math.pow(this.x - vector2.x, 2) + Math.pow(this.y - vector2.y, 2));
    }

    public Vec2 add(Vec2 vector2) {
        return new Vec2(this.x + vector2.x, this.y + vector2.y);
    }

    public Vec2 subtract(Vec2 vector2) {
        return new Vec2(this.x - vector2.x, this.y - vector2.y);
    }

    public Vec2 scale(double factor) {
        return new Vec2(this.x * factor, this.y * factor);
    }

    public boolean compare(Vec2 vector2) {
        return this.x == vector2.x && this.y == vector2.y;
    }
}
