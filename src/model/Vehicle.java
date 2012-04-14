package model;

import controller.Simulation;
import java.awt.Color;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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
    protected Lane currentLane;
    
    
    private int nextSegmentPercent;
    protected int maxAccelerationRate;
    protected int maxDecelerationRate;

    
    public static final int REACTION_TIME = 5; // 5 steps
    protected DriverAI ai;

    public Vehicle() {
    }

    public Vehicle(Lane lane, Segment segment, Vehicle inFront, Vehicle behind, Color c) {
        currentLane = lane;
        headSegment = segment;
        color = c;
        currentSpeed = 50;
        lane.addVehicle(this);
        nextSegmentPercent = 0;
        maxAccelerationRate = 0;
        maxDecelerationRate = 0;
        ai = new DriverAI(this);
    }

    public abstract void act();

    public void setDimensions(int w, int l) {
        this.width = w;
        this.length = l;
    }
    
    protected void accelerate(int acceleration) {
       int maxSpeed = (Integer) Simulation.getOption(Simulation.MAXIMUM_SPEED);
        if(acceleration > maxAccelerationRate) 
            acceleration = maxAccelerationRate;
        currentSpeed += acceleration;
        if (currentSpeed > maxSpeed) 
            currentSpeed = maxSpeed;
    }

    protected void decelerate(int deceleration) {
        if(deceleration > maxDecelerationRate){
            deceleration = maxDecelerationRate;
        }
        currentSpeed -= deceleration;
        if(currentSpeed < 0){
            currentSpeed = 0;
        }
        
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
    
    /**
     * 
     * @param v the vehicle ahead.
     * @return the distance between vehicles
     * assumption: vehicle ahead exists
     */
    public int findVehDistanceAhead() {
        Vehicle vAhead = currentLane.getVehicleAhead(headSegment);
        if (vAhead == null) throw new RuntimeException("no vehicle ahead");
        return vAhead.getHeadSegment().id() - vAhead.getLength() - headSegment.id();
    }

    public int findVehDistanceBehind() {
        Vehicle vBehind = currentLane.getVehicleBehind(headSegment);
        if (vBehind == null) throw new RuntimeException("no vehicle behind");
        // TODO: have an id specific to the lane
        return headSegment.id() - length - vBehind.getHeadSegment().id();
    }

    public Lane getLane(){
        return currentLane;
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

    public int getSpeed() {
        return currentSpeed;
    }

    public Segment getHeadSegment() {
        return this.headSegment;
    }

    public void setHeadSegment(Segment headSegment) {
        this.headSegment = headSegment;
    }

    public void setSpeed(int newSpeed) {
        currentSpeed = newSpeed;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    public int getMaxAccelerationRate() {
        return maxAccelerationRate;
    }

    public int getMaxDecelerationRate() {
        return maxDecelerationRate;
    }
    

    
}
