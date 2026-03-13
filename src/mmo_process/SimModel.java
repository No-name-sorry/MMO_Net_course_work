package mmo_process;

import java.util.List;

public class SimModel {

    public static void main(String[] args) {

        final double TIME_MODELING = 500000.0;

        final double OBSERVE_STEP = 1000.0;
        final int RUNS = 20;

        final double CREATE_DELAY = 30.0;
        final int BATCH_SIZE = 3;
        final double ROUTE_PROBABILITY = 0.5;

        final double PREPROCESS_DELAY = 7.0;
        final double ASSEMBLY_DELAY = 6.0;
        final double REGULATION_DELAY = 8.0;

        final double DEFECT_PROBABILITY = 0.04;

        Create create = new Create("Create", CREATE_DELAY, BATCH_SIZE, ROUTE_PROBABILITY);

        Process d2 = new Process("D2_Preprocess", PREPROCESS_DELAY, Process.ProcessType.PREPROCESS);
        Process d1 = new Process("D1_Assembly", ASSEMBLY_DELAY, Process.ProcessType.ASSEMBLY);
        Process d3 = new Process("D3_Regulation", REGULATION_DELAY, Process.ProcessType.REGULATION);

        d2.setNextProcess(d1);
        d1.setNextProcess(d3);
        d3.setPreprocessProcess(d2);
        d3.setDefectProbability(DEFECT_PROBABILITY);

        create.setPreprocess(d2);
        create.setAssembly(d1);

        Model model = new Model(List.of(create, d2, d1, d3));

        model.simulateWithObservation(TIME_MODELING, OBSERVE_STEP);

        model.simulate(TIME_MODELING);
        model.printAveragedResult(TIME_MODELING);

        System.out.println("RUN SERIES");

        for (int i = 0; i < RUNS; i++) {

            System.out.println("RUN " + (i + 1));

            Create createRun = new Create("Create", CREATE_DELAY, BATCH_SIZE, ROUTE_PROBABILITY);

            Process d2Run = new Process("D2_Preprocess", PREPROCESS_DELAY, Process.ProcessType.PREPROCESS);
            Process d1Run = new Process("D1_Assembly", ASSEMBLY_DELAY, Process.ProcessType.ASSEMBLY);
            Process d3Run = new Process("D3_Regulation", REGULATION_DELAY, Process.ProcessType.REGULATION);

            d2Run.setNextProcess(d1Run);
            d1Run.setNextProcess(d3Run);
            d3Run.setPreprocessProcess(d2Run);
            d3Run.setDefectProbability(DEFECT_PROBABILITY);

            createRun.setPreprocess(d2Run);
            createRun.setAssembly(d1Run);

            Model modelRun = new Model(List.of(createRun, d2Run, d1Run, d3Run));

            // warm-up
            modelRun.simulate(RUNS);

            modelRun.resetStatistics();

            // основний експеримент
            modelRun.simulate(TIME_MODELING);

            modelRun.printAveragedResult(TIME_MODELING);

            System.out.println("==================================");
        }
    }
}