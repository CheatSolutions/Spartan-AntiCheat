package kireiko.dev.millennium.math;

public final class Simplification {
    public static float castTo360(float num) {
        float value = Math.abs((num + 360) % 360 - 180);
        return value;
    }

    public static double scaleVal(double value, double scale) {
        double scale2 = Math.pow(10, scale);
        return Math.ceil(value * scale2) / scale2;
    }
}
