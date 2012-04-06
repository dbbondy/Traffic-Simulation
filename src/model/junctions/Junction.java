package model.junctions;

import controller.Simulation;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import model.*;

/**
 *
 * @author Dan
 */
public abstract class Junction {

    protected static final Color CAR_COLOR = new Color(0, 102, 153);
    protected static final Color TRUCK_COLOR = new Color(153, 0, 0);
    protected int numberOfVehicles;
    protected Random random;
    private ArrayList<Lane> lanes = new ArrayList<>();

    public Junction() {
        random = new Random(42);
        numberOfVehicles = 0;
    }

    public void distributeNewCars(int cars, int trucks) {
        // TODO
        int minDensity = (int) Simulation.getOption(Simulation.MIN_DENSITY);
        int maxDensity = (int) Simulation.getOption(Simulation.MAX_DENSITY);
        int variableDensity = random.nextInt((maxDensity - minDensity) + 1);
        int lowestRatio = (cars < trucks) ? cars : trucks;

        while (numberOfVehicles < variableDensity) {
            Lane l = chooseLane();
            if (l != null) {
                l = chooseLane();
                System.out.println(l.getVehicles().size());
                System.out.println("numOfVehicles var: " + numberOfVehicles);
            }

            if (l == null) {
                variableDensity--;
                continue;
            } else {
                generateVehicle(l, lowestRatio);
            }
        }
    }

    private void generateVehicle(Lane l, int lowestRatio) {
        double rnd = Math.random();
        rnd = rnd * 10; //conversion to same scale as ratio of cars/trucks

        if (!l.getVehicles().isEmpty()) {
            Vehicle v = l.getVehicleAhead(l.getFirstSegment());
            if (rnd <= lowestRatio) {
                new Car(l, l.getFirstSegment(), v, null, CAR_COLOR);
                numberOfVehicles++;

            } else {
                new Truck(l, l.getFirstSegment(), v, null, TRUCK_COLOR);
                numberOfVehicles++;

            }
        } else {
            if (rnd <= lowestRatio) {
                new Car(l, l.getFirstSegment(), CAR_COLOR);
                numberOfVehicles++;
            } else {
                new Truck(l, l.getFirstSegment(), TRUCK_COLOR);
                numberOfVehicles++;
            }
        }
    }

    protected Lane chooseLane() {
        ArrayList<Lane> potentialLanes = new ArrayList<>(lanes.size());
        for (Lane l : lanes) {
            if (l.getVehicles().isEmpty()) { //if lane is devoid of vehicles. it is obviously a potential lane.
                potentialLanes.add(l);
                continue;
            }
            Segment firstSegment = l.getFirstSegment();
            Vehicle closestVehicleToStart = l.getVehicleAhead(firstSegment);
            Vehicle atStart = l.getVehicleBehind(firstSegment);
            Segment closestVehiclePosition;
            int closestVehicleIndex;
            if (atStart == null) {
                atStart = closestVehicleToStart;
                closestVehiclePosition = atStart.getHeadSegment();
                closestVehicleIndex = l.getLaneSegments().indexOf(closestVehiclePosition);
            } else {
                closestVehiclePosition = atStart.getHeadSegment();
                closestVehicleIndex = l.getLaneSegments().indexOf(closestVehiclePosition);
            }



            if (closestVehiclePosition.equals(firstSegment)) { //if there is a vehicle in the first segment of this lane.
                continue;
            } else if ((closestVehicleIndex) < (closestVehicleToStart.getLength() + 10)) { //if there is no vehicle immediately in front, but is long enough to occupy some space of where the vehicle is to be positioned
                continue;
            } else {
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
            ArrayList<Vehicle> vehArray = l.getVehicles();
            synchronized (vehArray) {
                for (Vehicle v : vehArray) {
                    if (v.getHeadSegment().equals(l.getLastSegment())) {
                        vehArray.remove(v);
                        numberOfVehicles--;
                        if (v instanceof Car) {
                            SimulationStats.increment(Car.class);
                        } else if (v instanceof Truck) {
                            SimulationStats.increment(Truck.class);
                        }


                    }
                }
            }

        }
    }

    public void manageJunction() {
        for (Lane l : lanes) {
            for (Vehicle v : l.getVehicles()) {
                Segment head = v.getHeadSegment();
                if (head.getNextSegment() != null) {
                    v.setHeadSegment(head.getNextSegment());
                }
            }
        }
    }

    public ArrayList<Lane> getLanes() {
        return lanes;
    }

    public void registerLane(Lane lane) {
        lanes.add(lane);
    }
}
