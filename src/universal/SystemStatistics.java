package universal;

import java.util.HashMap;
import java.util.Map;

public class SystemStatistics {
    private final Map<String, QueueElement> queues = new HashMap<>();
    private final Map<String, Element> elements = new HashMap<>();

    private double totalTime;

    private double avgQ1 = 0;
    private double avgQ2 = 0;
    private double avgQ3 = 0;
    private double avgQ4 = 0;

    private double utilizationD1;
    private double utilizationD2;
    private double utilizationD3;
    private int totalAssembled;
    private int totalDefective;
    private int totalDisposed;
    private double avgTimeInSystem;

    public void registerQueue(String name, QueueElement queue) {
        queues.put(name, queue);
    }

    public void registerElement(Element e) {
        elements.put(e.getName(), e);
    }

    public void collect(double totalTime, AssemblyServer D1, Server D2, Server D3, Dispose dispose) {
        this.totalTime = totalTime;
        avgQ1 = queues.get("Q1").getMeanSize(totalTime);
        avgQ2 = queues.get("Q2").getMeanSize(totalTime);
        avgQ3 = queues.get("Q3").getMeanSize(totalTime);
        avgQ4 = queues.get("Q4").getMeanSize(totalTime);

        utilizationD1 = D1.getUtilization(totalTime);
        utilizationD2 = D2.getUtilization(totalTime);
        utilizationD3 = D3.getUtilization(totalTime);

        totalAssembled = D1.getTotalAssembled();
        totalDefective = D1.getTotalDefective();
        totalDisposed = dispose.getDisposedCount();

        if (dispose.getDisposedCount() > 0)
            avgTimeInSystem = dispose.getTotalTimeInSystem() / dispose.getDisposedCount();
    }

    public void printReport() {
        System.out.println("\n=========== SYSTEM STATISTICS REPORT ===========");
        System.out.printf("Average queue sizes:%n");
        System.out.printf("  Q1 (RAW before D1):        %.2f%n", avgQ1);
        System.out.printf("  Q2 (PROCESSED before D1):  %.2f%n", avgQ2);
        System.out.printf("  Q3 (before D2):            %.2f%n", avgQ3);
        System.out.printf("  Q4 (before D3):            %.2f%n", avgQ4);

        System.out.println("\nServer utilization:");
        System.out.printf("  D1 (Assembly):   %.2f%%%n", utilizationD1 * 100);
        System.out.printf("  D2 (Preprocess): %.2f%%%n", utilizationD2 * 100);
        System.out.printf("  D3 (Adjustment): %.2f%%%n", utilizationD3 * 100);

        System.out.println("\nProduction results:");
        System.out.printf("  Assembled (OK): %d%n", totalAssembled);
        System.out.printf("  Defective:      %d%n", totalDefective);
        System.out.printf("  Disposed:       %d%n", totalDisposed);
        System.out.printf("  Avg time in system: %.2f%n", avgTimeInSystem);

        System.out.println("\nAccumulation analysis:");
        analyzeAccumulation();

        System.out.println("================================================");
    }

    private void analyzeAccumulation() {
        String bottleneck = "Q1";
        double maxAvg = avgQ1;

        if (avgQ2 > maxAvg) { bottleneck = "Q2"; maxAvg = avgQ2; }
        if (avgQ3 > maxAvg) { bottleneck = "Q3"; maxAvg = avgQ3; }
        if (avgQ4 > maxAvg) { bottleneck = "Q4"; maxAvg = avgQ4; }

        System.out.printf("  → Most loaded queue: %s (avg %.2f)%n", bottleneck, maxAvg);

        switch (bottleneck) {
            case "Q3" -> System.out.println("  Reason: preprocessing (D2) is slower — consider adding another D2 server or reducing processing time.");
            case "Q1" -> System.out.println("  Reason: assembly (D1) waits for processed parts — consider balancing flow from D2 or increasing D1 speed.");
            case "Q2" -> System.out.println("  Reason: accumulation of processed parts — D1 may be too slow or synchronization inefficient.");
            case "Q4" -> System.out.println("  Reason: final adjustment (D3) is a bottleneck — consider adding another regulator or parallel line.");
        }

        double totalAvg = avgQ1 + avgQ2 + avgQ3 + avgQ4;
        System.out.printf("%n  → Average number of parts on the assembly section: %.2f%n", totalAvg);
    }
}
