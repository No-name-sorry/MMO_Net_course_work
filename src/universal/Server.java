package universal;

import lombok.Getter;
import universal.util.DetailType;

public class Server extends Element {
    private final QueueElement inputQueue;
    private final QueueElement outputQueue;
    @Getter
    private boolean busy = false;
    private Detail currentDetail;
    private final Dispose dispose;
    private double totalBusyTime = 0.0;
    private double lastChangeTime = 0.0;
    @Getter
    private int processedCount = 0;


    public Server(String name, double delay, QueueElement inputQueue, QueueElement outputQueue) {
        super(name, delay);
        this.inputQueue = inputQueue;
        this.outputQueue = outputQueue;
        this.dispose = null;
    }

    public Server(String name, double delay, QueueElement inputQueue, Dispose dispose) {
        super(name, delay);
        this.inputQueue = inputQueue;
        this.dispose = dispose;
        this.outputQueue = null;
    }

    @Override
    public void inAct() {
        if (!busy && !inputQueue.isEmpty()) {
            busy = true;
            currentDetail = inputQueue.poll();
            setTnext(getTcurr() + getDelay());
        }
    }

    @Override
    public void outAct() {
        super.outAct();
        busy = false;

        if (getName().contains("Processing") && currentDetail != null) {
            // змінюємо тип RAW → PROCESSED
            currentDetail = new Detail(DetailType.PROCESSED, getTcurr());
            if (outputQueue != null) {
                outputQueue.add(currentDetail);
            }
        } else if (getName().contains("Regulation") && currentDetail != null) {
            // регулювання → Dispose
            if (dispose != null) dispose.inAct();
        }

        currentDetail = null;
        setTnext(Double.MAX_VALUE);
        inAct();
    }

    public QueueElement getInputQueue() { return inputQueue; }
    public double getUtilization(double totalTime) {
        return totalBusyTime / totalTime;
    }
}
