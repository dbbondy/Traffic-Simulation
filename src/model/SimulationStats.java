package model;

import controller.Simulation;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class that is used to generate the statistics of the simulation
 * @author Daniel Bond
 */
public class SimulationStats {
    
    private static int truckCount = 0; // the counter for trucks
    private static int carCount = 0; // the counter for cars
    private static BufferedWriter outputStream; // the output stream that will write the statistics to a file
    private static ArrayList<Event> allEvents = new ArrayList<>(32); // collection to store all statistical events
    
    
    /**
     * Resets all statistics
     */
    public static void reset(){
        truckCount = 0;
        carCount = 0;
        allEvents.clear();
    }
    
    /**
     * Increments the car counter
     */
    public static void incrementCars() {
        carCount++;
    }
    
    /**
     * Increments the truck counter
     */
    public static void incrementTrucks() {
        truckCount++;
    }
    
    /**
     * Gets the number of cars that have been counted so far
     * @return the number of cars that have been counted so far
     */
    public static int getCarCount() {
        return carCount;
    }
    
    /**
     * Gets the number of trucks that have been counted so far
     * @return the number of trucks that have been counted so far
     */
    public static int getTruckCount() { 
        return truckCount;
    }
    
    /**
     * Sets the number of cars that have been counted so far
     * @param value the value that will be the number of cars counted so far
     */
    public static void setCarCount(int value) {
        carCount = value;
    }
    
    /**
     * Sets the number of trucks that have been counted so far
     * @param value the value that will be the number of trucks counted so far
     */
    public static void setTruckCount(int value) { 
        truckCount = value;
    }
    
    /**
     * Create an event for the statistics to report
     * @param v the vehicle we have to report an event for
     * @return the event instance 
     */
    public static  Event createVehicleLeavingEvent(Vehicle v) {
        String name = "";
        if(v instanceof Car){
            name = "Car leaving Junction";
        }else if(v instanceof Truck){
            name = "Truck leaving Junction";
        }
        
        int aggression = (int) Simulation.getOption(Simulation.AGGRESSION);
        int timeStep = (int) Simulation.getOption(Simulation.TIME_STEP);
        int minDensity = (int) Simulation.getOption(Simulation.MIN_DENSITY);
        int maxDensity = (int) Simulation.getOption(Simulation.MAX_DENSITY);
        int ratioCars = (int) Simulation.getOption(Simulation.CAR_RATIO);
        int ratioTrucks = (int) Simulation.getOption(Simulation.TRUCK_RATIO);
        int maxSpeed = (int) Simulation.getOption(Simulation.MAXIMUM_SPEED);
        Event e = new Event(name, timeStep, aggression, minDensity, maxDensity, ratioCars, ratioTrucks, maxSpeed);
        return e;
    }
    
    /**
     * Starts the output stream for writing the statistical events to a file
     * @param filePath the path where the file containing the statistics will be stored
     */
    public static void startBuffer(String filePath){
        try{
            outputStream = new BufferedWriter(new FileWriter(filePath));
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    
    /**
     * Adds an event to the collection of events
     * @param e the event to add to the collection
     */
    public static void addEvent(Event e){
        allEvents.add(e);
    }
    
    /**
     * Outputs all events that have occurred to a file and produces a summary for the whole simulation run.
     * It also resets the counters for the statistics once the method completes.
     */
    public static void outputEvents(){
        for(Event e: allEvents){ //print single events details
            try{
                outputStream.write("Name of event: " + e.getName());
                outputStream.newLine();
                outputStream.write("Time step: " + e.getTimeStep());
                outputStream.newLine();
                outputStream.write("Aggression value: " + e.getAggression());
                outputStream.newLine();
                outputStream.write("Vehicle Minimum Density: " + e.getMinDensity());
                outputStream.newLine();
                outputStream.write("Vehicle Maximum Density: " + e.getMaxDensity());
                outputStream.newLine();
                outputStream.write("Ratio of Cars in Simulation: " + e.getRatioCars());
                outputStream.newLine();
                outputStream.write("Ratio of Trucks in Simulation: " + e.getRatioTrucks());
                outputStream.newLine();
                outputStream.write("Maximum Speed of Vehicles in Simulation: " + e.getMaxSpeed());
                outputStream.newLine();
                outputStream.write("-------------------------------------------------------------");
                outputStream.newLine();
                outputStream.write("-------------------------------------------------------------");
                outputStream.newLine();
            }catch(IOException ioe){
                ioe.printStackTrace();
            }
        } 
        //print a summary of the whole set of events
        try{
            outputStream.newLine();
            outputStream.write("Total output of Cars: " + carCount);
            outputStream.newLine();
            outputStream.write("Total output of Trucks: " + truckCount);
            outputStream.newLine();
            outputStream.write("Total vehicles outputted in Simulation: " + (carCount + truckCount));
            outputStream.newLine();
            outputStream.write("Simulation was run for: "  + Simulation.getTotalTimeSteps() + " time steps");
            outputStream.close();
        }catch(IOException ioex){
            ioex.printStackTrace();
        }
        
        reset(); //reset the counters once we output.
    }
}
