package kireiko.dev.millennium.ml.data;

import kireiko.dev.millennium.ml.data.statistic.StatisticML;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class DataML implements Serializable {

    private static final long serialVersionUID = 1L;
    private final static double[] SCALE = new double[]{0.5, 0.25, 0.125, 0.0625};

    private List<StatisticML> statisticTable;
    public DataML(int stackSize) {
        this.statisticTable = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            this.statisticTable.add(new StatisticML(SCALE[i], stackSize));
        }
    }

    public List<Double> checkData(ObjectML data) {
        final List<Double> l = new ArrayList<>(4);
        for (StatisticML ml : this.statisticTable) {
            l.add(ml.checkData(data));
        }
        return l;
    }

    public void pushData(ObjectML data, boolean isMustBeBlocked) {
        for (StatisticML statisticML : this.statisticTable) {
            statisticML.pushData(data, isMustBeBlocked);
        }
    }
}
