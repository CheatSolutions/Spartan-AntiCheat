package kireiko.dev.millennium.ml.data.module;

public enum FlagType {
    NORMAL,
    UNUSUAL,
    STRANGE,
    SUSPECTED;

    public int getLevel() {
        return this.ordinal();
    }
}
