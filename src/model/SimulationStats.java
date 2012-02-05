package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author David J. Barnes and Michael Kölling
 * @author Daniel Bond
 */
public class SimulationStats {

    private HashMap<Class, VehicleCounter> vehicleCounters;

    public SimulationStats() {
        vehicleCounters = new HashMap<Class, VehicleCounter>();
    }

    /**
     * resets the statistics of the simulation.
     */
    public void reset(){
        for (Class vehType : vehicleCounters.keySet()) {
            VehicleCounter count = vehicleCounters.get(vehType);
            count.reset();
        }
    }

    /**
     * Increments the count of vehicles that have exited the junction area.
     * @param vehicle the vehicle type to increment
     */
    public void increment(Class vehicle) {
        VehicleCounter count = vehicleCounters.get(vehicle);
        if (count == null) {
            count = new VehicleCounter(vehicle.getName());
            vehicleCounters.put(vehicle, count);
        }
        count.increment();
    }

    /**
     * Publishes the statistics gathered by this class to a file named "Simulation Statistics.txt"
     */
    public void publishStats() {
        try {
            FileWriter fStream = new FileWriter("Simulation Statistics.txt");
            BufferedWriter bwout = new BufferedWriter(fStream);
            
            for (Class v : vehicleCounters.keySet()) {
                bwout.write("Vehicle type: " + v.getName() + "\n");
                bwout.write("Number of vehicles of this type: " + vehicleCounters.get(v).getCount() + "\r \n");
            }
            bwout.flush();
            bwout.close();
        } catch (IOException ioe) {
            System.out.println("failed to print to file correctly.");
        }
    }
}
