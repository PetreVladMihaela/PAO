package strategy_example;

/**
 * Concrete strategy - calculates the harmonic mean of the given numbers.
 * Concrete Strategies implement different variations of an algorithm the context uses.
 */
public class HarmonicMean implements Mean{
    @Override
    public double calculateMean(double... numbers) {
        double reciprocals_sum = 0;
        for (double n : numbers) {
            reciprocals_sum += 1/n;
        }
        double mean = numbers.length/reciprocals_sum;
        return Math.round(mean*10000.0)/10000.0;
    }
}
