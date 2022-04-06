package factory_method.travels;

import factory_method.travel_methods.Plane;
import factory_method.travel_methods.Transport;

/**
 * This is a concrete creator - a subclass of the creator class.
 * Concrete creators override the base factory method so it returns a specific type of product.
 */
public class TravelByPlane extends Trip{
    public TravelByPlane(String startingPlace, String destination) {
        super(startingPlace, destination);
    }

    @Override
    public Transport createTransport() {
        return new Plane();
    }
}
