package universal;

import java.util.LinkedList;
import java.util.Queue;

public class QueueElement {
    private final Queue<Detail> queue;
    private final int maxSize;
    private double meanSize = 0.0;
    private double lastUpdateTime = 0.0;

    public QueueElement() {
        this(Integer.MAX_VALUE);
    }

    public QueueElement(int maxSize) {
        this.queue = new LinkedList<>();
        this.maxSize = maxSize;
    }

    public boolean add(Detail detail) {
        if (queue.size() < maxSize) {
            queue.add(detail);
            return true;
        }
        return false;
    }

    public Detail poll() {
        return queue.poll();
    }

//    public Detail peek() {
//        return queue.peek();
//    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }

    public void updateStatistics(double currentTime) {
        double delta = currentTime - lastUpdateTime;
        meanSize += queue.size() * delta;
        lastUpdateTime = currentTime;
    }

    public double getMeanSize(double totalTime) {
        return meanSize / totalTime;
    }

    @Override
    public String toString() {
        return "QueueElement(size=" + queue.size() + "/" + maxSize + ")";
    }
}
