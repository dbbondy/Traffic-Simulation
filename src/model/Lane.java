package model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Models a lane for a junction
 * @author Daniel Bond
 */
public class Lane {

    private int xCoord; // the x-coordinate of the starting point of the lane
    private int yCoord; // the y-coordinate of the starting point of the lane
    private ArrayList<Segment> laneSegments; // the collection of segments that the lane has
    private int initialAngle; // the initial angle that the lane will be drawn from
    private ArrayList<Vehicle> vehicles; // the collection of vehicles contained in the lane
    private TurnDirection direction; // the direction of a turn for the lane
    private int newSegmentID = 0; // the id of each segment in a lane. the segment id is lane specfic
    
    public Lane(int xCoord, int yCoord, int initialAngle, TurnDirection direction) {
        laneSegments = new ArrayList<>(32);
        vehicles = new ArrayList<>(32);
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.initialAngle = initialAngle;
        this.newSegmentID = 0;
        this.direction = direction;
    }
    
    /**
     * gets a new segment ID for a segment
     * @return the new segment ID
     */
    public int getNewSegmentID() {
        return this.newSegmentID++;
    }
    
    /**
     * Predicate that determines if the lane allows a vehicle to go straight on.
     * @return <code> true </code> if the lane allows for a vehicle to go straight on. <code> false </code> otherwise
     */
    public boolean canGoStraight() {
        if (direction == TurnDirection.RIGHT_AND_STRAIGHT) return true;
        if (direction == TurnDirection.LEFT_AND_STRAIGHT) return true;
        if (direction == TurnDirection.STRAIGHT) return true;
        return false;
    }
    
    /**
     * Predicate that determines if the lane allows a vehicle to turn left.
     * @return <code> true </code> if the lane allows for a vehicle to turn left. <code> false </code> otherwise
     */
    public boolean canTurnLeft() {
        if (direction == TurnDirection.LEFT) return true;
        if (direction == TurnDirection.LEFT_AND_STRAIGHT) return true;
        if (direction == TurnDirection.ALL) return true;
        return false;
    }
    
    /**
     * Predicate that determines if the lane is blocked and vehicles need to change lanes
     * @return <code> true </code> if the lane is blocked. <code> false </code> otherwise.
     */
    public boolean isBlocked(){
        if(direction == TurnDirection.BLOCKED) return true;
        return false;
    }
        
    /**
     * Predicate that determines if the lane allows a vehicle to turn right.
     * @return <code> true </code> if the lane allows for a vehicle to turn right. <code> false </code> otherwise
     */
    public boolean canTurnRight() {
        if (direction == TurnDirection.RIGHT) return true;
        if (direction == TurnDirection.RIGHT_AND_STRAIGHT) return true;
        if (direction == TurnDirection.ALL) return true;
        return false;
    }
    
    /**
     * Removes all vehicles from a lane
     */
    public void removeVehicles() {
        vehicles.clear();
    }
    
    /**
     * Gets the list of segments that represents the lane
     * @return the list of segments for the lane
     */
    public ArrayList<Segment> getLaneSegments() {
        return this.laneSegments;
    }

    /**
     * Gets the initial angle of the lane 
     * @return the initial angle of the lane
     */
    public int getInitialAngle() {
        return this.initialAngle;
    }
    
    /**
     * Removes a single vehicle from the system.
     * If a vehicle does not exist and is attempted to be removed, this method throws a {@link java.lang.RuntimeException}
     * @param v the vehicle to remove
     */
    public void removeVehicle(Vehicle v){
        if(!vehicles.remove(v))
            throw new RuntimeException("You cannot remove a vehicle that does not exist!");
        
        vehicles.remove(v);
    }

    /**
     * Gets the first segment of the lane
     * @return the first segment of the lane
     */
    public Segment getFirstSegment() {
        return laneSegments.get(0);
    }

    /**
     * Gets the last segment of the lane
     * @return the last segment of the lane
     */
    public Segment getLastSegment() {
        return laneSegments.get(laneSegments.size() - 1);
    }

    /**
     * Gets the list of vehicles
     * @return the list of vehicles
     */
    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    /**
     * Adds segments to the lane to extend the lane
     * @param segments the array of segments to extend the lane to include
     */
    public void add(Segment[] segments) {
        if (!laneSegments.isEmpty()) { // if the list of segments is not empty
            Segment endSegment = laneSegments.get(laneSegments.size() - 1); // append to the end.
            laneSegments.addAll(Arrays.asList(segments));
            endSegment.setNextSegment(segments[0]);
        }else{
            laneSegments.addAll(Arrays.asList(segments)); // otherwise just add them in.
        }
    }

    /**
     * Finds the first instance of a vehicle ahead of a segment
     * @param segment the segment we want to find a vehicle ahead of
     * @return the vehicle ahead of our segment. <code> null </code> is returned if there is no vehicle ahead
     */
    public Vehicle getVehicleAhead(Segment segment) {
       return VehicleBinarySearch.findVehicleAhead(vehicles, segment);
    }
    
    /**
     * Finds the first instance of a vehicle behind a segment
     * @param segment the segment we want to find a vehicle behind
     * @return the vehicle behind of our segment. <code> null </code> is returned if there is no vehicle behind
     */
    public Vehicle getVehicleBehind(Segment segment) {
       return VehicleBinarySearch.findVehicleBehind(vehicles, segment);
    }

    /**
     * Adds a vehicle into the collection in order. 
     * This method preserves the order of vehicles in the lane so no extra ordering computation is necessary.
     * @param vehicle the vehicle to add to the collection
     */
    public void addVehicle(Vehicle vehicle) {
        VehicleBinarySearch.addVehicle(vehicles, vehicle);
    }

    /**
     * Gets the front most vehicle in the lane
     * @return the segment where the vehicle is at
     */
    public Segment getFrontVehicle() {

        int currentFront = 0;
        Segment headSegmentFront = null;
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getHeadSegment().id() > currentFront) { //if the ID > current segment. it is in front
                currentFront = vehicle.getHeadSegment().id();
                headSegmentFront = vehicle.getHeadSegment();
            }
        }
        return headSegmentFront;
    }
    
    /**
     * Predicate for determining if a vehicle is at a segment <code> s </code>
     * @param s the segment we want to check
     * @return <code> true </code> if there is a vehicle at the segment <code> s </code>. <code> false </code> is returned otherwise
     */
    public boolean isVehicleAtSegment(Segment s){
        for(Vehicle v : vehicles){
            if(v.getHeadSegment().equals(s)){
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the x-coordinate of the starting position of the lane
     * @return the x-coordinate of the starting position
     */
    public int getXStart() {
        return xCoord;
    }

    /**
     * Gets the y-coordinate of the starting position of the lane
     * @return the y-coordinate of the starting position
     */
    public int getYStart() {
        return yCoord;
    }
}
