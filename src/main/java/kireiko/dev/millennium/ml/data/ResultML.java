package kireiko.dev.millennium.ml.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

public class ResultML {
    public CheckResultML statisticsResult;
    public ResultML() {
        this.statisticsResult = new CheckResultML();
    }
    @Data
    @AllArgsConstructor
    public static class CheckResultML {
        public double UNUSUAL;
        public double STRANGE;
        public double SUSPECTED;
        public double SUSPICIOUSLY;
        public CheckResultML() {
            this.UNUSUAL = 0;
            this.STRANGE = 0;
            this.SUSPECTED = 0;
            this.SUSPICIOUSLY = 0;
        }
        public String toString() {
            return "[" + this.UNUSUAL + ", " + this.STRANGE
                            + ", " + this.SUSPECTED + ", " + this.SUSPICIOUSLY + "]";
        }
        public List<Double> toList() {
            return Arrays.asList(this.UNUSUAL, this.STRANGE,
                            this.SUSPECTED, this.SUSPICIOUSLY);
        }
        public void apply(List<Double> i, double m) {
            setUNUSUAL(getUNUSUAL() + i.get(0) / m);
            setSTRANGE(getSTRANGE() + i.get(1) / m);
            setSUSPECTED(getSUSPECTED() + i.get(2) / m);
            setSUSPICIOUSLY(getSUSPICIOUSLY() + i.get(3) / m);
        }
    }
}
