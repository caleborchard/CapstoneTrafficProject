import java.util.Random;

public class ExponentialDistribution extends RandomDistribution {
    private double lambda;

    public ExponentialDistribution(double lambda) { this.lambda = lambda; }

    @Override public double sample() {
        Random r = new Random();
        return -(1 / lambda) * Math.log(r.nextDouble(1));
    }
}