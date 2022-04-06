package factory_method.travels;

import factory_method.travel_methods.Train;
import factory_method.travel_methods.Transport;

/**
 * This is a concrete creator - a subclass of the creator class.
 * Concrete creators override the base factory method so it returns a specific type of product.
 */
public class TravelByTrain extends Trip{
    public TravelByTrain(String startingPlace, String destination) {
        super(startingPlace, destination);
    }

    @Override
    public Transport createTransport() {
        return new Train();
    }
}
