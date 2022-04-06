package strategy_example;

/**
 * The Client passes a specific strategy object to the context.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Numbers: "+2+", "+5+", "+10);

        Context context = new Context(new ArithmeticMean());
        System.out.println("Arithmetic Mean: " + context.executeStrategy(2, 5, 10));

        context.setStrategy(new GeometricMean());
        System.out.println("Geometric Mean: " + context.executeStrategy(2, 5, 10));

        context.setStrategy(new HarmonicMean());
        System.out.println("Harmonic Mean: " + context.executeStrategy(2, 5, 10));
    }
}
