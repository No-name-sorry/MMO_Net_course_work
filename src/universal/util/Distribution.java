package universal.util;

public enum Distribution {
    EXP,
    NORMAL,
    UNIFORM,
    ERLAND,
    UNKNOWN;

    @Override
    public String toString() {
        return this.name();
    }
}