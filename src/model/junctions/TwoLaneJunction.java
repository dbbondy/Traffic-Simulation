/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.junctions;

import model.Car;
import model.Lane;
import model.Vehicle;

/**
 *
 * @author Dan
 */
public class TwoLaneJunction extends Junction{

   
    private Lane bottomUpwardsLane; //lane comes from bottom, up into the center
    private Lane bottomUpwardsLane2;
    private Lane bottomDownwardsLane; //lane comes from center down to the bottom of the screen
    private Lane bottomDownwardsLane2;
    private Lane leftRightwardsLane; //lane comes from left, towards the center 
    private Lane leftRightwardsLane2;
    private Lane leftLeftwardsLane; //lane comes from center, towards the left exit
    private Lane leftLeftwardsLane2;
    private Lane rightLeftwardsLane; //lane comes from right, towards the center
    private Lane rightLeftwardsLane2;
    private Lane rightRightwardsLane; //lane comes from center, towards the right exit
    private Lane rightRightwardsLane2;
    private Lane upDownwardsLane; //lane comes from top, towards the center
    private Lane upDownwardsLane2;
    private Lane upUpwardsLane; //lane comes from center towards the upwards exit.
    private Lane upUpwardsLane2;

    public TwoLaneJunction() {
        bottomUpwardsLane = new Lane(0, 0);
        bottomUpwardsLane2 = new Lane(0, 0);
        bottomDownwardsLane = new Lane(0, 0);
        bottomDownwardsLane2 = new Lane(0, 0);
        leftRightwardsLane = new Lane(0, 0);
        leftRightwardsLane2 = new Lane(0, 0);
        leftLeftwardsLane = new Lane(0, 0);
        leftLeftwardsLane2 = new Lane(0, 0);
        rightLeftwardsLane = new Lane(0, 0);
        rightLeftwardsLane2 = new Lane(0, 0);
        rightRightwardsLane = new Lane(0, 0);
        rightRightwardsLane2 = new Lane(0, 0);
        upDownwardsLane = new Lane(0, 0);
        upDownwardsLane2 = new Lane(0, 0);
        upUpwardsLane = new Lane(0, 0);
        upUpwardsLane2 = new Lane(0, 0);
        
        registerAllLanes();
        //TODO once a car is on the screen, change these (0,0) coords to be correct. JUST A TEMPORARY MEASURE AT THE MOMENT.
                
    }
    
    private void registerAllLanes(){
        registerLane(bottomUpwardsLane);
        registerLane(bottomUpwardsLane2);
        registerLane(bottomDownwardsLane);
        registerLane(bottomDownwardsLane2);
        registerLane(leftRightwardsLane);
        registerLane(leftRightwardsLane2);
        registerLane(leftLeftwardsLane);
        registerLane(leftLeftwardsLane2);
        registerLane(rightLeftwardsLane);
        registerLane(rightLeftwardsLane2);
        registerLane(rightRightwardsLane);
        registerLane(rightRightwardsLane2);
        registerLane(upDownwardsLane);
        registerLane(upDownwardsLane2);
        registerLane(upUpwardsLane);
        registerLane(upUpwardsLane2);
        
        
    }
    
    @Override
    public void distributeNewCars(int cars, int trucks) {
        //placeholder method body. will be different to this in full code.
        Vehicle v1 = new Car(bottomUpwardsLane, bottomUpwardsLane.getFirstSegment()); 
        bottomUpwardsLane.addVehicle(v1);
    }

    @Override
    public void manageJunction(){
        //deal with cars at ends of junctions and either put them into new lanes, or remove them from the simulation altogether
        bottomUpwardsLane.updateVehicles();
    }
    
    
}
