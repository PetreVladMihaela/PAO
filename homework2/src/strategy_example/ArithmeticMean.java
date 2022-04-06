package strategy_example;

/**
 * Concrete strategy - calculates the arithmetic mean of the given numbers.
 * Concrete Strategies implement different variations of an algorithm the context uses.
 */
public class ArithmeticMean implements Mean{
    @Override
    public double calculateMean(double... numbers) {
        double sum = 0;
        for (double n : numbers) {
            sum += n;
        }
        double mean = sum/numbers.length;
        return Math.round(mean*10000.0)/10000.0;
    }
}
