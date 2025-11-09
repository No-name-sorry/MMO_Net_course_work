package universal;

import lombok.Getter;
import lombok.Setter;
import universal.util.Distribution;
import universal.util.FunRand;

import java.util.List;

@Getter
@Setter
public class Element {
    private static int nextId = 0;
    private String name;
    private double tnext;
    private double delayMean, delayDev;
    private Distribution distribution;
    private int quantity;
    private double tcurr;
    private int state;
    private Element nextElement;
    private int id;

    public Element() {
        tnext = Double.MAX_VALUE;
        delayMean = 1.0;
        distribution = Distribution.EXP;
        tcurr = tnext;
        state = 0;
        nextElement = null;
        id = nextId;
        nextId++;
        name = "element" + id;
    }

    public Element(double delay) {
        name = "anonymus";
        tnext = 0.0;
        delayMean = delay;
        distribution = Distribution.EXP;
        tcurr = tnext;
        state = 0;
        nextElement = null;
        id = nextId;
        nextId++;
        name = "element" + id;
    }

    public Element(String nameOfElement, double delay) {
        name = nameOfElement;
        tnext = 0.0;
        delayMean = delay;
        distribution = Distribution.EXP;
        tcurr = tnext;
        state = 0;
        nextElement = null;
        id = nextId;
        nextId++;
        name = "element" + id;
    }

    public static void resetIdCounter() {
        nextId = 0;
    }

    public double getDelay() {
        double delay = getDelayMean();
        if (Distribution.EXP == getDistribution()) {
            delay = FunRand.Exp(getDelayMean());
        } else {
            if (Distribution.NORMAL == getDistribution()) {
                delay = FunRand.Norm(getDelayMean(),
                        getDelayDev());
            } else {
                if (Distribution.UNIFORM == getDistribution()) {
                    delay = FunRand.Unif(getDelayMean(),
                            getDelayDev());
                } else {
                    if (Distribution.ERLAND == getDistribution()) {
                        int k = (int) getDelayDev();
                        delay = FunRand.Erlang(getDelayMean(), k);
                    } else {
                        // distribution unknown
                        delay = getDelayMean();
                    }
                }
            }
        }
        return delay;
    }

    protected Element selectNextELement(List<Element> nextPossible, List<Double> nextPossibleProbability){
        double random = Math.random();
        double c = 0;
        for(int i = 0; i < nextPossible.size(); i++){
            c += nextPossibleProbability.get(i);
            if(random < c){
                return nextPossible.get(i);
            }
        }
        throw new RuntimeException("Wrong probabilities for " + this.getId() + " " + this.getName() + " element");
    }

    public void inAct() { }

    public void outAct() {
        quantity++;
    }

    public void printResult() {
        System.out.println(getName() + " quantity = " + quantity);
    }

    public void printInfo() {
        System.out.println(getName() + " state= " + state +
                " quantity = " + quantity +
                " tnext= " + tnext);
    }

    public void doStatistics(double delta) { }
}