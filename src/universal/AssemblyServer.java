package universal;

import lombok.Getter;
import universal.util.DetailType;
import universal.util.FunRand;

public class AssemblyServer extends Element {
    @Getter
    private final QueueElement q1; // RAW
    @Getter
    private final QueueElement q2; // PROCESSED
    @Getter
    private final QueueElement q3; // черга processing
    @Getter
    private final QueueElement q4; // regulation
    @Getter
    private boolean busy = false;
    @Getter
    private int totalAssembled = 0;
    @Getter
    private int totalDefective = 0;
    private double totalBusyTime = 0.0;
    private double lastChangeTime = 0.0;

    private Detail rawDetail;
    private Detail processedDetail;

    public AssemblyServer(double delay, QueueElement q1, QueueElement q2,
                          QueueElement q3, QueueElement q4) {
        super("Assembly", delay);
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
        this.q4 = q4;
    }

    @Override
    public void inAct() {
        if (!busy && !q1.isEmpty() && !q2.isEmpty()) {
            busy = true;
            rawDetail = q1.poll();
            processedDetail = q2.poll();
            setTnext(getTcurr() + getDelay());
        }
    }

    @Override
    public void outAct() {
        super.outAct();
        busy = false;
        totalBusyTime += (getTcurr() - lastChangeTime);

        if (Math.random() < 0.04) {
            // 4% брак
            totalDefective++;
            q1.add(rawDetail);
            q3.add(processedDetail);
        } else {
            // 96% успіх
            totalAssembled++;
            Detail assembled = new Detail(DetailType.ASSEMBLED, getTcurr());
            q4.add(assembled);
        }
        rawDetail = null;
        processedDetail = null;
        setTnext(Double.MAX_VALUE);
        inAct();
    }

    public double getUtilization(double totalTime) {
        return totalBusyTime / totalTime;
    }

}
