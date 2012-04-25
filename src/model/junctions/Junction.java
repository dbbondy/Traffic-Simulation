package model.junctions;

import controller.Simulation;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import model.*;

/**
 * Abstract representation of a Junction
 * @author Daniel Bond
 */
public abstract class Junction {

    private static HashMap<String, Class> namedJunctionClasses = new HashMap<>(); // the names of each of the registered junction classes
    protected static final Color CAR_COLOR = new Color(0, 102, 153); // colour of cars in the junction
    protected static final Color TRUCK_COLOR = new Color(153, 0, 0); // colour of trucks in the junction
    protected int numberOfVehicles; // number of vehicles in the junction
    protected Random random; // random number generator
    private ArrayList<Lane> lanes = new ArrayList<>(); // collection of lanes in the junction
    private int variableDensityCounter = 0; // counter for determining when we find a new density 
    private int variableDensity = 0; // variable density value for varying the number of vehicles that can be instantiated

    public Junction() {
        random = Randomizer.getInstance();
        numberOfVehicles = 0;
    }

    /**
     * Registers a junction by its class reference.
     * If the junction declares a field with its "name" stored in it, then the junction is registered successfully.
     * If the junction we want to register does not declare a field named "name" then this method throws a {@link java.lang.NoSuchFieldException}
     * @param c the class object of the Junction we want to register
     */
    public static void registerJunctionType(Class c) {
        try {
            namedJunctionClasses.put((String) c.getDeclaredField("NAME").get(c), c);
            
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a junction type by its name
     * @param name the name to look up
     * @return the class object of the junction. Returns <code> null </code> if there was no junction with the name specified.
     */
    public static Class getJunctionTypeByName(String name) {
        return namedJunctionClasses.get(name);
    }

    /**
     * Gets the set of junction names stored 
     * @return the set of junction names
     */
    public static Set<String> getJunctionNames() {
        return namedJunctionClasses.keySet();
    }

    /**
     * Removes all vehicles from the junction
     */
    public void removeVehicles() {
        numberOfVehicles = 0;
        for (Lane lane : lanes) {
            lane.removeVehicles();
        }
    }

    /**
     * Gets all the vehicles contained in the junction
     * @return the collection of all vehicles in the junction
     */
    public ArrayList<Vehicle> getVehicles() {
        ArrayList<Vehicle> allVehicles = new ArrayList<>();
        for (Lane lane : lanes) {
            allVehicles.addAll(lane.getVehicles());
        }
        return allVehicles;
    }

    /**
     * Updates the numberOfVehicles in the junction
     */
    public void updateNumberOfVehicles() {
        numberOfVehicles = getVehicles().size();
    }

    /**
     * Distributes new cars given a ratio of cars and trucks
     * @param cars the cars value of the ratio
     * @param trucks the trucks value of the ratio
     */
    public void distributeNewCars(int cars, int trucks) {

        if (random.nextInt(5) == 0) { // we don't always want to distribute new cars every time step, so if a random number is 0 we don't do anything
            return;
        }

        if (variableDensityCounter % 200 == 0) {
            // choose a new density every ~ 1 second otherwise it will tend
            // towards the max density as a result of more this method
            // being executed more often than a car is removed.
            int minDensity = (int) Simulation.getOption(Simulation.MIN_DENSITY);
            int maxDensity = (int) Simulation.getOption(Simulation.MAX_DENSITY);
            variableDensity = random.nextInt((maxDensity - minDensity) + 1) + minDensity;
        }

        int localVariableDensity = variableDensity;

        if (numberOfVehicles < localVariableDensity) { // we still need to make vehicles as numberOfVehicles < localVariableDensity
            Lane l = chooseLane();
            if (l != null) {
                generateVehicle(l, cars, trucks);
            }
        }
    }

    /**
     * Generates a vehicle with a random distribution and instantiation ratio
     * @param l the lane to add the vehicle to
     * @param carRatio the ratio value for cars
     * @param truckRatio the ratio value for trucks
     */
    private void generateVehicle(Lane l, int carRatio, int truckRatio) {        
        int totalRatio = carRatio + truckRatio;
        int randRatio = random.nextInt(totalRatio);
        Vehicle v = l.getVehicleAhead(l.getFirstSegment());
        if (randRatio < carRatio) { // if ratio < carRatio, then make car
            new Car(l, l.getFirstSegment(), v, null, CAR_COLOR);
            numberOfVehicles++;
        } else { // else we make truck
            new Truck(l, l.getFirstSegment(), v, null, TRUCK_COLOR);
            numberOfVehicles++;
        }
    }

    /**
     * Choose a lane to add a vehicle to, if that lane is not occupied by a vehicle at the start of its lane
     * @return a lane that a vehicle can be added to
     */
    protected Lane chooseLane() {
        ArrayList<Lane> potentialLanes = new ArrayList<>(lanes.size());
        for (Lane l : lanes) {
            if (l.getVehicles().isEmpty()) {
                potentialLanes.add(l);
                continue;
            }
            Vehicle firstVehicle = l.getVehicles().get(0);
            Segment vehicleSegment = firstVehicle.getHeadSegment();
            Segment firstSegment = l.getFirstSegment();
            
            // written by Jonathan Pike (mats@staite.net)
            int allVehicleMaxDeceleration = Math.min(Car.MAX_DECELERATION_RATE, Truck.MAX_DECELERATION_RATE);
            int stoppingTimeDistance = (int) (((double) firstVehicle.getSpeed()) / allVehicleMaxDeceleration);
            int distance = vehicleSegment.id() - firstVehicle.getLength() - firstSegment.id() - 1;
            int crashTimeDistance = (int) ((distance * 100.0) / firstVehicle.getSpeed());
            if (crashTimeDistance > stoppingTimeDistance) {
                potentialLanes.add(l);
            }
            
            /* Daniel's version. does not depend on initial speed of vehicle in lane, so is buggy in certain cases
            if (firstSegment.id() < vehicleSegment.id() - firstVehicle.getLength() - 5) {
                potentialLanes.add(l);
            }*/
        }

        if (potentialLanes.isEmpty()) { // if no lanes could be found, return null, as there are no lanes suitable
            return null;
        } else { // else pick one at random.
            return potentialLanes.get(random.nextInt(potentialLanes.size()));
        }
    }

    /**
     * Update the deletions of vehicles exiting their lanes and the simulation.
     * This method also adds an event to the statistics to indicate that a deletion had occurred
     */
    public void updateDeletions() {
        for (Lane l : lanes) {
            Vehicle[] vehArray = l.getVehicles().toArray(new Vehicle[0]);
            for (Vehicle v : vehArray) {
                if (v.getHeadSegment().equals(l.getLastSegment())) {
                    l.getVehicles().remove(v);
                    numberOfVehicles--;
                    if (v instanceof Car) {
                        SimulationStats.incrementCars();
                        SimulationStats.addEvent(SimulationStats.createVehicleLeavingEvent(v));
                    } else if (v instanceof Truck) {
                        SimulationStats.incrementTrucks();
                        SimulationStats.addEvent(SimulationStats.createVehicleLeavingEvent(v));
                    }
                }
            }
        }
    }

    /**
     * Manages the vehicles in the junction
     */
    public void manageJunction() {
        for (Vehicle v : this.getVehicles()) {
            v.act();
        }
    }

    /**
     * Gets the collection of lanes in the junction
     * @return the collection of lanes in the junction
     */
    public ArrayList<Lane> getLanes() {
        return lanes;
    }

    /**
     * Registers a lane with the junction
     * @param lane the lane to register with the junction
     */
    public void registerLane(Lane lane) {
        lanes.add(lane);
    }
}
