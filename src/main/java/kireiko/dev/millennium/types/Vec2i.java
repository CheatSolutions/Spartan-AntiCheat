package kireiko.dev.millennium.types;

import lombok.Data;

@Data
public final class Vec2i {

    private int x, y;

    public Vec2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vec2i(Number x, Number y) {
        this.x = x.intValue();
        this.y = y.intValue();
    }

    public double distance(Vec2i vector2) {
        return Math.sqrt(Math.pow(this.x - vector2.x, 2) + Math.pow(this.y - vector2.y, 2));
    }

    public Vec2i add(Vec2i vector2) {
        return new Vec2i(this.x + vector2.x, this.y + vector2.y);
    }

    public Vec2i subtract(Vec2i vector2) {
        return new Vec2i(this.x - vector2.x, this.y - vector2.y);
    }

    public Vec2i scale(double factor) {
        return new Vec2i(this.x * factor, this.y * factor);
    }

    public boolean compare(Vec2i vector2) {
        return this.x == vector2.x && this.y == vector2.y;
    }
}
