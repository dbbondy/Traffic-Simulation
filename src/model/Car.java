package model;

/**
 *
 * @author Daniel Bond
 */
public class Car extends Vehicle {
     
    private Lane currentLane;
    
    public Car(Lane lane, Segment segment) {
        currentLane = lane;
        headSegment = segment;
    }
    
    public Car(Lane lane) {
        currentLane = lane;
        headSegment = lane.getFirstSegment();
    }

    @Override
    public void act() {
       
    }
    
}
