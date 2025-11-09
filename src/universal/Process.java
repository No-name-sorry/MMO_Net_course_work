package universal;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Process extends Element {
    private final static double DEFAULT_DELAY = 1.0;

    private final List<Element> nextPossible = new ArrayList<>();
    private final List<Double> nextPossibleProbability = new ArrayList<>();

    private int queue, maxQueue, failure;
    private double meanQueue;
    private double totalDeviceTime = 0.0;

    private int devices = 1;

    public Process() {
        super(DEFAULT_DELAY);
        this.queue = 0;
        this.maxQueue = Integer.MAX_VALUE;
        this.meanQueue = 0.0;
    }

    public Process(double delay) {
        super(delay);
        this.queue = 0;
        this.maxQueue = Integer.MAX_VALUE;
        this.meanQueue = 0.0;
    }

    public Process(double delay, int devices) {
        super(delay);
        this.queue = 0;
        this.maxQueue = Integer.MAX_VALUE;
        this.meanQueue = 0.0;
        this.devices = devices;
    }

    @Override
    public void inAct() {
        if (super.getState() < devices) {
            super.setState(super.getState() + 1);
            super.setTnext(super.getTcurr() + super.getDelay());
        } else {
            if (getQueue() < getMaxQueue()) {
                setQueue(getQueue() + 1);
            } else {
                failure++;
            }
        }
    }

    @Override
    public void outAct() {
        super.outAct();
        super.setState(super.getState() - 1);
        if (getQueue() > 0) {
            setQueue(getQueue() - 1);
            super.setState(super.getState() + 1);
            super.setTnext(super.getTcurr() + super.getDelay());
        } else {
            super.setTnext(Double.MAX_VALUE);
        }

        if(getNextElement() == null) {
            setNextElement(selectNextELement(nextPossible, nextPossibleProbability));
        }
        // Multi-route logic: select next element using routing strategy

        getNextElement().inAct();
    }

    @Override
    public void doStatistics(double delta) {
        meanQueue = getMeanQueue() + queue * delta;
        int devicesInUse = Math.min(super.getState(), devices);
        totalDeviceTime += devicesInUse * delta;
    }

    public double getDeviceUtilization(double totalTime) {
        return (totalDeviceTime / (devices * totalTime)) * 100.0;
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("failure = " + this.getFailure());
        System.out.println("devices in use = " + super.getState() + "/" + devices);
    }
}