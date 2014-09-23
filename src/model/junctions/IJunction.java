package model.junctions;

import model.Lane;
import model.Vehicle;

import java.util.List;

public interface IJunction {

    void removeVehicles();

    List<Vehicle> getVehicles();

    void updateVehicles();

    void distributeNewCars(int cars, int trucks);

    Lane chooseLane();

    void updateDeletions();

    List<Lane> getLanes();

    void registerLane(Lane lane);

    void manageJunction();
}
