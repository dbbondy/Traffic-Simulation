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

        //System.out.println(numberOfVehicles);



        while (numberOfVehicles < variableDensity) {


            Lane l = chooseLane();
            if (l != null) {
                l = chooseLane();
                System.out.println(l.getVehicles().size());
                System.out.println("numOfVehicles var: " + numberOfVehicles);
            }

            /*
             * if (numberOfVehicles == 14) { if (l != null) { for (Vehicle v : l.getVehicles()) { if (v.getVehicleBehind() == null) { int currentPos = l.getLaneSegments().indexOf(v.getHeadSegment());
             * int carInFront = l.getLaneSegments().indexOf(v.getVehicleInFront().getHeadSegment()); System.out.println((carInFront - currentPos) >= (v.getLength() + 5)); } else if
             * (v.getVehicleInFront() == null) { int currentPos = l.getLaneSegments().indexOf(v.getHeadSegment()); int carBehind = l.getLaneSegments().indexOf(v.getVehicleBehind().getHeadSegment());
             * System.out.println((currentPos - carBehind) >= (v.getLength() + 5)); }
             *
             * }
             * }
             * }
             */

            if (l == null) {
                variableDensity--;
                continue;
            } else {

                generateVehicle(l, lowestRatio);
            }
        }
    }

    private void generateVehicle(Lane l, int lowestRatio) {
        //double rnd = Math.random();
        //rnd = rnd * 10; //conversion to same scale as ratio of cars/trucks
        int rand = random.nextInt(11);
        if (!l.getVehicles().isEmpty()) {
            Vehicle v = l.getVehicleAhead(l.getFirstSegment());
            if (rand <= lowestRatio) {
                new Car(l, l.getFirstSegment(), v, null, CAR_COLOR);
                numberOfVehicles++;

            } else {
                new Truck(l, l.getFirstSegment(), v, null, TRUCK_COLOR);
                numberOfVehicles++;

            }
        } else {
            if (rand <= lowestRatio) {
                new Car(l, l.getFirstSegment(), CAR_COLOR);
                numberOfVehicles++;
            } else {
                new Truck(l, l.getFirstSegment(), TRUCK_COLOR);
                numberOfVehicles++;
            }
        }
    }

    protected Lane chooseLane() {
        ArrayList<Lane> potentialLanes = new ArrayList<>(getLanes().size());
        int lanes = 0;
        for (Lane l : getLanes()) {
            //System.out.println("lane " + lanes++ + "has " + l.getVehicles().size() + "vehicles");
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

    public abstract void manageJunction();

    public ArrayList<Lane> getLanes() {
        return lanes;
    }

    public void registerLane(Lane lane) {
        lanes.add(lane);
    }
}
