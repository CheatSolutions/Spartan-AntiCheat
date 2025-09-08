package kireiko.dev.millennium.ml.logic;

import kireiko.dev.millennium.ml.data.ObjectML;

public class MathML {
    public static StringBuilder getPattern(ObjectML data, double scale) {
        double oldD = 0;
        double delta, oldDelta = 0;
        double jiffDelta;
        int i = 0;
        StringBuilder deviancePattern = new StringBuilder();
        for (double d : data.getValues()) {
            {
                delta = getDelta(d, oldD);
                jiffDelta = getDelta(delta, oldDelta);
                if (i > 2) {
                    int deviance = (int) (jiffDelta / scale);
                    deviancePattern.append(deviance);
                }
            }
            oldD = d;
            oldDelta = delta;
            i++;
        }
        return deviancePattern;
    }
    public static double getDelta(double a, double b) {
        return Math.abs(Math.abs(a) - Math.abs(b));
    }
}
