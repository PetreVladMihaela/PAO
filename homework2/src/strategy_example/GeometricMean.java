package strategy_example;

import java.lang.Math;

/**
 * Concrete strategy - calculates the geometric mean of the given numbers.
 * Concrete Strategies implement different variations of an algorithm the context uses.
 */
public class GeometricMean implements Mean{
    @Override
    public double calculateMean(double... numbers) {
        double product = 1;
        for (double n : numbers) {
            product *= n;
        }
        double mean = Math.pow(product, 1.0/numbers.length);
        return Math.round(mean*10000.0)/10000.0;
    }
}
