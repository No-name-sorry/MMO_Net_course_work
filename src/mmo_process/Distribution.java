package mmo_process;

public enum Distribution {
    EXP,
    NORM,
    UNIF,
    ERLANG,
    DETERMINISTIC;

    @Override
    public String toString() {
        return this.name();
    }
}

