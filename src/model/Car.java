package model;

// my awesome change

import java.awt.Color;


// this is another change

/**
 *
 * @author Daniel Bond
 */
public class Car extends Vehicle {
     
    private static final int WIDTH = 12;
    private static final int LENGTH = 20;
    
    public Car() {}
    
    public Car(Lane lane) {
        super(lane, lane.getFirstSegment(),null, null, Color.RED);
        setDimensions(WIDTH, LENGTH);
    }
    
    public Car(Lane lane, Color c) {
        super(lane, lane.getFirstSegment(), null, null, c);
        setDimensions(WIDTH, LENGTH);
    }
    
    public Car(Lane lane, Segment seg, Color c) {
        super(lane, seg, null, null, c);
        setDimensions(WIDTH, LENGTH);
    }
    
    public Car(Lane lane, Segment seg) {
        super(lane, seg, null, null, Color.RED);
        setDimensions(WIDTH, LENGTH);
    }
    
    public Car(Lane lane, Segment seg, Vehicle inFront, Vehicle behind, Color c){
        super(lane, seg, inFront, behind, c);
        setDimensions(WIDTH, LENGTH);
    }
     
    @Override
    public void act() {
       advanceVehicle(currentSpeed);
    }
    
}
