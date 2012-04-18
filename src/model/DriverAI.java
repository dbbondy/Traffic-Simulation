/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

/**
 *
 * @author Dan
 */
public class DriverAI {

    public static final int DISTANCE_BEFORE_TURN_FOR_SAFE_LANE_CHANGE = 100;
    // extra distance to allow us to slow down gradually before a corner
    public static final int CORNER_STOP_TIME_DISTANCE = 10;
    
    protected Vehicle vehicle;
    protected Desire desire; // TODO: auto routing based on desired destination!
    protected Random rnd;
    protected int safeLaneChangeID; // the ID of the segment that is the last safe point of changing lanes for turning


    public DriverAI(Vehicle vehicle) {
        this.vehicle = vehicle;
        rnd = Randomizer.getInstance();
        Desire[] allDesires = Desire.values();
        desire = allDesires[rnd.nextInt(allDesires.length)];
        getSafeLaneChangeIndex();
    }

    protected void getSafeLaneChangeIndex() {
        ArrayList<Segment> laneSegments = vehicle.getLane().getLaneSegments();
        for (int i = 0; i < laneSegments.size(); i++) {
            Map<Segment, ConnectionType> connectedSegments = laneSegments.get(i).getConnectedSegments();
            if (connectedSegments.containsValue(ConnectionType.OVERLAP)) {
                safeLaneChangeID = laneSegments.get(i - DISTANCE_BEFORE_TURN_FOR_SAFE_LANE_CHANGE).id();
            }
        }
    }

    public void act() {
        int stoppingTimeDistance = ((vehicle.getSpeed()) / (vehicle.getMaxDecelerationRate())); // number of time steps it will take to stop
        int crashTimeDistance = Integer.MIN_VALUE;
        
        if (vehicle.getLane().getVehicleAhead(vehicle.getHeadSegment()) != null) {
            int distance = vehicle.findVehDistanceAhead();
            if(vehicle.getSpeed() != 0) crashTimeDistance = (distance * 100) / vehicle.getSpeed();
        }

        // TODO: other types of turn direction combinations
        if ((desire == Desire.TURN_LEFT) && (!vehicle.getLane().canTurnLeft())) {
            decideLaneChangeDecision(stoppingTimeDistance, crashTimeDistance);
        } else if ((desire == Desire.TURN_RIGHT) && (!vehicle.getLane().canTurnRight())) {
            decideLaneChangeDecision(stoppingTimeDistance, crashTimeDistance);
        } else { // if we are in correct lane then do AI based in that.
            performStraightLaneAI(stoppingTimeDistance, crashTimeDistance);
        }
        

    }

    private boolean safeLaneChangeProximity() {
        // TODO: PROBABLY SHOULD DO: this only works if we expect to change lane for 1 reason
        // what if we have 2 reasons to change lane
        // 1. turn left in a short while
        // 2. turn left in a long while 
        // this SHOULD allow for this
        // safeLaneChangeID is relevant to a specific corner
        // in another lane and NOT the entire lane
        if (safeLaneChangeID - vehicle.getHeadSegment().id() < 100) { // TODO: 100 should be a constant
            return true;
        }
        return false;
    }

    protected void performStraightLaneAI(int stoppingTimeDistance, int crashTimeDistance) {
        
        if (crashTimeDistance == Integer.MIN_VALUE) { //if there is no car in our lane
            vehicle.accelerate(vehicle.getMaxAccelerationRate()); // TODO: aggression
            return;
        }
        
        if(approachingTurn(vehicle.getHeadSegment(), stoppingTimeDistance)){ // if we are approaching a turn
            vehicle.decelerate(vehicle.getMaxDecelerationRate()); // TODO: aggression
        } else {
            int distance = vehicle.findVehDistanceAhead();
            if (vehicle.getSpeed() != 0) crashTimeDistance = (distance * 100) / vehicle.getSpeed();
            if (crashTimeDistance > stoppingTimeDistance) {
                // TODO: CRITICAL! ensure that the acceleration won't  
                // cause a case where next iteration crash is imminent
                vehicle.accelerate(5); // TODO: aggression 
                
                // 1. adjust distance (in local variable) by current speed    (take care of the factor 100)
                // 2. compute new speed
                // 3. recompute expected stoppingTimeDistance and crashTimeDistance (new variables)
                // 4. check for potential crash
                
            } else if (crashTimeDistance == stoppingTimeDistance) {
                vehicle.decelerate(2); // TODO: aggression (less important here)
            } else {
                // this should never really happen if you take care 
                // of everything else. the cars won't get too close!
                vehicle.decelerate(vehicle.getMaxDecelerationRate());
            }
        }
        
    }
    
    protected boolean approachingTurn(Segment s, int stoppingTimeDistance){
        Segment overlappingSegment = findOverlappingSegment(s);
        if(overlappingSegment != null){
            int distanceFromTurn = (overlappingSegment.id() - s.id());
            // TODO: CRITICAL!
            // distance from turn is physical distance
            // stoppingTimeDistance is in # of steps!!!
            // cannot compare 2 values of different type!
            // TODO: additional: add an additional constanst time distance
            // so that we don't have to break rapidly. (CORNER_STOP_TIME_DISTANCE)
            if(stoppingTimeDistance < distanceFromTurn){
                return true;
            }else{
                return false;
            }
        }
        return false;
        
        
    }
    
    private Segment findOverlappingSegment(Segment s){
        Segment overlappingSegment = null;
        Lane segmentLane = s.getLane();
        ArrayList<Segment> allSegments = segmentLane.getLaneSegments();
        // TODO: create a cache of overlapping segments (!important)
        for(int i = s.id(); i < allSegments.size(); i++){
            Segment seg = allSegments.get(i);
            Map<Segment, ConnectionType> connectedSegments = seg.getConnectedSegments();
            if(connectedSegments.containsValue(ConnectionType.OVERLAP)){
                for (Entry<Segment, ConnectionType> entry : connectedSegments.entrySet()) {
                    if(entry.getValue() == ConnectionType.OVERLAP){
                        overlappingSegment = allSegments.get(i);
                    }
                }
            }
        }
        return overlappingSegment;
    }
    
    

    private void decideLaneChangeDecision(int stoppingTimeDistance, int crashTimeDistance) {
        Segment adjacentSeg = getAdjacentSegment(vehicle.getHeadSegment());
        Lane adjacentLane = adjacentSeg.getLane();
        if (adjacentLane.getVehicles().isEmpty()) {
            changeLane(adjacentSeg, adjacentLane);
            return;
        } else {
            Vehicle otherVehicle = null;
            if (adjacentLane.isVehicleAtSegment(adjacentSeg)) { // if there is a vehicle in the immediately adjacent segment to us
                vehicle.decelerate(5); //TODO: aggression // TODO: CRITICAL left lane slows, right lane increases speed
                return;                
            } else if ((otherVehicle = adjacentLane.getVehicleAhead(adjacentSeg)) != null) {
                int distance =  ((otherVehicle.getHeadSegment().id() - otherVehicle.getLength()) - vehicle.getHeadSegment().id());
                if(vehicle.getSpeed() != 0) crashTimeDistance = (distance * 100) / vehicle.getSpeed();
            } else {
                // TODO: instead of assuming the car has 0 speed we can use the current speed for this one
                // since the car the AI is driving knows its own speed "reliably"
                // we then compute the same thing with the difference in speed only. 
                // careful with negative speed difference
                otherVehicle = adjacentLane.getVehicleBehind(adjacentSeg);
                int distance = ((vehicle.getHeadSegment().id() - vehicle.getLength()) - otherVehicle.getHeadSegment().id());
                if(vehicle.getSpeed() != 0) crashTimeDistance = (distance * 100) / vehicle.getSpeed();
            }
        }
        if (safeLaneChangeProximity()) {
            vehicle.decelerate(5);
        }

        // TODO: critical! this doesn't take into account the corner position
        // remember that stoppingTimeDistance is the same wherever you are
        // and only depends on speed!
        
        // TODO: additional: you are comparing physical distance and
        // timeDistance again! this is bad! 
        
        // TODO: additional: this should be relative to the safeLaneChangeID
                                                           // ^^^^ assumes original unfixed version
        
        /*if (stoppingTimeDistance < DISTANCE_BEFORE_TURN_FOR_SAFE_LANE_CHANGE) {
            vehicle.decelerate(vehicle.getMaxDecelerationRate());
            return;
        }*/

        if (crashTimeDistance > stoppingTimeDistance) { // if we can stop should the vehicle in front be at speed 0
            changeLane(adjacentSeg, adjacentLane);
        } else {
            // TODO: aggression 
            // TODO: CRITICAL: take into account the distance until the
            // DISTANCE_BEFORE_TURN_FOR_SAFE_LANE_CHANGE
            vehicle.decelerate(vehicle.getMaxDecelerationRate());
        }

    }

    private Segment getAdjacentSegment(Segment s) {
        Segment adjacentSeg = null;
        Map<Segment, ConnectionType> connectedSegments = s.getConnectedSegments();
        // TODO: IMPORTANT! make sure that changing lane is actually helpful!
        // we might be changing into a lane that also cannot turn!
        // need to know whether we actually want to change into a lane or not!
        // ------
        // if we need to jump 2 lanes then add some AI for that too. 
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

    protected Desire getDesire() {
        return desire;
    }

    protected Lane getOverlapLane(Segment s) {
        Segment overlapSeg = getOverlapSegment(s);
        return overlapSeg.getLane();
    }

    protected void turnCorner() {
        Segment overlapSeg = getOverlapSegment(vehicle.getHeadSegment());
        Lane overlapLane = getOverlapLane(vehicle.getHeadSegment());
        vehicle.setHeadSegment(overlapSeg);
        overlapLane.addVehicle(vehicle);
    }

    protected void changeLane(Segment adjacentSeg, Lane lane) {
        vehicle.getLane().removeVehicle(vehicle);
        lane.addVehicle(vehicle);        
        vehicle.setHeadSegment(adjacentSeg);
    }

    protected boolean isSafeDistanceAhead() {
        return false;
    }
}
