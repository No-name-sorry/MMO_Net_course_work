package mmo_process;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;
import java.util.Random;

public class Process extends Element {

    public enum ProcessType {
        PREPROCESS,
        ASSEMBLY,
        REGULATION
    }

    private final ProcessType processType;
    private final Random random = new Random();



    private Queue<Detail> inputQueue = new ArrayDeque<>();
    private Detail currentDetail;



    private Queue<Detail> assemblyQueue = new ArrayDeque<>();
    private Detail currentRaw;
    private Detail currentProcessed;



    private Queue<Detail> productQueue = new ArrayDeque<>();
    private Detail currentProduct;
    private double defectProbability = 0.04;



    private Process nextProcess;
    private Process preprocessProcess;


    private double queueArea = 0.0;
    private double busyTime = 0.0;
    private double totalWaitingTime = 0.0;
    private int startedCount = 0;
    private int failureCount = 0;
    private int completedCount = 0;
    private double totalTimeInSystem = 0.0;

    public Process(String name, double delay, ProcessType processType) {
        super(name, delay);
        this.processType = processType;

        if (processType == ProcessType.REGULATION) {
            setDistribution(Distribution.EXP);
        } else {
            setDistribution(Distribution.DETERMINISTIC);
        }

        setTnext(Double.MAX_VALUE);
    }

    public void setNextProcess(Process nextProcess) {
        this.nextProcess = nextProcess;
    }

    public void setPreprocessProcess(Process preprocessProcess) {
        this.preprocessProcess = preprocessProcess;
    }

    public void setDefectProbability(double defectProbability) {
        this.defectProbability = defectProbability;
    }

    public ProcessType getProcessType() {
        return processType;
    }

    @Override
    public void inAct(){
    }

    public void inAct(Detail detail) {
        detail.setQueueEnterTime(getTcurr());

        switch (processType) {
            case PREPROCESS -> {
                inputQueue.add(detail);
                startServiceIfFree();
            }
            case ASSEMBLY -> {
                assemblyQueue.add(detail);
                startServiceIfFree();
            }
            case REGULATION -> {
                productQueue.add(detail);
                startServiceIfFree();
            }
        }
    }

    private void startServiceIfFree() {
        switch (processType) {
            case PREPROCESS -> {
                if (getState() == 0 && !inputQueue.isEmpty()) {
                    currentDetail = inputQueue.poll();
                    totalWaitingTime += getTcurr() - currentDetail.getQueueEnterTime();
                    startedCount++;
                    setState(1);
                    setTnext(getTcurr() + getDelay());
                }
            }
            case ASSEMBLY -> {
                if (getState() == 0 && hasRawAndProcessed()) {
                    currentRaw = extractFirstByType(Detail.Type.RAW);
                    currentProcessed = extractFirstByType(Detail.Type.PROCESSED);
                    totalWaitingTime += getTcurr() - currentRaw.getQueueEnterTime();
                    totalWaitingTime += getTcurr() - currentProcessed.getQueueEnterTime();
                    startedCount += 2;
                    setState(1);
                    setTnext(getTcurr() + getDelay());
                }
            }
            case REGULATION -> {
                if (getState() == 0 && !productQueue.isEmpty()) {
                    currentProduct = productQueue.poll();
                    totalWaitingTime += getTcurr() - currentProduct.getQueueEnterTime();
                    startedCount++;
                    setState(1);
                    setTnext(getTcurr() + getDelay());
                }
            }
        }
    }

    @Override
    public void outAct() {
        super.outAct();

        switch (processType) {
            case PREPROCESS -> finishPreprocess();
            case ASSEMBLY   -> finishAssembly();
            case REGULATION -> finishRegulation();
        }

        startServiceIfFree();
    }

    private void finishPreprocess() {
        currentDetail.setType(Detail.Type.PROCESSED);
        setState(0);
        setTnext(Double.MAX_VALUE);

        // Передаємо через inAct наступного елемента
        if (nextProcess != null) {
            nextProcess.inAct(currentDetail);
        }

        currentDetail = null;
    }

    private void finishAssembly() {
        double productCreateTime = Math.min(
                currentRaw.getCreateTime(),
                currentProcessed.getCreateTime()
        );
        Detail product = new Detail(Detail.Type.PRODUCT, productCreateTime);

        setState(0);
        setTnext(Double.MAX_VALUE);
        currentRaw = null;
        currentProcessed = null;

        // Передаємо через inAct наступного елемента
        if (nextProcess != null) {
            nextProcess.inAct(product);
        }
    }

    private void finishRegulation() {
        setState(0);
        setTnext(Double.MAX_VALUE);

        if (random.nextDouble() < defectProbability) {
            failureCount++;

            Detail d1 = new Detail(Detail.Type.RAW, currentProduct.getCreateTime());
            Detail d2 = new Detail(Detail.Type.RAW, currentProduct.getCreateTime());

            if (preprocessProcess != null) {
                preprocessProcess.inAct(d1);
                preprocessProcess.inAct(d2);
            }
        } else {
            completedCount++;
            totalTimeInSystem += getTcurr() - currentProduct.getCreateTime();
        }

        currentProduct = null;
    }

    // Допоміжні методи для ASSEMBLY
    ////////////////////////////////////////////////////////////////////////////////
    private boolean hasRawAndProcessed() {
        boolean hasRaw = false;
        boolean hasProcessed = false;
        for (Detail d : assemblyQueue) {
            if (d.getType() == Detail.Type.RAW)       hasRaw = true;
            if (d.getType() == Detail.Type.PROCESSED) hasProcessed = true;
            if (hasRaw && hasProcessed)               return true;
        }
        return false;
    }

    private Detail extractFirstByType(Detail.Type type) {
        Iterator<Detail> it = assemblyQueue.iterator();
        while (it.hasNext()) {
            Detail d = it.next();
            if (d.getType() == type) {
                it.remove();
                return d;
            }
        }
        return null;
    }


    // Статистика
    //////////////////////////////////////////////////////////////////////////////

    @Override
    public void doStatistics(double delta) {
        switch (processType) {
            case PREPROCESS -> queueArea += inputQueue.size() * delta;
            case ASSEMBLY   -> queueArea += assemblyQueue.size() * delta;
            case REGULATION -> queueArea += productQueue.size() * delta;
        }
        if (getState() == 1) {
            busyTime += delta;
        }
    }

    public void resetStatistics() {
        queueArea        = 0;
        busyTime         = 0;
        totalWaitingTime = 0;
        startedCount     = 0;
        failureCount     = 0;
        completedCount   = 0;
        totalTimeInSystem = 0;
    }

    public double getAverageQueueLength(double time) {
        return queueArea / time;
    }

    public double getAverageWorkload(double time) {
        return busyTime / time;
    }

    public double getAverageWaitingTime() {
        return totalWaitingTime/startedCount;
    }

    public int getFailureCount()   { return failureCount; }
    public int getCompletedCount() { return completedCount; }

    @Override
    public void printResult() {
        super.printResult();
        System.out.println(getName() + " queue area = " + queueArea);
        System.out.println(getName() + " busy time = " + busyTime);
        System.out.println(getName() + " total waiting time = " + totalWaitingTime);
            if (processType == ProcessType.ASSEMBLY) {
            System.out.println(getName() + " completed = " + startedCount);
        }
        if (processType == ProcessType.REGULATION) {
            System.out.println(getName() + " completed = " + completedCount);
            System.out.println(getName() + " failures = " + failureCount);
        }
    }

    public void printAveragedResult(double time) {
        System.out.println(getName() + " average queue length = " + getAverageQueueLength(time));
        System.out.println(getName() + " average workload = " + getAverageWorkload(time));
        System.out.println(getName() + " average waiting time = " + getAverageWaitingTime());
        System.out.println("////////////////////////////////////////////////////////////////");
        if (processType == ProcessType.REGULATION) {
            System.out.println(getName() + " completed = " + completedCount);
            System.out.println(getName() + " failures = " + failureCount);
        }
    }
}