package mmo_process;
import mmo_process.Distribution;

public class Element {

    private static int nextId = 0;

    private int id;
    private String name;

    private double tcurr;
    private double tnext;

    private double delayMean;
    private double delayDev;

    private Distribution distribution;

    private int state;
    private int quantity;

    private Element nextElement;

    public Element() {
        this.id = nextId++;
        this.name = "element" + id;
        this.tcurr = 0.0;
        this.tnext = Double.MAX_VALUE;
        this.delayMean = 1.0;
        this.delayDev = 0.0;
        this.distribution = Distribution.EXP;
        this.state = 0;
        this.quantity = 0;
        this.nextElement = null;
    }

    public Element(double delay) {
        this.id = nextId++;
        this.name = "element" + id;
        this.tcurr = 0.0;
        this.tnext = 0.0;
        this.delayMean = delay;
        this.delayDev = 0.0;
        this.distribution = Distribution.DETERMINISTIC;
        this.state = 0;
        this.quantity = 0;
        this.nextElement = null;
    }

    public Element(String name, double delay) {
        this.id = nextId++;
        this.name = name;
        this.tcurr = 0.0;
        this.tnext = 0.0;
        this.delayMean = delay;
        this.delayDev = 0.0;
        this.distribution = Distribution.DETERMINISTIC;
        this.state = 0;
        this.quantity = 0;
        this.nextElement = null;
    }

    public double getDelay() {
        double delay;

        switch (distribution) {
            case EXP:
                delay = FunRand.Exp(delayMean);
                break;

            case NORM:
                delay = FunRand.Norm(delayMean, delayDev);
                break;

            case UNIF:
                delay = FunRand.Unif(delayMean, delayDev);
                break;

            case ERLANG:
                delay = FunRand.Erlang(delayMean, 2);
                break;

            case DETERMINISTIC:
            default:
                delay = delayMean;
                break;
        }

        return delay;
    }

    public void inAct() {
    }

    public void outAct() {
        quantity++;
    }

    public void doStatistics(double delta) {
    }

    public void printInfo() {
        System.out.println(
                name +
                        " state = " + state +
                        " quantity = " + quantity +
                        " tnext = " + tnext
        );
    }

    public void printResult() {
        System.out.println(name + " quantity = " + quantity);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTcurr() {
        return tcurr;
    }

    public void setTcurr(double tcurr) {
        this.tcurr = tcurr;
    }

    public double getTnext() {
        return tnext;
    }

    public void setTnext(double tnext) {
        this.tnext = tnext;
    }

    public double getDelayMean() {
        return delayMean;
    }

    public void setDelayMean(double delayMean) {
        this.delayMean = delayMean;
    }

    public double getDelayDev() {
        return delayDev;
    }

    public void setDelayDev(double delayDev) {
        this.delayDev = delayDev;
    }

    public Distribution getDistribution() {
        return distribution;
    }

    public void setDistribution(Distribution distribution) {
        this.distribution = distribution;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getQuantity() {
        return quantity;
    }

    public Element getNextElement() {
        return nextElement;
    }

    public void setNextElement(Element nextElement) {
        this.nextElement = nextElement;
    }
}