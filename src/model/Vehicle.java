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
    
    protected void setDimensions(int w, int l) {
        this.width = w;
        this.length = l;
    }
    
    public Vehicle(Lane lane, Segment segment, Color c) {
        currentLane = lane;
        headSegment = segment;
        lane.addVehicle(this);
        color = c;
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
    
    public abstract void act();

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
     
   /*  public static Vehicle getVehicle(int carRatio, int truckRatio){
        return new Car(); //just a placeholder right now until i figure out how to create based on ratio.
    }
     */
}
