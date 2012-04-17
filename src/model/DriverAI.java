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
    protected Vehicle vehicle;
    protected Desire desire; // TODO: auto routing based on desired destination!
    protected Random rnd;
    protected int safeLaneChangeID; // the ID of the segment that is the last safe point of changing lanes for turning
    protected Lane vehicleLane;

    public DriverAI(Vehicle vehicle) {
        this.vehicle = vehicle;
        rnd = Randomizer.getInstance();
        Desire[] allDesires = Desire.values();
        desire = allDesires[rnd.nextInt(allDesires.length)];
        vehicleLane = vehicle.getLane();
    }

    protected void getSafeLaneChangeIndex() {
        ArrayList<Segment> laneSegments = vehicleLane.getLaneSegments();
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
        if (vehicleLane.getVehicleAhead(vehicle.getHeadSegment()) != null) {
            int distance = vehicle.findVehDistanceAhead();
            if(vehicle.getSpeed() != 0) crashTimeDistance = (distance * 100) / vehicle.getSpeed();
        } else if (vehicleLane.getVehicleBehind(vehicle.getHeadSegment()) != null) {
            Vehicle behind = vehicleLane.getVehicleBehind(vehicle.getHeadSegment());
            int distance = vehicle.findVehDistanceBehind();
            if(behind.getSpeed() != 0)crashTimeDistance = (distance * 100) / behind.getSpeed();
        }

        if ((desire.toString().equals("TURN_LEFT")) && (vehicleLane.getTurnDirection().toString().equals("RIGHT_AND_STRAIGHT"))) {
            System.out.println("statement 1");
            decideLaneChangeDecision(stoppingTimeDistance, crashTimeDistance);
        } else if ((desire.toString().equals("TURN_RIGHT")) && (vehicleLane.getTurnDirection().toString().equals("LEFT"))) {
            System.out.println("statement 2");
            decideLaneChangeDecision(stoppingTimeDistance, crashTimeDistance);
        } else { //if we are in correct lane then do AI based in that.
            System.out.println("statement 3");
            performStraightLaneAI(stoppingTimeDistance, crashTimeDistance);
        }

    }

    private boolean safeLaneChangeProximity() {
        if (safeLaneChangeID - vehicle.getHeadSegment().id() < 100) {
            return true;
        }
        return false;
    }

    protected void performStraightLaneAI(int stoppingTimeDistance, int crashTimeDistance) {
        
        if (crashTimeDistance == Integer.MIN_VALUE) { //if there is no car in our lane
            vehicle.accelerate(vehicle.getMaxAccelerationRate());
            System.out.println("if statement 1");
            return;
        }
        if (vehicleLane.getVehicles().size() == 1) { //if 1 vehicle in the lane, then accelerate freely
            System.out.println("if statement 2");
            vehicle.accelerate(vehicle.getMaxAccelerationRate());
        }else if(approachingTurn(vehicle.getHeadSegment(), stoppingTimeDistance)){ // if we are approaching a turn
            vehicle.decelerate(vehicle.getMaxDecelerationRate());
            System.out.println("if statement 3");
        }else if (vehicleLane.getVehicleAhead(vehicle.getHeadSegment()) != null) {
            System.out.println("if statement 4");
            int distance = vehicle.findVehDistanceAhead();
            if(vehicle.getSpeed() != 0) crashTimeDistance = (distance * 100) / vehicle.getSpeed();
            if (crashTimeDistance > stoppingTimeDistance) {
                vehicle.accelerate(5);
                System.out.println("if statement 5");
            } else if (crashTimeDistance == stoppingTimeDistance) {
                vehicle.decelerate(2);
                System.out.println("if statement 6");
            } else {
                vehicle.decelerate(vehicle.getMaxDecelerationRate());
                System.out.println("if statement 7");
            }
        }
        
    }
    
    protected boolean approachingTurn(Segment s, int stoppingTimeDistance){
        Segment overlappingSegment = findOverlappingSegment(s);
        if(overlappingSegment != null){
            int distanceFromTurn = (overlappingSegment.id() - s.id());
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
        Lane adjacentLane = getAdjacentLane(vehicle.getHeadSegment());
        if (adjacentLane.getVehicles().isEmpty()) {
            System.out.println("we are changing lanes!");
            changeLane();
            return;
        } else {
            if (adjacentLane.getVehicleAhead(adjacentSeg) != null) {
                Vehicle ahead = adjacentLane.getVehicleAhead(adjacentSeg);
                int distance =  ((ahead.getHeadSegment().id() - ahead.getLength()) - vehicle.getHeadSegment().id());
                if(vehicle.getSpeed() != 0) crashTimeDistance = (distance * 100) / vehicle.getSpeed();
            } else if (adjacentLane.isVehicleAtSegment(adjacentSeg)) { //if there is a vehicle in the immediately adjacent segment to us
                vehicle.decelerate(5); //TODO: maybe think about constants for the deceleration and accel rate
                return;
            } else {
                Vehicle behind = adjacentLane.getVehicleBehind(adjacentSeg);
                int distance = ((vehicle.getHeadSegment().id() - vehicle.getLength()) - behind.getHeadSegment().id());
                if(vehicle.getSpeed() != 0) crashTimeDistance = (distance * 100) / vehicle.getSpeed();
            }
        }
        if (safeLaneChangeProximity()) {
            vehicle.decelerate(5);
        }

        if (stoppingTimeDistance < DISTANCE_BEFORE_TURN_FOR_SAFE_LANE_CHANGE) {
            vehicle.decelerate(vehicle.getMaxDecelerationRate());
            return;
        }

        if (crashTimeDistance > stoppingTimeDistance) { // if we can stop should the vehicle in front be at speed 0
            changeLane();
        } else {
            vehicle.decelerate(vehicle.getMaxDecelerationRate());
        }

    }

    private Lane getAdjacentLane(Segment s) {
        Segment adjacentSeg = getAdjacentSegment(s);
        return adjacentSeg.getLane();
    }

    private Segment getAdjacentSegment(Segment s) {
        Segment adjacentSeg = null;
        Map<Segment, ConnectionType> connectedSegments = s.getConnectedSegments();
        if (connectedSegments.containsValue(ConnectionType.NEXT_TO_LEFT)) {
            for (Entry<Segment, ConnectionType> entry : connectedSegments.entrySet()) {
                if (entry.getValue() == ConnectionType.NEXT_TO_LEFT) {
                    adjacentSeg = entry.getKey();
                }
            }
        } else if (connectedSegments.containsValue(ConnectionType.NEXT_TO_RIGHT)) {
            for (Entry<Segment, ConnectionType> entry : connectedSegments.entrySet()) {
                if (entry.getValue() == ConnectionType.NEXT_TO_RIGHT) {
                    adjacentSeg = entry.getKey();
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

    protected void changeLane() {

        Segment adjacentSeg = getAdjacentSegment(vehicle.getHeadSegment());
        Lane adjacentLane = getAdjacentLane(vehicle.getHeadSegment());
        vehicle.setHeadSegment(adjacentSeg);
        adjacentLane.addVehicle(vehicle);
    }

    protected boolean isSafeDistanceAhead() {
        return false;
    }
}
