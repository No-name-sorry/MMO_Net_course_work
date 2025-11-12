package universal;
import universal.util.DetailType;

public class Detail {
//    public enum Type { RAW, PROCESSED }

    private final DetailType type;
    private final double arrivalTime;
    private final int id;
    private static int nextId = 1;

    public Detail(DetailType type, double arrivalTime) {
        this.type = type;
        this.arrivalTime = arrivalTime;
        this.id = nextId++;
    }

    public DetailType getType() { return type; }
    public int getId() { return id; }
    public double getArrivalTime() { return arrivalTime; }
}

