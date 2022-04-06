package strategy_example;

/**
 * The Context maintains a reference to one of the concrete strategies
 * and communicates with this object only via the strategy interface.
 * The context calls the execution method on the linked strategy object each time it needs to run the algorithm.
 */
public class Context {
    private Mean strategy;

    public Context() {}

    public Context(Mean strategy) {
        this.strategy = strategy;
    }

    // the strategy can be switched at runtime.
    public void setStrategy(Mean strategy) {
        this.strategy = strategy;
    }

    double executeStrategy(double... numbers) {
        return strategy.calculateMean(numbers);
    }
}
