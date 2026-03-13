package mmo_process;

public class Detail {

    public enum Type {
        RAW,
        PROCESSED,
        PRODUCT
    }

    private static int nextId = 0;

    private int id;
    private Type type;
    private double createTime;
    private double queueEnterTime;

    public Detail(Type type, double createTime) {
        this.id = nextId++;
        this.type = type;
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public double getCreateTime() {
        return createTime;
    }

    public double getQueueEnterTime() {
        return queueEnterTime;
    }

    public void setQueueEnterTime(double queueEnterTime) {
        this.queueEnterTime = queueEnterTime;
    }
}