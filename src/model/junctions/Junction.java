package model.junctions;

import controller.Simulation;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import model.*;

/**
 *
 * @author Dan
 */
public abstract class Junction {

    private static HashMap<String, Class> namedJunctionClasses = new HashMap<>();
    protected static final Color CAR_COLOR = new Color(0, 102, 153);
    protected static final Color TRUCK_COLOR = new Color(153, 0, 0);
    protected int numberOfVehicles;
    protected Random random;
    private ArrayList<Lane> lanes = new ArrayList<>();
    private int variableDensityCounter = 0;
    private int variableDensity = 0;

    public Junction() {
        random = Randomizer.getInstance();
        numberOfVehicles = 0;
    }

    public static void registerJunctionType(Class cls) {
        try {
            namedJunctionClasses.put((String) cls.getDeclaredField("name").get(cls), cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Class getJunctionTypeByName(String name) {
        return namedJunctionClasses.get(name);
    }

    public static Set<String> getJunctionNames() {
        return namedJunctionClasses.keySet();
    }

    public void removeVehicles() {
        numberOfVehicles = 0;
        for (Lane lane : lanes) {
            lane.removeVehicles();
        }
    }

    public ArrayList<Vehicle> getVehicles() {
        ArrayList<Vehicle> allVehicles = new ArrayList<>();
        for (Lane lane : lanes) {
            allVehicles.addAll(lane.getVehicles());
        }
        return allVehicles;
    }

    public void updateNumberOfVehicles() {
        numberOfVehicles = getVehicles().size();
    }

    public void distributeNewCars(int cars, int trucks) {

        if (random.nextInt(5) == 0) {
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

        if (numberOfVehicles < localVariableDensity) {
            Lane l = chooseLane();
            if (l != null) {
                generateVehicle(l, cars, trucks);
            }
        }
    }

    private void generateVehicle(Lane l, int carRatio, int truckRatio) {
        int totalRatio = carRatio + truckRatio;
        int randRatio = random.nextInt(totalRatio);
        Vehicle v = l.getVehicleAhead(l.getFirstSegment());
        if (randRatio < carRatio) {
            new Car(l, l.getFirstSegment(), v, null, CAR_COLOR);
            numberOfVehicles++;
        } else {
            new Truck(l, l.getFirstSegment(), v, null, TRUCK_COLOR);
            numberOfVehicles++;
        }
    }

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
            // TODO: -5 is a constant. make it depend on the initial speed of the car!!!!
            if (firstSegment.id() < vehicleSegment.id() - firstVehicle.getLength() - 5) {
                potentialLanes.add(l);
            }
        }

        if (potentialLanes.isEmpty()) {
            return null;
        } else {
            return potentialLanes.get(random.nextInt(potentialLanes.size()));
        }
    }

    public void updateDeletions() {
        for (Lane l : lanes) {
            Vehicle[] vehArray = l.getVehicles().toArray(new Vehicle[0]);
            for (Vehicle v : vehArray) {
                if (v.getHeadSegment().equals(l.getLastSegment())) {
                    l.getVehicles().remove(v);
                    numberOfVehicles--;
                    if (v instanceof Car) {
                        SimulationStats.incrementCars();
                        SimulationStats.addEvent(createEvent(v));
                    } else if (v instanceof Truck) {
                        SimulationStats.incrementTrucks();
                        SimulationStats.addEvent(createEvent(v));
                    }
                }
            }
        }
    }

    private Event createEvent(Vehicle v) {
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

    public void manageJunction() {
        for (Vehicle v : this.getVehicles()) {
            v.act();
        }
    }

    public ArrayList<Lane> getLanes() {
        return lanes;
    }

    public void registerLane(Lane lane) {
        lanes.add(lane);
    }
}
