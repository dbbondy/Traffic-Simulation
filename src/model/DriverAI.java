package model;

import controller.Simulation;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

/**
 * Models the Driver AI of the vehicles in the simulation
 *
 * @author Daniel Bond
 */
public class DriverAI {

    // distance from the end of the safe lane change
    public static final int DISTANCE_BEFORE_TURN_FOR_SAFE_LANE_CHANGE = 100;
    // extra distance to allow us to slow down gradually before a corner
    public static final int CORNER_STOP_TIME_DISTANCE = 10;
    //the distance from the corner that we should consider slowing down 
    public static final int CORNER_STOP_DISTANCE = 100;
    // the time distance (exceeding stopping distance) that cars should aim for
    // between the current vehicle and the vehicle ahead of it
    public static final int EXTRA_GAP_TIME_DISTANCE = 10;
    
    protected Vehicle vehicle;
    protected Desire desire; 
    protected Random rnd;
    protected int safeLaneChangeID; // the ID of the segment that is the last safe point of changing lanes for turning

    public DriverAI(Vehicle vehicle) {
        this.vehicle = vehicle;
        rnd = Randomizer.getInstance();

        
        // TODO: (better): do auto-routing between segment A and segment B
        // Desire[] allDesires = Desire.values();
        // desire = allDesires[rnd.nextInt(allDesires.length)];
        desire = Desire.STRAIGHT;

        //getSafeLaneChangeIndex();
    }

    /**
     * Gets the last point of a lane in which we can change lanes to turn a corner.
     */
   /* protected void getSafeLaneChangeIndex() {
        
         

        ArrayList<Segment> laneSegments = vehicle.getLane().getLaneSegments();
        if(vehicle.getLane().isBlocked()){
            safeLaneChangeID = vehicle.getLane().getLastSegment().id() - DISTANCE_BEFORE_TURN_FOR_SAFE_LANE_CHANGE;
            return;
        }
        for (int i = 0; i < laneSegments.size(); i++) {
            Map<Segment, ConnectionType> connectedSegments = laneSegments.get(i).getConnectedSegments();
            if (connectedSegments.containsValue(ConnectionType.OVERLAP)) {
                safeLaneChangeID = laneSegments.get(i - DISTANCE_BEFORE_TURN_FOR_SAFE_LANE_CHANGE).id();
            }
        }

        
        
    }*/

    /**
     * Performs the general "acting" of the intelligence.
     */
    public void act() {

        int stoppingTimeDistance = (int) ((double) (vehicle.getSpeed()) / (vehicle.getMaxDecelerationRate())); // number of time steps it will take to stop
        int crashTimeDistance = Integer.MIN_VALUE;

        if (vehicle.getLane().getVehicleAhead(vehicle.getHeadSegment()) != null) {
            int distance = vehicle.findVehDistanceAhead();
            if (vehicle.getSpeed() != 0) {
                crashTimeDistance = (int) ((distance * 100.0) / vehicle.getSpeed());
            }
        }

        /* if ((desire == Desire.TURN_LEFT) && (!vehicle.getLane().canTurnLeft())) {
            decideLaneChangeDecision(stoppingTimeDistance, crashTimeDistance);
        } else if ((desire == Desire.TURN_RIGHT) && (!vehicle.getLane().canTurnRight())) {
            decideLaneChangeDecision(stoppingTimeDistance, crashTimeDistance);
        } else */
        
        if (vehicle.getLane().isBlocked()) {
            decideBlockedLaneChangeDecision();
            // still in a blocked lane => check distance to end
            // and slow down if required to prevent crash
            if (vehicle.getLane().isBlocked()) {
                if (!approachingBlockedLaneEnd()) {
                    // we are not near end of lane so normal rules 
                    performStraightLaneAI(stoppingTimeDistance, crashTimeDistance);
                }
            }
        } else {
            // we have no blocked lane, no desire to change
            performStraightLaneAI(stoppingTimeDistance, crashTimeDistance);
        }
    }
    
    /**
     * Determines whether we are approaching the end of a road that is blocked.
     * @return <code> true </code> if we are approaching the end of the road. <code> false </code> otherwise
     */
    public boolean approachingBlockedLaneEnd(){
        int distanceToEnd = vehicle.getLane().getLastSegment().id() - vehicle.getHeadSegment().id();
        double currentSpeed = vehicle.getSpeed();
        int maxDeceleration = vehicle.getMaxDecelerationRate();
        int distanceToStop = (int) ((currentSpeed / maxDeceleration) * (currentSpeed / 2));
        if (distanceToStop >= distanceToEnd) {
            vehicle.decelerate(maxDeceleration);
            return true;
        }
        return false;
    }

    /**
     * Determines if we are in proximity of final point of which we can change lanes
     *
     * @return
     * <code> true </code> if we are in proximity of the final point of lane changing.
     * <code> false </code> otherwise.
     */
    private boolean safeLaneChangeProximity() {
        if (safeLaneChangeID - vehicle.getHeadSegment().id() < 100) { // TODO: 100 should be a constant
            return true;
        }
        return false;
    }

    /**
     * Performs the AI related to travelling in a straight lane
     *
     * @param stoppingTimeDistance the time distance it would take for this vehicle to stop
     * @param crashTimeDistance the time distance it would take for this vehicle to crash into the vehicle in front, if that vehicle was going at 0 speed
     */
    protected void performStraightLaneAI(int stoppingTimeDistance, int crashTimeDistance) {

        if (crashTimeDistance == Integer.MIN_VALUE) { //if there is no car in our lane
            vehicle.accelerate(vehicle.getMaxAccelerationRate());
            return;
        }

        int aggression = (int) Simulation.getOption(Simulation.AGGRESSION);
        if (approachingTurn(vehicle.getHeadSegment())) { // if we are approaching a turn         

            int decelerationRate = (int) ((aggression / 100.0) * vehicle.getMaxDecelerationRate());
            vehicle.decelerate(decelerationRate);

        } else { // if we are not approaching a turn

            int distance = vehicle.findVehDistanceAhead();
            int maxDecel = vehicle.getMaxDecelerationRate();
            
            if (distance < Vehicle.BUMPER_DISTANCE) {
                vehicle.decelerate(maxDecel);
                return;
            }

            // determine increase or decrease in speed proportional to how much we are safely allowed to
            // written by Jonathan Pike (mats@staite.net)
            int newSpeed = (int) ((1.0 / 2.0) * (Math.sqrt(
                    (400 * distance * maxDecel)
                    + Math.pow(maxDecel, 2)
                    - (2 * maxDecel * EXTRA_GAP_TIME_DISTANCE)
                    + Math.pow(EXTRA_GAP_TIME_DISTANCE, 2))
                    + maxDecel
                    - EXTRA_GAP_TIME_DISTANCE));

            int accelerationRate = newSpeed - vehicle.getSpeed();

            if (accelerationRate > 0) {
                accelerationRate = Math.min(accelerationRate, vehicle.getMaxAccelerationRate());
                vehicle.accelerate((int) (accelerationRate * (aggression / 100.0)));
            } else if (accelerationRate < 0) {
                int decelerationRate = Math.min(-accelerationRate, vehicle.getMaxDecelerationRate());
                vehicle.decelerate(decelerationRate);
            }

        }

    }

    /**
     * Determines if the vehicle is approaching a lane turn (and has desire to make such a turn)
     *
     * @param s the segment we are currently at in the lane
     * @return
     * <code> true </code> if we are approaching the turn.
     * <code> false </code> otherwise.
     */
    protected boolean approachingTurn(Segment s) {
        
        Segment overlappingSegment = findOverlappingSegment(s);
        if (desire == Desire.STRAIGHT) {
            return false;
        }
        if (desire == Desire.TURN_LEFT && !vehicle.getLane().canTurnLeft()) {
            return false;
        }
        if (desire == Desire.TURN_RIGHT && !vehicle.getLane().canTurnRight()) {
            return false;
        }
        if (overlappingSegment != null) {
            int distanceFromTurn = (overlappingSegment.id() - s.id());
            if (distanceFromTurn < (CORNER_STOP_DISTANCE + CORNER_STOP_TIME_DISTANCE)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * Finds the first instance of a overlapping segment in the lane of the parameter
     * <code> s </code>
     *
     * @param s the segment of the lane we want to find an overlapping segment for.
     * @return
     * <code> null </code> if there was no overlapping segment found for this segments lane.
     * @return otherwise we return the reference of the first instance of an overlapping segment
     */
    private Segment findOverlappingSegment(Segment s) {
        Segment overlappingSegment = null;
        Lane segmentLane = s.getLane();
        ArrayList<Segment> allSegments = segmentLane.getLaneSegments();
        // TODO: create a cache of overlapping segments (!important)
        for (int i = s.id(); i < allSegments.size(); i++) {
            Segment seg = allSegments.get(i);
            Map<Segment, ConnectionType> connectedSegments = seg.getConnectedSegments();
            if (connectedSegments.containsValue(ConnectionType.OVERLAP)) {
                for (Entry<Segment, ConnectionType> entry : connectedSegments.entrySet()) {
                    if (entry.getValue() == ConnectionType.OVERLAP) {
                        overlappingSegment = allSegments.get(i);
                    }
                }
            }
        }
        return overlappingSegment;
    }

    /**
     * Intelligence for determining if we can change lanes or not.
     *
     * @param stoppingTimeDistance the time distance it would take for our vehicle to come to a halt.
     * @param crashTimeDistance the time distance it would take us to crash into the vehicle in front, should its speed be 0.
     */
    
    
    private void decideBlockedLaneChangeDecision() {
        
        Segment adjacentSeg = getAdjacentSegment(vehicle.getHeadSegment());
        if (adjacentSeg == null) return;
        
        Lane adjacentLane = adjacentSeg.getLane();
        
        if (adjacentLane.getVehicles().isEmpty()) { // if the adjacent lane is empty then we can change lanes
            changeLane(adjacentSeg);
            return;
        } else {
            Vehicle otherVehicle = null;
            if (adjacentLane.isVehicleAtSegment(adjacentSeg)) return;
            
            // if there is a vehicle behind our desired change position
            // written by Jonathan Pike (mats@staite.net)
            if ((otherVehicle = adjacentLane.getVehicleBehind(adjacentSeg)) != null) {
                int distance = ((adjacentSeg.id() - vehicle.getLength()) - otherVehicle.getHeadSegment().id());
                if (distance < Vehicle.BUMPER_DISTANCE) return;
                int otherVehicleStoppingTimeDistance = (int) (((double) otherVehicle.getSpeed()) / otherVehicle.getMaxDecelerationRate());
                int otherVehicleCrashTimeDistance = (int) ((distance * 100.0) / otherVehicle.getSpeed());
                if(otherVehicleCrashTimeDistance <= otherVehicleStoppingTimeDistance) return;
            }
                        
            // if there is a vehicle ahead of the desired change position
            if ((otherVehicle = adjacentLane.getVehicleAhead(adjacentSeg)) != null) {
                int distance = ((otherVehicle.getHeadSegment().id() - otherVehicle.getLength()) - adjacentSeg.id());
                if (distance < Vehicle.BUMPER_DISTANCE) return;
                int stoppingTimeDistance = (int) (((double) vehicle.getSpeed()) / vehicle.getMaxDecelerationRate());
                int crashTimeDistance = (int) ((distance * 100.0) / vehicle.getSpeed());
                if (crashTimeDistance <= stoppingTimeDistance) return;
            }
            
            changeLane(adjacentSeg);
        }
    }
    
     /** Daniel's version. buggy and does not work as intended.used in place of decideBlockedLaneDecision()
      * 
     * Intelligence for determining if we can change lanes or not.
     *
     * @param stoppingTimeDistance the time distance it would take for our vehicle to come to a halt.
     * @param crashTimeDistance the time distance it would take us to crash into the vehicle in front, should its speed be 0.
     */
    /*private void decideLaneChangeDecision(int stoppingTimeDistance, int crashTimeDistance) {
        Segment adjacentSeg = getAdjacentSegment(vehicle.getHeadSegment());
        if (adjacentSeg == null) {
            return;
        }
        Lane adjacentLane = adjacentSeg.getLane();
        if (adjacentLane.getVehicles().isEmpty()) { // if the adjacent lane is empty then we can change lanes
            changeLane(adjacentSeg);
            return;
        } else {
            Vehicle otherVehicle = null;
            if (adjacentLane.isVehicleAtSegment(adjacentSeg)) { // if there is a vehicle in the immediately adjacent segment to us

                if (vehicle.getLane().canTurnRight()) { // if we are in right lane, we accelerate to avoid deadlock between two cars
                    int aggression = (int) Simulation.getOption(Simulation.AGGRESSION);
                    int accelerationRate = (aggression / 100) * vehicle.getMaxAccelerationRate();
                    vehicle.accelerate(accelerationRate);
                } else if (vehicle.getLane().isBlocked() && (vehicle.getLane().getLaneSegments().size() - vehicle.getHeadSegment().id() > BLOCKED_LANE_SAFE_DISTANCE)) { // if our lane is blocked and we are distant enough from the end of the lane
                    int aggression = (int) Simulation.getOption(Simulation.AGGRESSION);
                    int accelerationRate = (aggression / 100) * vehicle.getMaxAccelerationRate();
                    vehicle.accelerate(accelerationRate);
                } else if(vehicle.getLane().isBlocked() && (vehicle.getLane().getLastSegment().id() - vehicle.getHeadSegment().id() < BLOCKED_LANE_SAFE_DISTANCE)){ // if our lane is blocked and we are getting close to the end of the lane
                    int aggression = (int) Simulation.getOption(Simulation.AGGRESSION);
                    int decelerationRate = (aggression / 100) * vehicle.getMaxDecelerationRate();
                    vehicle.decelerate(decelerationRate);
                }else {
                    int aggression = (int) Simulation.getOption(Simulation.AGGRESSION);
                    int decelerationRate = (aggression / 100) * vehicle.getMaxDecelerationRate();
                    vehicle.decelerate(decelerationRate);
                }
                return;
            } else if ((otherVehicle = adjacentLane.getVehicleAhead(adjacentSeg)) != null) {
                int distance = ((otherVehicle.getHeadSegment().id() - otherVehicle.getLength()) - vehicle.getHeadSegment().id());
                if(vehicle.getLane().isBlocked() && (vehicle.getLane().getLastSegment().id() - vehicle.getHeadSegment().id() > BLOCKED_LANE_SAFE_DISTANCE)){ // if our lane is blocked and we are distant enough from the end of the lane
                    int aggression = (int) Simulation.getOption(Simulation.AGGRESSION);
                    int accelerationRate = (aggression / 100) * vehicle.getMaxAccelerationRate();
                    vehicle.accelerate(accelerationRate);
                    if(distance > (vehicle.getLength()+ 50)){
                        changeLane(adjacentSeg);
                        return;
                    }
                    
                }else if(vehicle.getLane().isBlocked() && (vehicle.getLane().getLastSegment().id() - vehicle.getHeadSegment().id() < BLOCKED_LANE_SAFE_DISTANCE)){ // if our lane is blocked and we are getting close to the end of the lane
                    int aggression = (int) Simulation.getOption(Simulation.AGGRESSION);
                    int decelerationRate = (aggression / 100) * vehicle.getMaxDecelerationRate();
                    vehicle.decelerate(decelerationRate);
                    if(distance > (vehicle.getLength() + 50)){
                        changeLane(adjacentSeg);
                        
                    }
                }
                if (vehicle.getSpeed() != 0) {
                    crashTimeDistance = (distance * 100) / vehicle.getSpeed();
                }
                return;
            } else {
                otherVehicle = adjacentLane.getVehicleBehind(adjacentSeg);
                int distance = ((vehicle.getHeadSegment().id() - vehicle.getLength()) - otherVehicle.getHeadSegment().id());
                if(vehicle.getLane().isBlocked() && (vehicle.getLane().getLastSegment().id() - vehicle.getHeadSegment().id() > BLOCKED_LANE_SAFE_DISTANCE)){ // if our lane is blocked and we are distant enough from the end of the lane
                    int aggression = (int) Simulation.getOption(Simulation.AGGRESSION);
                    int accelerationRate = (aggression / 100) * vehicle.getMaxAccelerationRate();
                    vehicle.accelerate(accelerationRate);
                    if(distance > (vehicle.getLength()+ 50)){
                        changeLane(adjacentSeg);
                        return;
                    }
                    return;
                }else if(vehicle.getLane().isBlocked() && (vehicle.getLane().getLastSegment().id() - vehicle.getHeadSegment().id() < BLOCKED_LANE_SAFE_DISTANCE)){ // if our lane is blocked and we are getting close to the end of the lane
                    int aggression = (int) Simulation.getOption(Simulation.AGGRESSION);
                    int decelerationRate = (aggression / 100) * vehicle.getMaxDecelerationRate();
                    vehicle.decelerate(decelerationRate);
                    if(distance > (vehicle.getLength()+ 50)){
                        changeLane(adjacentSeg);
                        return;
                    }
                    return;
                }
                if (vehicle.getSpeed() != 0) {
                    crashTimeDistance = (distance * 100) / vehicle.getSpeed();
                }
            }
        }
        if (safeLaneChangeProximity()) {
            vehicle.decelerate(5);
            return;
        }

        if ((safeLaneChangeID - vehicle.getHeadSegment().id()) < DISTANCE_BEFORE_TURN_FOR_SAFE_LANE_CHANGE) {
            vehicle.decelerate(vehicle.getMaxDecelerationRate());
            return;
        }

        if (crashTimeDistance > stoppingTimeDistance) { // if we can stop should the vehicle in front be at speed 0
            changeLane(adjacentSeg);
        } else {
            int distanceBeforeLaneChange = safeLaneChangeID - vehicle.getHeadSegment().id();
            if (distanceBeforeLaneChange < DISTANCE_BEFORE_TURN_FOR_SAFE_LANE_CHANGE) {
                int aggression = (int) Simulation.getOption(Simulation.AGGRESSION);
                int decelerationRate = (aggression / 100) * vehicle.getMaxDecelerationRate();
                vehicle.decelerate(decelerationRate);
            } else {
                int aggression = (int) Simulation.getOption(Simulation.AGGRESSION);
                int accelerationRate = (aggression / 100) * vehicle.getMaxAccelerationRate();
                vehicle.accelerate(accelerationRate);
            }

        }

    }*/
    
    /*public void decideLaneChangeDecision(int  stoppingTimeDistance, int crashTimeDistance) {
        
         

        if (safeLaneChangeProximity()) {
            vehicle.decelerate(5);
            return;
        }

        if ((safeLaneChangeID - vehicle.getHeadSegment().id()) < DISTANCE_BEFORE_TURN_FOR_SAFE_LANE_CHANGE) {
            vehicle.decelerate(vehicle.getMaxDecelerationRate());
            return;
        }

        if (crashTimeDistance > stoppingTimeDistance) { // if we can stop should the vehicle in front be at speed 0
            changeLane(adjacentSeg);
        } else {
            int distanceBeforeLaneChange = safeLaneChangeID - vehicle.getHeadSegment().id();
            if (distanceBeforeLaneChange < DISTANCE_BEFORE_TURN_FOR_SAFE_LANE_CHANGE) {
                int aggression = (int) Simulation.getOption(Simulation.AGGRESSION);
                int decelerationRate = (aggression / 100) * vehicle.getMaxDecelerationRate();
                vehicle.decelerate(decelerationRate);
            } else {
                int aggression = (int) Simulation.getOption(Simulation.AGGRESSION);
                int accelerationRate = (aggression / 100) * vehicle.getMaxAccelerationRate();
                vehicle.accelerate(accelerationRate);
            }
        }
         
        
        
    }*/

    /**
     * Gets the immediately adjacent segment to the parameter of this function
     *
     * @param s the segment we want to retrieve the immediately adjacent segment of
     * @return
     * <code> null </code> if no adjacent segments were found, otherwise we return the reference of the adjacent segment.
     */
    private Segment getAdjacentSegment(Segment s) {
        Segment adjacentSeg = null;
        Map<Segment, ConnectionType> connectedSegments = s.getConnectedSegments();

        if (connectedSegments.containsValue(ConnectionType.NEXT_TO_LEFT)) {
            for (Entry<Segment, ConnectionType> entry : connectedSegments.entrySet()) {
                if (entry.getValue() == ConnectionType.NEXT_TO_LEFT) {
                    adjacentSeg = entry.getKey();
                    break;
                }
            }
        } else if (connectedSegments.containsValue(ConnectionType.NEXT_TO_RIGHT)) {
            for (Entry<Segment, ConnectionType> entry : connectedSegments.entrySet()) {
                if (entry.getValue() == ConnectionType.NEXT_TO_RIGHT) {
                    adjacentSeg = entry.getKey();
                    break;
                }
            }
        }
        return adjacentSeg;
    }

    /**
     * Gets the overlapping segment from the passed in segment. This method assumes that we are immediately connected to an overlapping segment. If the programmer wishes to search a lane for an
     * overlapping segment, use the
     * <code> findOverlappingSegment(...) </code> function.
     *
     * @param s the segment we want to retrieve the overlapped segment from.
     * @return the overlapped segment.
     * <code> null </code> is returned if there is no overlapped segment to be found.
     */
    private Segment getOverlapSegment(Segment s) {
        Segment overlapSeg = null;
        Map<Segment, ConnectionType> connectedSegments = s.getConnectedSegments();
        if (connectedSegments.containsValue(ConnectionType.OVERLAP)) {
            for (Entry<Segment, ConnectionType> entry : connectedSegments.entrySet()) {
                if (entry.getValue() == ConnectionType.OVERLAP) {
                    overlapSeg = entry.getKey();
                }
            }
        }
        return overlapSeg;
    }

    /**
     * Turns the corner of the lane that overlaps with the current lane the vehicle resides on.
     */
    protected void turnCorner() {
        Segment overlapSeg = getOverlapSegment(vehicle.getHeadSegment());
        Lane overlapLane = overlapSeg.getLane();
        vehicle.getLane().removeVehicle(vehicle);
        overlapLane.addVehicle(vehicle);
        vehicle.setHeadSegment(overlapSeg);

    }

    /**
     * Changes the lane of the vehicle to the lane of the
     * <code> adjacentSeg </code> parameter
     *
     * @param adjacentSeg the adjacent segment whose lane we want to move into
     */
    protected void changeLane(Segment adjacentSeg) {
        vehicle.getLane().removeVehicle(vehicle);
        Lane lane = adjacentSeg.getLane();
        lane.addVehicle(vehicle);
        vehicle.setHeadSegment(adjacentSeg);
    }
}
