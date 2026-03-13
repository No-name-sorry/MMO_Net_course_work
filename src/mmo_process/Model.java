package mmo_process;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private final List<Element> elements;
    private double tcurr;

    public Model(List<Element> elements) {
        this.elements = new ArrayList<>(elements);
        this.tcurr = 0.0;
    }

    public void simulate(double timeModeling) {
        while (tcurr < timeModeling) {
            double tnext = Double.MAX_VALUE;

            for (Element e : elements) {
                if (e.getTnext() < tnext) {
                    tnext = e.getTnext();
                }
            }

            if (tnext == Double.MAX_VALUE) {
                break;
            }

            double delta = tnext - tcurr;
            tcurr = tnext;

            for (Element e : elements) {
                e.setTcurr(tcurr);
                e.doStatistics(delta);
            }

            for (Element e : elements) {
                if (e.getTnext() == tcurr) {
                    e.outAct();
                }
            }
        }
    }

    public void simulateWithObservation(double time, double step) {

        double nextObservation = step;

        while (tcurr < time) {

            double tnext = Double.MAX_VALUE;
            for (Element e : elements) {
                if (e.getTnext() < tnext) {
                    tnext = e.getTnext();
                }
            }

            double delta = tnext - tcurr;

            for (Element e : elements) {
                e.doStatistics(delta);
            }

            tcurr = tnext;

            for (Element e : elements) {
                e.setTcurr(tcurr);
            }

            for (Element e : elements) {
                if (e.getTnext() == tcurr) {
                    e.outAct();
                }
            }

            if (tcurr >= nextObservation) {

                System.out.println("t = " + tcurr);

                for (Element e : elements) {
                    if (e instanceof Process p) {
                        System.out.println(
                                p.getName() +
                                        " avgQueue = " + p.getAverageQueueLength(tcurr)
                        );
                    }
                }
                System.out.println("--------------------------------");
                nextObservation += step;
            }
        }
    }

    public void resetStatistics() {

        for (Element e : elements) {

            if (e instanceof Process p) {

                p.resetStatistics();
            }
        }
    }

    public void printAveragedResult(double timeModeling) {
        System.out.println("----- AVERAGED RESULTS -----");
        for (Element e : elements) {
            if (e instanceof Process p) {
                p.printAveragedResult(timeModeling);
            } else {
                e.printResult();
            }
        }
    }
}