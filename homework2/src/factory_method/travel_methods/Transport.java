package factory_method.travel_methods;

/**
 * The Product Interface - common interface for all the objects(products)
 *                         that can be produced by the creator and its subclasses.
 * This interface declares the operations that all concrete products must implement.
 */
public interface Transport {
    String travelMethod();
}
