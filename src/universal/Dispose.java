package universal;

public class Dispose extends Element {
    private int disposedCount = 0;
    private double totalTimeInSystem = 0.0;

    public void inAct(Detail d, double currentTime) {
        disposedCount++;
        totalTimeInSystem += (currentTime - d.getArrivalTime());
    }

    @Override
    public void inAct() {
    }

    @Override
    public void printResult() {
        System.out.println("=== Dispose statistics ===");
        System.out.println("Total disposed: " + disposedCount);
        if (disposedCount > 0) {
            System.out.printf("Avg time in system: %.2f%n", totalTimeInSystem / disposedCount);
        }
    }

    public double getTotalTimeInSystem (){return totalTimeInSystem;}

    public int getDisposedCount() {
        return disposedCount;
    }
}
