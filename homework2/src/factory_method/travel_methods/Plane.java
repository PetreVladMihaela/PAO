package factory_method.travel_methods;

/**
 * This is a concrete product.
 * Concrete products provide various implementations of the product interface.
 */
public class Plane implements Transport{
    @Override
    public String travelMethod() {
        return "Flying on a Plane in the sky";
    }
}
