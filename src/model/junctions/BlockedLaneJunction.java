package model.junctions;

import controller.Simulation;
import model.*;
import view.SimulationPanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class to model a junction with a blocked lane
 *
 * @author Daniel Bond
 */
public class BlockedLaneJunction implements IJunction{

    public static final String NAME = "Blocked-Lane Junction";
    private Lane unblockedLane;
    private Lane blockedLane;
    private int numberOfVehicles;
    private int variableDensityCounter = 0; // counter for determining when we find a new density
    private int variableDensity = 0; // variable density value for varying the number of vehicles that can be instantiated
    private ThreadLocalRandom random;
    private static final Color CAR_COLOR = new Color(0, 102, 153); // colour of cars in the junction
    private static final Color TRUCK_COLOR = new Color(153, 0, 0); // colour of trucks in the junction
    private List<Lane> lanes;


    public BlockedLaneJunction() {

        unblockedLane = new Lane(400 - Segment.WIDTH, SimulationPanel.HEIGHT, 180, TurnDirection.STRAIGHT); 
        blockedLane = new Lane(400, SimulationPanel.HEIGHT, 180, TurnDirection.BLOCKED);
        random = ThreadLocalRandom.current();
        lanes = new ArrayList<>();

        buildRoads();
        registerLane(unblockedLane);
        registerLane(blockedLane);
    }

    private void buildRoads() {
        RoadDesigner.buildParallelLanes(SimulationPanel.HEIGHT / 2, unblockedLane, blockedLane);
        Segment[] s = RoadDesigner.buildStraight(SimulationPanel.HEIGHT / 2, unblockedLane);
        unblockedLane.add(s);
    }

    @Override
    public void updateDeletions() {
        Vehicle[] vehArray = unblockedLane.getVehicles().toArray(new Vehicle[0]);
        for (Vehicle v : vehArray) {
            if (v.getHeadSegment().equals(unblockedLane.getLastSegment())) {
                unblockedLane.getVehicles().remove(v);
                numberOfVehicles--;
                if (v instanceof Car) {
                    SimulationStats.addEvent(SimulationStats.createVehicleLeavingEvent(v));
                } else if (v instanceof Truck) {
                    SimulationStats.addEvent(SimulationStats.createVehicleLeavingEvent(v));
                }
            }
        }
    }
    
    @Override
    public String toString(){
        return NAME;
    }

    @Override
    public void removeVehicles() {
        numberOfVehicles = 0;
        unblockedLane.removeVehicles();
        blockedLane.removeVehicles();
    }

    @Override
    public List<Vehicle> getVehicles() {
        List<Vehicle> allVehicles = new ArrayList<>();
        for (Lane lane : lanes) {
            allVehicles.addAll(lane.getVehicles());
        }

        return allVehicles;
    }

    @Override
    public void updateVehicles() {
        numberOfVehicles = getVehicles().size();
    }

    /**
     * Distributes new cars given a ratio of cars and trucks
     * @param cars the cars value of the ratio
     * @param trucks the trucks value of the ratio
     */
    @Override
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

    @Override
    public Lane chooseLane() {
        List<Lane> potentialLanes = new ArrayList<>();
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

    @Override
    public void manageJunction() {
        for (Vehicle vehicle : getVehicles()) {
            vehicle.act();
        }
    }

    @Override
    public List<Lane> getLanes() {
        return lanes;
    }

    @Override
    public void registerLane(Lane lane) {
        lanes.add(lane);
    }
}
