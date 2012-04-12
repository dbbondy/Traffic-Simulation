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

        // TODO: remove this later
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
            if (next == null) {
                return;
            }
            
            headSegment = next;
        }
    }
    
    /**
     * 
     * @param v the vehicle ahead.
     * @return the distance between vehicles
     */
    public int findVehDistanceAhead(Vehicle v) {
        if (v == null) {
            return -1;
        }

        Vehicle vAhead = v.getVehicleInFront();
        if (vAhead == null) {
            return -1;
        }

        int currentPos = currentLane.getLaneSegments().indexOf(this);
        int aheadPos = currentLane.getLaneSegments().indexOf(v);
        if (aheadPos == -1) {
            return -1;
        }
        currentPos -= getLength();
        aheadPos -= getLength();

        return aheadPos - currentPos;
    }

    public int findVehDistanceBehind(Vehicle v) {
        if (v == null) {
            return -1;
        }

        Vehicle vBehind = v.getVehicleBehind();
        if (vBehind == null) {
            return -1;
        }

        int currentPos = currentLane.getLaneSegments().indexOf(this);
        int behindPos = currentLane.getLaneSegments().indexOf(v);
        if (behindPos == -1) {
            return -1;
        }
        
        currentPos -= getLength();
        behindPos -= getLength();

        return currentPos - behindPos;
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

    public boolean adjacentLaneAvailability() {
        Segment s = this.getHeadSegment();
        Map<Segment, ConnectionType> connectedSegments = s.getConnectedSegments();
        Set<Map.Entry<Segment, ConnectionType>> allEntries = connectedSegments.entrySet();
        Segment adjacentSeg = null;
        for (Map.Entry e : allEntries) {
            if (e.getValue() == ConnectionType.NEXT_TO) {
                adjacentSeg = (Segment) e.getKey();
            }
        }

        if (adjacentSeg == null) { // there are no "adjacent" segments so we cannot change lanes adjacent
            return false;
        }

        Lane adjacentLane = adjacentSeg.getLane();
        Vehicle vAhead = adjacentLane.getVehicleAhead(adjacentSeg);
        Vehicle vBehind = adjacentLane.getVehicleBehind(adjacentSeg);

        if (adjacentLane.getVehicles().isEmpty()) { // if there are no vehicles in the adjacent lane we can change lanes.
            return true;
        }

        if ((adjacentLane.getVehicles().size() == 1 && findVehDistanceAhead(vAhead) > Lane.SAFE_VEHICLE_DISTANCE) 
                ||(adjacentLane.getVehicles().size() == 1 && findVehDistanceBehind(vBehind) > Lane.SAFE_VEHICLE_DISTANCE)) {
            return true;
        }

        
        if (vAhead == null) {
            return true;
        }
        
        if (vBehind == null && (findVehDistanceAhead(vAhead) > Lane.SAFE_VEHICLE_DISTANCE)) {
            return true;
        }else if(vBehind == null || (findVehDistanceAhead(vBehind) < Lane.SAFE_VEHICLE_DISTANCE)){
            return false;
        }
         
        if (findVehDistanceAhead(vAhead) != -1  && findVehDistanceAhead(vAhead) > Lane.SAFE_VEHICLE_DISTANCE) { //if vehicle ahead exists and the distance is greater than the safe distance (10 segments)
            if ((this.getSpeed() - vAhead.getSpeed()) > Lane.SAFE_SPEED_DIFFERENTIAL) { //if this vehicle is going faster than 5 speed more than the vehicle ahead in the adjacent lane
                return false;
            }

        } else if (findVehDistanceBehind(vBehind) != -1 && findVehDistanceBehind(vBehind) > Lane.SAFE_VEHICLE_DISTANCE) { //if vehicle behind exists and the distance is greater than the safe distance (10 segments)
            if ((vBehind.getSpeed() - this.getSpeed()) > Lane.SAFE_SPEED_DIFFERENTIAL) { //if this vehicle is going slower than 5 speed more than the vehicle behind in the adjacent lane
                return false;
            }
        }
        //if no errors were found in the transition between lanes, it is safe to state we can change lanes.
        return true;
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
