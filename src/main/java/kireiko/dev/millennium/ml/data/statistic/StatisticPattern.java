package kireiko.dev.millennium.ml.data.statistic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
public class StatisticPattern implements Serializable {
    private static final long serialVersionUID = 1L;

    public int legit = 0, detected = 0;
    private final int kurtosis;
    private final int outliersX, outliersY;
    private final double iqr, shannon;
    private final int outliersGeneric;
    private final double jiff;
    private final double kTest;
    private final int distinct;
    public boolean compare(StatisticPattern other, double scale) {
        return
        (dev(this.kurtosis, other.kurtosis) < scale * 3)
        && (dev(this.outliersX, other.outliersX) < scale * 4)
        && (dev(this.outliersY, other.outliersY) < scale * 4)
        && (dev(this.outliersGeneric, other.outliersGeneric) < (scale * 2) + 1)
        && (dev(this.iqr, other.iqr) < scale * 15)
        && (dev(this.shannon, other.shannon) < (scale / 100))
        && (dev(this.jiff, other.jiff) < scale * 4)
        && (dev(this.kTest, other.kTest) < scale * 2)
        && (dev(this.distinct, other.distinct) < scale * 2)
        ;
    }
    public void optimize() {
        if (legit + detected > 25) {
            legit /= 2;
            detected /= 2;
        }
    }
    private static double dev(double v1, double v2) {
        return Math.abs(Math.abs(v1) - Math.abs(v2));
    }
}
