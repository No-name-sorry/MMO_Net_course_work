package universal;

import universal.util.Distribution;

import java.util.ArrayList;

public class SimModel {
    public static void main(String[] args) {
        Create c = new Create(2.0);
        Process p = new Process(1.0);
        System.out.println("id0 = " + c.getId() + " id1=" + p.getId());
        c.setNextElement(p);
        p.setMaxQueue(5);
        c.setName("CREATOR");
        p.setName("PROCESSOR");
        c.setDistribution(Distribution.EXP);
        p.setDistribution(Distribution.EXP);
        ArrayList<Element> list = new ArrayList<>();
        list.add(c);
        list.add(p);
        MMOModel model = new MMOModel(list);
//        model.simulate(1000.0, true);
    }
}
