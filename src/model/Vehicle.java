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
    protected Vehicle vehicleInFront;
    protected Vehicle vehicleBehind;
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
        vehicleInFront = inFront;
        vehicleBehind = behind;
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
    
    /**
     * 
     * @param v the vehicle ahead.
     * @return the distance between vehicles
     */
    public int findVehDistanceAhead(Vehicle v) {
        if (v == null) return -1;
        Vehicle vAhead = v.getVehicleInFront();
        if (vAhead == null) return -1;
        // TODO: have an id specific to the lane
        return vAhead.getHeadSegment().id() - vAhead.getLength() - v.getHeadSegment().id();
    }

    public int findVehDistanceBehind(Vehicle v) {
        if (v == null) return -1;
        Vehicle vBehind = v.getVehicleBehind();
        if (vBehind == null) return -1;
        // TODO: have an id specific to the lane
        return v.getHeadSegment().id() - v.getLength() - vBehind.getHeadSegment().id();
    }

    public void changeLaneAdjacent() {
        /*
        Segment s = this.getHeadSegment();
        Map<Segment, ConnectionType> connectedSegments = s.getConnectedSegments();
        Set<Map.Entry<Segment, ConnectionType>> allEntries = connectedSegments.entrySet();
        Segment adjacentSeg = null;
        for (Map.Entry e : allEntries) {
            if (e.getValue() == ConnectionType.NEXT_TO) {
                adjacentSeg = (Segment) e.getKey();
            }
        }
        this.setHeadSegment(adjacentSeg);
        */
    }
    
    public void changeLaneOverlap(){
        Segment s = this.getHeadSegment();
        Map<Segment, ConnectionType> connectedSegments = s.getConnectedSegments();
        Set<Map.Entry<Segment, ConnectionType>> allEntries = connectedSegments.entrySet();
        Segment overlapSeg = null;
        for(Map.Entry e : allEntries){
            if(e.getValue() == ConnectionType.NEXT_TO_LEFT || e.getValue() == ConnectionType.NEXT_TO_RIGHT){
                overlapSeg = (Segment) e.getKey();
            }
        }
        this.setHeadSegment(overlapSeg);
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

    
}
