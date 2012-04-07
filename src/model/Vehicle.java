package model;

import java.awt.Color;
import java.util.Random;

/**
 *
 * @author Daniel Bond
 */
public abstract class Vehicle {

    protected int width = 14;
    protected int length = 22;
    
    protected Color color;
    
    protected int currentSpeed = 100;
    protected Segment headSegment;
        
    protected Vehicle vehicleInFront;
    protected Vehicle vehicleBehind;
    
    protected Lane currentLane;
    
    private int nextSegmentPercent = 0;
    
    public Vehicle() {}
    
    public Vehicle(Lane lane, Segment segment, Vehicle inFront, Vehicle behind, Color c) {
        currentLane = lane;
        headSegment = segment;
        lane.addVehicle(this);
        vehicleInFront = inFront;
        vehicleBehind = behind;
        color = c;
        
        // TODO: remove this later
        currentSpeed = new Random(System.nanoTime()).nextInt(100) + 50;
    }
    
    public abstract void act();

    public void setDimensions(int w, int l) {
        this.width = w;
        this.length = l;
    }
    
    public Vehicle getVehicleBehind() {
        return vehicleBehind;
    }

    public void setVehicleBehind(Vehicle vehicleBehind) {
        this.vehicleBehind = vehicleBehind;
    }

    public Vehicle getVehicleInFront() {
        return vehicleInFront;
    }

    public void setVehicleInFront(Vehicle vehicleInFront) {
        this.vehicleInFront = vehicleInFront;
        setVehicleBehind(this);
    }
    
    public void advanceVehicle(int value) {
        int newValue = value + nextSegmentPercent;
        nextSegmentPercent = newValue % 100;
        int advanceSegments = newValue / 100;
        for (int i = 0; i < advanceSegments; i++) {
            Segment next = headSegment.getNextSegment();
            if (next == null) return;
            headSegment = next;
        }
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getLength() {
        return this.length;
    }
    
    public Color getColor() {
        return this.color;
    }
    
    public int getSpeed(){
        return currentSpeed;
    }
    
    public Segment getHeadSegment() {
        return this.headSegment;
    }
    
    public void setHeadSegment(Segment headSegment) {
        this.headSegment = headSegment;
    }
    
    public void setSpeed(int newSpeed){
        currentSpeed = newSpeed;
    }
    
    public void setColor(Color color) {
        this.color = color;
    }
     
}
