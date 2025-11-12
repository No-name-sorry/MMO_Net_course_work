package universal;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        QueueElement Q1 = new QueueElement(); // необроблені
        QueueElement Q2 = new QueueElement(); // оброблені (після D2)
        QueueElement Q3 = new QueueElement(); // черга перед D2
        QueueElement Q4 = new QueueElement(); // черга перед D3

        Dispose dispose = new Dispose();

        ArrivalPoint arrival = new ArrivalPoint(30.0, Q1, Q3);
        Server D2 = new Server("Processing", 7.0, Q3, Q2);
        AssemblyServer D1 = new AssemblyServer(6.0, Q1, Q2, Q3, Q4);
        Server D3 = new Server("Regulation", 8.0, Q4, dispose);

        List<Element> elements = List.of(arrival, D2, D1, D3, dispose);

        SystemStatistics statistics = new SystemStatistics();
        statistics.registerQueue("Q1", Q1);
        statistics.registerQueue("Q2", Q2);
        statistics.registerQueue("Q3", Q3);
        statistics.registerQueue("Q4", Q4);

        ProcessingModel model = new ProcessingModel(elements, 5000.0, statistics);
        model.simulate();
    }
}
