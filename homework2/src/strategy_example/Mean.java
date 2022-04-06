package strategy_example;

/**
 * Common interface for all concrete strategies.
 * It declares a method the context uses to execute a strategy.
 */
public interface Mean {
    double calculateMean(double... numbers);
}
