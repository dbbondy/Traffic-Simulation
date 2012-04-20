/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.Simulation;
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
    
    public static final int CORNER_STOP_DISTANCE = 100; //the distance from the corner that we should consider slowing down 
    
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
            
            vehicle.accelerate(vehicle.getMaxAccelerationRate()); 
            return;
        }
        
        if(approachingTurn(vehicle.getHeadSegment(), stoppingTimeDistance)){ // if we are approaching a turn
            int aggression = (int) Simulation.getOption(Simulation.AGGRESSION);
            int decelerationRate = (aggression / 100) * vehicle.getMaxDecelerationRate();
            vehicle.decelerate(decelerationRate); 
        } else {
            int distance = vehicle.findVehDistanceAhead();
            if (vehicle.getSpeed() != 0) crashTimeDistance = (distance * 100) / vehicle.getSpeed();
            if (crashTimeDistance > stoppingTimeDistance) {
                
                int aggression = (int) Simulation.getOption(Simulation.AGGRESSION);
                int accelerationRate = (aggression / 100) * vehicle.getMaxAccelerationRate();
                int newSpeed = vehicle.getSpeed() + accelerationRate;
                Segment currentPos = vehicle.getHeadSegment();
                vehicle.advanceVehicle(newSpeed);
                distance = vehicle.findVehDistanceAhead();
                int newCrashTimeDistance = (distance * 100) / (vehicle.getSpeed() + accelerationRate);
                int newStoppingDistance = newSpeed / vehicle.getMaxDecelerationRate();
                
                if(newCrashTimeDistance < newStoppingDistance){ // if we would crash if we accelerated, then we need to slow down perhaps.
                    int decelerationRate = (aggression / 100) * vehicle.getMaxDecelerationRate();
                    vehicle.setHeadSegment(currentPos);
                    vehicle.decelerate(decelerationRate);
                }
                
                vehicle.accelerate(accelerationRate); 
                
                
            } else if (crashTimeDistance == stoppingTimeDistance) {
                vehicle.decelerate(2); 
            } else {
                vehicle.decelerate(vehicle.getMaxDecelerationRate());
            }
        }
        
    }
    
    protected boolean approachingTurn(Segment s, int stoppingTimeDistance){
        Segment overlappingSegment = findOverlappingSegment(s);
        if(overlappingSegment != null){
            int distanceFromTurn = (overlappingSegment.id() - s.id());
            if(distanceFromTurn < (CORNER_STOP_DISTANCE + CORNER_STOP_TIME_DISTANCE)){
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
                
                if(vehicle.getLane().canTurnRight()){ // if we are in right lane, we accelerate to avoid deadlock between two cars
                    int aggression = (int) Simulation.getOption(Simulation.AGGRESSION);
                    int accelerationRate = (aggression / 100) * vehicle.getMaxAccelerationRate();
                    vehicle.accelerate(accelerationRate);
                }else{
                    int aggression = (int) Simulation.getOption(Simulation.AGGRESSION);
                    int decelerationRate = (aggression / 100) * vehicle.getMaxDecelerationRate();
                    vehicle.decelerate(decelerationRate);  
                }
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

        if ((safeLaneChangeID - vehicle.getHeadSegment().id()) < DISTANCE_BEFORE_TURN_FOR_SAFE_LANE_CHANGE) { 
            vehicle.decelerate(vehicle.getMaxDecelerationRate());
            return;
        }

        if (crashTimeDistance > stoppingTimeDistance) { // if we can stop should the vehicle in front be at speed 0
            changeLane(adjacentSeg, adjacentLane);
        } else {
            int distanceBeforeLaneChange = safeLaneChangeID - vehicle.getHeadSegment().id();
            if(distanceBeforeLaneChange < DISTANCE_BEFORE_TURN_FOR_SAFE_LANE_CHANGE){
                int aggression = (int) Simulation.getOption(Simulation.AGGRESSION);
                int decelerationRate = (aggression / 100) * vehicle.getMaxDecelerationRate();
                vehicle.decelerate(decelerationRate);
            }else{
                int aggression = (int) Simulation.getOption(Simulation.AGGRESSION);
                int accelerationRate = (aggression / 100) * vehicle.getMaxAccelerationRate();
                vehicle.accelerate(accelerationRate);
            }
            
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
