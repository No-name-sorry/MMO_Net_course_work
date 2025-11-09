package universal;

import java.util.ArrayList;

public class MMOModel {
    private final ArrayList<Element> list;
    double tnext, tcurr;
    int event;

    public MMOModel(ArrayList<Element> elements) {
        list = elements;
        tnext = 0.0;
        event = 0;
        tcurr = tnext;
    }

    public void simulate(double time, boolean verbose, int endEventId) {
        while (tcurr < time) {
            tnext = Double.MAX_VALUE;
            for (Element e : list) {
                if (e.getTnext() < tnext) {
                    tnext = e.getTnext();
                    event = e.getId();
                }
            }

            if (verbose) {
                System.out.println("\nIt's time for event in " +
                        list.get(event).getName() +
                        ", time = " + tnext);
            }

            for (Element e : list) {
                e.doStatistics(tnext - tcurr);
            }
            tcurr = tnext;
            for (Element e : list) {
                e.setTcurr(tcurr);
            }
            list.get(event).outAct();
            for (Element e : list) {
                if (e.getTnext() == tcurr) {
                    e.outAct();
                }
            }
            if (event == endEventId) {
                break;
            }
            if (verbose) {
                printInfo();
            }
        }
        if(verbose)
            printResult();
    }

    public void printInfo() {
        for (Element e : list) {
            e.printInfo();
        }
    }

    public void printResult() {
        System.out.println("\n-------------RESULTS-------------");
        for (Element e : list) {
            e.printResult();
            if (e instanceof Process) {
                Process p = (Process) e;
                System.out.println("mean length of queue = " +
                        p.getMeanQueue() / tcurr
                        + "\nfailure probability = " +
                        p.getFailure() / (double) p.getQuantity());
            }
            System.out.println();
        }
    }
}