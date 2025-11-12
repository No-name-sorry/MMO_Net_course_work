package universal;

import java.util.List;

public class ProcessingModel {
    private final List<Element> elements;
    private final double timeModel;
    private double tcurr = 0.0;
    private final SystemStatistics stats;

    public ProcessingModel(List<Element> elements, double timeModel, SystemStatistics stats) {
        this.elements = elements;
        this.timeModel = timeModel;
        this.stats = stats;
    }

    public void simulate() {
        while (tcurr < timeModel) {
            Element nextEvent = findNextElement();
            tcurr = nextEvent.getTnext();

            for (Element e : elements) e.setTcurr(tcurr);

            nextEvent.outAct();

            for (Element e : elements) {
                if (e instanceof Server s) s.inAct();
                if (e instanceof AssemblyServer a) a.inAct();
            }

            // оновлюємо статистику по чергах
            for (Element e : elements) {
                if (e instanceof Server s && s.getInputQueue() != null)
                    s.getInputQueue().updateStatistics(tcurr);
                if (e instanceof AssemblyServer a) {
                    a.getQ1().updateStatistics(tcurr);
                    a.getQ2().updateStatistics(tcurr);
                    a.getQ3().updateStatistics(tcurr);
                    a.getQ4().updateStatistics(tcurr);
                }
            }
        }

        // збір фінальної статистики
        AssemblyServer D1 = (AssemblyServer) elements.stream().filter(e -> e.getName().contains("Assembly")).findFirst().get();
        Server D2 = (Server) elements.stream().filter(e -> e.getName().contains("Processing")).findFirst().get();
        Server D3 = (Server) elements.stream().filter(e -> e.getName().contains("Regulation")).findFirst().get();
        Dispose dispose = (Dispose) elements.stream().filter(e -> e instanceof Dispose).findFirst().get();

        stats.collect(tcurr, D1, D2, D3, dispose);
        stats.printReport();
    }

    private Element findNextElement() {
        Element min = elements.get(0);
        double tnext = min.getTnext();
        for (Element e : elements) {
            if (e.getTnext() < tnext) {
                min = e;
                tnext = e.getTnext();
            }
        }
        return min;
    }
}
