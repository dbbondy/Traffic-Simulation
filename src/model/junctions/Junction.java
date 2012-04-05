
package model.junctions;

import controller.Simulation;
import java.awt.Color;
import java.util.ArrayList;
import model.Car;
import model.Lane;
import model.Truck;

/**
 *
 * @author Dan
 */
public abstract class Junction{
    
    protected static final Color CAR_COLOR = new Color(0, 102, 153);
    protected static final Color TRUCK_COLOR = new Color(153, 0, 0);
    protected int numberOfVehicles;
    
    
    private ArrayList<Lane> lanes = new ArrayList<>();
    
    
    public void distributeNewCars(int cars, int trucks) {
        // TODO
        int density = (int) Simulation.getOption(Simulation.MIN_DENSITY);

        if (numberOfVehicles < density) {
            int lowestRatio = (cars < trucks) ? cars : trucks;
            if (density - numberOfVehicles == density) { // if num of vehicles is 0.
                for (Lane l : getLanes()) {
                    generateVehicle(l, lowestRatio);
                }
            } else if ((density - numberOfVehicles) > getLanes().size()) { // if difference between density and number of vehicles on road is greater than the number of lanes
                for (int i = 0; i < getLanes().size(); i++) {
                    Lane l = chooseLane();
                    generateVehicle(l, lowestRatio);
                }
            } else { //else the difference between density value and number of vehicles on road is less than the number of lanes.
                for (int i = 0; i < (density - numberOfVehicles); i++) { // 0 <= i <= (density - number of vehicles)
                    Lane l = chooseLane();
                    generateVehicle(l, lowestRatio);
                }
            }
        }
    }

    private void generateVehicle(Lane l, int lowestRatio) {
        double rnd = Math.random();
        rnd = rnd * 10; //conversion to same scale as ratio of cars/trucks
        if (rnd < lowestRatio) {
            new Car(l, l.getFirstSegment(), CAR_COLOR);
            numberOfVehicles++;
        } else {
            new Truck(l, l.getFirstSegment(), TRUCK_COLOR);
            numberOfVehicles++;
        }
    }

    public abstract void manageJunction();
    protected abstract Lane chooseLane();
        
    public ArrayList<Lane> getLanes(){
        return lanes;
    }
     public void registerLane(Lane lane){
        lanes.add(lane);
    }
    
}
