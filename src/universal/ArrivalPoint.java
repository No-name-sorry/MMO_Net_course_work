package universal;

import universal.util.DetailType;

import java.util.Random;

public class ArrivalPoint extends Element {
    private final QueueElement q1;
    private final QueueElement q3;
    private final Random rnd = new Random();
    private final int partsPerBatch = 3;

    public ArrivalPoint(double delayMean, QueueElement q1, QueueElement q3) {
        super("Arrival", delayMean);
        this.q1 = q1;
        this.q3 = q3;
        setTnext(0.0);
    }

    @Override
    public void outAct() {
        super.outAct();
        setTnext(getTcurr() + getDelay());
        for (int i = 0; i < partsPerBatch; i++) {
            Detail d = new Detail(DetailType.RAW, getTcurr());

            if (Math.random() < 0.5) {
                q1.add(d);
            } else {
                q3.add(d);
            }
        }
    }
}
