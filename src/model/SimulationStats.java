package model;

/**
 *
 * @author David J. Barnes and Michael Kölling
 * @author Daniel Bond
 */
public class SimulationStats {
    
    private static int truckCount = 0;
    private static int carCount = 0;
    
    public static void reset(){
        truckCount = 0;
        carCount = 0;
    }
    
    public static void incrementCars() {
        carCount++;
    }
    
    public static void incrementTrucks() {
        truckCount++;
    }
    
    public static int getCarCount() {
        return carCount;
    }
    
    public static int getTruckCount() { 
        return truckCount;
    }
    
    public static void setCarCount(int value) {
        carCount = value;
    }
    
    public static void setTruckCount(int value) { 
        truckCount = value;
    }
}
