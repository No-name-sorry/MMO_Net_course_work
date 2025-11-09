package universal;

import java.util.ArrayList;
import java.util.List;

public class Create extends Element {
    private final static double DEFAULT_DELAY = 2.0;

    private final List<Element> nextPossible = new ArrayList<>();
    private final List<Double> nextPossibleProbability = new ArrayList<>();

    private int rejectedCount = 0;

    public Create() {
        super(DEFAULT_DELAY);
        super.setTnext(0.0);
    }

    public Create(double delay) {
        super(delay);
        super.setTnext(0.0);
    }

    @Override
    public void outAct() {
        super.outAct();
        super.setTnext(super.getTcurr() + super.getDelay());

        if(getNextElement() == null) {
            setNextElement(selectNextELement(nextPossible, nextPossibleProbability));
        }

        getNextElement().inAct();
    }

    @Override
    public void printResult() {
        super.printResult();
        System.out.println(getName() + " rejected = " + rejectedCount);
    }
}