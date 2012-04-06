package model.junctions;

import controller.Simulation;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import model.Car;
import model.Lane;
import model.Truck;
import model.Vehicle;

/**
 *
 * @author Dan
 */
public abstract class Junction {

    protected static final Color CAR_COLOR = new Color(0, 102, 153);
    protected static final Color TRUCK_COLOR = new Color(153, 0, 0);
    protected int numberOfVehicles;
    protected Random rnd;
    private ArrayList<Lane> lanes = new ArrayList<>();

    public Junction() {
        rnd = new Random(42);
    }

    public void distributeNewCars(int cars, int trucks) {
        // TODO
        int minDensity = (int) Simulation.getOption(Simulation.MIN_DENSITY);
        int maxDensity = (int) Simulation.getOption(Simulation.MAX_DENSITY);
        int variableDensity = rnd.nextInt((maxDensity - minDensity) + 1);
        int lowestRatio = (cars < trucks) ? cars : trucks;

        while (numberOfVehicles < variableDensity) {

            Lane l = chooseLane();
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
            
            if (rnd < lowestRatio) {
               new Car(l, l.getFirstSegment(), v, null, CAR_COLOR);
                numberOfVehicles++;
            } else {
                new Truck(l, l.getFirstSegment(),v, null,  TRUCK_COLOR);
                numberOfVehicles++;
            }
        }else {
            if (rnd < lowestRatio) {
                new Car(l, l.getFirstSegment(), CAR_COLOR);
                numberOfVehicles++;
            } else {
                new Truck(l, l.getFirstSegment(), TRUCK_COLOR);
                numberOfVehicles++;
            }
        }
    }

    public abstract void manageJunction();

    protected abstract Lane chooseLane();

    public ArrayList<Lane> getLanes() {
        return lanes;
    }

    public void registerLane(Lane lane) {
        lanes.add(lane);
    }
}
