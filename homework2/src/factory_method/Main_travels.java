package factory_method;

import factory_method.travels.TravelByCar;
import factory_method.travels.TravelByPlane;
import factory_method.travels.TravelByTrain;
import factory_method.travels.Trip;

/**
 * The client code works with an instance of a concrete creator through its base interface.
 */
public class Main_travels {
    public static void main(String[] args) {
        Trip trip1 = new TravelByCar("Bucharest", "Constanta");
        trip1.traveling();

        Trip trip2 = new TravelByPlane("Rome", "London");
        trip2.traveling();

        Trip trip3 = new TravelByTrain("Iasi", "Bucharest");
        trip3.traveling();
    }
}
