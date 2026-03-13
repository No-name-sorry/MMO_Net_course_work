package mmo_process;

import java.util.Random;

public class Create extends Element {

    private final int batchSize;
    private final double routeProbability;
    private final Random random;

    private Process preprocess;
    private Process assembly;

    private int createdDetails = 0;

    public Create(String name, double delay, int batchSize, double routeProbability) {
        super(name, delay);
        this.batchSize = batchSize;
        this.routeProbability = routeProbability;
        this.random = new Random();

        setDistribution(Distribution.EXP);
        setTnext(getDelay());
    }

    public void setPreprocess(Process preprocess) {
        this.preprocess = preprocess;
    }

    public void setAssembly(Process assembly) {
        this.assembly = assembly;
    }

    @Override
    public void outAct() {
        super.outAct();

        for (int i = 0; i < batchSize; i++) {
            Detail d = new Detail(Detail.Type.RAW, getTcurr());
            createdDetails++;

            // Передаємо через inAct — єдина точка входу в наступний елемент
            if (random.nextDouble() < routeProbability) {
                preprocess.inAct(d);
            } else {
                assembly.inAct(d);
            }
        }

        setTnext(getTcurr() + getDelay());
    }

    @Override
    public void printResult() {
        super.printResult();
        System.out.println(getName() + " created details = " + createdDetails);
        System.out.println("////////////////////////////////////////////////////////////////");
    }
}