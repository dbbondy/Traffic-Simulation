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

    private static HashMap<Class, VehicleCounter> vehicleCounters = new HashMap<>();

    /**
     * resets the statistics of the simulation.
     */
    public static void reset(){
        for (Class vehType : vehicleCounters.keySet()) {
            VehicleCounter count = vehicleCounters.get(vehType);
            count.reset();
        }
    }

    /**
     * Increments the count of vehicles that have exited the junction area.
     * @param vehicle the vehicle type to increment
     */
    public static void increment(Class vehicle) {
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
    public static void publishStats() {
        try {
            FileWriter fStream = new FileWriter("Simulation Statistics.txt");
            try (BufferedWriter bwout = new BufferedWriter(fStream)) {
                for (Class v : vehicleCounters.keySet()) {
                    bwout.write("Vehicle type: " + v.getName() + "\n");
                    bwout.write("Number of vehicles of this type: " + vehicleCounters.get(v).getCount() + "\r \n");
                }
                bwout.flush();
            }
        } catch (IOException ioe) {
            System.out.println("failed to print to file correctly.");
        }
    }
}
