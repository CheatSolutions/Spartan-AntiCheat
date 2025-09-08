package kireiko.dev.millennium.ml.data.statistic;

import kireiko.dev.millennium.math.Statistics;
import kireiko.dev.millennium.ml.data.ObjectML;
import lombok.Data;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

@Data
public class StatisticML implements Serializable {

    private static final long serialVersionUID = 1L;

    private int stackSize;
    private static final int MIN_VALUES = 10;
    private static final double KURTOSIS_DIVISOR = 10.0;
    private static final double AVERAGE_DIVISOR = 3.0;
    private static final double OUTLIERS_DIVISOR = 8.0;

    private Set<StatisticPattern> parameters;
    private double scale;
    private int fixed, learned;

    public StatisticML(double scale, int stackSize) {
        this.stackSize = stackSize;
        this.scale = scale;
        this.fixed = 0;
        this.learned = 0;
        this.parameters = Collections.synchronizedSet(new HashSet<>());
    }

    public double checkData(ObjectML data) {
        validateData(data);
        double vl = 0;
        List<Double> values = data.getValues();
        this.fixed = 0;

        for (int i = 0; i <= values.size() / stackSize; i++) {
            int index = i * stackSize;
            if (index + stackSize > values.size()) break;

            List<Double> stack = values.subList(index, index + stackSize);
            List<Double> stackDelta = calculateStackDelta(stack);
            StatisticPattern statisticPattern = createStatisticPattern(stackDelta);
            for (StatisticPattern pattern : this.parameters) {
                if (pattern.compare(statisticPattern, scale)) {
                    final double localVl = Math.min(((double) pattern.detected) / ((double) pattern.legit + 1), 5.0) / (stackSize * 4);
                    vl += localVl;
                    break;
                }
            }
        }
        return vl;
    }


    public void pushData(ObjectML data, boolean isMustBeBlocked) {
        validateData(data);
        List<Double> values = data.getValues();
        this.fixed = 0;

        for (int i = 0; i <= values.size() / stackSize; i++) {
            int index = i * stackSize;
            if (index + stackSize > values.size()) break;
            List<Double> stack = values.subList(index, index + stackSize);
            List<Double> stackDelta = calculateStackDelta(stack);
            StatisticPattern statisticPattern = createStatisticPattern(stackDelta);
            updatePatterns(statisticPattern, isMustBeBlocked);
        }
    }

    private void validateData(ObjectML data) {
        if (data == null || data.getValues() == null || data.getValues().size() < MIN_VALUES) {
            throw new IllegalArgumentException("Data must contain at least " + MIN_VALUES + " values.");
        }
    }

    private List<Double> calculateStackDelta(List<Double> stack) {
        List<Double> stackDelta = new ArrayList<>();
        double previousValue = stack.get(0);
        for (double currentValue : stack) {
            stackDelta.add(Math.abs(currentValue - previousValue));
            previousValue = currentValue;
        }
        return stackDelta;
    }

    private StatisticPattern createStatisticPattern(List<Double> stackDelta) {
        int kurtosis = (int) (Statistics.getKurtosis(stackDelta) / (KURTOSIS_DIVISOR * scale));
        int outliersX = (int) (Statistics.getStandardDeviation(Statistics.getOutliers(stackDelta).getX()) / (OUTLIERS_DIVISOR * scale));
        int outliersY = (int) (Statistics.getStandardDeviation(Statistics.getOutliers(stackDelta).getY()) / (OUTLIERS_DIVISOR * scale));
        double iqr = Statistics.getIQR(stackDelta);
        double entropy = Statistics.getShannonEntropy(stackDelta);
        int outliersGeneric = Statistics.getZScoreOutliers(stackDelta, 0.5f).size();
        double jiff = Statistics.getStandardDeviation(Statistics.getJiffDelta(stackDelta, 3));
        double kTest = Statistics.kolmogorovSmirnovTest(stackDelta, Function.identity()) / 10;
        int distinct = Statistics.getDistinct(stackDelta);
        return new StatisticPattern(
                        kurtosis,
                        outliersX,
                        outliersY,
                        iqr,
                        entropy,
                        outliersGeneric,
                        jiff,
                        kTest,
                        distinct
        );
    }

    private void updatePatterns(StatisticPattern statisticPattern, boolean blocked) {
        for (StatisticPattern pattern : this.parameters) {
            pattern.optimize();
            if (pattern.compare(statisticPattern, this.scale)) {
                if (blocked) pattern.detected++;
                else pattern.legit++;
                fixed++;
                return;
            }
        }
        this.parameters.add(statisticPattern);
        learned++;
    }
}