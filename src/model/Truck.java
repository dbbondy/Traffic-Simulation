package model;

import java.awt.Color;

/**
 * Models a Truck in the simulation
 * @author Daniel Bond
 */
public class Truck extends Vehicle{
    
    // the width of the truck
    public static final int WIDTH = 14;
    // the length of the truck
    public static final int LENGTH = 30;
    //the maximum rate of acceleration that a truck can do
    public static final int MAX_ACCELERATION_RATE = 5;
    // the maximum rate of deceleration that a truck can do
    public static final int MAX_DECELERATION_RATE = 15;
    
    public Truck() {}
    
    public Truck(Lane lane) {
        super(lane, lane.getFirstSegment(),null, null, Color.RED);
        setDimensions(WIDTH, LENGTH);
        maxAccelerationRate = MAX_ACCELERATION_RATE;
        maxDecelerationRate = MAX_DECELERATION_RATE;
    }
    
    public Truck(Lane lane, Color c) {
        super(lane, lane.getFirstSegment(),null, null, c);
        setDimensions(WIDTH, LENGTH);
        maxAccelerationRate = MAX_ACCELERATION_RATE;
        maxDecelerationRate = MAX_DECELERATION_RATE;
    }
    
    public Truck(Lane lane, Segment seg, Color c) {
        super(lane, seg,null, null, c);
        setDimensions(WIDTH, LENGTH);
        maxAccelerationRate = MAX_ACCELERATION_RATE;
        maxDecelerationRate = MAX_DECELERATION_RATE;
    }
    
    public Truck(Lane lane, Segment seg) {
        super(lane, seg,null, null, Color.RED);
        setDimensions(WIDTH, LENGTH);
        maxAccelerationRate = MAX_ACCELERATION_RATE;
        maxDecelerationRate = MAX_DECELERATION_RATE;
    }
    
    public Truck(Lane lane, Segment seg, Vehicle inFront, Vehicle behind, Color c){
        super(lane, seg, inFront, behind, c);
        setDimensions(WIDTH, LENGTH);
        maxAccelerationRate = MAX_ACCELERATION_RATE;
        maxDecelerationRate = MAX_DECELERATION_RATE;
    }
    
    @Override
    public void act() {
        advanceVehicle(currentSpeed);
        ai.act();
    }
}
