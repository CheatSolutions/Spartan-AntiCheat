package kireiko.dev.millennium.math;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public final class SensitivityProcessor {

    @Getter
    private static final double[] SENSITIVITY_MCP_VALUES;

    static {
        SENSITIVITY_MCP_VALUES = new double[]{
                0.0, 0.00704225, 0.01408451, 0.01760563, 0.02112676,
                0.02816901, 0.02816902, 0.03521127, 0.04225352, 0.04929578,
                0.04929577, 0.05633803, 0.06338028, 0.06690141, 0.07042254,
                0.07746479, 0.08450704, 0.08802817, 0.09154929, 0.09859155,
                0.10211267, 0.1056338, 0.11267605, 0.11971831, 0.12323943,
                0.12676056, 0.13380282, 0.13732395, 0.14084508, 0.14788732,
                0.15492958, 0.15845071, 0.16197184, 0.16901408, 0.17253521,
                0.17605634, 0.18309858, 0.18661971, 0.19014084, 0.1971831,
                0.20422535, 0.20774647, 0.2112676, 0.21830986, 0.22183098,
                0.22535211, 0.23239437, 0.23943663, 0.24295775, 0.24647887,
                0.2535211, 0.25704224, 0.26056337, 0.26760563, 0.2746479,
                0.27816902, 0.28169015, 0.28873238, 0.29225351, 0.29577464,
                0.3028169, 0.30985916, 0.31338029, 0.31690142, 0.32394367,
                0.32746478, 0.3309859, 0.33802816, 0.34507042, 0.34859155,
                0.35211268, 0.35915494, 0.36267605, 0.36619717, 0.37323943,
                0.37676056, 0.3802817, 0.38732395, 0.3943662, 0.39788733,
                0.40140846, 0.4084507, 0.41197183, 0.41549295, 0.4225352,
                0.42957747, 0.4330986, 0.43661973, 0.44366196, 0.44718309,
                0.45070422, 0.45774648, 0.46478873, 0.46830986, 0.471831,
                0.47887325, 0.48239436, 0.48591548, 0.49295774, 0.5,
                0.5, 0.5070422, 0.5140845, 0.51760563, 0.52112675,
                0.52816904, 0.53169015, 0.53521127, 0.5422535, 0.5492958,
                0.5528169, 0.556338, 0.5633803, 0.56690142, 0.57042253,
                0.57746476, 0.58450705, 0.58802818, 0.5915493, 0.59859157,
                0.60211269, 0.6056338, 0.6126761, 0.6197183, 0.62323942,
                0.62676054, 0.63380283, 0.63732395, 0.64084506, 0.64788735,
                0.6549296, 0.6584507, 0.6619718, 0.6690141, 0.6725352,
                0.6760563, 0.6830986, 0.68661972, 0.69014084, 0.6971831,
                0.70422536, 0.70774648, 0.7112676, 0.7183099, 0.7253521,
                0.7253521, 0.73239434, 0.7394366, 0.74295773, 0.74647886,
                0.75352114, 0.75704227, 0.7605634, 0.76760566, 0.7746479,
                0.778169, 0.7816901, 0.7887324, 0.79225352, 0.79577464,
                0.8028169, 0.80985916, 0.81338028, 0.8169014, 0.8239437,
                0.8274648, 0.8309859, 0.8380282, 0.8415493, 0.8450704,
                0.85211265, 0.85915494, 0.86267605, 0.86619717, 0.87323946,
                0.87676058, 0.8802817, 0.8873239, 0.8943662, 0.89788731,
                0.90140843, 0.9084507, 0.91197182, 0.91549295, 0.92253524,
                0.92957747, 0.93309858, 0.9366197, 0.943662, 0.9471831,
                0.9507042, 0.9577465, 0.96478873, 0.96830985, 0.97183096,
                0.97887325, 0.98239437, 0.9859155, 0.9929578, 1.0,
                1.0
        };
    }

    private final List<Integer>
                    sensitivitySamples = new ArrayList<>(),
                    sensitivityStrictSamples = new ArrayList<>();
    private final PlayerProtocol playerProfile;
    public double totalSensitivity = 0;
    public int totalSensitivityClient = 0;
    @Setter
    private float deltaPitch = 0;
    @Setter
    @Getter
    private float lastDeltaPitch = 0;
    @Getter
    private double finalSensitivity;
    @Getter
    private int sensitivity;
    private int lastStrictSensitivity = 0;

    public SensitivityProcessor(PlayerProtocol playerProfile) {
        this.playerProfile = playerProfile;
    }

    public static double getGcd(final double a, final double b) {
        if (Math.abs(b) < 0.001 || a == b) {
            return a;
        }
        if (a < b) {
            return getGcd(b, a);
        }
        return getGcd(b, a - Math.floor(a / b) * b);
    }

    public static int getMode(final Collection<? extends Number> array) {
        int mode = (int) array.toArray()[0];
        int maxCount = 0;
        for (final Number value : array) {
            int count = 1;
            for (final Number i : array) {
                if (i.equals(value)) {
                    ++count;
                }
                if (count > maxCount) {
                    mode = (int) value;
                    maxCount = count;
                }
            }
        }
        return mode;
    }

    public void processSensitivity() {
        if (Math.abs(this.deltaPitch) < 0.31) {
            for (int i = 0; i < 200; i++) {
                final double gcdValue = Statistics.getGCDValue(SENSITIVITY_MCP_VALUES[i]);
                final double diff = Math.abs(gcdValue - Math.abs(this.deltaPitch));
                if (diff < 1e-3) {
                    sensitivityStrictSamples.add(i);
                    if (sensitivityStrictSamples.size() >= 10) {
                        final int finalSens = (int) Statistics.getMin(sensitivityStrictSamples);
                        if (finalSens == lastStrictSensitivity) {
                            playerProfile.getSensitivity().clear();
                            for (int r = 0; r < 5; r++) playerProfile.getSensitivity().add(finalSens);
                        } else playerProfile.getSensitivity().add(finalSens);
                        sensitivityStrictSamples.clear();
                        lastStrictSensitivity = finalSens;
                    }
                    break;
                }
            }
        }
        final float gcd = (float) getGcd(this.deltaPitch, this.lastDeltaPitch);
        final double sensitivityModifier = Math.cbrt(0.8333 * gcd);
        final double sensitivityStepTwo = 1.666 * sensitivityModifier - 0.3333;
        final double finalSensitivity = sensitivityStepTwo * 200.0;
        this.finalSensitivity = finalSensitivity;
        this.sensitivitySamples.add((int) finalSensitivity);
        if (this.sensitivitySamples.size() >= 40) {
            this.sensitivity = getMode(this.sensitivitySamples);
            if (this.hasValidSensitivity()) {
                this.totalSensitivityClient = this.sensitivity;
                this.totalSensitivity = SENSITIVITY_MCP_VALUES[this.sensitivity];
                playerProfile.getSensitivity().add(this.totalSensitivityClient);
            }
            this.sensitivitySamples.clear();
        }
    }

    public double getWrappedSensitivity() {
        return (this.totalSensitivity == 0.0
                ? SENSITIVITY_MCP_VALUES[100]
                : this.totalSensitivity);
    }

    private int calculateMode(List<Integer> samples) {
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (int sample : samples) {
            frequencyMap.put(sample, frequencyMap.getOrDefault(sample, 0) + 1);
        }

        return frequencyMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(0);
    }

    private boolean hasValidSensitivity() {
        return sensitivity >= 0 && sensitivity <= 200;
    }

}
