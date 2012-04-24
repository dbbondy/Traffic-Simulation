package model;

import java.awt.Color;

/**
 * Models a Car in the simulation
 * @author Daniel Bond
 */
public class Car extends Vehicle {

    // maximum acceleration rate of a car
    public static final int MAX_ACCELERATION_RATE = 10;
    // maximum deceleration rate of a car
    public static final int MAX_DECELERATION_RATE = 20;
    
    // width of a car
    public static final int WIDTH = 12;
    // length of a car
    public static final int LENGTH = 20;

    public Car() {}

    public Car(Lane lane) {
        super(lane, lane.getFirstSegment(), null, null, Color.RED);
        setDimensions(WIDTH, LENGTH);
        maxAccelerationRate = MAX_ACCELERATION_RATE;
        maxDecelerationRate = MAX_DECELERATION_RATE;
    }

    public Car(Lane lane, Color c) {
        super(lane, lane.getFirstSegment(), null, null, c);
        setDimensions(WIDTH, LENGTH);
        maxAccelerationRate = MAX_ACCELERATION_RATE;
        maxDecelerationRate = MAX_DECELERATION_RATE;
    }

    public Car(Lane lane, Segment seg, Color c) {
        super(lane, seg, null, null, c);
        setDimensions(WIDTH, LENGTH);
        maxAccelerationRate = MAX_ACCELERATION_RATE;
        maxDecelerationRate = MAX_DECELERATION_RATE;
    }

    public Car(Lane lane, Segment seg) {
        super(lane, seg, null, null, Color.RED);
        setDimensions(WIDTH, LENGTH);
        maxAccelerationRate = MAX_ACCELERATION_RATE;
        maxDecelerationRate = MAX_DECELERATION_RATE;
    }

    public Car(Lane lane, Segment seg, Vehicle inFront, Vehicle behind, Color c) {
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
