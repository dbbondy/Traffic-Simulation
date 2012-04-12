package model;

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
    protected Desire desire; // TODO: auto routing based on desired destination!
    protected Random rnd;
    private int nextSegmentPercent = 0;

    public Vehicle() {
    }

    public Vehicle(Lane lane, Segment segment, Vehicle inFront, Vehicle behind, Color c) {
        currentLane = lane;
        headSegment = segment;
        lane.addVehicle(this);
        vehicleInFront = inFront;
        vehicleBehind = behind;
        color = c;
        rnd = Randomizer.getInstance();
        Desire[] allDesires = Desire.values();
        desire = allDesires[rnd.nextInt(allDesires.length)];
        currentSpeed = 0;
    }

    public abstract void act();

    public void setDimensions(int w, int l) {
        this.width = w;
        this.length = l;
    }
    
    protected void accelerate() {
        this.currentSpeed++;
    }

    protected void decelerate() {
        this.currentSpeed--;
    }
    
    protected Desire getDesire(){
        return desire;
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

    }
    
    public void changeLaneOverlap(){
        Segment s = this.getHeadSegment();
        Map<Segment, ConnectionType> connectedSegments = s.getConnectedSegments();
        Set<Map.Entry<Segment, ConnectionType>> allEntries = connectedSegments.entrySet();
        Segment overlapSeg = null;
        for(Map.Entry e : allEntries){
            if(e.getValue() == ConnectionType.OVERLAP){
                overlapSeg = (Segment) e.getKey();
            }
        }
        this.setHeadSegment(overlapSeg);
    }
    
    public void lookAhead(Vehicle v){
        Desire d = v.getDesire();
        
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
