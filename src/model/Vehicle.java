package model;

import java.awt.Color;

/**
 *
 * @author Daniel Bond
 */
public abstract class Vehicle {

    protected int width = 14;
    protected int length = 22;
    
    protected Color color;
    
    protected int currentSpeed;
    protected Segment headSegment;
        
    protected Vehicle vehicleInFront;
    protected Vehicle vehicleBehind;
    
    protected Lane currentLane;
    
    public Vehicle(Lane lane, Segment segment, Vehicle inFront, Vehicle behind, Color c) {
        currentLane = lane;
        headSegment = segment;
        lane.addVehicle(this);
        vehicleInFront = inFront;
        vehicleBehind = behind;
        color = c;
    }
    
    public abstract void act();

    protected void setDimensions(int w, int l) {
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
    
    public void updateSpeed(int newSpeed){
        currentSpeed = newSpeed;
    }
     
}
