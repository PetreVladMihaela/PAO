package factory_method.travel_methods;

/**
 * This is a concrete product.
 * Concrete products provide various implementations of the product interface.
 */
public class Train implements Transport{
    @Override
    public String travelMethod() {
        return "Traveling by Train on railway tracks";
    }
}
