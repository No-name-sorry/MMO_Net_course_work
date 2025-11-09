package universal.util;
import java.util.Random;

public class FunRand {
    /**
     * Generates a random value according to an exponential
     * distribution
     *
     * @param timeMean mean value
     * @return a random value according to an exponential
     * distribution
     */
    public static double Exp(double timeMean) {
        double a = 0;
        while (a == 0) {
            a = Math.random();
        }
        a = -timeMean * Math.log(a);
        return a;
    }

    /**
     * Generates a random value according to a uniform
     * distribution
     *
     * @param timeMin
     * @param timeMax
     * @return a random value according to a uniform distribution
     */
    public static double Unif(double timeMin, double timeMax) {
        double a = 0;
        while (a == 0) {
            a = Math.random();
        }
        a = timeMin + a * (timeMax - timeMin);
        return a;
    }

    /**
     * Generates a random value according to a normal (Gauss)
     * distribution
     *
     * @param timeMean
     * @param timeDeviation
     * @return a random value according to a normal (Gauss)
     * distribution
     */
    public static double Norm(double timeMean, double timeDeviation) {
        double a;
        Random r = new Random();
        a = timeMean + timeDeviation * r.nextGaussian();
        return a;
    }

    /**
     * Generates a random value according to an Erlang distribution
     * Erlang distribution is a sum of k exponential distributions with rate lambda
     *
     * @param timeMean mean value
     * @param k shape parameter
     * @return a random value according to an Erlang distribution
     */
    public static double Erlang(double timeMean, int k) {
        double lambda = k / timeMean;
        double sum = 0;
        for (int i = 0; i < k; i++) {
            sum += Exp(1.0 / lambda);
        }
        return sum;
    }
}