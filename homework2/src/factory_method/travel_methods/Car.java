package factory_method.travel_methods;

/**
 * This is a concrete product.
 * Concrete products provide various implementations of the product interface.
 */
public class Car implements Transport {
    @Override
    public String travelMethod() {
        return "Driving a car on the road";
    }
}
