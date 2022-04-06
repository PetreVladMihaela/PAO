package factory_method.travels;

import factory_method.travel_methods.Transport;

/**
 * Tne Creator Class -> declares the factory method that must return an object of a concrete product class.
 * The creator's subclasses usually provide the implementation of the factory method.
 * The creator may also provide some default implementation of the factory method.
 */
public abstract class Trip {
    private final String startingPlace;
    private final String destination;

    public Trip(String startingPlace, String destination) {
        this.startingPlace = startingPlace;
        this.destination = destination;
    }

    // The base factory method -> subclasses will override this method in order to create specific product objects.
    // It’s important that the return type of this method matches the product interface.
    public abstract Transport createTransport();

    public void traveling() {
        // Calls the factory method to create a product object in order to use the product.
        Transport transport = createTransport();
        System.out.println(transport.travelMethod()+" from "+startingPlace+" to "+destination);
    }
}
